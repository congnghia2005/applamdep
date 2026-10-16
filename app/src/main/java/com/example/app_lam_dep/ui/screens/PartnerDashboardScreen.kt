package com.example.app_lam_dep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_lam_dep.ui.icons.GlowIcon
import com.example.app_lam_dep.ui.icons.GlowIcons
import com.example.app_lam_dep.ui.icons.IconMedallion
import com.example.app_lam_dep.ui.theme.ApplamdepTheme
import com.example.app_lam_dep.ui.theme.ChampagneGold
import com.example.app_lam_dep.ui.theme.DustyMerlot
import com.example.app_lam_dep.ui.theme.PinkPanther
import com.example.app_lam_dep.ui.theme.SuccessGreen
import com.example.app_lam_dep.ui.theme.TextMuted

data class PartnerServiceItem(val name: String, val duration: String, val price: String, val deposit: String)

@Composable
fun PartnerDashboardScreen(
    onNavigateToReviews: () -> Unit,
    modifier: Modifier = Modifier
) {
    val services = listOf(
        PartnerServiceItem("Sơn Gel Hữu Cơ Pháp + Dưỡng Móng", "45 phút", "180.000 đ", "Cọc 50k"),
        PartnerServiceItem("Nail Art Mắt Mèo Cực Quang VIP", "60 phút", "280.000 đ", "Cọc 84k"),
        PartnerServiceItem("Massage Đá Nóng Himalaya & Thảo Dược", "90 phút", "380.000 đ", "Cọc 114k")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF7E8)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Large Top Salon Profile Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = DustyMerlot),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconMedallion(
                        icon = GlowIcons.Crown,
                        size = 76.dp,
                        iconSize = 38.dp,
                        tint = ChampagneGold,
                        contentDescription = "Salon Crown"
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "L'Amour Luxury Spa & Nail",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            GlowIcon(icon = GlowIcons.MapPin, tint = PinkPanther, size = 13.dp)
                            Text(
                                text = "Quận 1, TP. Hồ Chí Minh • Mở cửa: 09:00 - 21:00",
                                color = PinkPanther,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(SuccessGreen)
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "● Đang hoạt động",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 2. Separate Card: Doanh Thu & Lịch Hẹn
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Booking hôm nay", fontSize = 12.sp, color = TextMuted)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "12", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .height(40.dp)
                            .width(1.dp),
                        color = Color(0xFFEBDCD3)
                    )
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Doanh thu ngày", fontSize = 12.sp, color = TextMuted)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "4.850.000 đ", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                    }
                }
            }
        }

        // 3. Separate Card: Số Điểm & Sao (Clickable to Reviews Screen)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToReviews() },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Đánh giá của khách hàng", fontSize = 12.sp, color = TextMuted)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            GlowIcon(icon = GlowIcons.StarFilled, tint = ChampagneGold, size = 18.dp)
                            Text(text = "4.9 (1.8k+ reviews)", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "Xem tất cả", fontSize = 13.sp, color = DustyMerlot, fontWeight = FontWeight.Bold)
                        GlowIcon(icon = GlowIcons.ChevronRight, tint = DustyMerlot, size = 14.dp)
                    }
                }
            }
        }

        // 4. Shop Services List (Replacing Quick Actions)
        item {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                GlowIcon(icon = GlowIcons.NailPolish, tint = DustyMerlot, size = 18.dp)
                Text(
                    text = "Danh Mục Dịch Vụ Của Shop",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DustyMerlot
                )
            }
        }

        items(services.size) { idx ->
            val serv = services[idx]
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = serv.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DustyMerlot)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "⏱ ${serv.duration} • ${serv.deposit}", fontSize = 12.sp, color = TextMuted)
                    }
                    Text(text = serv.price, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DustyMerlot)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
fun PartnerDashboardScreenPreview() {
    ApplamdepTheme {
        PartnerDashboardScreen(onNavigateToReviews = {})
    }
}
