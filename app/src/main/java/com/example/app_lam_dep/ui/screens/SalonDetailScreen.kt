package com.example.app_lam_dep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_lam_dep.ui.icons.GlowBackButton
import com.example.app_lam_dep.ui.icons.GlowIcon
import com.example.app_lam_dep.ui.icons.GlowIcons
import com.example.app_lam_dep.ui.icons.IconMedallion
import com.example.app_lam_dep.ui.theme.ChampagneGold
import com.example.app_lam_dep.ui.theme.DustyMerlot
import com.example.app_lam_dep.ui.theme.PinkPanther
import com.example.app_lam_dep.ui.theme.TextMuted

data class Specialist(val name: String, val role: String)
data class ServiceMenuItem(val id: String, val name: String, val duration: String, val price: String, val tag: String)

@Composable
fun SalonDetailScreen(
    salonName: String = "L'Amour Luxury Spa & Nail",
    onBack: () -> Unit,
    onSelectServiceForBooking: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val specialists = listOf(
        Specialist("Master Linh (VIP)", "Chuyên gia tạo mẫu móng chính"),
        Specialist("Chuyên viên Sarah", "Master Nail Art & Sơn Gel"),
        Specialist("Chuyên viên Minh Thư", "Chăm sóc da & Spa Retreat")
    )

    val serviceMenu = listOf(
        ServiceMenuItem("1", "Sơn Gel Hữu Cơ Pháp + Dưỡng Móng", "45 phút", "180.000 đ", "Signature"),
        ServiceMenuItem("2", "Nail Art Mắt Mèo Cực Quang & Tráng Gương", "60 phút", "280.000 đ", "Xu hướng"),
        ServiceMenuItem("3", "Đắp Móng Form Hoàng Gia & Đính Đá Swarovski", "75 phút", "450.000 đ", "Haute VIP"),
        ServiceMenuItem("4", "Chăm Sóc Móng Spa Tinh Chất Ngọc Trai", "50 phút", "220.000 đ", "Dưỡng sâu")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF7E8))
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DustyMerlot)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GlowBackButton(onClick = onBack)
            Column {
                Text(text = salonName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    GlowIcon(icon = GlowIcons.MapPin, tint = PinkPanther, size = 12.dp)
                    Text(text = "Quận 1, TP.HCM • Mở cửa: 09:00 - 21:00", fontSize = 11.sp, color = PinkPanther)
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Salon Info Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconMedallion(
                            icon = GlowIcons.Crown,
                            size = 64.dp,
                            iconSize = 32.dp,
                            tint = ChampagneGold,
                            contentDescription = salonName
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = salonName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3A101A))
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                GlowIcon(icon = GlowIcons.StarFilled, tint = ChampagneGold, size = 14.dp)
                                Text(text = "4.9 (1.8k+ đánh giá)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF3A101A))
                                Text(text = "• Hoạt động 09:00 - 21:00", fontSize = 12.sp, color = TextMuted)
                            }
                        }
                    }
                }
            }

            // Specialists / Nhân viên section
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        GlowIcon(icon = GlowIcons.Scissors, tint = DustyMerlot, size = 18.dp)
                        Text(text = "Đội Ngũ Chuyên Gia & Nhân Viên", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                    }
                    specialists.forEach { spec ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                IconMedallion(
                                    icon = GlowIcons.User,
                                    size = 42.dp,
                                    iconSize = 20.dp
                                )
                                Column {
                                    Text(text = spec.name, fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = DustyMerlot)
                                    Text(text = spec.role, fontSize = 11.sp, color = TextMuted)
                                }
                            }
                        }
                    }
                }
            }

            // Service Menu Categories (Clickable to Booking)
            item {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    GlowIcon(icon = GlowIcons.Sparkle, tint = ChampagneGold, size = 18.dp)
                    Text(text = "Danh Mục Dịch Vụ Của Salon", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                }
            }

            items(serviceMenu) { serv ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectServiceForBooking(serv.name) },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = serv.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = DustyMerlot
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    GlowIcon(icon = GlowIcons.Clock, tint = TextMuted, size = 12.dp)
                                    Text(
                                        text = "${serv.duration} • Cọc 30% giữ suất",
                                        fontSize = 12.sp,
                                        color = TextMuted
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(PinkPanther.copy(alpha = 0.5f))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = serv.tag,
                                    fontSize = 10.sp,
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
                                text = serv.price,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = DustyMerlot
                            )

                            Button(
                                onClick = { onSelectServiceForBooking(serv.name) },
                                colors = ButtonDefaults.buttonColors(containerColor = DustyMerlot),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text("Đặt cọc", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
