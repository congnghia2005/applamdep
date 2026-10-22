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

                if (!isLoggedIn) {
                    AuthScreen(
                        onLoginSuccess = { isLoggedIn = true }
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            topBar = {
                                if (!viewingPartnerReviews && !viewingMembershipPlans && activeChatName == null && selectedSalonName == null && selectedBookingServiceName == null && selectedCategoryName == null) {
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
                                        }
                                    )
                                }
                            },
                            bottomBar = {
                                if (!viewingPartnerReviews && !viewingMembershipPlans && activeChatName == null && selectedSalonName == null && selectedBookingServiceName == null && selectedCategoryName == null) {
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
                                        selectedBookingServiceName != null -> {
                                            val salon = selectedSalonName ?: "L'Amour Luxury Spa & Nail"
                                            // Demo flexible deposit: An Yên does not require deposit; L'Amour requires deposit
                                            val needDeposit = !salon.contains("An Yên", ignoreCase = true)
                                            val tiers = listOf(50000L, 100000L, 200000L, 300000L, 500000L)

                                            BookingScreen(
                                                serviceName = selectedBookingServiceName!!,
                                                salonName = salon,
                                                requiresDeposit = needDeposit,
                                                depositTiers = tiers,
                                                onBack = { selectedBookingServiceName = null },
                                                onConfirmBooking = { newTicket ->
                                                    customerBookings.add(0, newTicket)
                                                    selectedBookingServiceName = null
                                                    selectedSalonName = null
                                                    selectedCategoryName = null
                                                    currentTab = 1 // Switch to Bookings tab
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
                                                onNavigateToSalon = { sName -> selectedSalonName = sName },
                                                onSelectServiceForBooking = { sName -> selectedBookingServiceName = sName },
                                                modifier = Modifier.padding(innerPadding)
                                            )
                                        }
                                        else -> {
                                            when (currentTab) {
                                                1 -> CustomerBookingsScreen(
                                                    bookings = customerBookings,
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
                                                3 -> CustomerProfileScreen(modifier = Modifier.padding(innerPadding))
                                                else -> CustomerHomeScreen(
                                                    onNavigateToSalon = { sName -> selectedSalonName = sName },
                                                    onSelectServiceForBooking = { sName -> selectedBookingServiceName = sName },
                                                    onSelectCategory = { cName -> selectedCategoryName = cName },
                                                    onOpenShopPlans = { viewingMembershipPlans = true },
                                                    modifier = Modifier.padding(innerPadding)
                                                )
                                            }
                                        }
                                    }
                                }
                                1 -> { // Partner (Shop)
                                    if (viewingPartnerReviews) {
                                        PartnerReviewsScreen(
                                            onBack = { viewingPartnerReviews = false },
                                            modifier = Modifier.padding(innerPadding)
                                        )
                                    } else {
                                        when (currentTab) {
                                            1 -> PartnerScheduleScreen(modifier = Modifier.padding(innerPadding))
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
                                            3 -> PartnerProfileScreen(
                                                currentPlanName = currentShopPlan,
                                                onOpenMembershipPlans = { viewingMembershipPlans = true },
                                                modifier = Modifier.padding(innerPadding)
                                            )
                                            else -> PartnerDashboardScreen(
                                                onNavigateToReviews = { viewingPartnerReviews = true },
                                                modifier = Modifier.padding(innerPadding)
                                            )
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
                                onNavigateToSalon = { sName -> selectedSalonName = sName },
                                onSelectServiceForBooking = { sName, sShop ->
                                    selectedSalonName = sShop
                                    selectedBookingServiceName = sName
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
