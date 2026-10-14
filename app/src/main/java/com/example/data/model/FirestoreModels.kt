package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class FirestoreUser(
    @DocumentId val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val phone: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreServiceOrder(
    @DocumentId val documentId: String = "",
    val id: String = "",
    val userId: String = "",
    val clientName: String = "",
    val clientPhone: String = "",
    val clientType: String = "",
    val serviceTitle: String = "",
    val platformName: String = "",
    val category: String = "",
    val notes: String = "",
    val status: String = "قيد المراجعة",
    val trackingNotes: String = "",
    val estimatedCompletion: String = "",
    val isUrgent: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreConsultation(
    @DocumentId val documentId: String = "",
    val id: String = "",
    val userId: String = "",
    val clientName: String = "",
    val phone: String = "",
    val email: String = "",
    val consultationType: String = "",
    val preferredDate: String = "",
    val preferredTime: String = "",
    val meetingType: String = "",
    val notes: String = "",
    val supervisor: String = AlWatanConstants.OWNER_FULL_NAME,
    val status: String = "مؤكد",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)
