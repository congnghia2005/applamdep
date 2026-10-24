package com.example.app_lam_dep.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_lam_dep.R
import com.example.app_lam_dep.ui.icons.GlowIcon
import com.example.app_lam_dep.ui.icons.GlowIcons
import com.example.app_lam_dep.ui.theme.ChampagneGold
import com.example.app_lam_dep.ui.theme.DustyMerlot
import com.example.app_lam_dep.ui.theme.GoldSoft
import com.example.app_lam_dep.ui.theme.PinkPanther
import com.example.app_lam_dep.ui.theme.TextMuted

data class BeautyCategoryItem(val name: String, val icon: ImageVector)
data class FavoriteServiceItem(val name: String, val price: String, val imageRes: Int, val salon: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomeScreen(
    onNavigateToSalon: (String) -> Unit,
    onSelectServiceForBooking: (String) -> Unit,
    onSelectCategory: (String) -> Unit = {},
    onOpenShopPlans: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var activeCategoryModal by remember { mutableStateOf<String?>(null) }
    var isFavorite by remember { mutableStateOf(false) }

    // 8 categories matching Image 1
    val categories = listOf(
        BeautyCategoryItem("Làm Nail Art", GlowIcons.NailPolish),
        BeautyCategoryItem("Spa Trị Liệu", GlowIcons.Lotus),
        BeautyCategoryItem("Chăm sóc da", GlowIcons.Serum),
        BeautyCategoryItem("Đặt lịch VIP", GlowIcons.Crown),
        BeautyCategoryItem("Hair Salon", GlowIcons.Scissors),
        BeautyCategoryItem("Makeup Tiệc", GlowIcons.Lipstick),
        BeautyCategoryItem("Dưỡng sinh", GlowIcons.Leaf),
        BeautyCategoryItem("Khác", GlowIcons.MoreHorizontal)
    )

    val favoriteServices = listOf(
        FavoriteServiceItem("Sơn Gel Hữu Cơ + Dưỡng Móng", "180.000 đ", R.drawable.nail_french_gel, "1994 Nailsroom"),
        FavoriteServiceItem("Nail Art Mắt Mèo Cực Quang VIP", "280.000 đ", R.drawable.nail_cateye_aurora, "BONNIE NAIL"),
        FavoriteServiceItem("Gội Đầu Dưỡng Sinh Thảo Dược", "250.000 đ", R.drawable.shop_hero_spa, "Thì Là SPA")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF7E8)),
        contentPadding = PaddingValues(bottom = 36.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Search Bar Top Row (Image 1 Header integration)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF360814), Color(0xFF2B0710))
                        )
                    )
                    .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 18.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFEBDCD3), RoundedCornerShape(24.dp))
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GlowIcon(
                        icon = GlowIcons.Search,
                        tint = Color(0xFF7A686D),
                        size = 20.dp,
                        contentDescription = "Search"
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Tìm salon Đà Nẵng, dịch vụ, quận...",
                                color = Color(0xFF8E7A80),
                                fontSize = 13.sp
                            )
                        }
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        GlowIcon(
                            icon = GlowIcons.Filter,
                            tint = Color(0xFF360814),
                            size = 18.dp,
                            contentDescription = "Bộ lọc"
                        )
                    }
                }
            }
        }

        // 2. Section: Khám phá dịch vụ (Image 1 4x2 grid with soft rose medallions)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Khám phá dịch vụ",
                        fontSize = 17.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3A101A)
                    )
                    Row(
                        modifier = Modifier.clickable { activeCategoryModal = "Tất cả dịch vụ" },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "Tất cả",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6B1228)
                        )
                        GlowIcon(
                            icon = GlowIcons.ChevronRight,
                            tint = Color(0xFF6B1228),
                            size = 12.dp
                        )
                    }
                }

                // 4x2 Grid of services
                val rows = categories.chunked(4)
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    rows.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            rowItems.forEach { cat ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .width(76.dp)
                                        .clickable {
                                            activeCategoryModal = cat.name
                                            onSelectCategory(cat.name)
                                        }
                                ) {
                                    // Soft blush circular medallion matching Image 1
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(CircleShape)
                                            .background(
                                                Brush.radialGradient(
                                                    colors = listOf(
                                                        Color(0xFFFFF9F5),
                                                        Color(0xFFF9E7E2)
                                                    )
                                                )
                                            )
                                            .border(1.dp, Color(0xFFF3CEC5), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        GlowIcon(
                                            icon = cat.icon,
                                            tint = Color(0xFF6B1228),
                                            size = 24.dp,
                                            contentDescription = cat.name
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = cat.name,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF24090F),
                                        maxLines = 1
                                    )
                                }
                            }
                            repeat(4 - rowItems.size) {
                                Spacer(modifier = Modifier.width(76.dp))
                            }
                        }
                    }
                }
            }
        }

        // 3. Dual Quick Action Cards (Image 1: Ví GlowPrivé & Hôm nay 15:30)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Left Card: Ví GlowPrivé (Dark wine card)
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(86.dp)
                        .clickable { },
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF3D0C18),
                                        Color(0xFF290610),
                                        Color(0xFF1F030B)
                                    )
                                )
                            )
                            .padding(12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.12f))
                                    .border(1.dp, ChampagneGold.copy(alpha = 0.5f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                GlowIcon(
                                    icon = GlowIcons.Crown,
                                    tint = ChampagneGold,
                                    size = 20.dp,
                                    contentDescription = "Crown"
                                )
                            }
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Text(
                                        text = "VÍ TÀI KHOẢN",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFE5B8C2),
                                        letterSpacing = 0.5.sp
                                    )
                                    GlowIcon(icon = GlowIcons.ChevronRight, tint = Color(0xFFE5B8C2), size = 9.dp)
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "3.450.000 đ",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                // Right Card: Hôm nay 15:30 · L'Amour (White card)
                Card(
                    modifier = Modifier
                        .weight(1.05f)
                        .height(86.dp)
                        .clickable { },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF9EFEF)),
                            contentAlignment = Alignment.Center
                        ) {
                            GlowIcon(icon = GlowIcons.Calendar, tint = Color(0xFF6B1228), size = 20.dp)
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(text = "Hôm nay", fontSize = 10.sp, color = Color(0xFF7A686D), fontWeight = FontWeight.Medium)
                                Box(
                                    modifier = Modifier
                                        .size(5.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE11D48))
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "15:30 · 1994 Nailsroom",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF24090F)
                                )
                            }
                            // Star rating pill
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFFFF7ED))
                                    .padding(horizontal = 5.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                GlowIcon(icon = GlowIcons.StarFilled, tint = ChampagneGold, size = 10.dp)
                                Text(text = "4.9", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF882C42))
                                GlowIcon(icon = GlowIcons.ChevronRight, tint = Color(0xFF882C42), size = 9.dp)
                            }
                        }
                    }
                }
            }
        }

        // 4. Suggested Stores Banner Card (Cửa Hàng Gợi Ý Dành Cho Bạn)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(185.dp)
                    .clickable { onNavigateToSalon("1994 Nailsroom") },
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // High-res VIP image
                    Image(
                        painter = painterResource(id = R.drawable.banner_glowprive_vip),
                        contentDescription = "Cửa Hàng Gợi Ý Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Dark luxury gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF2C0610).copy(alpha = 0.95f),
                                        Color(0xFF2C0610).copy(alpha = 0.70f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            // Pill badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White.copy(alpha = 0.15f))
                                    .border(0.75.dp, ChampagneGold.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    GlowIcon(icon = GlowIcons.Sparkle, tint = ChampagneGold, size = 11.dp)
                                    Text(
                                        text = "CỬA HÀNG GỢI Ý DÀNH CHO BẠN",
                                        color = ChampagneGold,
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }

                            Text(
                                text = "Top Salon Đà Nẵng Đã Kiểm Duyệt &\nƯu Đãi Độc Quyền",
                                color = Color.White,
                                fontFamily = FontFamily.Serif,
                                fontSize = 17.5.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 22.sp
                            )

                            Text(
                                text = "• 1994 Nailsroom: Giảm 20% dịch vụ Nail Art & Mi\n• BONNIE NAIL: Tặng Voucher 100.000đ cho khách mới\n• Thì Là SPA: Gội đầu dưỡng sinh & Thư giãn chuyên sâu",
                                color = Color(0xFFF3CEC5),
                                fontSize = 9.5.sp,
                                lineHeight = 13.5.sp
                            )
                        }

                        // Explore button
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White)
                                .clickable { onNavigateToSalon("1994 Nailsroom") }
                                .padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                text = "Khám phá các Cửa hàng gợi ý",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF360814)
                            )
                            GlowIcon(icon = GlowIcons.ChevronRight, tint = Color(0xFF360814), size = 12.dp)
                        }
                    }

                    // Carousel 3-dots indicator at bottom center
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 14.dp, height = 4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color.White)
                        )
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.45f))
                        )
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.45f))
                        )
                    }
                }
            }
        }

        // 5. Section: Salon nổi bật (Image 1 L'Amour Luxury Spa & Nail)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Salon nổi bật",
                        fontSize = 17.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3A101A)
                    )
                    Row(
                        modifier = Modifier.clickable { onNavigateToSalon("1994 Nailsroom") },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "Xem tất cả",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6B1228)
                        )
                        GlowIcon(
                            icon = GlowIcons.ChevronRight,
                            tint = Color(0xFF6B1228),
                            size = 12.dp
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToSalon("1994 Nailsroom") },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Thumbnail photo: salon_luxury_interior
                            Image(
                                painter = painterResource(id = R.drawable.salon_luxury_interior),
                                contentDescription = "Salon Interior",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(105.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            )

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "1994 Nailsroom",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.5.sp,
                                        color = Color(0xFF24090F)
                                    )
                                    Box(
                                        modifier = Modifier.clickable { isFavorite = !isFavorite }
                                    ) {
                                        GlowIcon(
                                            icon = if (isFavorite) GlowIcons.HeartFilled else GlowIcons.Heart,
                                            tint = if (isFavorite) Color(0xFFE11D48) else Color(0xFF8E7A80),
                                            size = 18.dp
                                        )
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    GlowIcon(icon = GlowIcons.StarFilled, tint = ChampagneGold, size = 13.dp)
                                    Text(
                                        text = "4.9",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF24090F)
                                    )
                                    Text(
                                        text = "(238 đánh giá)",
                                        fontSize = 11.sp,
                                        color = Color(0xFF7A686D)
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    GlowIcon(icon = GlowIcons.MapPin, tint = Color(0xFF7A686D), size = 12.dp)
                                    Text(
                                        text = "172/5 Trần Phú, Hải Châu, Đà Nẵng · 1.2 km",
                                        fontSize = 11.sp,
                                        color = Color(0xFF7A686D)
                                    )
                                }

                                // Tags row
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    listOf("Nail Art", "Spa", "Chăm sóc da").forEach { tag ->
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(0xFFF9EFEF))
                                                .padding(horizontal = 7.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = tag,
                                                fontSize = 10.sp,
                                                color = Color(0xFF6B1228),
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Promo discount pill banner
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFFDF2EE))
                                .padding(horizontal = 10.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            GlowIcon(icon = GlowIcons.Tag, tint = Color(0xFF6B1228), size = 14.dp)
                            Text(
                                text = "Giảm 20% cho dịch vụ nail art",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF6B1228)
                            )
                        }
                    }
                }
            }
        }

        // 6. Section: Dịch vụ yêu thích (Image 1 bottom carousel)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Dịch vụ yêu thích",
                        fontSize = 17.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3A101A)
                    )
                    Row(
                        modifier = Modifier.clickable { },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "Xem tất cả",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6B1228)
                        )
                        GlowIcon(
                            icon = GlowIcons.ChevronRight,
                            tint = Color(0xFF6B1228),
                            size = 12.dp
                        )
                    }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(end = 8.dp)
                ) {
                    items(favoriteServices) { fav ->
                        Card(
                            modifier = Modifier
                                .width(155.dp)
                                .clickable { onSelectServiceForBooking(fav.name) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column {
                                Image(
                                    painter = painterResource(id = fav.imageRes),
                                    contentDescription = fav.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(110.dp)
                                )
                                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                    Text(
                                        text = fav.name,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF24090F),
                                        maxLines = 1
                                    )
                                    Text(
                                        text = fav.salon,
                                        fontSize = 10.5.sp,
                                        color = Color(0xFF7A686D)
                                    )
                                    Text(
                                        text = fav.price,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF6B1228)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Category Bottom Sheet Modal
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
                    text = "DỊCH VỤ LÀM ĐẸP THƯỢNG LƯU",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6B1228),
                    letterSpacing = 1.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GlowIcon(icon = GlowIcons.Sparkle, tint = ChampagneGold, size = 18.dp)
                    Text(
                        text = "Bộ Sưu Tập $activeCategoryModal",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3A101A)
                    )
                }

                HorizontalDivider(color = Color(0xFFEBDCD3))

                val modalServices = listOf(
                    "Sơn Gel Hữu Cơ Pháp + Dưỡng Móng" to "180.000 đ",
                    "Nail Art Mắt Mèo Cực Quang VIP" to "280.000 đ",
                    "Đắp Móng Hoàng Gia Đính Đá Swarovski" to "450.000 đ"
                )

                modalServices.forEach { (sName, sPrice) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                activeCategoryModal = null
                                onSelectServiceForBooking(sName)
                            },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF7E8))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = sName, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                Text(text = "Thời lượng 45-60 phút", fontSize = 11.sp, color = Color(0xFF7A686D))
                            }
                            Text(text = sPrice, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B1228))
                        }
                    }
                }
            }
        }
    }
}
