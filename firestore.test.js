const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: cannot read user profiles or orders", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("orders").get());
});

test("Authenticated user: cannot read another user's profile or orders", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("users").doc(BOB_UID).set({
      userId: BOB_UID,
      displayName: "Bob",
      email: "bob@example.com",
      createdAt: new Date(),
      updatedAt: new Date(),
    });
    await context.firestore().collection("users").doc(BOB_UID).collection("orders").doc("order_bob").set({
      id: "WTN-2026-9999",
      userId: BOB_UID,
      clientName: "Bob Corp",
      clientPhone: "0500000000",
      clientType: "شركة",
      serviceTitle: "تطبيق ساب",
      platformName: "SAP",
      category: "برمجة نظم ساب (SAP)",
      status: "قيد المراجعة",
      createdAt: new Date(),
      updatedAt: new Date(),
    });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(aliceDb.collection("users").doc(BOB_UID).get());
  await assertFails(aliceDb.collection("users").doc(BOB_UID).collection("orders").doc("order_bob").get());
});

test("Authenticated user: can create and read their own valid order", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const orderRef = aliceDb.collection("users").doc(ALICE_UID).collection("orders").doc("order_alice");

  await assertSucceeds(
    orderRef.set({
      id: "WTN-2026-1001",
      userId: ALICE_UID,
      clientName: "Alice Trading",
      clientPhone: "0555555555",
      clientType: "مؤسسة فردية",
      serviceTitle: "منصة قوى",
      platformName: "قوى",
      category: "المنصات الحكومية",
      status: "قيد المراجعة",
      createdAt: new Date(),
      updatedAt: new Date(),
    })
  );

  await assertSucceeds(orderRef.get());
});
