package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.R
import com.example.data.model.AlWatanConstants
import com.example.data.model.FirestoreConsultation
import com.example.data.model.FirestoreServiceOrder
import com.example.data.model.FirestoreUser
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlin.random.Random

class AlWatanFirestoreRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    // Secondary constructor resolving named database ID from string resource
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            FirebaseApp.getInstance(),
            context.getString(R.string.firestore_database_id)
        )
    )

    fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    suspend fun saveUserProfile(displayName: String, email: String, phone: String = "") {
        val uid = requireUserId()
        val userRef = db.collection("users").document(uid)
        val payload = mapOf(
            "userId" to uid,
            "displayName" to displayName,
            "email" to email,
            "phone" to phone,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        userRef.set(payload).await()
    }

    fun observeOrders(): Flow<List<FirestoreServiceOrder>> {
        val uid = requireUserId()
        return db.collection("users")
            .document(uid)
            .collection("orders")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(FirestoreServiceOrder::class.java)
            }
            .catch { error ->
                Log.e("FirestoreRepo", "Error observing orders: ${error.message}", error)
                throw error
            }
    }

    fun observeConsultations(): Flow<List<FirestoreConsultation>> {
        val uid = requireUserId()
        return db.collection("users")
            .document(uid)
            .collection("consultations")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(FirestoreConsultation::class.java)
            }
            .catch { error ->
                Log.e("FirestoreRepo", "Error observing consultations: ${error.message}", error)
                throw error
            }
    }

    suspend fun createServiceOrder(
        clientName: String,
        clientPhone: String,
        clientType: String,
        serviceTitle: String,
        platformName: String,
        category: String,
        notes: String,
        isUrgent: Boolean
    ): String {
        val uid = requireUserId()
        val randomNum = 1000 + Random.nextInt(9000)
        val orderCode = "${AlWatanConstants.ORDER_PREFIX}-$randomNum"

        val docRef = db.collection("users").document(uid).collection("orders").document(orderCode)
        val payload = mapOf(
            "id" to orderCode,
            "userId" to uid,
            "clientName" to clientName,
            "clientPhone" to clientPhone,
            "clientType" to clientType,
            "serviceTitle" to serviceTitle,
            "platformName" to platformName,
            "category" to category,
            "notes" to notes,
            "status" to "قيد المراجعة",
            "trackingNotes" to "تم استلام الطلب وتوجيهه إلى فريق العمل للتدقيق والمطابقة النظامية.",
            "estimatedCompletion" to if (isUrgent) "خلال 24 ساعة (عاجلة)" else "خلال 2-3 أيام عمل",
            "isUrgent" to isUrgent,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        docRef.set(payload).await()
        return orderCode
    }

    suspend fun createConsultation(
        clientName: String,
        phone: String,
        email: String,
        consultationType: String,
        preferredDate: String,
        preferredTime: String,
        meetingType: String,
        notes: String
    ): String {
        val uid = requireUserId()
        val randomNum = 1000 + Random.nextInt(9000)
        val consultationId = "CONS-$randomNum"

        val docRef = db.collection("users").document(uid).collection("consultations").document(consultationId)
        val payload = mapOf(
            "id" to consultationId,
            "userId" to uid,
            "clientName" to clientName,
            "phone" to phone,
            "email" to email,
            "consultationType" to consultationType,
            "preferredDate" to preferredDate,
            "preferredTime" to preferredTime,
            "meetingType" to meetingType,
            "notes" to notes,
            "supervisor" to AlWatanConstants.OWNER_FULL_NAME,
            "status" to "مؤكد",
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        docRef.set(payload).await()
        return consultationId
    }
}
