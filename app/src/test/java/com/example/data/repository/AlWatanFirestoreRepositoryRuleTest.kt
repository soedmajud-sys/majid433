package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AlWatanFirestoreRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun createServiceOrder_authenticatedUser_success() = runBlocking {
        val uid = signInTestUser("client_alice@example.com")
        val repo = AlWatanFirestoreRepository(firestore, auth)

        val orderCode = withTimeout(5000L) {
            repo.createServiceOrder(
                clientName = "مؤسسة الرياض للتقنية",
                clientPhone = "0551122334",
                clientType = "مؤسسة فردية",
                serviceTitle = "ربط نظام ساب مع هيئة الزكاة",
                platformName = "ZATCA Phase 2",
                category = "برمجة نظم ساب (SAP)",
                notes = "معاملة تجريبية",
                isUrgent = true
            )
        }

        assertTrue(orderCode.startsWith("WTN-2026-"))

        val orders = withTimeout(5000L) {
            repo.observeOrders().first { list -> list.any { it.id == orderCode } }
        }
        val order = orders.firstOrNull { it.id == orderCode }
        assertNotNull(order)
        assertEquals("مؤسسة الرياض للتقنية", order?.clientName)
        assertEquals("ربط نظام ساب مع هيئة الزكاة", order?.serviceTitle)
    }

    @Test
    fun unauthenticatedAccess_throwsIllegalStateException() {
        auth.signOut()
        val repo = AlWatanFirestoreRepository(firestore, auth)
        var exceptionThrown = false
        try {
            repo.requireUserId()
        } catch (e: IllegalStateException) {
            exceptionThrown = true
        }
        assertTrue(exceptionThrown)
    }
}
