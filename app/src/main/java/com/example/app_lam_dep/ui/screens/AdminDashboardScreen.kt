package com.example.app_lam_dep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_lam_dep.ui.icons.GlowIcon
import com.example.app_lam_dep.ui.icons.GlowIcons
import com.example.app_lam_dep.ui.theme.ChampagneGold
import com.example.app_lam_dep.ui.theme.DustyMerlot
import com.example.app_lam_dep.ui.theme.GoldSoft
import com.example.app_lam_dep.ui.theme.PinkPanther
import com.example.app_lam_dep.ui.theme.SuccessGreen
import com.example.app_lam_dep.ui.theme.TextMuted
import com.example.app_lam_dep.ui.theme.WarningOrange

// Data models for Admin Management
data class AdminShopItem(
    val id: String,
    val name: String,
    val code: String,
    val phone: String,
    val representative: String,
    val address: String,
    val regDate: String,
    var status: String, // "Chờ duyệt", "Đang hoạt động", "Tạm khóa", "Từ chối"
    val legalDoc: String,
    val verifiedDate: String,
    val servicesCount: Int,
    val bookingsCount: Int,
    val rating: Double,
    val violationHistory: String = "Không có vi phạm"
)

data class AdminCustomerItem(
    val id: String,
    val name: String,
    val code: String,
    val phone: String,
    val email: String,
    val joinDate: String,
    val bookingsCount: Int,
    val cancelCount: Int,
    val totalSpent: String,
    val depositHeld: String,
    var status: String // "Đang hoạt động", "Tạm khóa"
)

data class AdminBookingItem(
    val code: String,
    val time: String,
    val salon: String,
    val customer: String,
    val service: String,
    val value: String,
    val deposit: String,
    var status: String, // "Chờ salon xác nhận", "Đã xác nhận", "Hoàn thành", "Đã hủy"
    var paymentStatus: String // "Đã đặt cọc qua sàn", "Chờ hoàn cọc", "Đã hoàn cọc", "Đã thanh toán đủ"
)

data class AdminRefundRequest(
    val id: String,
    val bookingCode: String,
    val customer: String,
    val salon: String,
    val depositAmount: String,
    val reason: String,
    var status: String // "Chờ duyệt", "Đã duyệt hoàn", "Từ chối"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    selectedTab: Int = 0,
    onTabChange: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Sub-view navigation when in Tab 3 ("Thêm")
    var activeMoreSubView by remember { mutableStateOf<String?>(null) } // "bookings", "finance", "violations", "settings"
    var financeActiveTab by remember { mutableStateOf(0) } // 0: Doanh thu sàn, 1: Tiền cọc

    // State for modals
    var selectedShopDetail by remember { mutableStateOf<AdminShopItem?>(null) }
    var selectedCustomerDetail by remember { mutableStateOf<AdminCustomerItem?>(null) }
    var selectedBookingDetail by remember { mutableStateOf<AdminBookingItem?>(null) }
    var showAddCustomerDialog by remember { mutableStateOf(false) }

    // Sample Shop Data
    val shops = remember {
        mutableStateListOf(
            AdminShopItem(
                id = "1",
                name = "L'Amour Luxury Spa & Nail",
                code = "SALON-001",
                phone = "0908 123 456",
                representative = "Trần Minh Anh",
                address = "45 Đồng Khởi, P. Bến Nghé, Quận 1, TP.HCM",
                regDate = "10/01/2025",
                status = "Đang hoạt động",
                legalDoc = "GPKD số 0314892182 do Sở KHĐT TP.HCM cấp",
                verifiedDate = "12/01/2025",
                servicesCount = 18,
                bookingsCount = 342,
                rating = 4.9
            ),
            AdminShopItem(
                id = "2",
                name = "An Yên Thảo Dược Retreat",
                code = "SALON-002",
                phone = "0912 345 678",
                representative = "Lê Hoàng Yến",
                address = "12 Tú Xương, Phường 7, Quận 3, TP.HCM",
                regDate = "15/02/2025",
                status = "Đang hoạt động",
                legalDoc = "GPKD số 0317823901 do Sở KHĐT TP.HCM cấp",
                verifiedDate = "17/02/2025",
                servicesCount = 12,
                bookingsCount = 186,
                rating = 4.95
            ),
            AdminShopItem(
                id = "3",
                name = "The Royal Nail & Lash Lounge",
                code = "SALON-003",
                phone = "0933 888 999",
                representative = "Nguyễn Ngọc Bích",
                address = "88 Pasteur, Phường Bến Nghé, Quận 1, TP.HCM",
                regDate = "18/08/2025",
                status = "Chờ duyệt",
                legalDoc = "GPKD số 0319203810 - Đang xác minh hồ sơ",
                verifiedDate = "Chưa duyệt",
                servicesCount = 10,
                bookingsCount = 0,
                rating = 5.0
            ),
            AdminShopItem(
                id = "4",
                name = "Aura Beauty & Spa",
                code = "SALON-004",
                phone = "0944 555 666",
                representative = "Phạm Thu Hương",
                address = "102 Lê Thị Riêng, Quận 1, TP.HCM",
                regDate = "19/08/2025",
                status = "Chờ duyệt",
                legalDoc = "Hồ sơ đăng ký kèm giấy phép hành nghề",
                verifiedDate = "Chưa duyệt",
                servicesCount = 8,
                bookingsCount = 0,
                rating = 0.0
            ),
            AdminShopItem(
                id = "5",
                name = "Glow Paris Nails",
                code = "SALON-005",
                phone = "0977 111 222",
                representative = "Đặng Vũ Hải",
                address = "15 Nguyễn Đình Chiểu, Quận 3, TP.HCM",
                regDate = "19/08/2025",
                status = "Chờ duyệt",
                legalDoc = "Hồ sơ xác minh căn cước & mặt bằng",
                verifiedDate = "Chưa duyệt",
                servicesCount = 14,
                bookingsCount = 0,
                rating = 0.0
            )
        )
    }

    // Sample Customer Data
    val customers = remember {
        mutableStateListOf(
            AdminCustomerItem("1", "Quý cô Khánh Linh", "CUST-001", "0901 888 777", "khanhlinh@glow.vn", "05/01/2025", 14, 0, "4.850.000 đ", "100.000 đ", "Đang hoạt động"),
            AdminCustomerItem("2", "Nguyễn Thị Mai", "CUST-002", "0902 333 444", "mainguyen@gmail.com", "12/02/2025", 8, 1, "2.150.000 đ", "50.000 đ", "Đang hoạt động"),
            AdminCustomerItem("3", "Trần Thảo Anh", "CUST-003", "0909 555 666", "thaoanh@outlook.com", "20/03/2025", 5, 0, "1.480.000 đ", "84.000 đ", "Đang hoạt động"),
            AdminCustomerItem("4", "Lê Minh Trang", "CUST-004", "0918 777 999", "minhtrang@gmail.com", "14/04/2025", 3, 0, "850.000 đ", "0 đ", "Đang hoạt động")
        )
    }

    // Sample Refund Requests
    val refundRequests = remember {
        mutableStateListOf(
            AdminRefundRequest("REF-101", "BK-9021", "Nguyễn Thị Mai", "L'Amour Luxury", "50.000 đ", "Khách báo hủy trước 24h theo đúng chính sách hoàn tiền 100%", "Chờ duyệt"),
            AdminRefundRequest("REF-102", "BK-8842", "Hoàng Kim Oanh", "An Yên Thảo Dược", "84.000 đ", "Salon đổi lịch đột xuất không báo trước", "Chờ duyệt")
        )
    }

    // Sample Bookings Data
    val bookings = remember {
        mutableStateListOf(
            AdminBookingItem("BK-9021", "15:30 · 12/08", "L'Amour Luxury", "Nguyễn Thị Mai", "Sơn Gel Hữu Cơ Pháp", "180.000 đ", "50.000 đ", "Chờ salon xác nhận", "Đã đặt cọc qua sàn"),
            AdminBookingItem("BK-9022", "16:00 · 12/08", "L'Amour Luxury", "Quý cô Khánh Linh", "Nail Art Mắt Mèo Cực Quang", "280.000 đ", "84.000 đ", "Đã xác nhận", "Đã đặt cọc qua sàn"),
            AdminBookingItem("BK-8842", "10:00 · 11/08", "An Yên Thảo Dược", "Hoàng Kim Oanh", "Massage Thảo Dược Cung Đình", "350.000 đ", "100.000 đ", "Đã hủy", "Chờ hoàn cọc"),
            AdminBookingItem("BK-8830", "14:00 · 10/08", "L'Amour Luxury", "Trần Thảo Anh", "Đắp Móng Hoàng Gia Swarovski", "450.000 đ", "135.000 đ", "Hoàn thành", "Đã thanh toán đủ")
        )
    }

    // Search and filter states
    var shopSearchQuery by remember { mutableStateOf("") }
    var shopStatusFilter by remember { mutableStateOf("Tất cả") }
    var customerSearchQuery by remember { mutableStateOf("") }
    var bookingSearchQuery by remember { mutableStateOf("") }
    var bookingStatusFilter by remember { mutableStateOf("Tất cả") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF7E8))
    ) {
        when (selectedTab) {
            0 -> {
                // ==================== SECTION A: TRANG CHỦ ADMIN (MATCHING IMAGE 3) ====================
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Card 1: Tổng quan hôm nay (Image 3)
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Header row: "Tổng quan hôm nay" & Date selector
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Tổng quan hôm nay",
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF24090F)
                                        )
                                        Text(
                                            text = "Cập nhật lúc 15:56 · 12/08/2025",
                                            fontSize = 11.sp,
                                            color = Color(0xFF7A686D)
                                        )
                                    }

                                    // Filter pill
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(Color(0xFFFBF7E8))
                                            .border(1.dp, Color(0xFFEBDCD3), RoundedCornerShape(14.dp))
                                            .padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        GlowIcon(icon = GlowIcons.Calendar, tint = Color(0xFF6B1228), size = 12.dp)
                                        Text(
                                            text = "Hôm nay",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF24090F)
                                        )
                                        GlowIcon(icon = GlowIcons.ChevronDown, tint = Color(0xFF24090F), size = 10.dp)
                                    }
                                }

                                // 3 Metric Cards in 1 Row (Image 3 exact metrics)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // 1. Doanh thu sàn
                                    Card(
                                        modifier = Modifier.weight(1.05f),
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDFC)),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3ECE6))
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(10.dp),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFFFCEEEB)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                GlowIcon(icon = GlowIcons.Coins, tint = Color(0xFF6B1228), size = 16.dp)
                                            }
                                            Text(text = "Doanh thu sàn", fontSize = 10.5.sp, color = Color(0xFF7A686D))
                                            Text(text = "125.680.000đ", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                                GlowIcon(icon = GlowIcons.TrendingUp, tint = SuccessGreen, size = 10.dp)
                                                Text(text = "+12% so với hôm qua", fontSize = 9.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }

                                    // 2. Tổng tiền cọc tạm giữ (TÁCH RIÊNG)
                                    Card(
                                        modifier = Modifier.weight(1.05f),
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDFC)),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3ECE6))
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(10.dp),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFFFCEEEB)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                GlowIcon(icon = GlowIcons.Shield, tint = Color(0xFF6B1228), size = 16.dp)
                                            }
                                            Text(text = "Tổng tiền cọc tạm giữ", fontSize = 10.5.sp, color = Color(0xFF7A686D), maxLines = 1)
                                            Text(text = "28.400.000đ", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                            Text(text = "3 đơn đang giữ cọc", fontSize = 9.sp, color = Color(0xFF7A686D))
                                        }
                                    }

                                    // 3. Tổng đơn hàng
                                    Card(
                                        modifier = Modifier.weight(0.9f),
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDFC)),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3ECE6))
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(10.dp),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFFFCEEEB)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                GlowIcon(icon = GlowIcons.Wallet, tint = Color(0xFF6B1228), size = 16.dp)
                                            }
                                            Text(text = "Tổng đơn hàng", fontSize = 10.5.sp, color = Color(0xFF7A686D))
                                            Text(text = "48", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                                GlowIcon(icon = GlowIcons.TrendingUp, tint = SuccessGreen, size = 10.dp)
                                                Text(text = "+8% so với hôm qua", fontSize = 9.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Section: Việc cần xử lý (Spec A: 3 hồ sơ cửa hàng & 2 yêu cầu hoàn cọc)
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text(text = "Việc cần xử lý", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0xFFE11D48))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(text = "5 chờ xử lý", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                    }
                                }

                                // Row 1: Hồ sơ cửa hàng chờ duyệt
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFFFF9F7))
                                        .clickable { onTabChange(1) } // Navigate to Shops tab
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFFCEEEB)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            GlowIcon(icon = GlowIcons.Store, tint = Color(0xFF6B1228), size = 18.dp)
                                        }
                                        Column {
                                            Text(text = "Hồ sơ cửa hàng chờ duyệt", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                            Text(text = "Kiểm tra thông tin và giấy phép", fontSize = 11.sp, color = Color(0xFF7A686D))
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFFFCEEEB))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(text = "3 hồ sơ", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B1228))
                                    }
                                }

                                // Row 2: Yêu cầu hoàn cọc
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFFFF9F7))
                                        .clickable {
                                            activeMoreSubView = "finance"
                                            financeActiveTab = 1
                                            onTabChange(3) // Navigate to Finance Tab
                                        }
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFFCEEEB)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            GlowIcon(icon = GlowIcons.Coins, tint = Color(0xFF6B1228), size = 18.dp)
                                        }
                                        Column {
                                            Text(text = "Yêu cầu hoàn cọc", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                            Text(text = "Kiểm tra lịch hẹn và giao dịch", fontSize = 11.sp, color = Color(0xFF7A686D))
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFFFCEEEB))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(text = "2 yêu cầu", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B1228))
                                    }
                                }
                            }
                        }
                    }

                    // Section: Chức năng chính (Image 3: 4 large cards in 2x2 grid)
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Chức năng chính", fontSize = 17.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                Text(text = "Quản lý dễ dàng hơn >", fontSize = 12.sp, color = Color(0xFF7A686D))
                            }

                            // Row 1: Quản lý cửa hàng & Quản lý khách hàng
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Card 1: Quản lý cửa hàng
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(130.dp)
                                        .clickable { onTabChange(1) },
                                    shape = RoundedCornerShape(18.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(14.dp),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(Color(0xFFFCEEEB)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                GlowIcon(icon = GlowIcons.Store, tint = Color(0xFF6B1228), size = 20.dp)
                                            }
                                            GlowIcon(icon = GlowIcons.ChevronRight, tint = Color(0xFF8E7A80), size = 16.dp)
                                        }
                                        Column {
                                            Text(text = "Quản lý cửa hàng", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(text = "Thêm / Sửa / Xóa cửa hàng, quản lý thông tin & lịch.", fontSize = 10.sp, color = Color(0xFF7A686D), lineHeight = 13.sp)
                                        }
                                    }
                                }

                                // Card 2: Quản lý khách hàng
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(130.dp)
                                        .clickable { onTabChange(2) },
                                    shape = RoundedCornerShape(18.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(14.dp),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(Color(0xFFFCEEEB)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                GlowIcon(icon = GlowIcons.Users, tint = Color(0xFF6B1228), size = 20.dp)
                                            }
                                            GlowIcon(icon = GlowIcons.ChevronRight, tint = Color(0xFF8E7A80), size = 16.dp)
                                        }
                                        Column {
                                            Text(text = "Quản lý khách hàng", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(text = "Tra cứu, xem chi tiết, thêm / sửa / khóa khách hàng.", fontSize = 10.sp, color = Color(0xFF7A686D), lineHeight = 13.sp)
                                        }
                                    }
                                }
                            }

                            // Row 2: Doanh thu & Tổng tiền cọc tạm giữ
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Card 3: Doanh thu
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(130.dp)
                                        .clickable {
                                            activeMoreSubView = "finance"
                                            financeActiveTab = 0
                                            onTabChange(3)
                                        },
                                    shape = RoundedCornerShape(18.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(14.dp),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(Color(0xFFF3E8FF)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                GlowIcon(icon = GlowIcons.FileText, tint = Color(0xFF7E22CE), size = 20.dp)
                                            }
                                            GlowIcon(icon = GlowIcons.ChevronRight, tint = Color(0xFF8E7A80), size = 16.dp)
                                        }
                                        Column {
                                            Text(text = "Doanh thu", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(text = "Theo ngày / tuần / tháng. Báo cáo đối soát hoa hồng.", fontSize = 10.sp, color = Color(0xFF7A686D), lineHeight = 13.sp)
                                        }
                                    }
                                }

                                // Card 4: Tổng tiền cọc tạm giữ
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(130.dp)
                                        .clickable {
                                            activeMoreSubView = "finance"
                                            financeActiveTab = 1
                                            onTabChange(3)
                                        },
                                    shape = RoundedCornerShape(18.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(14.dp),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(Color(0xFFE6F4EA)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                GlowIcon(icon = GlowIcons.Coins, tint = Color(0xFF137333), size = 20.dp)
                                            }
                                            GlowIcon(icon = GlowIcons.ChevronRight, tint = Color(0xFF8E7A80), size = 16.dp)
                                        }
                                        Column {
                                            Text(text = "Tổng tiền cọc tạm giữ", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(text = "Xem và quản lý các khoản cọc, duyệt hoàn tiền.", fontSize = 10.sp, color = Color(0xFF7A686D), lineHeight = 13.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // ==================== SECTION B: QUẢN LÝ CỬA HÀNG (SPEC B) ====================
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(text = "Quản lý Cửa Hàng (Đối tác Salon)", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))

                    // Search box (tên, mã, SĐT)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0xFFEBDCD3), RoundedCornerShape(22.dp))
                            .padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        GlowIcon(icon = GlowIcons.Search, tint = Color(0xFF7A686D), size = 18.dp)
                        Box(modifier = Modifier.weight(1f)) {
                            if (shopSearchQuery.isEmpty()) {
                                Text(text = "Tìm theo tên, mã cửa hàng, SĐT...", color = Color(0xFF8E7A80), fontSize = 12.5.sp)
                            }
                            TextField(
                                value = shopSearchQuery,
                                onValueChange = { shopSearchQuery = it },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                singleLine = true
                            )
                        }
                    }

                    // Status filters: Tất cả, Chờ duyệt, Đang hoạt động, Tạm khóa, Từ chối
                    val shopFilterTabs = listOf("Tất cả", "Chờ duyệt", "Đang hoạt động", "Tạm khóa", "Từ chối")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(shopFilterTabs) { fTab ->
                            val isSel = fTab == shopStatusFilter
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSel) Color(0xFF62202F) else Color.White)
                                    .border(1.dp, if (isSel) Color.Transparent else Color(0xFFEBDCD3), RoundedCornerShape(16.dp))
                                    .clickable { shopStatusFilter = fTab }
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = fTab,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSel) Color.White else Color(0xFF7A686D)
                                )
                            }
                        }
                    }

                    // Shops list
                    val filteredShops = shops.filter { s ->
                        val matchesSearch = shopSearchQuery.isEmpty() ||
                                s.name.contains(shopSearchQuery, ignoreCase = true) ||
                                s.code.contains(shopSearchQuery, ignoreCase = true) ||
                                s.phone.contains(shopSearchQuery)
                        val matchesFilter = when (shopStatusFilter) {
                            "Tất cả" -> true
                            else -> s.status == shopStatusFilter
                        }
                        matchesSearch && matchesFilter
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(filteredShops) { shop ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedShopDetail = shop },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                            ) {
                                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(text = shop.name, fontWeight = FontWeight.Bold, fontSize = 14.5.sp, color = Color(0xFF24090F))
                                            Text(text = "Mã: ${shop.code} · Ngày ĐK: ${shop.regDate}", fontSize = 11.sp, color = Color(0xFF7A686D))
                                        }

                                        // Status badge
                                        val (bgColor, textColor) = when (shop.status) {
                                            "Đang hoạt động" -> Color(0xFFE6F4EA) to Color(0xFF137333)
                                            "Chờ duyệt" -> Color(0xFFFEF3C7) to Color(0xFFD97706)
                                            "Tạm khóa" -> Color(0xFFFCEEEB) to Color(0xFFE11D48)
                                            else -> Color(0xFFF3F4F6) to Color(0xFF6B7280)
                                        }
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(bgColor)
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text(text = shop.status, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = textColor)
                                        }
                                    }

                                    HorizontalDivider(color = Color(0xFFF5EFEF))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "SĐT: ${shop.phone} · ĐD: ${shop.representative}", fontSize = 11.5.sp, color = Color(0xFF7A686D))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(text = "Hồ sơ chi tiết", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B1228))
                                            GlowIcon(icon = GlowIcons.ChevronRight, tint = Color(0xFF6B1228), size = 12.dp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // ==================== SECTION C: QUẢN LÝ KHÁCH HÀNG (SPEC C) ====================
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Quản lý Khách Hàng (Tài khoản)", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                        Button(
                            onClick = { showAddCustomerDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF62202F)),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            GlowIcon(icon = GlowIcons.Plus, tint = Color.White, size = 12.dp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Thêm KH", fontSize = 11.sp, color = Color.White)
                        }
                    }

                    // Search box
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0xFFEBDCD3), RoundedCornerShape(22.dp))
                            .padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        GlowIcon(icon = GlowIcons.Search, tint = Color(0xFF7A686D), size = 18.dp)
                        Box(modifier = Modifier.weight(1f)) {
                            if (customerSearchQuery.isEmpty()) {
                                Text(text = "Tìm theo tên, SĐT, mã khách hàng...", color = Color(0xFF8E7A80), fontSize = 12.5.sp)
                            }
                            TextField(
                                value = customerSearchQuery,
                                onValueChange = { customerSearchQuery = it },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                singleLine = true
                            )
                        }
                    }

                    // Customer list
                    val filteredCustomers = customers.filter { c ->
                        customerSearchQuery.isEmpty() ||
                                c.name.contains(customerSearchQuery, ignoreCase = true) ||
                                c.code.contains(customerSearchQuery, ignoreCase = true) ||
                                c.phone.contains(customerSearchQuery)
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(filteredCustomers) { cust ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedCustomerDetail = cust },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                            ) {
                                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFFF9EFEF)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                GlowIcon(icon = GlowIcons.User, tint = Color(0xFF6B1228), size = 20.dp)
                                            }
                                            Column {
                                                Text(text = cust.name, fontWeight = FontWeight.Bold, fontSize = 14.5.sp, color = Color(0xFF24090F))
                                                Text(text = "Mã: ${cust.code} · Ngày tham gia: ${cust.joinDate}", fontSize = 11.sp, color = Color(0xFF7A686D))
                                            }
                                        }

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (cust.status == "Đang hoạt động") Color(0xFFE6F4EA) else Color(0xFFFCEEEB))
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = cust.status,
                                                fontSize = 10.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (cust.status == "Đang hoạt động") Color(0xFF137333) else Color(0xFFE11D48)
                                            )
                                        }
                                    }

                                    HorizontalDivider(color = Color(0xFFF5EFEF))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "Số lịch hẹn: ${cust.bookingsCount} (Hủy: ${cust.cancelCount})", fontSize = 11.5.sp, color = Color(0xFF7A686D))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(text = "Xem hồ sơ", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B1228))
                                            GlowIcon(icon = GlowIcons.ChevronRight, tint = Color(0xFF6B1228), size = 12.dp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            3 -> {
                // ==================== SECTION F: THÊM (LỊCH, TÀI CHÍNH, BÁO CÁO, CÀI ĐẶT) ====================
                if (activeMoreSubView == "bookings") {
                    // SUB-VIEW: QUẢN LÝ ĐƠN ĐẶT LỊCH (SECTION D)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(onClick = { activeMoreSubView = null }) {
                                GlowIcon(icon = GlowIcons.ChevronLeft, tint = Color(0xFF24090F), size = 20.dp)
                            }
                            Text(text = "Quản lý Đơn Đặt Lịch", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                        }

                        // Search
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .clip(RoundedCornerShape(22.dp))
                                .background(Color.White)
                                .border(1.dp, Color(0xFFEBDCD3), RoundedCornerShape(22.dp))
                                .padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            GlowIcon(icon = GlowIcons.Search, tint = Color(0xFF7A686D), size = 18.dp)
                            Box(modifier = Modifier.weight(1f)) {
                                if (bookingSearchQuery.isEmpty()) {
                                    Text(text = "Tìm theo mã lịch, tên khách, salon...", color = Color(0xFF8E7A80), fontSize = 12.5.sp)
                                }
                                TextField(
                                    value = bookingSearchQuery,
                                    onValueChange = { bookingSearchQuery = it },
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    singleLine = true
                                )
                            }
                        }

                        // Bookings list
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(bookings) { bk ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedBookingDetail = bk },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White)
                                ) {
                                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = "${bk.code} · ${bk.time}", fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = Color(0xFF24090F))
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Color(0xFFFCEEEB))
                                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                                            ) {
                                                Text(text = bk.status, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B1228))
                                            }
                                        }
                                        Text(text = "${bk.salon} → ${bk.customer}", fontSize = 12.sp, color = Color(0xFF7A686D))
                                        Text(text = "Dịch vụ: ${bk.service}", fontSize = 11.5.sp, color = Color(0xFF24090F))
                                        HorizontalDivider(color = Color(0xFFF5EFEF))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = "Tổng: ${bk.value} (Cọc: ${bk.deposit})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B1228))
                                            Text(text = bk.paymentStatus, fontSize = 11.sp, color = Color(0xFF137333), fontWeight = FontWeight.Medium)
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (activeMoreSubView == "finance") {
                    // SUB-VIEW: TÀI CHÍNH (SECTION E: 2 TABS - DOANH THU SÀN & TIỀN CỌC)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(onClick = { activeMoreSubView = null }) {
                                GlowIcon(icon = GlowIcons.ChevronLeft, tint = Color(0xFF24090F), size = 20.dp)
                            }
                            Text(text = "Tài chính — Doanh thu & Tiền cọc", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                        }

                        // 2 Tabs: Doanh thu sàn | Tiền cọc
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFEBDCD3))
                                .padding(3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (financeActiveTab == 0) Color.White else Color.Transparent)
                                    .clickable { financeActiveTab = 0 }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Doanh thu sàn",
                                    fontSize = 13.sp,
                                    fontWeight = if (financeActiveTab == 0) FontWeight.Bold else FontWeight.Medium,
                                    color = if (financeActiveTab == 0) Color(0xFF6B1228) else Color(0xFF7A686D)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (financeActiveTab == 1) Color.White else Color.Transparent)
                                    .clickable { financeActiveTab = 1 }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Tiền cọc tạm giữ",
                                    fontSize = 13.sp,
                                    fontWeight = if (financeActiveTab == 1) FontWeight.Bold else FontWeight.Medium,
                                    color = if (financeActiveTab == 1) Color(0xFF6B1228) else Color(0xFF7A686D)
                                )
                            }
                        }

                        if (financeActiveTab == 0) {
                            // Tab 1: Doanh thu sàn
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text(text = "Báo cáo doanh thu phí sàn", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                    Text(text = "Bộ lọc: Hôm nay · 7 ngày · Tháng này · Tùy chỉnh", fontSize = 11.sp, color = Color(0xFF7A686D))
                                    HorizontalDivider(color = Color(0xFFF5EFEF))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(text = "Tổng phí/hoa hồng sàn thu:", fontSize = 12.sp, color = Color(0xFF7A686D))
                                        Text(text = "125.680.000 ₫", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B1228))
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(text = "Giao dịch thành công:", fontSize = 12.sp, color = Color(0xFF7A686D))
                                        Text(text = "348 giao dịch", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(text = "Phí hoàn trả/điều chỉnh:", fontSize = 12.sp, color = Color(0xFF7A686D))
                                        Text(text = "- 1.250.000 ₫", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE11D48))
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(text = "Doanh thu ròng sàn:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                        Text(text = "124.430.000 ₫", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF137333))
                                    }
                                    Button(
                                        onClick = { },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF62202F)),
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text(text = "Xuất báo cáo đối soát Excel", fontSize = 12.sp, color = Color.White)
                                    }
                                }
                            }
                        } else {
                            // Tab 2: Tiền cọc tạm giữ & Phê duyệt hoàn cọc
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text(text = "Quỹ tiền cọc đang tạm giữ", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                    Text(text = "Tách riêng hoàn toàn, không tính vào doanh thu sàn", fontSize = 11.sp, color = Color(0xFF7A686D))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(text = "Tổng tiền cọc đang giữ:", fontSize = 12.sp, color = Color(0xFF7A686D))
                                        Text(text = "28.400.000 ₫", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B1228))
                                    }
                                    Text(text = "3 khoản cọc hợp lệ chờ hoàn thành dịch vụ", fontSize = 11.sp, color = Color(0xFF137333))
                                }
                            }

                            Text(text = "Yêu cầu hoàn cọc chờ xử lý (2)", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))

                            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                items(refundRequests) { req ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White)
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = "${req.id} · ${req.bookingCode}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF24090F))
                                                Text(text = req.depositAmount, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF6B1228))
                                            }
                                            Text(text = "Khách hàng: ${req.customer} · Salon: ${req.salon}", fontSize = 12.sp, color = Color(0xFF7A686D))
                                            Text(text = "Lý do: ${req.reason}", fontSize = 11.5.sp, color = Color(0xFF24090F))

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Button(
                                                    onClick = { req.status = "Đã duyệt hoàn" },
                                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                                    modifier = Modifier.weight(1f),
                                                    shape = RoundedCornerShape(8.dp),
                                                    contentPadding = PaddingValues(vertical = 4.dp)
                                                ) {
                                                    Text(text = if (req.status == "Đã duyệt hoàn") "Đã duyệt ✓" else "Duyệt hoàn cọc", fontSize = 11.5.sp)
                                                }
                                                OutlinedButton(
                                                    onClick = { req.status = "Từ chối" },
                                                    modifier = Modifier.weight(1f),
                                                    shape = RoundedCornerShape(8.dp),
                                                    contentPadding = PaddingValues(vertical = 4.dp)
                                                ) {
                                                    Text(text = "Từ chối", fontSize = 11.5.sp, color = Color(0xFFE11D48))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // MAIN "THÊM" MENU (SPEC F & 4 NAVIGATION SHORTCUTS)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(text = "Chức năng Mở rộng (Admin Hub)", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                        }

                        // Shortcut 1: Quản lý đơn đặt lịch
                        item {
                            AdminMoreMenuCard(
                                title = "Quản lý Đơn Đặt Lịch",
                                subtitle = "Tra cứu mã lịch, đối soát trạng thái cọc và thanh toán",
                                icon = GlowIcons.Calendar,
                                onClick = { activeMoreSubView = "bookings" }
                            )
                        }

                        // Shortcut 2: Tài chính & Tiền cọc
                        item {
                            AdminMoreMenuCard(
                                title = "Tài chính — Doanh thu và Tiền cọc",
                                subtitle = "Phí sàn 125,68tr, kiểm soát 28,4tr tiền cọc đang giữ",
                                icon = GlowIcons.Coins,
                                onClick = { activeMoreSubView = "finance" }
                            )
                        }

                        // Shortcut 3: Đánh giá & Báo cáo vi phạm
                        item {
                            AdminMoreMenuCard(
                                title = "Đánh giá & Báo cáo vi phạm",
                                subtitle = "Duyệt báo cáo spam, nội dung không phù hợp và xử lý khiếu nại",
                                icon = GlowIcons.Alert,
                                onClick = { }
                            )
                        }

                        // Shortcut 4: Thông báo hệ thống
                        item {
                            AdminMoreMenuCard(
                                title = "Thông báo hệ thống",
                                subtitle = "Hồ sơ đối tác mới, giao dịch bất thường cần xử lý",
                                icon = GlowIcons.Bell,
                                onClick = { }
                            )
                        }

                        // Shortcut 5: Tài khoản & Phân quyền
                        item {
                            AdminMoreMenuCard(
                                title = "Tài khoản & Phân quyền",
                                subtitle = "Quản lý tài khoản nhân viên quản trị, vai trò và quyền truy cập",
                                icon = GlowIcons.Shield,
                                onClick = { }
                            )
                        }

                        // Shortcut 6: Cài đặt hệ thống
                        item {
                            AdminMoreMenuCard(
                                title = "Cài đặt hệ thống",
                                subtitle = "Cấu hình hoa hồng nền tảng, chính sách hủy & hoàn cọc",
                                icon = GlowIcons.Settings,
                                onClick = { }
                            )
                        }
                    }
                }
            }
        }

        // ==================== DETAIL MODALS (SPEC B: 4 NHÓM HỒ SƠ SALON) ====================
        if (selectedShopDetail != null) {
            val shop = selectedShopDetail!!
            ModalBottomSheet(
                onDismissRequest = { selectedShopDetail = null },
                containerColor = Color.White
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = shop.name, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                Text(text = "Mã cơ sở: ${shop.code}", fontSize = 12.sp, color = Color(0xFF7A686D))
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFCEEEB))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = shop.status, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B1228))
                            }
                        }
                    }

                    item { HorizontalDivider(color = Color(0xFFF5EFEF)) }

                    // Nhóm 1: Thông tin chung
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "1. THÔNG TIN CHUNG", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B1228))
                            Text(text = "• Người đại diện: ${shop.representative}", fontSize = 12.5.sp, color = Color(0xFF24090F))
                            Text(text = "• Số điện thoại: ${shop.phone}", fontSize = 12.5.sp, color = Color(0xFF24090F))
                            Text(text = "• Địa chỉ hoạt động: ${shop.address}", fontSize = 12.5.sp, color = Color(0xFF24090F))
                        }
                    }

                    // Nhóm 2: Hồ sơ pháp lý
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "2. HỒ SƠ PHÁP LÝ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B1228))
                            Text(text = "• Giấy phép KD: ${shop.legalDoc}", fontSize = 12.5.sp, color = Color(0xFF24090F))
                            Text(text = "• Trạng thái xác minh: Đã kiểm tra CCCD & Hợp đồng thuê", fontSize = 12.5.sp, color = Color(0xFF24090F))
                            Text(text = "• Ngày phê duyệt: ${shop.verifiedDate}", fontSize = 12.5.sp, color = Color(0xFF24090F))
                        }
                    }

                    // Nhóm 3: Dịch vụ
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "3. DỊCH VỤ & BẢNG GIÁ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B1228))
                            Text(text = "• Tổng số dịch vụ niêm yết: ${shop.servicesCount} dịch vụ", fontSize = 12.5.sp, color = Color(0xFF24090F))
                            Text(text = "• Khung giá: 180.000 đ – 450.000 đ", fontSize = 12.5.sp, color = Color(0xFF24090F))
                            Text(text = "• Thiết lập cọc: Mở nhận cọc từ 50k", fontSize = 12.5.sp, color = Color(0xFF24090F))
                        }
                    }

                    // Nhóm 4: Hoạt động
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "4. HOẠT ĐỘNG VÀ ĐỐI SOÁT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B1228))
                            Text(text = "• Tổng lượt booking hoàn thành: ${shop.bookingsCount}", fontSize = 12.5.sp, color = Color(0xFF24090F))
                            Text(text = "• Đánh giá trung bình: ${shop.rating} ★ (1.8k reviews)", fontSize = 12.5.sp, color = Color(0xFF24090F))
                            Text(text = "• Lịch sử vi phạm: ${shop.violationHistory}", fontSize = 12.5.sp, color = Color(0xFF24090F))
                        }
                    }

                    // Thao tác quản trị (Duyệt, Khóa, Từ chối)
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (shop.status == "Chờ duyệt") {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                    Button(
                                        onClick = {
                                            shop.status = "Đang hoạt động"
                                            selectedShopDetail = null
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Duyệt hồ sơ mở sàn")
                                    }
                                    OutlinedButton(
                                        onClick = {
                                            shop.status = "Từ chối"
                                            selectedShopDetail = null
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Từ chối hồ sơ", color = Color(0xFFE11D48))
                                    }
                                }
                            } else {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                    Button(
                                        onClick = {
                                            shop.status = if (shop.status == "Đang hoạt động") "Tạm khóa" else "Đang hoạt động"
                                            selectedShopDetail = null
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (shop.status == "Đang hoạt động") Color(0xFFE11D48) else SuccessGreen
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(if (shop.status == "Đang hoạt động") "Tạm khóa gian hàng" else "Mở khóa gian hàng")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // ==================== DETAIL MODALS (SPEC C: 4 NHÓM HỒ SƠ KHÁCH HÀNG) ====================
        if (selectedCustomerDetail != null) {
            val cust = selectedCustomerDetail!!
            ModalBottomSheet(
                onDismissRequest = { selectedCustomerDetail = null },
                containerColor = Color.White
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = cust.name, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                Text(text = "Mã KH: ${cust.code}", fontSize = 12.sp, color = Color(0xFF7A686D))
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFE6F4EA))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = cust.status, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF137333))
                            }
                        }
                    }

                    item { HorizontalDivider(color = Color(0xFFF5EFEF)) }

                    // Nhóm 1: Thông tin tài khoản & liên hệ
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "1. THÔNG TIN LIÊN HỆ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B1228))
                            Text(text = "• Số điện thoại: ${cust.phone}", fontSize = 12.5.sp, color = Color(0xFF24090F))
                            Text(text = "• Email: ${cust.email}", fontSize = 12.5.sp, color = Color(0xFF24090F))
                            Text(text = "• Ngày đăng ký tài khoản: ${cust.joinDate}", fontSize = 12.5.sp, color = Color(0xFF24090F))
                        }
                    }

                    // Nhóm 2: Lịch sử đặt lịch & hủy
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "2. LỊCH SỬ ĐẶT LỊCH", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B1228))
                            Text(text = "• Tổng số đơn đã đặt: ${cust.bookingsCount} lịch hẹn", fontSize = 12.5.sp, color = Color(0xFF24090F))
                            Text(text = "• Số lần hủy lịch: ${cust.cancelCount} lần (Hủy trước 24h hợp lệ)", fontSize = 12.5.sp, color = Color(0xFF24090F))
                        }
                    }

                    // Nhóm 3: Thanh toán, tiền cọc & hoàn tiền
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "3. LỊCH SỬ THANH TOÁN VÀ TIỀN CỌC", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B1228))
                            Text(text = "• Tổng chi tiêu tích lũy: ${cust.totalSpent}", fontSize = 12.5.sp, color = Color(0xFF24090F))
                            Text(text = "• Tiền cọc đang tạm giữ: ${cust.depositHeld}", fontSize = 12.5.sp, color = Color(0xFF24090F))
                        }
                    }

                    // Thao tác: Khóa / Mở khóa tài khoản
                    item {
                        Button(
                            onClick = {
                                cust.status = if (cust.status == "Đang hoạt động") "Tạm khóa" else "Đang hoạt động"
                                selectedCustomerDetail = null
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (cust.status == "Đang hoạt động") Color(0xFFE11D48) else SuccessGreen
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (cust.status == "Đang hoạt động") "Khóa tài khoản khách hàng" else "Mở khóa tài khoản")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminMoreMenuCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFCEEEB)),
                    contentAlignment = Alignment.Center
                ) {
                    GlowIcon(icon = icon, tint = Color(0xFF6B1228), size = 20.dp)
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = title, fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                    Text(text = subtitle, fontSize = 11.5.sp, color = Color(0xFF7A686D), lineHeight = 15.sp)
                }
            }
            GlowIcon(icon = GlowIcons.ChevronRight, tint = Color(0xFF7A686D), size = 16.dp)
        }
    }
}
