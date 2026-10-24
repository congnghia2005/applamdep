package com.example.app_lam_dep.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_lam_dep.ui.icons.GlowBackButton
import com.example.app_lam_dep.ui.icons.GlowIcon
import com.example.app_lam_dep.ui.icons.GlowIcons
import com.example.app_lam_dep.ui.icons.IconMedallion
import com.example.app_lam_dep.ui.theme.ChampagneGold
import com.example.app_lam_dep.ui.theme.DustyMerlot
import com.example.app_lam_dep.ui.theme.GoldSoft
import com.example.app_lam_dep.ui.theme.IconBlush
import com.example.app_lam_dep.ui.theme.IconCream
import com.example.app_lam_dep.ui.theme.PinkPanther
import com.example.app_lam_dep.ui.theme.SuccessGreen
import com.example.app_lam_dep.ui.theme.TextMuted

// ─────────────────────────── Data Models (Chuẩn Layout Grab) ───────────────────────────

data class GrabFeaturedService(
    val id: String,
    val name: String,
    val price: String,
    val bgGradient: List<Color>
)

data class GrabSalon(
    val id: String,
    val name: String,
    val rating: Double,
    val reviewCount: String,
    val priceRange: String,
    val categoryTags: String,
    val distance: String,
    val eta: String,
    val promoVoucher: String,
    val isSignatureBadge: Boolean,
    val signatureBadgeText: String,
    val services: List<GrabFeaturedService>,
    val requiresDeposit: Boolean = true,
    val depositLabel: String = "Cọc từ 50k (5 mệnh giá)"
)

@Composable
fun CategorySalonsScreen(
    categoryName: String,
    onBack: () -> Unit,
    onNavigateToSalon: (String) -> Unit,
    onSelectServiceForBooking: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilterChip by remember { mutableStateOf("Tất cả") }

    // Dữ liệu mẫu phong phú theo chuyên môn
    val salons = remember(categoryName) {
        when {
            categoryName.contains("Nail", ignoreCase = true) -> listOf(
                GrabSalon(
                    id = "salon_01",
                    name = "1994 Nailsroom",
                    rating = 4.95,
                    reviewCount = "238+",
                    priceRange = "$$$",
                    categoryTags = "Nail Art • Đính Đá • Sơn Gel Cao Cấp",
                    distance = "0.8 km",
                    eta = "12 phút",
                    promoVoucher = "Giảm 30.000đ cọc • Đơn từ 150K",
                    isSignatureBadge = true,
                    signatureBadgeText = "Top 1 Nail Đà Nẵng",
                    services = listOf(
                        GrabFeaturedService("s1", "Sơn Gel Hữu Cơ + Dưỡng", "180.000đ", listOf(Color(0xFF5A1E2D), Color(0xFF8A2E44))),
                        GrabFeaturedService("s2", "Nail Mắt Mèo Cực Quang", "280.000đ", listOf(Color(0xFF2E1A47), Color(0xFF6A3B82))),
                        GrabFeaturedService("s3", "Đắp Móng Đá Swarovski", "450.000đ", listOf(Color(0xFF4A3B18), Color(0xFF9E7B34))),
                        GrabFeaturedService("s4", "Chăm Sóc Móng Toàn Diện", "220.000đ", listOf(Color(0xFF28483B), Color(0xFF4C7D69)))
                    )
                ),
                GrabSalon(
                    id = "salon_02",
                    name = "BONNIE NAIL",
                    rating = 4.90,
                    reviewCount = "180+",
                    priceRange = "$$",
                    categoryTags = "Sơn Gel Hàn Quốc • Charm Xinh",
                    distance = "1.5 km",
                    eta = "18 phút",
                    promoVoucher = "Giảm 20K cọc cho khách mới",
                    isSignatureBadge = true,
                    signatureBadgeText = "Hot Trend Sơn Trà",
                    services = listOf(
                        GrabFeaturedService("s5", "Vẽ Móng Nghệ Thuật 3D", "250.000đ", listOf(Color(0xFF6B2636), Color(0xFF964354))),
                        GrabFeaturedService("s6", "Sơn Thạch Ombre Hồng", "190.000đ", listOf(Color(0xFF6B4550), Color(0xFF9E6E7B))),
                        GrabFeaturedService("s7", "Dưỡng Móng Tế Bào Gốc", "220.000đ", listOf(Color(0xFF384A3B), Color(0xFF5B7860)))
                    )
                ),
                GrabSalon(
                    id = "salon_07",
                    name = "Nails 1996",
                    rating = 4.88,
                    reviewCount = "140+",
                    priceRange = "$$",
                    categoryTags = "Nail Studio • Ngũ Hành Sơn",
                    distance = "2.8 km",
                    eta = "22 phút",
                    promoVoucher = "Giảm 15% gói combo tiệc",
                    isSignatureBadge = false,
                    signatureBadgeText = "Được Yêu Thích",
                    services = listOf(
                        GrabFeaturedService("s8", "Tráng Gương Bạc Chrome", "240.000đ", listOf(Color(0xFF3C3C4D), Color(0xFF686882))),
                        GrabFeaturedService("s9", "Nail Xà Cừ Cao Cấp", "320.000đ", listOf(Color(0xFF4D3844), Color(0xFF7A596C)))
                    )
                ),
                GrabSalon(
                    id = "salon_11",
                    name = "1m65.nail",
                    rating = 4.85,
                    reviewCount = "95+",
                    priceRange = "$$",
                    categoryTags = "Nail Xinh • Hải Châu",
                    distance = "1.1 km",
                    eta = "14 phút",
                    promoVoucher = "Tặng dưỡng móng khi làm nail",
                    isSignatureBadge = false,
                    signatureBadgeText = "Nail Trẻ Trung",
                    services = listOf(
                        GrabFeaturedService("s10", "Sơn Gel Tươi Mới", "160.000đ", listOf(Color(0xFF4D3844), Color(0xFF7A596C)))
                    )
                )
            )
            categoryName.contains("Spa", ignoreCase = true) || categoryName.contains("Dưỡng sinh", ignoreCase = true) -> listOf(
                GrabSalon(
                    id = "salon_09",
                    name = "Thì Là SPA - Gội Đầu Dưỡng Sinh",
                    rating = 4.97,
                    reviewCount = "310+",
                    priceRange = "$$$",
                    categoryTags = "Gội Đầu Thảo Dược • Massage Thư Giãn",
                    distance = "0.9 km",
                    eta = "15 phút",
                    promoVoucher = "Tặng tinh dầu bưởi dưỡng tóc",
                    isSignatureBadge = true,
                    signatureBadgeText = "Dưỡng Sinh Hàng Đầu",
                    services = listOf(
                        GrabFeaturedService("s11", "Gội Dưỡng Sinh Thảo Dược 60p", "250.000đ", listOf(Color(0xFF1E3A2F), Color(0xFF386653))),
                        GrabFeaturedService("s12", "Massage Đá Nóng Toàn Thân 90p", "450.000đ", listOf(Color(0xFF4D2B1C), Color(0xFF7D4A32))),
                        GrabFeaturedService("s13", "Ngâm Chân Thảo Mộc", "120.000đ", listOf(Color(0xFF303C24), Color(0xFF53663E)))
                    ),
                    requiresDeposit = false,
                    depositLabel = "Không cần cọc • Giữ chỗ 0đ"
                ),
                GrabSalon(
                    id = "salon_03",
                    name = "GÀ SPA",
                    rating = 4.86,
                    reviewCount = "220+",
                    priceRange = "$$",
                    categoryTags = "Chăm Sóc Da • Lấy Nhân Mụn Chuẩn Y Khoa",
                    distance = "1.8 km",
                    eta = "20 phút",
                    promoVoucher = "Giảm 50.000đ gói trị mụn",
                    isSignatureBadge = true,
                    signatureBadgeText = "Chuyên Trị Mụn",
                    services = listOf(
                        GrabFeaturedService("s14", "Lấy Mụn Chuẩn Y Khoa", "250.000đ", listOf(Color(0xFF2A423B), Color(0xFF446B5F))),
                        GrabFeaturedService("s15", "Điện Di Vitamin C Sáng Da", "300.000đ", listOf(Color(0xFF4B3224), Color(0xFF754F3A)))
                    )
                ),
                GrabSalon(
                    id = "salon_10",
                    name = "Tun Beauty Salon & Spa",
                    rating = 4.88,
                    reviewCount = "175+",
                    priceRange = "$$$",
                    categoryTags = "Spa • Massage Thư Giãn • Sơn Trà",
                    distance = "2.1 km",
                    eta = "22 phút",
                    promoVoucher = "Giảm 10% tổng hóa đơn",
                    isSignatureBadge = false,
                    signatureBadgeText = "Thư Giãn Sơn Trà",
                    services = listOf(
                        GrabFeaturedService("s16", "Massage Body Tinh Dầu", "350.000đ", listOf(Color(0xFF303C24), Color(0xFF53663E)))
                    )
                )
            )
            else -> listOf(
                GrabSalon(
                    id = "salon_04",
                    name = "HIEN HAIR STUDIO",
                    rating = 4.92,
                    reviewCount = "260+",
                    priceRange = "$$$",
                    categoryTags = "Cắt Tóc Stylist • Uốn Nhuộm Hàn Quốc",
                    distance = "2.4 km",
                    eta = "20 phút",
                    promoVoucher = "Tặng hấp dầu Collagen",
                    isSignatureBadge = true,
                    signatureBadgeText = "Hair Studio Nổi Tiếng",
                    services = listOf(
                        GrabFeaturedService("s17", "Cắt Tạo Kiểu Thời Trang", "150.000đ", listOf(Color(0xFF1F384C), Color(0xFF3B6282))),
                        GrabFeaturedService("s18", "Uốn Sóng Lơi Hàn Quốc", "750.000đ", listOf(Color(0xFF541C2E), Color(0xFF85304B))),
                        GrabFeaturedService("s19", "Nhuộm Thời Trang Cao Cấp", "650.000đ", listOf(Color(0xFF4A4222), Color(0xFF7A6D3B)))
                    )
                ),
                GrabSalon(
                    id = "salon_06",
                    name = "MNH THUY MAKEUP",
                    rating = 4.90,
                    reviewCount = "156+",
                    priceRange = "$$$",
                    categoryTags = "Makeup Dạ Tiệc • Cô Dâu • Kỷ Yếu",
                    distance = "2.6 km",
                    eta = "24 phút",
                    promoVoucher = "Giảm 10% nhóm từ 2 người",
                    isSignatureBadge = true,
                    signatureBadgeText = "Makeup Chuyên Nghiệp",
                    services = listOf(
                        GrabFeaturedService("s20", "Makeup Dạ Tiệc Douyin", "350.000đ", listOf(Color(0xFF541C2E), Color(0xFF85304B))),
                        GrabFeaturedService("s21", "Makeup & Làm Tóc Cô Dâu", "1.200.000đ", listOf(Color(0xFF4A4222), Color(0xFF7A6D3B)))
                    )
                ),
                GrabSalon(
                    id = "salon_08",
                    name = "SI MÊ BEAUTY",
                    rating = 4.88,
                    reviewCount = "190+",
                    priceRange = "$$",
                    categoryTags = "Nail • Mi • Chăm Sóc Da • Hải Châu",
                    distance = "1.3 km",
                    eta = "15 phút",
                    promoVoucher = "Giảm 20K cọc đơn từ 150K",
                    isSignatureBadge = false,
                    signatureBadgeText = "Đa Dịch Vụ",
                    services = listOf(
                        GrabFeaturedService("s22", "Nối Mi Tự Nhiên Thiết Kế", "220.000đ", listOf(Color(0xFF1F384C), Color(0xFF3B6282)))
                    )
                )
            )
        }
    }

    val topHorizontalHighlights = remember(salons) { salons.take(3) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF7E8))
    ) {
        // ─────────────────────────── 1. Header phong cách Grab ───────────────────────────
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Hàng 1: Nút Back + Vị trí / Địa chỉ phục vụ + Dropdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GlowBackButton(
                        onClick = onBack,
                        tint = DustyMerlot,
                        background = Color(0xFFFBF7E8)
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { /* Mở chọn địa chỉ */ }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Salon gần bạn",
                                fontSize = 11.sp,
                                color = TextMuted,
                                fontWeight = FontWeight.Medium
                            )
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(SuccessGreen)
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Trần Phú, Hải Châu, Đà Nẵng",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF24090F),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            GlowIcon(icon = GlowIcons.ChevronDown, tint = DustyMerlot, size = 14.dp)
                        }
                    }
                }

                // Hàng 2: Thanh tìm kiếm "Bạn đang cần làm đẹp gì?" (giống Grab)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF5EFEB))
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GlowIcon(icon = GlowIcons.Search, tint = DustyMerlot.copy(alpha = 0.7f), size = 18.dp)
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Tìm kiếm tiệm $categoryName, mẫu móng, chuyên viên...",
                                fontSize = 12.5.sp,
                                color = TextMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        androidx.compose.foundation.text.BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            textStyle = LocalTextStyle.current.copy(
                                color = Color(0xFF24090F),
                                fontSize = 13.sp
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                }

                // Hàng 3: Các nút Filter chips ngang (Lọc theo, Dưới 200k, Đặt hẹn ngay...)
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        // Nút Icon Filter (như Grab)
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, Color(0xFFEBDCD3), RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            GlowIcon(icon = GlowIcons.Filter, tint = DustyMerlot, size = 16.dp)
                        }
                    }

                    val filterChips = listOf("Lọc theo", "Dưới 200.000đ", "⚡ Đặt hẹn ngay", "👑 Salon Trứ Danh", "★ 4.8+")
                    items(filterChips) { chip ->
                        val isSelected = selectedFilterChip == chip
                        Row(
                            modifier = Modifier
                                .height(34.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) DustyMerlot else Color(0xFFEBDCD3),
                                    RoundedCornerShape(18.dp)
                                )
                                .background(if (isSelected) PinkPanther.copy(alpha = 0.35f) else Color.White)
                                .clickable { selectedFilterChip = if (isSelected) "Tất cả" else chip }
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = chip,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) DustyMerlot else Color(0xFF24090F)
                            )
                            if (chip == "Lọc theo") {
                                GlowIcon(icon = GlowIcons.ChevronDown, tint = DustyMerlot, size = 12.dp)
                            }
                        }
                    }
                }
            }
        }

        // ─────────────────────────── 2. Danh sách Salon (Feed chuẩn Grab) ───────────────────────────
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Phần 2A: Row ngang các Salon hàng đầu (như Cơm Thố Thiên Phúc, Jollibee, KFC trên Grab)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Tiệm $categoryName Nổi Bật Được Yêu Thích",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3A101A),
                        modifier = Modifier.padding(horizontal = 14.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(topHorizontalHighlights) { salon ->
                            Card(
                                modifier = Modifier
                                    .width(170.dp)
                                    .clickable { onNavigateToSalon(salon.name) },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column {
                                    // Thumbnail trên
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(130.dp)
                                            .background(
                                                Brush.linearGradient(
                                                    listOf(Color(0xFF2B0811), DustyMerlot, Color(0xFF882C42))
                                                )
                                            )
                                            .padding(8.dp)
                                    ) {
                                        // Badge góc trên bên phải (Quán trứ danh / Giảm 50K)
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.TopStart)
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(ChampagneGold)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                                            ) {
                                                GlowIcon(icon = GlowIcons.Crown, tint = Color.White, size = 9.dp)
                                                Text(
                                                    text = "TRỨ DANH",
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            }
                                        }

                                        // Icon đại diện salon ở giữa
                                        Box(
                                            modifier = Modifier.align(Alignment.Center),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            IconMedallion(
                                                icon = GlowIcons.Diamond,
                                                size = 46.dp,
                                                iconSize = 22.dp,
                                                tint = ChampagneGold
                                            )
                                        }

                                        // Badge cọc dưới cùng ảnh
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.BottomStart)
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(DustyMerlot.copy(alpha = 0.9f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "ƯU ĐÃI CỌC 30K",
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = PinkPanther
                                            )
                                        }
                                    }

                                    // Thông tin dưới
                                    Column(
                                        modifier = Modifier.padding(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = salon.name,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF24090F),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                                        ) {
                                            GlowIcon(icon = GlowIcons.Bolt, tint = SuccessGreen, size = 11.dp)
                                            Text(
                                                text = "${salon.eta} • ${salon.distance}",
                                                fontSize = 10.5.sp,
                                                color = TextMuted
                                            )
                                        }

                                        Row(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(PinkPanther.copy(alpha = 0.4f))
                                                .padding(horizontal = 5.dp, vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                                        ) {
                                            GlowIcon(icon = GlowIcons.Tag, tint = DustyMerlot, size = 9.dp)
                                            Text(
                                                text = "Giảm 30K cọc",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = DustyMerlot
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Phần 2B: "Chỉ có trên Glow Prestige" (Container cam nhạt/hồng nude như trên Grab)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFBECE6)),
                    border = BorderStroke(1.dp, PinkPanther)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Title header container
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            GlowIcon(icon = GlowIcons.Sparkle, tint = DustyMerlot, size = 15.dp)
                            Text(
                                text = "Chỉ có trên Glow Prestige",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DustyMerlot
                            )
                        }

                        // Danh sách từng Salon lớn có dịch vụ đính kèm bên dưới (ĐÚNG NHƯ ẢNH ÔNG NHỊ TRÊN GRAB)
                        salons.forEach { salon ->
                            GrabStyleSalonDetailCard(
                                salon = salon,
                                onSalonClick = { onNavigateToSalon(salon.name) },
                                onServiceClick = { sName -> onSelectServiceForBooking(sName) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────── Card Salon chuẩn phong cách Grab (Kèm 3 dịch vụ) ───────────────────────────

@Composable
private fun GrabStyleSalonDetailCard(
    salon: GrabSalon,
    onSalonClick: () -> Unit,
    onServiceClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onSalonClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Phần Header Salon: Thumbnail vuông bên trái + Chi tiết bên phải
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Thumbnail vuông bo góc
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF380914), DustyMerlot, Color(0xFF7A273A))
                            )
                        )
                        .border(1.dp, GoldSoft.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    IconMedallion(
                        icon = GlowIcons.Crown,
                        size = 46.dp,
                        iconSize = 22.dp,
                        tint = ChampagneGold
                    )

                    // Tag cọc góc dưới
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.65f))
                            .padding(vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (salon.requiresDeposit) "ƯU ĐÃI CỌC" else "MIỄN CỌC 0Đ",
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (salon.requiresDeposit) GoldSoft else SuccessGreen
                        )
                    }
                }

                // Cột thông tin chi tiết
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = salon.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF24090F),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Đánh giá ★ 4.7 (1K+) • $$$$ • Danh mục
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        GlowIcon(icon = GlowIcons.StarFilled, tint = ChampagneGold, size = 12.dp)
                        Text(
                            text = "${salon.rating} (${salon.reviewCount})",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF24090F)
                        )
                        Text(text = "•", fontSize = 11.sp, color = TextMuted)
                        Text(text = salon.priceRange, fontSize = 11.sp, color = TextMuted)
                        Text(text = "•", fontSize = 11.sp, color = TextMuted)
                        Text(
                            text = salon.categoryTags,
                            fontSize = 11.sp,
                            color = TextMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Giá / Khuyến mãi & Khoảng cách (như Grab)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = salon.depositLabel,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (salon.requiresDeposit) DustyMerlot else SuccessGreen
                        )
                        Text(text = "•", fontSize = 11.sp, color = TextMuted)
                        Text(text = "${salon.distance} • ${salon.eta}", fontSize = 11.sp, color = TextMuted)
                    }

                    // Huy hiệu "Quán trứ danh Grab" -> "★ Salon Trứ Danh Glow >"
                    if (salon.isSignatureBadge) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFFFF7DF))
                                .border(0.75.dp, ChampagneGold.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            GlowIcon(icon = GlowIcons.StarFilled, tint = ChampagneGold, size = 10.dp)
                            Text(
                                text = salon.signatureBadgeText,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6B4800)
                            )
                            GlowIcon(icon = GlowIcons.ChevronRight, tint = Color(0xFF6B4800), size = 10.dp)
                        }
                    }

                    // Voucher chip (Giảm 18.000đ đơn từ 80k...)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF7F2EE))
                            .border(0.75.dp, Color(0xFFEBDCD3), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        IconMedallion(
                            icon = GlowIcons.Tag,
                            size = 18.dp,
                            iconSize = 10.dp,
                            tint = DustyMerlot
                        )
                        Text(
                            text = salon.promoVoucher,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DustyMerlot,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            HorizontalDivider(color = Color(0xFFF0E5DE))

            // Phần Danh Sách Món / Dịch vụ tiêu biểu (3 hình ảnh ngang như món ăn Grab)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Dịch vụ được đặt nhiều nhất:",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(salon.services) { s ->
                        Column(
                            modifier = Modifier
                                .width(108.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onServiceClick(s.name) },
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // Ảnh dịch vụ (Gradient cao cấp + biểu tượng)
                            Box(
                                modifier = Modifier
                                    .size(108.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Brush.linearGradient(s.bgGradient)),
                                contentAlignment = Alignment.Center
                            ) {
                                IconMedallion(
                                    icon = GlowIcons.Sparkle,
                                    size = 36.dp,
                                    iconSize = 18.dp,
                                    tint = ChampagneGold
                                )

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.White.copy(alpha = 0.9f))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "+ Đặt cọc",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DustyMerlot
                                    )
                                }
                            }

                            // Giá tiền in đậm (như 145.000, 69.000 trên Grab)
                            Text(
                                text = s.price,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF24090F)
                            )

                            // Tên dịch vụ
                            Text(
                                text = s.name,
                                fontSize = 10.5.sp,
                                color = Color(0xFF3A101A),
                                fontWeight = FontWeight.Medium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                lineHeight = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
