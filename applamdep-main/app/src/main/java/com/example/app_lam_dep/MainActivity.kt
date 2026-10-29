package com.example.app_lam_dep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.app_lam_dep.model.BookingTicket
import com.example.app_lam_dep.ui.components.AppHeader
import com.example.app_lam_dep.ui.components.BottomNavigationBar
import com.example.app_lam_dep.ui.components.DraggableFloatingAiButton
import com.example.app_lam_dep.ui.screens.AdminDashboardScreen
import com.example.app_lam_dep.ui.screens.AuthScreen
import com.example.app_lam_dep.ui.screens.BookingScreen
import com.example.app_lam_dep.ui.screens.CategorySalonsScreen
import com.example.app_lam_dep.ui.screens.ChatDetailScreen
import com.example.app_lam_dep.ui.screens.CustomerBookingsScreen
import com.example.app_lam_dep.ui.screens.CustomerHomeScreen
import com.example.app_lam_dep.ui.screens.MessagesAndActivityScreen
import com.example.app_lam_dep.ui.screens.CustomerProfileScreen
import com.example.app_lam_dep.ui.screens.PartnerDashboardScreen
import com.example.app_lam_dep.ui.screens.PartnerProfileScreen
import com.example.app_lam_dep.ui.screens.PartnerReviewsScreen
import com.example.app_lam_dep.ui.screens.PartnerScheduleScreen
import com.example.app_lam_dep.ui.screens.SalonDetailScreen
import com.example.app_lam_dep.ui.screens.ShopMembershipPlansScreen
import com.example.app_lam_dep.ui.theme.ApplamdepTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApplamdepTheme {
                var isLoggedIn by remember { mutableStateOf(false) }
                var currentRole by remember { mutableStateOf(0) } // 0: Customer, 1: Partner, 2: Admin
                var currentTab by remember { mutableStateOf(0) } // 0..3
                var viewingPartnerReviews by remember { mutableStateOf(false) }
                var viewingMembershipPlans by remember { mutableStateOf(false) }
                var currentShopPlan by remember { mutableStateOf("Gói VIP Sponsor (Còn 24 ngày)") }
                var activeChatName by remember { mutableStateOf<String?>(null) }
                
                // Customer dynamic bookings list
                val customerBookings = remember {
                    mutableStateListOf(
                        BookingTicket(
                            id = "#GLOW-8921",
                            salonName = "L’Amour Haute Nail & Lash Lounge",
                            serviceName = "Sơn Gel Hữu Cơ Pháp + Dưỡng Móng (45p)",
                            servicePrice = "180.000 đ",
                            date = "Hôm nay",
                            timeSlot = "15:30",
                            specialist = "Master Linh (VIP)",
                            customerName = "Quý cô Khánh Linh",
                            customerPhone = "0903 123 456",
                            note = "Sơn tone hồng thạch tự nhiên, da tay nhạy cảm",
                            requiresDeposit = true,
                            depositAmount = 50000L,
                            status = "ĐÃ XÁC NHẬN & ĐẶT CỌC"
                        )
                    )
                }

                // Customer navigation sub-states
                var selectedCategoryName by remember { mutableStateOf<String?>(null) }
                var selectedSalonName by remember { mutableStateOf<String?>(null) }
                var selectedBookingServiceName by remember { mutableStateOf<String?>(null) }
                var selectedShopId by remember { mutableStateOf<String?>(null) }
                var viewingCustomerShopList by remember { mutableStateOf(false) }
                var bookingShopId by remember { mutableStateOf<String?>(null) }
                var bookingServiceId by remember { mutableStateOf<String?>(null) }
                var confirmedBookingId by remember { mutableStateOf<String?>(null) }
                var pendingQrTopUpTx by remember { mutableStateOf<com.applamdep.customer.domain.model.TopUpTransaction?>(null) }
                var reviewingBookingId by remember { mutableStateOf<String?>(null) }
                var viewingCustomerReviews by remember { mutableStateOf(false) }
                var merchantSubScreen by remember { mutableStateOf<String?>(null) }

                if (!isLoggedIn) {
                    AuthScreen(
                        onLoginSuccess = { isLoggedIn = true }
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            topBar = {
                                val isSubScreen = viewingPartnerReviews || viewingMembershipPlans || activeChatName != null || selectedSalonName != null || selectedBookingServiceName != null || selectedCategoryName != null || selectedShopId != null || viewingCustomerShopList || bookingShopId != null || confirmedBookingId != null || pendingQrTopUpTx != null || reviewingBookingId != null || viewingCustomerReviews || merchantSubScreen != null
                                if (!isSubScreen) {
                                    AppHeader(
                                        currentRole = currentRole,
                                        onRoleChange = { role ->
                                            currentRole = role
                                            currentTab = 0
                                            viewingPartnerReviews = false
                                            viewingMembershipPlans = false
                                            activeChatName = null
                                            selectedCategoryName = null
                                            selectedSalonName = null
                                            selectedBookingServiceName = null
                                            selectedShopId = null
                                            viewingCustomerShopList = false
                                            bookingShopId = null
                                            bookingServiceId = null
                                            confirmedBookingId = null
                                            pendingQrTopUpTx = null
                                            reviewingBookingId = null
                                            viewingCustomerReviews = false
                                            merchantSubScreen = null
                                        }
                                    )
                                }
                            },
                            bottomBar = {
                                val isSubScreen = viewingPartnerReviews || viewingMembershipPlans || activeChatName != null || selectedSalonName != null || selectedBookingServiceName != null || selectedCategoryName != null || selectedShopId != null || viewingCustomerShopList || bookingShopId != null || confirmedBookingId != null || pendingQrTopUpTx != null || reviewingBookingId != null || viewingCustomerReviews || merchantSubScreen != null
                                if (!isSubScreen) {
                                    BottomNavigationBar(
                                        currentRole = currentRole,
                                        currentTab = currentTab,
                                        onTabSelected = { 
                                            currentTab = it
                                            viewingPartnerReviews = false
                                            viewingMembershipPlans = false
                                            activeChatName = null
                                            selectedCategoryName = null
                                            selectedSalonName = null
                                            selectedBookingServiceName = null
                                            selectedShopId = null
                                            viewingCustomerShopList = false
                                            bookingShopId = null
                                            bookingServiceId = null
                                            confirmedBookingId = null
                                            pendingQrTopUpTx = null
                                            reviewingBookingId = null
                                            viewingCustomerReviews = false
                                            merchantSubScreen = null
                                        }
                                    )
                                }
                            }
                        ) { innerPadding ->
                            if (viewingMembershipPlans) {
                                ShopMembershipPlansScreen(
                                    currentPlanName = currentShopPlan,
                                    onBack = { viewingMembershipPlans = false },
                                    onPlanSubscribed = { newPlan ->
                                        currentShopPlan = newPlan
                                        viewingMembershipPlans = false
                                    },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            } else when (currentRole) {
                                0 -> { // Customer
                                    when {
                                        reviewingBookingId != null -> {
                                            com.applamdep.customer.ui.review.WriteReviewScreen(
                                                bookingId = reviewingBookingId!!,
                                                onBack = { reviewingBookingId = null },
                                                onReviewSubmitted = { reviewingBookingId = null },
                                                modifier = Modifier.padding(innerPadding)
                                            )
                                        }
                                        viewingCustomerReviews -> {
                                            com.applamdep.customer.ui.review.CustomerReviewsScreen(
                                                onBack = { viewingCustomerReviews = false },
                                                modifier = Modifier.padding(innerPadding)
                                            )
                                        }
                                        pendingQrTopUpTx != null -> {
                                            val walletVm: com.applamdep.customer.ui.wallet.WalletViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = com.applamdep.customer.core.di.ViewModelFactory.Factory)
                                            com.applamdep.customer.ui.wallet.MockPaymentQrScreen(
                                                transaction = pendingQrTopUpTx!!,
                                                onPaymentSuccess = {
                                                    pendingQrTopUpTx = null
                                                },
                                                onBack = {
                                                    pendingQrTopUpTx = null
                                                },
                                                viewModel = walletVm,
                                                modifier = Modifier.padding(innerPadding)
                                            )
                                        }
                                        confirmedBookingId != null -> {
                                            com.applamdep.customer.ui.booking.BookingConfirmationScreen(
                                                bookingId = confirmedBookingId!!,
                                                onViewHistory = {
                                                    confirmedBookingId = null
                                                    currentTab = 1
                                                },
                                                onBackHome = {
                                                    confirmedBookingId = null
                                                    currentTab = 0
                                                },
                                                modifier = Modifier.padding(innerPadding)
                                            )
                                        }
                                        bookingShopId != null && bookingServiceId != null -> {
                                            com.applamdep.customer.ui.booking.BookingScreen(
                                                shopId = bookingShopId!!,
                                                serviceId = bookingServiceId!!,
                                                onBack = {
                                                    bookingShopId = null
                                                    bookingServiceId = null
                                                },
                                                onBookingSuccess = { bId ->
                                                    confirmedBookingId = bId
                                                    bookingShopId = null
                                                    bookingServiceId = null
                                                },
                                                modifier = Modifier.padding(innerPadding)
                                            )
                                        }
                                        selectedShopId != null -> {
                                            com.applamdep.customer.ui.shop.ShopDetailScreen(
                                                shopId = selectedShopId!!,
                                                onBack = { selectedShopId = null },
                                                onSelectServiceForBooking = { sId, srvId ->
                                                    bookingShopId = sId
                                                    bookingServiceId = srvId
                                                    selectedShopId = null
                                                },
                                                modifier = Modifier.padding(innerPadding)
                                            )
                                        }
                                        viewingCustomerShopList -> {
                                            com.applamdep.customer.ui.shop.ShopListScreen(
                                                onNavigateToShopDetail = { sId ->
                                                    selectedShopId = sId
                                                },
                                                modifier = Modifier.padding(innerPadding)
                                            )
                                        }
                                        selectedSalonName != null -> {
                                            SalonDetailScreen(
                                                salonName = selectedSalonName!!,
                                                onBack = { selectedSalonName = null },
                                                onSelectServiceForBooking = { sName ->
                                                    selectedBookingServiceName = sName
                                                },
                                                modifier = Modifier.padding(innerPadding)
                                            )
                                        }
                                        selectedCategoryName != null -> {
                                            CategorySalonsScreen(
                                                categoryName = selectedCategoryName!!,
                                                onBack = { selectedCategoryName = null },
                                                onNavigateToSalon = { sName -> selectedShopId = resolveShopId(sName) },
                                                onSelectServiceForBooking = { sName ->
                                                    bookingShopId = "salon_01"
                                                    bookingServiceId = "srv_01"
                                                },
                                                modifier = Modifier.padding(innerPadding)
                                            )
                                        }
                                        selectedBookingServiceName != null -> {
                                            BookingScreen(
                                                serviceName = selectedBookingServiceName!!,
                                                salonName = selectedSalonName ?: "1994 Nailsroom",
                                                onBack = { selectedBookingServiceName = null },
                                                onConfirmBooking = {
                                                    selectedBookingServiceName = null
                                                    selectedSalonName = null
                                                    selectedCategoryName = null
                                                    currentTab = 1
                                                },
                                                modifier = Modifier.padding(innerPadding)
                                            )
                                        }
                                        else -> {
                                            when (currentTab) {
                                                1 -> com.applamdep.customer.ui.booking.BookingHistoryScreen(
                                                    onNavigateToReview = { bId -> reviewingBookingId = bId },
                                                    onViewCustomerReviews = { viewingCustomerReviews = true },
                                                    modifier = Modifier.padding(innerPadding)
                                                )
                                                2 -> {
                                                    if (activeChatName != null) {
                                                        ChatDetailScreen(
                                                            chatName = activeChatName!!,
                                                            onBack = { activeChatName = null }
                                                        )
                                                    } else {
                                                        MessagesAndActivityScreen(
                                                            onChatClick = { activeChatName = it },
                                                            modifier = Modifier.padding(innerPadding)
                                                        )
                                                    }
                                                }
                                                3 -> com.applamdep.customer.ui.wallet.WalletScreen(
                                                    onNavigateToQrPayment = { tx ->
                                                        pendingQrTopUpTx = tx
                                                    },
                                                    modifier = Modifier.padding(innerPadding)
                                                )
                                                else -> CustomerHomeScreen(
                                                    onNavigateToSalon = { sName -> 
                                                        selectedShopId = resolveShopId(sName)
                                                    },
                                                    onSelectServiceForBooking = { sName ->
                                                        bookingShopId = "salon_01"
                                                        bookingServiceId = "srv_01"
                                                    },
                                                    onSelectCategory = { viewingCustomerShopList = true },
                                                    onOpenShopPlans = { viewingMembershipPlans = true },
                                                    modifier = Modifier.padding(innerPadding)
                                                )
                                            }
                                        }
                                    }
                                }
                                1 -> { // Partner (Merchant Portal - 10 Chức năng quy chuẩn)
                                    when (merchantSubScreen) {
                                        "BOOKINGS" -> com.applamdep.merchant.ui.booking.MerchantBookingListScreen(
                                            onBack = { merchantSubScreen = null },
                                            modifier = Modifier.padding(innerPadding)
                                        )
                                        "SERVICES" -> com.applamdep.merchant.ui.service.MerchantServiceManagementScreen(
                                            onBack = { merchantSubScreen = null },
                                            modifier = Modifier.padding(innerPadding)
                                        )
                                        "SCHEDULE" -> com.applamdep.merchant.ui.schedule.MerchantScheduleScreen(
                                            onBack = { merchantSubScreen = null },
                                            modifier = Modifier.padding(innerPadding)
                                        )
                                        "REVIEWS" -> com.applamdep.merchant.ui.review.MerchantReviewsScreen(
                                            onBack = { merchantSubScreen = null },
                                            modifier = Modifier.padding(innerPadding)
                                        )
                                        "FINANCE" -> com.applamdep.merchant.ui.finance.MerchantFinanceScreen(
                                            onBack = { merchantSubScreen = null },
                                            modifier = Modifier.padding(innerPadding)
                                        )
                                        "PROFILE" -> com.applamdep.merchant.ui.profile.MerchantProfileScreen(
                                            onBack = { merchantSubScreen = null },
                                            onNavigateToRegistration = { merchantSubScreen = "REGISTRATION" },
                                            modifier = Modifier.padding(innerPadding)
                                        )
                                        "REGISTRATION" -> com.applamdep.merchant.ui.register.MerchantRegisterScreen(
                                            onBack = { merchantSubScreen = "PROFILE" },
                                            onRegistrationSubmitted = { merchantSubScreen = "PROFILE" },
                                            modifier = Modifier.padding(innerPadding)
                                        )
                                        else -> {
                                            when (currentTab) {
                                                1 -> com.applamdep.merchant.ui.booking.MerchantBookingListScreen(
                                                    onBack = { currentTab = 0 },
                                                    modifier = Modifier.padding(innerPadding)
                                                )
                                                2 -> com.applamdep.merchant.ui.schedule.MerchantScheduleScreen(
                                                    onBack = { currentTab = 0 },
                                                    modifier = Modifier.padding(innerPadding)
                                                )
                                                3 -> com.applamdep.merchant.ui.finance.MerchantFinanceScreen(
                                                    onBack = { currentTab = 0 },
                                                    modifier = Modifier.padding(innerPadding)
                                                )
                                                else -> com.applamdep.merchant.ui.dashboard.MerchantDashboardScreen(
                                                    onNavigateToBookings = { merchantSubScreen = "BOOKINGS" },
                                                    onNavigateToServices = { merchantSubScreen = "SERVICES" },
                                                    onNavigateToSchedule = { merchantSubScreen = "SCHEDULE" },
                                                    onNavigateToReviews = { merchantSubScreen = "REVIEWS" },
                                                    onNavigateToFinance = { merchantSubScreen = "FINANCE" },
                                                    onNavigateToProfile = { merchantSubScreen = "PROFILE" },
                                                    modifier = Modifier.padding(innerPadding)
                                                )
                                            }
                                        }
                                    }
                                }
                                else -> { // Admin
                                    AdminDashboardScreen(
                                        selectedTab = currentTab,
                                        onTabChange = { currentTab = it },
                                        modifier = Modifier.padding(innerPadding)
                                    )
                                }
                            }
                        }

                        // Floating Draggable AI Chatbot Button (visible in Customer mode)
                        if (currentRole == 0 && selectedBookingServiceName == null) {
                            DraggableFloatingAiButton(
                                onNavigateToSalon = { sName -> selectedShopId = resolveShopId(sName) },
                                onSelectServiceForBooking = { sName, sShop ->
                                    selectedShopId = resolveShopId(sShop)
                                    bookingShopId = resolveShopId(sShop)
                                    bookingServiceId = "srv_01"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun resolveShopId(input: String): String {
    return when {
        input.startsWith("salon_") -> input
        input.contains("1994", true) || input.contains("L'Amour", true) || input.contains("L’Amour", true) -> "salon_01"
        input.contains("BONNIE", true) -> "salon_02"
        input.contains("GÀ SPA", true) || input.contains("Ga Spa", true) -> "salon_03"
        input.contains("HIEN", true) || input.contains("Hienz", true) -> "salon_04"
        input.contains("KỲ DUYÊN", true) || input.contains("Ky Duyen", true) -> "salon_05"
        input.contains("MNH", true) || input.contains("Thuy", true) -> "salon_06"
        input.contains("1996", true) -> "salon_07"
        input.contains("SI MÊ", true) || input.contains("Sime", true) -> "salon_08"
        input.contains("Thì Là", true) || input.contains("Thi La", true) || input.contains("An Yên", true) -> "salon_09"
        input.contains("Tun", true) -> "salon_10"
        input.contains("1m65", true) -> "salon_11"
        else -> "salon_01"
    }
}

