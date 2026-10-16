package com.example.app_lam_dep.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

@Composable
fun CustomerBookingsScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF7E8)), // Warm White base canvas
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Page Header
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Lịch Hẹn Của Quý Cô",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3A101A)
                )
                Text(
                    text = "Quản lý vé booking, mã QR check-in, nhắc hẹn và đánh giá",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
        }

        // 2. Smart Reminder Alert Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFDF6EE)),
                border = BorderStroke(1.dp, PinkPanther)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconMedallion(icon = GlowIcons.Bell, size = 38.dp, iconSize = 18.dp)
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "Nhắc hẹn tự động: Còn 45 phút",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DustyMerlot
                        )
                        Text(
                            text = "Lịch hẹn tại L'Amour Lounge bắt đầu lúc 15:30. Nghệ nhân đã sẵn sàng.",
                            fontSize = 11.sp,
                            color = TextMuted,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // 3. Active Booking Ticket Card (#GLOW-8921)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column {
                    // Card Top Status Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DustyMerlot)
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            GlowIcon(icon = GlowIcons.Check, tint = Color.White, size = 14.dp)
                            Text(
                                text = "ĐÃ XÁC NHẬN & ĐẶT CỌC",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "#GLOW-8921",
                            color = PinkPanther,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Card Body
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Salon & Service Name
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "L’Amour Haute Nail & Lash Lounge",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF3A101A)
                            )
                            Text(
                                text = "Sơn Gel Hữu Cơ Pháp + Dưỡng Móng (45p)",
                                fontSize = 13.sp,
                                color = TextMuted
                            )
                        }

                        // Time & Specialist Info Boxes
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFBF7E8))
                                    .padding(12.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(text = "THỜI GIAN", fontSize = 9.5.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                                    Text(text = "15:30 - Hôm nay", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFBF7E8))
                                    .padding(12.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(text = "CHUYÊN VIÊN", fontSize = 9.5.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                                    Text(text = "Master Linh (VIP)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                }
                            }
                        }

                        // QR Code Container (Dotted Border)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, PinkPanther, RoundedCornerShape(14.dp))
                                .background(Color(0xFFFDF6EE))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(112.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White)
                                        .border(1.dp, ChampagneGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    GlowIcon(icon = GlowIcons.QrCode, tint = Color(0xFF24090F), size = 88.dp, contentDescription = "Mã QR check-in")
                                }
                                Text(
                                    text = "Đưa mã QR này cho lễ tân để nhận phòng VIP ngay",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DustyMerlot,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }

                        // Financial Summary
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Tổng giá trị dịch vụ:", fontSize = 12.5.sp, color = TextMuted)
                                Text(text = "180.000 đ", fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF24090F))
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Tiền cọc 30% đã thanh toán (MoMo Sandbox):", fontSize = 12.5.sp, color = TextMuted)
                                Text(text = "-50.000 đ", fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold, color = DustyMerlot)
                            }
                            HorizontalDivider(color = Color(0xFFEBDCD3))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Thanh toán còn lại tại Salon:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3A101A))
                                Text(text = "130.000 đ", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                            }
                        }

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = {},
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, DustyMerlot),
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                            ) {
                                Text(text = "↺ Hủy & Hoàn cọc", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                            }

                            Button(
                                onClick = {},
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = DustyMerlot)
                            ) {
                                GlowIcon(icon = GlowIcons.Calendar, tint = Color.White, size = 16.dp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Đổi giờ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // 4. Completed Booking Card (Đã hoàn tất)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFEFECE6))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(text = "ĐÃ HOÀN TẤT", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "An Yên Thảo Dược & Luxury Retreat",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3A101A)
                        )
                        Text(
                            text = "Gội đầu dưỡng sinh cung đình 75p • Ngày 05/10/2026",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDF2EF))
                    ) {
                        GlowIcon(icon = GlowIcons.StarFilled, tint = ChampagneGold, size = 16.dp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Đánh giá 5 sao & Nhận xét (+100 xu)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DustyMerlot
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
fun CustomerBookingsScreenPreview() {
    ApplamdepTheme {
        CustomerBookingsScreen()
    }
}
