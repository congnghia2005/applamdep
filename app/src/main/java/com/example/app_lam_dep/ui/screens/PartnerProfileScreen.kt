package com.example.app_lam_dep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.app_lam_dep.ui.icons.GlowIcon
import com.example.app_lam_dep.ui.icons.GlowIcons
import com.example.app_lam_dep.ui.theme.ChampagneGold
import com.example.app_lam_dep.ui.theme.DustyMerlot
import com.example.app_lam_dep.ui.theme.PinkPanther
import com.example.app_lam_dep.ui.theme.TextMuted
import com.example.app_lam_dep.ui.theme.WarningOrange

@Composable
fun PartnerProfileScreen(
    modifier: Modifier = Modifier
) {
    var reported by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF7E8)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Cài Đặt Shop & Kiểm Duyệt Đánh Giá",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DustyMerlot
                )
                GlowIcon(icon = GlowIcons.Shield, tint = DustyMerlot, size = 18.dp)
            }
        }

        // Section: Customer Reviews & Report to Admin (As sketched)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "Đánh giá của khách hàng (1", fontWeight = FontWeight.Bold, color = DustyMerlot)
                            GlowIcon(icon = GlowIcons.StarFilled, tint = ChampagneGold, size = 13.dp)
                            Text(text = ")", fontWeight = FontWeight.Bold, color = DustyMerlot)
                        }
                        Text(text = "Hôm nay", fontSize = 12.sp, color = TextMuted)
                    }
                    Text(
                        text = "Nội dung: 'Nhân viên thái độ không tốt, làm không đúng hẹn...' (Đánh giá tiêu cực / spam)",
                        fontSize = 13.sp,
                        color = TextMuted
                    )

                    if (reported) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(WarningOrange.copy(alpha = 0.2f))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                GlowIcon(icon = GlowIcons.Alert, tint = WarningOrange, size = 14.dp)
                                Text(
                                    text = "Đã tố cáo lên Admin (Chờ Admin kiểm duyệt xóa)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WarningOrange
                                )
                            }
                        }
                    } else {
                        Button(
                            onClick = { reported = true },
                            colors = ButtonDefaults.buttonColors(containerColor = WarningOrange),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                GlowIcon(icon = GlowIcons.Flag, tint = Color.White, size = 14.dp)
                                Text("Tố cáo cho Admin xem xét", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Cài đặt cơ sở", fontWeight = FontWeight.Bold, color = DustyMerlot)
                    HorizontalDivider()
                    Text(text = "Thông tin chung & Giờ mở cửa", fontSize = 14.sp, color = TextMuted)
                    HorizontalDivider()
                    Text(text = "Quản lý gói Membership (VIP Sponsor)", fontSize = 14.sp, color = TextMuted)
                    HorizontalDivider()
                    Text(text = "Đăng xuất tài khoản Shop", fontSize = 14.sp, color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
