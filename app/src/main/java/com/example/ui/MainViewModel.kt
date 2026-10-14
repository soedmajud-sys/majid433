package com.example.ui

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.AuthManager
import com.example.data.local.AppDatabase
import com.example.data.model.AlWatanConstants
import com.example.data.model.ConsultationBookingEntity
import com.example.data.model.FinancialRecordEntity
import com.example.data.model.PlatformGuide
import com.example.data.model.SapModuleInfo
import com.example.data.model.ServiceCategory
import com.example.data.model.ServiceItem
import com.example.data.model.ServiceOrderEntity
import com.example.data.repository.AlWatanFirestoreRepository
import com.example.data.repository.AlWatanRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen(val titleArabic: String) {
    HOME("الرئيسية"),
    SERVICES("المنصات والخدمات"),
    SAP_SYSTEMS("تخصص ساب (SAP)"),
    ORDER_TRACKER("متابعة الطلبات"),
    CONSULTATION("حجز استشارة"),
    ADMIN("لوحة الإدارة"),
    OFFICIAL_CREDENTIALS("الوثيقة والحقوق"),
    AUTH("بوابة الدخول والتسجيل")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AlWatanRepository
    private val firestoreRepository: AlWatanFirestoreRepository
    private val authManager: AuthManager = AuthManager(application)

    init {
        val db = AppDatabase.getDatabase(application)
        repository = AlWatanRepository(db)
        firestoreRepository = AlWatanFirestoreRepository(application)
        authManager.attemptSilentAutoSignIn(viewModelScope)
    }

    val currentUser: StateFlow<FirebaseUser?> = authManager.currentUser

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _selectedCategory = MutableStateFlow<ServiceCategory?>(null)
    val selectedCategory: StateFlow<ServiceCategory?> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Orders & Financials
    val allOrders: StateFlow<List<ServiceOrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allConsultations: StateFlow<List<ConsultationBookingEntity>> = repository.allConsultations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFinancialRecords: StateFlow<List<FinancialRecordEntity>> = repository.allFinancialRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Admin Authentication State
    private val _isAdminAuthenticated = MutableStateFlow(false)
    val isAdminAuthenticated: StateFlow<Boolean> = _isAdminAuthenticated.asStateFlow()

    private val _adminAuthError = MutableStateFlow<String?>(null)
    val adminAuthError: StateFlow<String?> = _adminAuthError.asStateFlow()

    // Selected Service for Details / Submission
    private val _selectedService = MutableStateFlow<ServiceItem?>(null)
    val selectedService: StateFlow<ServiceItem?> = _selectedService.asStateFlow()

    // Tracking Search
    private val _trackedOrder = MutableStateFlow<ServiceOrderEntity?>(null)
    val trackedOrder: StateFlow<ServiceOrderEntity?> = _trackedOrder.asStateFlow()

    private val _trackingSearchAttempted = MutableStateFlow(false)
    val trackingSearchAttempted: StateFlow<Boolean> = _trackingSearchAttempted.asStateFlow()

    // Notification / Toast Message
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    // Auth Loading State
    private val _isAuthLoading = MutableStateFlow(false)
    val isAuthLoading: StateFlow<Boolean> = _isAuthLoading.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun setSelectedCategory(category: ServiceCategory?) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectService(service: ServiceItem?) {
        _selectedService.value = service
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    fun signInWithGoogle(activity: Activity) {
        _isAuthLoading.value = true
        authManager.signInWithGoogle(
            activity = activity,
            scope = viewModelScope,
            onSuccess = { user ->
                _isAuthLoading.value = false
                _userMessage.value = "تم تسجيل الدخول بنجاح: ${user.displayName ?: user.email}"
                viewModelScope.launch {
                    try {
                        firestoreRepository.saveUserProfile(
                            displayName = user.displayName ?: "عميل منصة الوطن",
                            email = user.email ?: ""
                        )
                    } catch (e: Exception) {
                        Log.w("MainViewModel", "Could not sync user profile to Firestore: ${e.message}")
                    }
                }
            },
            onError = { error ->
                _isAuthLoading.value = false
                _userMessage.value = "تعذر تسجيل الدخول: $error"
            },
            onCancelled = {
                _isAuthLoading.value = false
            }
        )
    }

    fun signOut() {
        authManager.signOut(viewModelScope) {
            _userMessage.value = "تم تسجيل الخروج بنجاح"
        }
    }

    fun authenticateAdmin(passcode: String): Boolean {
        return if (passcode.trim() == AlWatanConstants.ADMIN_ACCESS_KEY) {
            _isAdminAuthenticated.value = true
            _adminAuthError.value = null
            true
        } else {
            _adminAuthError.value = "رمز الدخول غير صحيح، يرجى التحقق من المفتاح الإداري المعتمد"
            false
        }
    }

    fun logoutAdmin() {
        _isAdminAuthenticated.value = false
    }

    fun submitNewOrder(
        clientName: String,
        clientPhone: String,
        clientType: String,
        serviceTitle: String,
        platformName: String,
        category: String,
        notes: String,
        isUrgent: Boolean,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            // Save locally in Room
            val orderId = repository.submitServiceOrder(
                clientName = clientName,
                clientPhone = clientPhone,
                clientType = clientType,
                serviceTitle = serviceTitle,
                platformName = platformName,
                category = category,
                notes = notes,
                isUrgent = isUrgent
            )

            // If user is logged into Firebase, sync to Firestore
            if (currentUser.value != null) {
                try {
                    firestoreRepository.createServiceOrder(
                        clientName = clientName,
                        clientPhone = clientPhone,
                        clientType = clientType,
                        serviceTitle = serviceTitle,
                        platformName = platformName,
                        category = category,
                        notes = notes,
                        isUrgent = isUrgent
                    )
                    _userMessage.value = "تم تقديم طلبك وحفظه سحابياً برقم: $orderId"
                } catch (e: Exception) {
                    Log.w("MainViewModel", "Saved locally, Firestore sync pending: ${e.message}")
                    _userMessage.value = "تم تقديم طلبك بنجاح برقم المعاملة: $orderId"
                }
            } else {
                _userMessage.value = "تم تقديم طلبك بنجاح برقم المعاملة: $orderId"
            }

            onSuccess(orderId)
        }
    }

    fun searchOrder(query: String) {
        viewModelScope.launch {
            _trackingSearchAttempted.value = true
            val cleanQuery = query.trim()
            if (cleanQuery.isEmpty()) {
                _trackedOrder.value = null
                return@launch
            }
            val byId = repository.getOrderById(cleanQuery)
            if (byId != null) {
                _trackedOrder.value = byId
            } else {
                val byPhone = allOrders.value.firstOrNull {
                    it.clientPhone.contains(cleanQuery) || it.id.contains(cleanQuery, ignoreCase = true)
                }
                _trackedOrder.value = byPhone
            }
        }
    }

    fun bookConsultation(
        clientName: String,
        phone: String,
        email: String,
        consultationType: String,
        preferredDate: String,
        preferredTime: String,
        meetingType: String,
        notes: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.bookConsultation(
                clientName = clientName,
                phone = phone,
                email = email,
                consultationType = consultationType,
                preferredDate = preferredDate,
                preferredTime = preferredTime,
                meetingType = meetingType,
                notes = notes
            )

            if (currentUser.value != null) {
                try {
                    firestoreRepository.createConsultation(
                        clientName = clientName,
                        phone = phone,
                        email = email,
                        consultationType = consultationType,
                        preferredDate = preferredDate,
                        preferredTime = preferredTime,
                        meetingType = meetingType,
                        notes = notes
                    )
                } catch (e: Exception) {
                    Log.w("MainViewModel", "Firestore consultation sync pending: ${e.message}")
                }
            }

            _userMessage.value = "تم تأكيد موعد استشارتك مع الأستاذ ماجد سعود العميري بنجاح!"
            onSuccess()
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: String, notes: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, newStatus, notes)
            _userMessage.value = "تم تحديث حالة المعاملة $orderId إلى: $newStatus"
        }
    }

    fun createFinancialRecord(
        type: String,
        clientName: String,
        clientTaxOrId: String,
        serviceTitle: String,
        amount: Double,
        notes: String,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            val refNumber = repository.createFinancialRecord(
                type = type,
                clientName = clientName,
                clientTaxOrId = clientTaxOrId,
                serviceTitle = serviceTitle,
                amount = amount,
                notes = notes
            )
            _userMessage.value = "تم إصدار $type برقم مرجعي: $refNumber"
            onSuccess(refNumber)
        }
    }

    // Catalogs
    val services: List<ServiceItem> = repository.getServicesList()
    val sapModules: List<SapModuleInfo> = repository.getSapModules()
    val platformGuides: List<PlatformGuide> = repository.getPlatformGuides()
}
