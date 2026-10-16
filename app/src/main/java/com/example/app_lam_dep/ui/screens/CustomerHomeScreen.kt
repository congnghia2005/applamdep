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
import com.example.app_lam_dep.ui.icons.IconMedallion
import com.example.app_lam_dep.ui.theme.ChampagneGold
import com.example.app_lam_dep.ui.theme.DustyMerlot
import com.example.app_lam_dep.ui.theme.GoldSoft
import com.example.app_lam_dep.ui.theme.PinkPanther
import com.example.app_lam_dep.ui.theme.SuccessGreen
import com.example.app_lam_dep.ui.theme.TextMuted

data class BeautyCategory(val name: String, val icon: ImageVector, val isHot: Boolean = false)
data class HeroBanner(val badge: String, val title: String, val subtitle: String, val buttonText: String)
data class SalonMarketplaceItem(val id: Int, val name: String, val services: String, val rating: Double, val reviews: String, val distance: String, val price: String, val isSponsored: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomeScreen(
    onNavigateToSalon: (String) -> Unit,
    onSelectServiceForBooking: (String) -> Unit,
    onSelectCategory: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var activeCategoryModal by remember { mutableStateOf<String?>(null) }

    // Mỗi danh mục có icon riêng, đúng ngữ nghĩa dịch vụ (không dùng emoji)
    val categories = listOf(
        BeautyCategory("Làm Nail Art", GlowIcons.NailPolish, true),
        BeautyCategory("Spa Trị Liệu", GlowIcons.Lotus),
        BeautyCategory("Chăm sóc da", GlowIcons.Serum),
        BeautyCategory("Đặt lịch VIP", GlowIcons.Crown),
        BeautyCategory("Hair Salon", GlowIcons.Scissors),
        BeautyCategory("Makeup Tiệc", GlowIcons.Lipstick),
        BeautyCategory("Dưỡng sinh", GlowIcons.Leaf)
    )

    val banners = listOf(
        HeroBanner("HAUTE NAIL", "Nail Art Mắt Mèo", "Sơn gel ngọc trai & Dưỡng móng Pháp", "Đặt cọc giữ suất"),
        HeroBanner("SIGNATURE SPA", "Nghỉ Dưỡng Đá Nóng", "Xông tinh dầu hoa hồng & Thảo dược 90p", "Trải nghiệm")
    )

    val salons = listOf(
        SalonMarketplaceItem(1, "L’Amour Haute Nail & Lash Lounge", "Sơn gel hữu cơ • Thiết kế móng nghệ thuật Haute • Nối mi tơ lụa", 4.99, "(1.8k+ đánh giá)", "0.8 km • Đồng Khởi, Q.1", "50.000đ", true),
        SalonMarketplaceItem(2, "An Yên Thảo Dược & Luxury Retreat", "Gội đầu dưỡng sinh cung đình • Massage đá nóng Himalaya", 4.97, "(1.1k+ đánh giá)", "1.5 km • Tú Xương, Q.3", "50.000đ", false)
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF7E8)),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Search Bar Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DustyMerlot)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                        .border(1.dp, GoldSoft.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    GlowIcon(icon = GlowIcons.Camera, tint = GoldSoft, size = 20.dp, contentDescription = "Tìm bằng hình ảnh")
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GlowIcon(icon = GlowIcons.Search, tint = DustyMerlot.copy(alpha = 0.7f), size = 18.dp)
                    Text(text = "Tìm theo tên tiệm, dịch vụ, quận...", fontSize = 12.sp, color = TextMuted)
                }

                // Nút hạng thành viên VIP
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(Color(0xFFF3E2B8), ChampagneGold)))
                        .border(1.dp, Color.White.copy(alpha = 0.6f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    GlowIcon(icon = GlowIcons.Crown, tint = DustyMerlot, size = 20.dp, contentDescription = "Hạng VIP")
                }
            }
        }

        // Categories Grid (Clickable to open category bottom sheet)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val rows = categories.chunked(4)
                    rows.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            rowItems.forEach { cat ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .width(70.dp)
                                        .clickable { onSelectCategory(cat.name) }
                                ) {
                                    Box(
                                        modifier = Modifier.size(56.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        IconMedallion(
                                            icon = cat.icon,
                                            size = 52.dp,
                                            iconSize = 24.dp,
                                            contentDescription = cat.name
                                        )
                                        if (cat.isHot) {
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Brush.linearGradient(listOf(Color(0xFF8A2E44), DustyMerlot)))
                                                    .border(0.75.dp, GoldSoft, RoundedCornerShape(6.dp))
                                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                                            ) {
                                                Text(text = "HOT", fontSize = 7.sp, color = GoldSoft, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = cat.name,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF24090F),
                                        maxLines = 1
                                    )
                                }
                            }
                            repeat(4 - rowItems.size) {
                                Spacer(modifier = Modifier.width(70.dp))
                            }
                        }
                    }
                }
            }
        }

        // Dual Quick Action Hub
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "VÍ GLOWPRIVÉ", fontSize = 9.5.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = "3.450.000 đ", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF24090F))
                        }
                        IconMedallion(
                            icon = GlowIcons.Diamond,
                            size = 34.dp,
                            iconSize = 17.dp,
                            tint = ChampagneGold,
                            contentDescription = "Ví GlowPrivé"
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(text = "HÔM NAY", fontSize = 9.5.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(SuccessGreen)
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = "15:30 • L'Amour", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF24090F))
                        }
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(PinkPanther.copy(alpha = 0.4f))
                                .padding(horizontal = 6.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            GlowIcon(icon = GlowIcons.StarFilled, tint = ChampagneGold, size = 12.dp)
                            Text(text = "4.9", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                        }
                    }
                }
            }
        }

        // Hero Banners
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Trải nghiệm Thượng lưu", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3A101A))
                    Row(
                        modifier = Modifier.clickable { },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Tất cả", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                        GlowIcon(icon = GlowIcons.ChevronRight, tint = DustyMerlot, size = 14.dp)
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(banners) { banner ->
                        Card(
                            modifier = Modifier
                                .width(290.dp)
                                .height(150.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = DustyMerlot)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Brush.linearGradient(listOf(Color(0xFF2B0811), DustyMerlot, Color(0xFF882C42))))
                                    .padding(14.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxHeight(),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color.White.copy(alpha = 0.2f))
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(text = banner.badge, fontSize = 8.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                    Column {
                                        Text(text = banner.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(text = banner.subtitle, fontSize = 10.sp, color = PinkPanther)
                                    }
                                    Button(
                                        onClick = { onSelectServiceForBooking(banner.title) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                        modifier = Modifier.height(30.dp)
                                    ) {
                                        Text(text = banner.buttonText, fontSize = 10.sp, color = DustyMerlot, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Salon Marketplace Feed (Clickable to Salon Detail)
        item {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Không Gian Làm Đẹp Thượng Lưu",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3A101A)
                )

                salons.forEach { salon ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToSalon(salon.name) },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    if (salon.isSponsored) {
                                        Row(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(PinkPanther)
                                                .padding(horizontal = 6.dp, vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                                        ) {
                                            GlowIcon(icon = GlowIcons.Crown, tint = DustyMerlot, size = 10.dp)
                                            Text(text = "SPONSORED TOP", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                    }
                                    Text(text = salon.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3A101A))
                                }
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    GlowIcon(icon = GlowIcons.StarFilled, tint = ChampagneGold, size = 14.dp)
                                    Text(text = "${salon.rating}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3A101A))
                                    Text(text = salon.reviews, fontSize = 10.sp, color = TextMuted)
                                }
                            }
                            Text(text = salon.services, fontSize = 12.sp, color = TextMuted)
                            HorizontalDivider(color = Color(0xFFEBDCD3))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    GlowIcon(icon = GlowIcons.MapPin, tint = DustyMerlot.copy(alpha = 0.7f), size = 13.dp)
                                    Text(text = salon.distance, fontSize = 11.sp, color = TextMuted)
                                }
                                Text(text = "Cọc 30%: ${salon.price}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                            }
                        }
                    }
                }
            }
        }
    }

    // Category Service Bottom Sheet Modal (Matching second screenshot)
    if (activeCategoryModal != null) {
        ModalBottomSheet(
            onDismissRequest = { activeCategoryModal = null },
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "NGHỆ NHÂN TẠO MẪU MÓNG CHUẨN SALON CHÂU ÂU",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = DustyMerlot,
                    letterSpacing = 1.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GlowIcon(icon = GlowIcons.Sparkle, tint = ChampagneGold, size = 18.dp)
                    Text(
                        text = "Bộ Sưu Tập $activeCategoryModal Haute Couture",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3A101A)
                    )
                }

                HorizontalDivider(color = Color(0xFFEBDCD3))

                val categoryServices = listOf(
                    CategoryServiceModalItem("Sơn Gel Hữu Cơ Pháp + Dưỡng Móng Tinh Dầu", "45 phút", 4.99, "54k", "180.000 đ", "Signature"),
                    CategoryServiceModalItem("Nail Art Mắt Mèo Cực Quang & Tráng Gương", "60 phút", 5.0, "84k", "280.000 đ", "Xu hướng"),
                    CategoryServiceModalItem("Đắp Móng Form Hoàng Gia & Đính Đá Swarovski", "75 phút", 4.98, "135k", "450.000 đ", "Haute VIP"),
                    CategoryServiceModalItem("Chăm Sóc Móng Spa Tinh Chất Ngọc Trai", "50 phút", 4.95, "66k", "220.000 đ", "Dưỡng sâu")
                )

                categoryServices.forEach { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                activeCategoryModal = null
                                onSelectServiceForBooking(item.name)
                            },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF7E8)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = DustyMerlot
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(text = "${item.duration} •", fontSize = 11.sp, color = TextMuted)
                                        GlowIcon(icon = GlowIcons.StarFilled, tint = ChampagneGold, size = 11.dp)
                                        Text(text = "${item.rating} • Cọc 30%: ${item.deposit}", fontSize = 11.sp, color = TextMuted)
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(PinkPanther.copy(alpha = 0.5f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = item.tag,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DustyMerlot
                                    )
                                }
                            }

                            HorizontalDivider(color = Color(0xFFF0E5DE))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.price,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                    color = DustyMerlot
                                )

                                Button(
                                    onClick = {
                                        activeCategoryModal = null
                                        onSelectServiceForBooking(item.name)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = DustyMerlot),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text("Đặt cọc", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

data class CategoryServiceModalItem(
    val name: String,
    val duration: String,
    val rating: Double,
    val deposit: String,
    val price: String,
    val tag: String
)
