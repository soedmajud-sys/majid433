package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

object AlWatanConstants {
    const val APP_TITLE = "منصة الحلول الرقمية"
    const val OFFICE_NAME = "مكتب الوطن للخدمات المساندة"
    const val OWNER_FULL_NAME = "ماجد بن سعود بن فواز العميري"
    const val NATIONAL_ID = "1064454570"
    const val LEGAL_STATUS = "مطور ومستقل معتمد"
    const val ISSUING_AUTHORITY = "وزارة الموارد البشرية والتنمية الاجتماعية"
    const val FREELANCE_DOC_NO = "FL-827973875"
    const val SPECIALIZATION = "برمجة نظم المعلومات - تخصص ساب (SAP)"
    const val OFFICIAL_EMAIL = "soedmajud@gmail.com"
    const val OFFICIAL_DOMAIN = "https://maktab-alwatan.com"
    const val PRIVACY_URL = "https://maktab-alwatan.com/privacy"
    const val ADMIN_ACCESS_KEY = "00551660"

    val OFFICIAL_PHONES = listOf(
        "0555614852",
        "0556907115",
        "0551683899"
    )

    const val CONTRACT_PREFIX = "CON-DIG"
    const val INVOICE_PREFIX = "INV-DIG"
    const val RECEIPT_PREFIX = "REC"
    const val ORDER_PREFIX = "WTN-2026"
}

enum class ServiceCategory(val displayName: String, val subtitle: String) {
    GOVERNMENT(
        displayName = "المنصات الحكومية",
        subtitle = "أبشر، قوى، مقيم، مُدد، الزكاة والجمارك، بلدي، وزارة التجارة، ناجز، التأمينات"
    ),
    SAP_ERP(
        displayName = "برمجة نظم ساب (SAP)",
        subtitle = "تطبيق وحلول SAP S/4HANA، الفوترة الإلكترونية ZATCA، كود ABAP، وحدات ERP"
    ),
    PRIVATE_BUSINESS(
        displayName = "حلول المنشآت والشركات",
        subtitle = "تأسيس الكيانات، عقود العمل الرقمية، الحسابات البنكية، شهادات الامتثال"
    ),
    IT_SYSTEMS(
        displayName = "نظم المعلومات والتحول الرقمي",
        subtitle = "قواعد البيانات، بوابات API السحابية، أتمتة العمليات والأمن السيبراني"
    )
}

data class ServiceItem(
    val id: String,
    val title: String,
    val platformName: String,
    val category: ServiceCategory,
    val description: String,
    val detailedScope: List<String>,
    val requiredDocs: List<String>,
    val processingDuration: String,
    val officialFeeGuide: String,
    val isFeatured: Boolean = false,
    val badge: String? = null
)

@Entity(tableName = "service_orders")
data class ServiceOrderEntity(
    @PrimaryKey val id: String,
    val clientName: String,
    val clientPhone: String,
    val clientType: String, // أفراد, مؤسسة فردية, شركة
    val serviceTitle: String,
    val platformName: String,
    val category: String,
    val notes: String,
    val status: String, // قيد المراجعة، جاري المعالجة والتدقيق، مرحلة الاعتماد، مكتمل بنجاح، بانتظار متطلبات
    val trackingNotes: String,
    val createdAtFormatted: String,
    val estimatedCompletion: String,
    val isUrgent: Boolean = false
)

@Entity(tableName = "consultation_bookings")
data class ConsultationBookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientName: String,
    val phone: String,
    val email: String,
    val consultationType: String,
    val preferredDate: String,
    val preferredTime: String,
    val meetingType: String, // حضور بمقر المكتب، اجتماع مرئي افتراضي
    val notes: String,
    val supervisor: String = AlWatanConstants.OWNER_FULL_NAME,
    val status: String = "مؤكد",
    val bookedAtFormatted: String
)

@Entity(tableName = "financial_records")
data class FinancialRecordEntity(
    @PrimaryKey val referenceNumber: String, // CON-DIG-1021, INV-DIG-3042, REC-5021
    val type: String, // عقد إلكتروني، فاتورة ضريبية، سند قبض
    val clientName: String,
    val clientTaxOrId: String,
    val serviceTitle: String,
    val amount: Double,
    val taxAmount: Double,
    val totalAmount: Double,
    val issueDate: String,
    val status: String, // معتمد ومسدد، ساري، قيد التنفيذ
    val notes: String
)

data class SapModuleInfo(
    val code: String,
    val nameArabic: String,
    val nameEnglish: String,
    val description: String,
    val highlights: List<String>
)

data class PlatformGuide(
    val platformName: String,
    val authority: String,
    val purpose: String,
    val commonServices: List<String>,
    val portalUrl: String,
    val tips: String
)
