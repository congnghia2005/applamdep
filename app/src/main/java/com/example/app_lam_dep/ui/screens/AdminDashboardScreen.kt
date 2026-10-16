package com.example.app_lam_dep.ui.screens

import androidx.compose.foundation.background
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
import com.example.app_lam_dep.ui.theme.ApplamdepTheme
import com.example.app_lam_dep.ui.theme.ChampagneGold
import com.example.app_lam_dep.ui.theme.DustyMerlot
import com.example.app_lam_dep.ui.theme.PinkPanther
import com.example.app_lam_dep.ui.theme.SuccessGreen
import com.example.app_lam_dep.ui.theme.TextMuted
import com.example.app_lam_dep.ui.theme.WarningOrange

@Composable
fun AdminDashboardScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF7E8)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Admin Overview Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DustyMerlot)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Glow Prestige Admin",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Hệ thống kiểm duyệt & Sàn giao dịch",
                                color = PinkPanther,
                                fontSize = 12.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(PinkPanther)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Super Admin",
                                color = DustyMerlot,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AdminStatItem(label = "Đối tác Active", value = "48")
                        AdminStatItem(label = "Hồ sơ chờ duyệt", value = "3")
                        AdminStatItem(label = "Tổng tiền cọc", value = "18.5 Tr đ")
                    }
                }
            }
        }

        // Section: Pending Salon Approvals
        item {
            Text(
                text = "Duyệt hồ sơ cơ sở mới",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DustyMerlot
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "The Royal Nail & Spa", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DustyMerlot)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(WarningOrange.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(text = "Chờ duyệt", fontSize = 11.sp, color = WarningOrange, fontWeight = FontWeight.Bold)
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        GlowIcon(icon = GlowIcons.MapPin, tint = DustyMerlot.copy(alpha = 0.7f), size = 13.dp)
                        Text(text = "12 Nguyễn Huệ, Q.1, TP.HCM • Gói: VIP Sponsored", fontSize = 13.sp, color = TextMuted)
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Phê duyệt", fontSize = 13.sp)
                        }
                        OutlinedButton(
                            onClick = {},
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Xem giấy phép", fontSize = 13.sp, color = DustyMerlot)
                        }
                    }
                }
            }
        }

        // Section: Review Moderation
        item {
            Text(
                text = "Kiểm duyệt Đánh giá vi phạm",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DustyMerlot
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "Đánh giá từ khách hàng (1", fontWeight = FontWeight.Bold, color = DustyMerlot)
                            GlowIcon(icon = GlowIcons.StarFilled, tint = ChampagneGold, size = 13.dp)
                            Text(text = ")", fontWeight = FontWeight.Bold, color = DustyMerlot)
                        }
                        Text(text = "2 giờ trước", fontSize = 12.sp, color = TextMuted)
                    }
                    Text(text = "Nội dung: 'Tiệm làm không đúng giờ hẹn, nhân viên thái độ...' (Bị 3 salon báo cáo spam)", fontSize = 13.sp, color = TextMuted)
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(onClick = {}) {
                            Text("Ẩn đánh giá", color = Color.Red, fontSize = 12.sp)
                        }
                        TextButton(onClick = {}) {
                            Text("Bỏ qua báo cáo", color = DustyMerlot, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminStatItem(label: String, value: String) {
    Column {
        Text(text = label, color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
fun AdminDashboardScreenPreview() {
    ApplamdepTheme {
        AdminDashboardScreen()
    }
}
