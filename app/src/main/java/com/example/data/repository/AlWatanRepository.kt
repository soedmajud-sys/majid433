package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.AlWatanConstants
import com.example.data.model.ConsultationBookingEntity
import com.example.data.model.FinancialRecordEntity
import com.example.data.model.PlatformGuide
import com.example.data.model.SapModuleInfo
import com.example.data.model.ServiceCategory
import com.example.data.model.ServiceItem
import com.example.data.model.ServiceOrderEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class AlWatanRepository(private val database: AppDatabase) {

    private val orderDao = database.orderDao()
    private val consultationDao = database.consultationDao()
    private val financialDao = database.financialDao()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedInitialDataIfNeeded()
        }
    }

    // Flows
    val allOrders: Flow<List<ServiceOrderEntity>> = orderDao.getAllOrders()
    val allConsultations: Flow<List<ConsultationBookingEntity>> = consultationDao.getAllBookings()
    val allFinancialRecords: Flow<List<FinancialRecordEntity>> = financialDao.getAllRecords()

    fun getOrdersByPhone(phone: String): Flow<List<ServiceOrderEntity>> =
        orderDao.getOrdersByPhone(phone)

    suspend fun getOrderById(id: String): ServiceOrderEntity? =
        orderDao.getOrderById(id)

    suspend fun submitServiceOrder(
        clientName: String,
        clientPhone: String,
        clientType: String,
        serviceTitle: String,
        platformName: String,
        category: String,
        notes: String,
        isUrgent: Boolean
    ): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale("ar", "SA"))
        val now = dateFormat.format(Date())
        val randomNum = 1000 + Random.nextInt(9000)
        val orderId = "${AlWatanConstants.ORDER_PREFIX}-$randomNum"

        val entity = ServiceOrderEntity(
            id = orderId,
            clientName = clientName,
            clientPhone = clientPhone,
            clientType = clientType,
            serviceTitle = serviceTitle,
            platformName = platformName,
            category = category,
            notes = notes,
            status = "قيد المراجعة",
            trackingNotes = "تم استلام الطلب وتوجيهه إلى فريق العمل للتدقيق والمطابقة النظامية.",
            createdAtFormatted = now,
            estimatedCompletion = if (isUrgent) "خلال 24 ساعة (معاملة عاجلة)" else "خلال 2-3 أيام عمل",
            isUrgent = isUrgent
        )
        orderDao.insertOrder(entity)
        return orderId
    }

    suspend fun updateOrderStatus(orderId: String, newStatus: String, notes: String) {
        val existing = orderDao.getOrderById(orderId) ?: return
        val updated = existing.copy(
            status = newStatus,
            trackingNotes = notes
        )
        orderDao.updateOrder(updated)
    }

    suspend fun bookConsultation(
        clientName: String,
        phone: String,
        email: String,
        consultationType: String,
        preferredDate: String,
        preferredTime: String,
        meetingType: String,
        notes: String
    ): Long {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale("ar", "SA"))
        val now = dateFormat.format(Date())

        val booking = ConsultationBookingEntity(
            clientName = clientName,
            phone = phone,
            email = email,
            consultationType = consultationType,
            preferredDate = preferredDate,
            preferredTime = preferredTime,
            meetingType = meetingType,
            notes = notes,
            supervisor = AlWatanConstants.OWNER_FULL_NAME,
            status = "مؤكد",
            bookedAtFormatted = now
        )
        return consultationDao.insertBooking(booking)
    }

    suspend fun createFinancialRecord(
        type: String, // عقد إلكتروني، فاتورة ضريبية، سند قبض
        clientName: String,
        clientTaxOrId: String,
        serviceTitle: String,
        amount: Double,
        notes: String
    ): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale("ar", "SA"))
        val today = dateFormat.format(Date())
        val randomDigits = 1000 + Random.nextInt(9000)
        val prefix = when (type) {
            "عقد إلكتروني" -> AlWatanConstants.CONTRACT_PREFIX
            "سند قبض" -> AlWatanConstants.RECEIPT_PREFIX
            else -> AlWatanConstants.INVOICE_PREFIX
        }
        val refNumber = "$prefix-2026-$randomDigits"
        val taxAmount = amount * 0.15
        val totalAmount = amount + taxAmount

        val record = FinancialRecordEntity(
            referenceNumber = refNumber,
            type = type,
            clientName = clientName,
            clientTaxOrId = clientTaxOrId,
            serviceTitle = serviceTitle,
            amount = amount,
            taxAmount = taxAmount,
            totalAmount = totalAmount,
            issueDate = today,
            status = "معتمد وساري",
            notes = notes
        )
        financialDao.insertRecord(record)
        return refNumber
    }

    // Static Services Catalog
    fun getServicesList(): List<ServiceItem> {
        return listOf(
            // SAP & IT Systems
            ServiceItem(
                id = "sap_zatca",
                title = "ربط وتكامل نظام ساب (SAP) مع الفوترة الإلكترونية ZATCA",
                platformName = "هيئة الزكاة والضريبة والجمارك (ZATCA Phase 2)",
                category = ServiceCategory.SAP_ERP,
                description = "ربط وتكامل برمجي شامل لنظام SAP ERP مع المرحلة الثانية لهيئة الزكاة (الربط والتكامل) وإصدار أختام التشفير الرقمية Cryptographic Stamp ومطابقة معايير UBL 2.1 و QR Code.",
                detailedScope = listOf(
                    "تطبيق واجهات API المباشرة بين SAP وبوابة فاتورة Fatoora",
                    "إعداد حلول التشفير وشهادات CSID والربط السحابي الآمن",
                    "معالجة سيناريوهات الفواتير الضريبية والإشعارات المدينة والدائنة",
                    "اختبار بيئة المحاكاة (Sandbox) والاعتماد النهائي في البيئة الحية"
                ),
                requiredDocs = listOf("الرقم الضريبي للمنشأة", "بيانات نظام SAP الحالي", "صلاحيات الوصول لبوابة فاتورة"),
                processingDuration = "5 - 10 أيام عمل",
                officialFeeGuide = "تعتمد على حجم المعاملات وإصدار SAP",
                isFeatured = true,
                badge = "تخصص ساب معتمد"
            ),
            ServiceItem(
                id = "sap_implementation",
                title = "تطبيق وترقية نظم ساب (SAP S/4HANA & Business One)",
                platformName = "SAP ERP Enterprise",
                category = ServiceCategory.SAP_ERP,
                description = "استشارات وتنفيذ شامل لوحدات ساب للشركات والمؤسسات المتوسطة والكبرى، مع هندسة العمليات وتدريب الكوادر ودعم مستمر.",
                detailedScope = listOf(
                    "دراسة الفجوات ومتطلبات الأعمال (Business Blueprint)",
                    "إعداد وحدات FI (المالية) و CO (المحاسبة الإدارية) و MM (المشتريات والمخازن)",
                    "إعداد وحدات SD (المبيعات والتوزيع) و HCM (الموارد البشرية)",
                    "ترحيل البيانات من الأنظمة القديمة وضمان سلامة التقارير المالية"
                ),
                requiredDocs = listOf("مخطط هيكل المنشأة", "قائمة العمليات المحاسبية والمخزنية", "بيانات الفروع"),
                processingDuration = "حسب حجم المشروع وخطة العمل",
                officialFeeGuide = "استشارة مجدولة مع المشرف",
                isFeatured = true,
                badge = "حلول مؤسسية"
            ),
            ServiceItem(
                id = "sap_abap_custom",
                title = "تطوير وبرمجة كود ABAP وتقارير ساب المخصصة",
                platformName = "SAP ABAP & Development",
                category = ServiceCategory.SAP_ERP,
                description = "تطوير برمجيات مخصصة داخل بيئة ساب، بناء تقارير ALV المتقدمة، نماذج Smart Forms و Adobe Forms، وتطوير واجهات BAPI و RFC للربط الخارجي.",
                detailedScope = listOf(
                    "تطوير البرامج والواجهات المخصصة وفق المعايير القياسية لساب",
                    "أتمتة القيود والمستندات المحاسبية وحركات المخازن",
                    "إصلاح وتحسين كفاءة كود ABAP وزيادة سرعة الاستعلامات"
                ),
                requiredDocs = listOf("مواصفات التطوير التقني (Technical Specification)", "صلاحيات بيئة التطوير Development Server"),
                processingDuration = "3 - 7 أيام عمل",
                officialFeeGuide = "حسب نطاق الساعات البرمجية",
                badge = "تطوير تقني"
            ),
            ServiceItem(
                id = "gov_qiwa_contracts",
                title = "إدارة منصة قوى والتوثيق الرقمي لعقود العمل",
                platformName = "منصة قوى (Qiwa)",
                category = ServiceCategory.GOVERNMENT,
                description = "توثيق وإدارة عقود العمل لجميع موظفي المنشأة، متابعة مؤشر الامتثال ونسب التوطين، اللائحة التنظيمية للعمل، وخدمات نقل الخدمات وإصدار التأشيرات المهنية.",
                detailedScope = listOf(
                    "مراجعة وتدقيق بنود العقود ومطابقتها لنظام العمل السعودي",
                    "توثيق العقود ورفعها للاعتماد الفوري من الموظف والمنصة",
                    "إصدار شهادات السعودة ورفع نسب التوطين بنطاقات"
                ),
                requiredDocs = listOf("السجل التجاري", "بيانات الموظف وعقد العمل", "صلاحيات المفوض بمنصة قوى"),
                processingDuration = "24 - 48 ساعة",
                officialFeeGuide = "رسوم المنصة الرسمية محددة حسب نوع الخدمة",
                isFeatured = true,
                badge = "الأكثر طلباً"
            ),
            ServiceItem(
                id = "gov_mudad_payroll",
                title = "إدارة نظام حماية الأجور والامتثال المالي (مُدد)",
                platformName = "منصة مُدد (Mudad)",
                category = ServiceCategory.GOVERNMENT,
                description = "إدارة ملفات الرواتب الرقمية WIF، معالجة تبريرات نسب حماية الأجور، وتفادي المخالفات المالية والتجميد لنشاط المنشأة.",
                detailedScope = listOf(
                    "رفع وتدقيق ملف صرف الرواتب الشهري",
                    "الرد على التبريرات والملاحظات الواردة من وزارة الموارد البشرية",
                    "ربط الحساب البنكي التجاري بالمنصة لضمان السلاسة"
                ),
                requiredDocs = listOf("كشف الرواتب الشهري", "كشف الحساب البنكي التجاري", "حساب المنشأة بمُدد"),
                processingDuration = "نفس اليوم",
                officialFeeGuide = "رسوم الاشتراك السنوي للمنصة الرسمية",
                badge = "حماية الأجور"
            ),
            ServiceItem(
                id = "gov_muqeem_services",
                title = "خدمات منصة مقيم والعمليات الميدانية للإقامات",
                platformName = "منصة مقيم (Muqeem)",
                category = ServiceCategory.GOVERNMENT,
                description = "إصدار وتجديد الإقامات، تأشيرات الخروج والعودة، الخروج النهائي، ونقل الخدمات وطباعة طرود الإقامات المعتمدة.",
                detailedScope = listOf(
                    "تجديد الإقامات مع التحقق من سداد الرسوم والتأمين الصحي",
                    "إصدار تأشيرات السفر المفردة والمتعددة",
                    "تعديل المهن وإسقاط العمالة وتحديث البيانات"
                ),
                requiredDocs = listOf("رقم الإقامة أو الحدود", "سريان التأمين الطبي ورخصة العمل"),
                processingDuration = "ساعة إلى 24 ساعة",
                officialFeeGuide = "رسوم الجوازات ورخصة العمل حسب النظام",
                isFeatured = true
            ),
            ServiceItem(
                id = "gov_zatca_tax",
                title = "الإقرارات الضريبية وشهادات الزكاة وضريبة الدخل",
                platformName = "هيئة الزكاة والضريبة والجمارك (ZATCA)",
                category = ServiceCategory.GOVERNMENT,
                description = "إعداد وتقديم إقرارات ضريبة القيمة المضافة (VAT) وإقرارات الزكاة الشرعية، إصدار شهادات التسجيل والزكاة، والاعتراض على التقديرات الجزافية.",
                detailedScope = listOf(
                    "مراجعة الفواتير الضريبية للمشتريات والمبيعات",
                    "حساب الضريبة المستحقة بدقة وفق المعايير المحاسبية",
                    "رفع الإقرار واستخراج فاتورة السداد الرسمية (سداد)"
                ),
                requiredDocs = listOf("ميزان المراجعة أو كشف الفواتير", "الرقم الضريبي", "السجل التجاري"),
                processingDuration = "1 - 3 أيام عمل",
                officialFeeGuide = "الضريبة المستحقة حسب الإقرار",
                badge = "امتثال ضريبي"
            ),
            ServiceItem(
                id = "gov_absher_business",
                title = "خدمات منصة أبشر أعمال وحلول التفويض",
                platformName = "منصة أبشر (Absher)",
                category = ServiceCategory.GOVERNMENT,
                description = "إدارة المركبات التجارية، التفويض الإلكتروني، إصدار وتجديد رخص السير، وتفعيل خدمات المنشأة.",
                detailedScope = listOf(
                    "التفويض الإلكتروني للقيادة ومراجعة الدوائر الحكومية",
                    "تحديث بيانات المنشأة والممثل النظامي",
                    "إصدار شهادات البيانات الرسمية"
                ),
                requiredDocs = listOf("الهوية الوطنية للمالك", "السجل التجاري"),
                processingDuration = "فوري - 24 ساعة",
                officialFeeGuide = "رسوم الخدمات الرسمية للمرور والجوازات"
            ),
            ServiceItem(
                id = "gov_balady_licenses",
                title = "إصدار وتجديد الرخص البلدية وشهادات الإشغال",
                platformName = "منصة بلدي (Balady)",
                category = ServiceCategory.GOVERNMENT,
                description = "استخراج وتجديد الرخص التجارية للمحلات والمستودعات والمكاتب، شهادات سلامة التمديدات، وإصدار اللوحات الإعلانية.",
                detailedScope = listOf(
                    "التحقق من اشتراطات كود البناء السعودي وتصريح الدفاع المدني",
                    "رفع المخططات وتحديد إحداثيات الموقع التجاري",
                    "متابعة التفتيش والاعتماد النهائي للرخصة"
                ),
                requiredDocs = listOf("عقد الإيجار الإلكتروني (إيجار)", "السجل التجاري", "شهادة الدفاع المدني"),
                processingDuration = "2 - 5 أيام عمل",
                officialFeeGuide = "رسوم الأمتار المعتمدة من أمانة المنطقة"
            ),
            ServiceItem(
                id = "gov_commercial_register",
                title = "تأسيس وتعديل السجلات التجارية والشركات",
                platformName = "وزارة التجارة (Ministry of Commerce)",
                category = ServiceCategory.PRIVATE_BUSINESS,
                description = "تأسيس الشركات (مساهمة، ذات مسؤولية محدودة، مهنية)، حجز الأسماء التجارية، فتح وتعديل الفروع، ومواءمة عقود التأسيس مع نظام الشركات الجديد.",
                detailedScope = listOf(
                    "صياغة عقد التأسيس ومطابقته لنظام الشركات السعودي الحديث",
                    "حجز الاسم التجاري وتوثيق الشركاء عبر منصة أبشر",
                    "إصدار السجل التجاري والاشتراك الفوري بالغرفة التجارية"
                ),
                requiredDocs = listOf("هويات الشركاء", "بيانات رأس المال والأنشطة الاقتصادية ISIC4"),
                processingDuration = "24 - 48 ساعة",
                officialFeeGuide = "رسوم وزارة التجارة والغرفة التجارية",
                isFeatured = true,
                badge = "تأسيس شركات"
            ),
            ServiceItem(
                id = "gov_gosi_social",
                title = "إدارة التأمينات الاجتماعية والشهادات المعتمدة",
                platformName = "المؤسسة العامة للتأمينات الاجتماعية (GOSI)",
                category = ServiceCategory.GOVERNMENT,
                description = "تسجيل واستبعاد المشتركين السعوديين وغير السعوديين، تعديل الأجور الخاضعة للاشتراك، وإصدار شهادات الالتزام التأميني وخلو الطرف.",
                detailedScope = listOf(
                    "تحديث مدد وأجور المشتركين وفق الضوابط النظامية",
                    "تسوية المستحقات وتقسيط الاشتراكات إن لزم",
                    "استخراج شهادة التأمينات لتقديمها للمناقصات والجهات الرسمية"
                ),
                requiredDocs = listOf("بيانات المنشأة التأمينية", "عقود المشتركين"),
                processingDuration = "نفس اليوم",
                officialFeeGuide = "حسب نسبة الاشتراكات المقررة"
            ),
            ServiceItem(
                id = "gov_najiz_legal",
                title = "خدمات منصة ناجز والتوثيق والوكالات الشرعية",
                platformName = "منصة ناجز (Najiz - وزارة العدل)",
                category = ServiceCategory.GOVERNMENT,
                description = "إصدار وفسخ الوكالات الإلكترونية للشركات، توثيق العقود والاتفاقيات، الاستعلام عن القضايا ومتابعة طلبات التنفيذ.",
                detailedScope = listOf(
                    "صياغة وإصدار الوكالات الإدارية والشرعية والتجارية",
                    "تقديم ومتابعة طلبات التنفيذ المالي وسندات لأمر",
                    "توثيق قرارات الشركاء والتنازلات"
                ),
                requiredDocs = listOf("الهوية الوطنية", "السجل التجاري أو صك الملكية"),
                processingDuration = "فوري إلكترونياً",
                officialFeeGuide = "مجانية أو حسب رسوم التوثيق الخاصة"
            ),
            ServiceItem(
                id = "it_custom_database",
                title = "برمجة نظم المعلومات وقواعد البيانات السحابية",
                platformName = "نظم المعلومات وحلول البرمجة",
                category = ServiceCategory.IT_SYSTEMS,
                description = "تصميم وتطوير نظم إدارة المؤسسات ERP المخصصة، قواعد البيانات السحابية، وربط التطبيقات بواجهات API المعيارية وتأمين تدفق البيانات.",
                detailedScope = listOf(
                    "هندسة وتصميم قواعد البيانات SQL & NoSQL عالية الأداء",
                    "بناء بوابات الويب وتطبيقات الأجهزة الذكية المتكاملة",
                    "تطبيق سياسات حماية البيانات والأمن السيبراني"
                ),
                requiredDocs = listOf("وثيقة المتطلبات التشغيلية", "بيانات البنية التحتية"),
                processingDuration = "حسب نطاق النظام",
                officialFeeGuide = "دراسة تحليلية وعرض سعر مخصص",
                badge = "حلول رقمية"
            )
        )
    }

    fun getSapModules(): List<SapModuleInfo> {
        return listOf(
            SapModuleInfo(
                code = "FI",
                nameArabic = "المحاسبة المالية",
                nameEnglish = "Financial Accounting",
                description = "إدارة دفتر الأستاذ العام، حسابات المدينين والدائنين، الأصول الثابتة، والتقارير المالية الدقيقة المتوافقة مع المعايير السعودية والدولية.",
                highlights = listOf("General Ledger (GL)", "Accounts Payable (AP)", "Accounts Receivable (AR)", "Asset Accounting (AA)")
            ),
            SapModuleInfo(
                code = "CO",
                nameArabic = "المحاسبة الإدارية والتحكم بالتكاليف",
                nameEnglish = "Controlling & Cost Management",
                description = "تحليل مراكز التكلفة، محاسبة أوامر العمل الداخلية، تحليل الربحية للمنتجات والخدمات وقياس الأداء المالي الداخلي.",
                highlights = listOf("Cost Center Accounting", "Internal Orders", "Profitability Analysis (CO-PA)", "Profit Center Accounting")
            ),
            SapModuleInfo(
                code = "MM",
                nameArabic = "إدارة المواد والمشتريات والمستودعات",
                nameEnglish = "Materials Management",
                description = "دورة المشتريات الكاملة بدءاً من طلب الشراء إلى أمر التوريد، تقييم الموردين، وإدارة حركات المخازن والتجرد الآلي.",
                highlights = listOf("Procurement Cycle", "Inventory Management", "Vendor Evaluation", "Physical Inventory")
            ),
            SapModuleInfo(
                code = "SD",
                nameArabic = "المبيعات والتوزيع وإدارة العملاء",
                nameEnglish = "Sales and Distribution",
                description = "إدارة عروض الأسعار، أوامر البيع، الشحن والتسليم، وتوليد فواتير المبيعات المرتبطة آلياً بالفوترة الإلكترونية لهيئة الزكاة.",
                highlights = listOf("Sales Order Processing", "Delivery & Shipping", "ZATCA E-Invoicing Integration", "Pricing & Discounts")
            ),
            SapModuleInfo(
                code = "HCM",
                nameArabic = "إدارة رأس المال البشري والموارد البشرية",
                nameEnglish = "Human Capital Management",
                description = "إدارة الهيكل التنظيمي، ملفات الموظفين، مسيرات الرواتب المتوافقة مع منصة مُدد، ومتابعة الإجازات والتقييم الدوري.",
                highlights = listOf("Organizational Management", "Personnel Administration", "Payroll & Mudad Compliance", "Time Management")
            ),
            SapModuleInfo(
                code = "ABAP",
                nameArabic = "برمجة وتطوير كود ساب المخصص",
                nameEnglish = "Advanced Business Application Programming",
                description = "تطوير شاشات ساب المخصصة، بناء واجهات الربط API / OData، تقارير ALV المتقدمة، وتعديل نماذج المخرجات Smart Forms & Adobe Forms.",
                highlights = listOf("ALV Reports & Custom Screens", "BAPI & RFC Integration", "Adobe & Smart Forms", "Fiori & UI5 Apps")
            )
        )
    }

    fun getPlatformGuides(): List<PlatformGuide> {
        return listOf(
            PlatformGuide(
                platformName = "قوى (Qiwa)",
                authority = "وزارة الموارد البشرية والتنمية الاجتماعية",
                purpose = "المنصة الموحدة لجميع خدمات العمل وتنظيم المنشآت وتوثيق العقود في المملكة.",
                commonServices = listOf("توثيق العقود", "إصدار التأشيرات المهنية", "نقل الموظفين", "شهادة السعودة"),
                portalUrl = "https://qiwa.sa",
                tips = "احرص على توثيق كافة عقود موظفيك لتجنب إيقاف خدمات المنشأة ولرفع نسبة الامتثال فوق 90%."
            ),
            PlatformGuide(
                platformName = "الزكاة والضريبة والجمارك (ZATCA)",
                authority = "هيئة الزكاة والضريبة والجمارك",
                purpose = "إدارة الامتثال الضريبي والزكوي وتطبيق منظومة الفوترة الإلكترونية المرحلة الثانية.",
                commonServices = listOf("إقرارات القيمة المضافة", "إقرارات الزكاة", "الربط والتكامل للفوترة", "شهادة الزكاة"),
                portalUrl = "https://zatca.gov.sa",
                tips = "يتطلب الربط بالمرحلة الثانية إعداد نظام ساب لإنشاء ختم التشفير CSID ورمز الاستجابة السريعة QR."
            ),
            PlatformGuide(
                platformName = "مُدد (Mudad)",
                authority = "وزارة الموارد البشرية والبنك المركزي السعودي",
                purpose = "حماية أجور العاملين في القطاع الخاص وإدارة الرواتب آلياً.",
                commonServices = listOf("رفع ملف الرواتب WIF", "تبرير ملاحظات الأجور", "نظام إدارة الرواتب"),
                portalUrl = "https://mudad.com.sa",
                tips = "يجب رفع ملف الرواتب شهرياً قبل تاريخ 10 لتفادي تنبيهات عدم الالتزام بنظام حماية الأجور."
            ),
            PlatformGuide(
                platformName = "مقيم (Muqeem)",
                authority = "المديرية العامة للجوازات",
                purpose = "التعاملات الإلكترونية للمقيمين والتابعين وإصدار الإقامات وتأشيرات السفر.",
                commonServices = listOf("إصدار وتجديد الإقامة", "تأشيرات الخروج والعودة", "تعديل المهنة", "نقل الخدمات"),
                portalUrl = "https://muqeem.sa",
                tips = "تأكد من سداد رسوم رخصة العمل والجوازات وتوفر التأمين الصحي الفعال قبل إجراء التجديد."
            ),
            PlatformGuide(
                platformName = "بلدي (Balady)",
                authority = "وزارة البلديات والإسكان",
                purpose = "بوابة الخدمات البلدية الموحدة للتراخيص المهنية والإنشائية والتجارية.",
                commonServices = listOf("إصدار رخصة تجارية", "تجديد رخصة محل", "تصاريح اللوحات الإعلانية", "شهادة إشغال"),
                portalUrl = "https://balady.gov.sa",
                tips = "ربط عقد إيجار الإلكتروني الموحد وشهادة سلامة الدفاع المدني شرط أساسي لإصدار الرخصة."
            ),
            PlatformGuide(
                platformName = "وزارة التجارة (Ministry of Commerce)",
                authority = "وزارة التجارة",
                purpose = "تنظيم وتطوير بيئة الأعمال التجارية والشركات وحماية المستهلك.",
                commonServices = listOf("إصدار سجل تجاري", "تعديل عقد التأسيس", "حجز الأسماء التجارية", "الغرفة التجارية"),
                portalUrl = "https://mc.gov.sa",
                tips = "استفد من نظام الشركات الجديد الذي يتيح إصدار سجلات بدون حد أدنى لرأس المال وسهولة نقل الحصص."
            )
        )
    }

    private suspend fun seedInitialDataIfNeeded() {
        if (orderDao.getCount() == 0) {
            val sampleOrders = listOf(
                ServiceOrderEntity(
                    id = "WTN-2026-1048",
                    clientName = "شركة أفق البناء للمقاولات",
                    clientPhone = "0501234567",
                    clientType = "شركة كبرى",
                    serviceTitle = "ربط وتكامل نظام ساب (SAP) مع الفوترة الإلكترونية ZATCA",
                    platformName = "هيئة الزكاة والضريبة والجمارك",
                    category = ServiceCategory.SAP_ERP.displayName,
                    notes = "تجهيز وتطبيق الربط المباشر للمرحلة الثانية مع خوادم SAP S/4HANA",
                    status = "جاري المعالجة والتدقيق",
                    trackingNotes = "تم الانتهاء من اختبار بيئة Sandbox وجاري استخراج شهادات CSID للإنتاج الحي.",
                    createdAtFormatted = "2026/10/12 11:30",
                    estimatedCompletion = "2026/10/18",
                    isUrgent = false
                ),
                ServiceOrderEntity(
                    id = "WTN-2026-1035",
                    clientName = "مؤسسة النور للتجارة الحديثة",
                    clientPhone = "0559876543",
                    clientType = "مؤسسة فردية",
                    serviceTitle = "توثيق عقود العمل وشهادة السعودة",
                    platformName = "منصة قوى (Qiwa)",
                    category = ServiceCategory.GOVERNMENT.displayName,
                    notes = "توثيق 14 عقد عمل وتحديث نسبة التوطين في نطاقات",
                    status = "مكتمل بنجاح",
                    trackingNotes = "تم اعتماد جميع العقود بنجاح وإصدار شهادة السعودة الرسمية.",
                    createdAtFormatted = "2026/10/11 09:15",
                    estimatedCompletion = "2026/10/12",
                    isUrgent = true
                ),
                ServiceOrderEntity(
                    id = "WTN-2026-1021",
                    clientName = "مجموعة التقنية المتكاملة",
                    clientPhone = "0543219876",
                    clientType = "شركة",
                    serviceTitle = "تجديد رخصة بلدية فورية ومطابقة الدفاع المدني",
                    platformName = "منصة بلدي",
                    category = ServiceCategory.GOVERNMENT.displayName,
                    notes = "تجديد رخصة المستودع المركزي بمدينة الرياض",
                    status = "في مرحلة الاعتماد",
                    trackingNotes = "المعاملة بانتظار الاعتماد النهائي من أمانة منطقة الرياض.",
                    createdAtFormatted = "2026/10/13 14:00",
                    estimatedCompletion = "2026/10/15",
                    isUrgent = false
                )
            )
            orderDao.insertAll(sampleOrders)
        }

        if (financialDao.getCount() == 0) {
            val sampleRecords = listOf(
                FinancialRecordEntity(
                    referenceNumber = "INV-DIG-2026-1001",
                    type = "فاتورة ضريبية",
                    clientName = "شركة أفق البناء للمقاولات",
                    clientTaxOrId = "310458921100003",
                    serviceTitle = "ربط وتكامل نظام ساب (SAP) مع الفوترة الإلكترونية ZATCA المرحلة الثانية",
                    amount = 12000.0,
                    taxAmount = 1800.0,
                    totalAmount = 13800.0,
                    issueDate = "2026/10/12",
                    status = "معتمد ومسدد",
                    notes = "دفعة تعاقدية للربط والتكامل البرمجي لنظام SAP"
                ),
                FinancialRecordEntity(
                    referenceNumber = "CON-DIG-2026-0084",
                    type = "عقد إلكتروني",
                    clientName = "مؤسسة النور للتجارة الحديثة",
                    clientTaxOrId = "1010784512",
                    serviceTitle = "إدارة ومتابعة المنصات الحكومية والامتثال السنوي",
                    amount = 8500.0,
                    taxAmount = 1275.0,
                    totalAmount = 9775.0,
                    issueDate = "2026/10/10",
                    status = "ساري المفعول",
                    notes = "عقد خدمات مساندة وإشراف إداري وتقني سنوي"
                ),
                FinancialRecordEntity(
                    referenceNumber = "REC-2026-0512",
                    type = "سند قبض",
                    clientName = "مجموعة التقنية المتكاملة",
                    clientTaxOrId = "1010332211",
                    serviceTitle = "استشارات تقنية وتطوير برمجيات نظم المعلومات",
                    amount = 4000.0,
                    taxAmount = 600.0,
                    totalAmount = 4600.0,
                    issueDate = "2026/10/13",
                    status = "مسدد بالكامل",
                    notes = "سند استلام مقابل الاستشارات التقنية"
                )
            )
            financialDao.insertAll(sampleRecords)
        }

        if (consultationDao.getCount() == 0) {
            consultationDao.insertBooking(
                ConsultationBookingEntity(
                    clientName = "عبدالرحمن الشمري",
                    phone = "0505551234",
                    email = "a.shammari@example.com",
                    consultationType = "استشارة نظم ساب والتحول الرقمي",
                    preferredDate = "2026/10/16",
                    preferredTime = "11:00 ص",
                    meetingType = "اجتماع مرئي افتراضي",
                    notes = "مناقشة متطلبات الترحيل السحابي وربط الفروع بنظام ساب",
                    supervisor = AlWatanConstants.OWNER_FULL_NAME,
                    status = "مؤكد",
                    bookedAtFormatted = "2026/10/13 16:20"
                )
            )
        }
    }
}
