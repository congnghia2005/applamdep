package com.example.app_lam_dep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.app_lam_dep.ui.components.AppHeader
import com.example.app_lam_dep.ui.components.BottomNavigationBar
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
                var activeChatName by remember { mutableStateOf<String?>(null) }
                
                // Customer navigation sub-states
                var selectedCategoryName by remember { mutableStateOf<String?>(null) }
                var selectedSalonName by remember { mutableStateOf<String?>(null) }
                var selectedBookingServiceName by remember { mutableStateOf<String?>(null) }

                if (!isLoggedIn) {
                    AuthScreen(
                        onLoginSuccess = { isLoggedIn = true }
                    )
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            if (!viewingPartnerReviews && activeChatName == null && selectedSalonName == null && selectedBookingServiceName == null && selectedCategoryName == null) {
                                AppHeader(
                                    currentRole = currentRole,
                                    onRoleChange = { role ->
                                        currentRole = role
                                        currentTab = 0
                                        viewingPartnerReviews = false
                                        activeChatName = null
                                        selectedCategoryName = null
                                        selectedSalonName = null
                                        selectedBookingServiceName = null
                                    }
                                )
                            }
                        },
                        bottomBar = {
                            if (currentRole != 2 && !viewingPartnerReviews && activeChatName == null && selectedSalonName == null && selectedBookingServiceName == null && selectedCategoryName == null) {
                                BottomNavigationBar(
                                    isPartnerMode = (currentRole == 1),
                                    currentTab = currentTab,
                                    onTabSelected = { 
                                        currentTab = it
                                        viewingPartnerReviews = false
                                        activeChatName = null
                                        selectedCategoryName = null
                                        selectedSalonName = null
                                        selectedBookingServiceName = null
                                    }
                                )
                            }
                        }
                    ) { innerPadding ->
                        when (currentRole) {
                            0 -> { // Customer
                                when {
                                    selectedBookingServiceName != null -> {
                                        BookingScreen(
                                            serviceName = selectedBookingServiceName!!,
                                            salonName = selectedSalonName ?: "L'Amour Luxury Spa & Nail",
                                            onBack = { selectedBookingServiceName = null },
                                            onConfirmBooking = {
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
                                            1 -> CustomerBookingsScreen(modifier = Modifier.padding(innerPadding))
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
                                        3 -> PartnerProfileScreen(modifier = Modifier.padding(innerPadding))
                                        else -> PartnerDashboardScreen(
                                            onNavigateToReviews = { viewingPartnerReviews = true },
                                            modifier = Modifier.padding(innerPadding)
                                        )
                                    }
                                }
                            }
                            else -> { // Admin
                                AdminDashboardScreen(modifier = Modifier.padding(innerPadding))
                            }
                        }
                    }
                }
            }
        }
    }
}
