package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Dns
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AlWatanConstants
import com.example.data.model.ServiceItem
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.ContactIntents
import com.example.ui.components.ServiceDetailDialog
import com.example.ui.components.ServiceOrderSubmissionDialog
import com.example.ui.screens.AdminPortalScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.ConsultationScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OfficialCredentialsScreen
import com.example.ui.screens.OrderTrackerScreen
import com.example.ui.screens.SapSystemsScreen
import com.example.ui.screens.ServicesScreen
import com.example.ui.theme.AlWatanTheme
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.SapAccentBlue

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AlWatanTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    val context = LocalContext.current
                    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
                    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
                    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
                    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
                    val allConsultations by viewModel.allConsultations.collectAsStateWithLifecycle()
                    val financialRecords by viewModel.allFinancialRecords.collectAsStateWithLifecycle()
                    val isAdminAuth by viewModel.isAdminAuthenticated.collectAsStateWithLifecycle()
                    val adminAuthError by viewModel.adminAuthError.collectAsStateWithLifecycle()
                    val selectedService by viewModel.selectedService.collectAsStateWithLifecycle()
                    val trackedOrder by viewModel.trackedOrder.collectAsStateWithLifecycle()
                    val searchAttempted by viewModel.trackingSearchAttempted.collectAsStateWithLifecycle()
                    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()
                    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
                    val isAuthLoading by viewModel.isAuthLoading.collectAsStateWithLifecycle()

                    var showOrderSubmissionDialog by remember { mutableStateOf(false) }
                    var showAccountDialog by remember { mutableStateOf(false) }
                    var serviceToSubmitFor by remember { mutableStateOf<ServiceItem?>(null) }

                    val snackbarHostState = remember { SnackbarHostState() }

                    LaunchedEffect(userMessage) {
                        userMessage?.let { msg ->
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                            viewModel.clearUserMessage()
                        }
                    }

                    // Handle Back Navigation when not on Home
                    BackHandler(enabled = currentScreen != AppScreen.HOME) {
                        viewModel.navigateTo(AppScreen.HOME)
                    }

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = AlWatanConstants.APP_TITLE,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(
                                            imageVector = Icons.Default.Verified,
                                            contentDescription = "موثق",
                                            tint = GoldSecondary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                },
                                navigationIcon = {
                                    IconButton(
                                        onClick = {
                                            ContactIntents.dialPhone(
                                                context,
                                                AlWatanConstants.OFFICIAL_PHONES.first()
                                            )
                                        },
                                        modifier = Modifier.testTag("top_app_bar_call_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Call,
                                            contentDescription = "الاتصال بالمكتب",
                                            tint = GreenPrimary
                                        )
                                    }
                                },
                                actions = {
                                    if (currentUser != null) {
                                        IconButton(
                                            onClick = { viewModel.navigateTo(AppScreen.AUTH) },
                                            modifier = Modifier.testTag("top_app_bar_account_button")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.AccountCircle,
                                                contentDescription = "حسابي",
                                                tint = GreenPrimary
                                            )
                                        }
                                    } else {
                                        androidx.compose.material3.TextButton(
                                            onClick = { viewModel.navigateTo(AppScreen.AUTH) },
                                            modifier = Modifier.testTag("top_app_bar_google_login_button")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.AccountCircle,
                                                contentDescription = null,
                                                tint = GreenPrimary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "دخول / تسجيل",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = GreenPrimary
                                            )
                                        }
                                    }

                                    IconButton(
                                        onClick = { viewModel.navigateTo(AppScreen.ADMIN) },
                                        modifier = Modifier.testTag("top_app_bar_admin_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AdminPanelSettings,
                                            contentDescription = "لوحة الإدارة",
                                            tint = if (isAdminAuth) GreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            )
                        },
                        bottomBar = {
                            NavigationBar(
                                modifier = Modifier
                                    .testTag("bottom_nav_bar")
                                    .windowInsetsPadding(WindowInsets.navigationBars),
                                containerColor = MaterialTheme.colorScheme.surface,
                                tonalElevation = 3.dp
                            ) {
                                NavigationBarItem(
                                    selected = currentScreen == AppScreen.HOME,
                                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                                    icon = {
                                        Icon(
                                            imageVector = if (currentScreen == AppScreen.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                                            contentDescription = "الرئيسية"
                                        )
                                    },
                                    label = { Text("الرئيسية", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = GreenPrimary,
                                        selectedTextColor = GreenPrimary
                                    ),
                                    modifier = Modifier.testTag("nav_item_home")
                                )

                                NavigationBarItem(
                                    selected = currentScreen == AppScreen.SERVICES,
                                    onClick = { viewModel.navigateTo(AppScreen.SERVICES) },
                                    icon = {
                                        Icon(
                                            imageVector = if (currentScreen == AppScreen.SERVICES) Icons.Filled.AccountBalance else Icons.Outlined.AccountBalance,
                                            contentDescription = "الخدمات"
                                        )
                                    },
                                    label = { Text("المنصات", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = GreenPrimary,
                                        selectedTextColor = GreenPrimary
                                    ),
                                    modifier = Modifier.testTag("nav_item_services")
                                )

                                NavigationBarItem(
                                    selected = currentScreen == AppScreen.SAP_SYSTEMS,
                                    onClick = { viewModel.navigateTo(AppScreen.SAP_SYSTEMS) },
                                    icon = {
                                        Icon(
                                            imageVector = if (currentScreen == AppScreen.SAP_SYSTEMS) Icons.Filled.Dns else Icons.Outlined.Dns,
                                            contentDescription = "ساب (SAP)"
                                        )
                                    },
                                    label = { Text("نظم ساب", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = SapAccentBlue,
                                        selectedTextColor = SapAccentBlue
                                    ),
                                    modifier = Modifier.testTag("nav_item_sap")
                                )

                                NavigationBarItem(
                                    selected = currentScreen == AppScreen.ORDER_TRACKER,
                                    onClick = { viewModel.navigateTo(AppScreen.ORDER_TRACKER) },
                                    icon = {
                                        BadgedBox(
                                            badge = {
                                                if (allOrders.isNotEmpty()) {
                                                    Badge { Text("${allOrders.size}") }
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = if (currentScreen == AppScreen.ORDER_TRACKER) Icons.Filled.TrackChanges else Icons.Outlined.TrackChanges,
                                                contentDescription = "متابعة الطلبات"
                                            )
                                        }
                                    },
                                    label = { Text("المتابعة", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = GreenPrimary,
                                        selectedTextColor = GreenPrimary
                                    ),
                                    modifier = Modifier.testTag("nav_item_tracker")
                                )

                                NavigationBarItem(
                                    selected = currentScreen == AppScreen.CONSULTATION,
                                    onClick = { viewModel.navigateTo(AppScreen.CONSULTATION) },
                                    icon = {
                                        Icon(
                                            imageVector = if (currentScreen == AppScreen.CONSULTATION) Icons.Filled.DateRange else Icons.Outlined.DateRange,
                                            contentDescription = "استشارة"
                                        )
                                    },
                                    label = { Text("استشارة", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = GoldSecondary,
                                        selectedTextColor = GoldSecondary
                                    ),
                                    modifier = Modifier.testTag("nav_item_consultation")
                                )
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (currentScreen) {
                                AppScreen.HOME -> HomeScreen(
                                    featuredServices = viewModel.services.filter { it.isFeatured },
                                    platformGuides = viewModel.platformGuides,
                                    onNavigate = { viewModel.navigateTo(it) },
                                    onSelectService = { service ->
                                        viewModel.selectService(service)
                                    },
                                    onRequestNewOrder = {
                                        serviceToSubmitFor = null
                                        showOrderSubmissionDialog = true
                                    },
                                    user = currentUser,
                                    isAuthLoading = isAuthLoading,
                                    onSignInClick = { viewModel.navigateTo(AppScreen.AUTH) },
                                    onSignOutClick = { viewModel.signOut() }
                                )

                                AppScreen.AUTH -> AuthScreen(
                                    currentUser = currentUser,
                                    isLoading = isAuthLoading,
                                    onSignInWithGoogle = { viewModel.signInWithGoogle(this@MainActivity) },
                                    onSignOut = { viewModel.signOut() },
                                    onContinueAsGuest = { viewModel.navigateTo(AppScreen.HOME) }
                                )

                                AppScreen.SERVICES -> ServicesScreen(
                                    services = viewModel.services,
                                    selectedCategory = selectedCategory,
                                    searchQuery = searchQuery,
                                    onCategorySelected = { viewModel.setSelectedCategory(it) },
                                    onSearchQueryChanged = { viewModel.setSearchQuery(it) },
                                    onSelectService = { service ->
                                        viewModel.selectService(service)
                                    }
                                )

                                AppScreen.SAP_SYSTEMS -> SapSystemsScreen(
                                    sapModules = viewModel.sapModules,
                                    onBookConsultation = { viewModel.navigateTo(AppScreen.CONSULTATION) },
                                    onRequestSapProject = {
                                        serviceToSubmitFor = viewModel.services.firstOrNull { it.id == "sap_implementation" }
                                        showOrderSubmissionDialog = true
                                    }
                                )

                                AppScreen.ORDER_TRACKER -> OrderTrackerScreen(
                                    allOrders = allOrders,
                                    trackedOrder = trackedOrder,
                                    searchAttempted = searchAttempted,
                                    onSearchOrder = { viewModel.searchOrder(it) },
                                    onRequestNewOrder = {
                                        serviceToSubmitFor = null
                                        showOrderSubmissionDialog = true
                                    }
                                )

                                AppScreen.CONSULTATION -> ConsultationScreen(
                                    consultations = allConsultations,
                                    onBookConsultation = { name, phone, email, type, date, time, meetingType, notes ->
                                        viewModel.bookConsultation(
                                            clientName = name,
                                            phone = phone,
                                            email = email,
                                            consultationType = type,
                                            preferredDate = date,
                                            preferredTime = time,
                                            meetingType = meetingType,
                                            notes = notes,
                                            onSuccess = {
                                                // Success message is shown via ViewModel state
                                            }
                                        )
                                    }
                                )

                                AppScreen.ADMIN -> AdminPortalScreen(
                                    isAuthenticated = isAdminAuth,
                                    authError = adminAuthError,
                                    allOrders = allOrders,
                                    financialRecords = financialRecords,
                                    onAuthenticate = { passcode ->
                                        viewModel.authenticateAdmin(passcode)
                                    },
                                    onLogout = { viewModel.logoutAdmin() },
                                    onUpdateOrderStatus = { orderId, newStatus, notes ->
                                        viewModel.updateOrderStatus(orderId, newStatus, notes)
                                    },
                                    onCreateFinancialRecord = { type, clientName, clientTaxOrId, serviceTitle, amount, notes ->
                                        viewModel.createFinancialRecord(
                                            type, clientName, clientTaxOrId, serviceTitle, amount, notes, onSuccess = {}
                                        )
                                    }
                                )

                                AppScreen.OFFICIAL_CREDENTIALS -> OfficialCredentialsScreen()
                            }
                        }
                    }

                    // Selected Service Detail Dialog
                    selectedService?.let { service ->
                        ServiceDetailDialog(
                            service = service,
                            onDismiss = { viewModel.selectService(null) },
                            onRequestNow = {
                                viewModel.selectService(null)
                                serviceToSubmitFor = service
                                showOrderSubmissionDialog = true
                            }
                        )
                    }

                    // Order Submission Dialog
                    if (showOrderSubmissionDialog) {
                        ServiceOrderSubmissionDialog(
                            preselectedService = serviceToSubmitFor,
                            onDismiss = { showOrderSubmissionDialog = false },
                            onSubmit = { name, phone, clientType, serviceTitle, platformName, category, notes, isUrgent ->
                                viewModel.submitNewOrder(
                                    clientName = name,
                                    clientPhone = phone,
                                    clientType = clientType,
                                    serviceTitle = serviceTitle,
                                    platformName = platformName,
                                    category = category,
                                    notes = notes,
                                    isUrgent = isUrgent,
                                    onSuccess = {
                                        showOrderSubmissionDialog = false
                                        viewModel.navigateTo(AppScreen.ORDER_TRACKER)
                                    }
                                )
                            }
                        )
                    }

                    // User Account Dialog
                    if (showAccountDialog && currentUser != null) {
                        com.example.ui.components.UserAccountDialog(
                            user = currentUser!!,
                            onDismiss = { showAccountDialog = false },
                            onSignOut = { viewModel.signOut() }
                        )
                    }
                }
            }
        }
    }
}
