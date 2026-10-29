package com.example.app_lam_dep.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.app_lam_dep.ui.theme.GoldSoft

data class TransactionItem(
    val title: String,
    val time: String,
    val amount: String,
    val isDeduction: Boolean,
    val status: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerProfileScreen(
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var walletBalance by remember { mutableLongStateOf(3450000L) }
    var rewardPoints by remember { mutableIntStateOf(1250) }
    var showTopUpDialog by remember { mutableStateOf(false) }
    var showHistoryDialog by remember { mutableStateOf(false) }

    val transactions = remember {
        mutableStateListOf(
            TransactionItem(
                title = "Thanh toán cọc Nail Art - L'Amour",
                time = "15:30 · Hôm nay",
                amount = "-50.000 đ",
                isDeduction = true,
                status = "Đã cọc"
            ),
            TransactionItem(
                title = "Nạp tiền Ví GLOW Pay (VietQR)",
                time = "10:15 · 18/10/2026",
                amount = "+1.000.000 đ",
                isDeduction = false,
                status = "Thành công"
            ),
            TransactionItem(
                title = "Thanh toán cọc Spa An Yên",
                time = "14:00 · 15/10/2026",
                amount = "-50.000 đ",
                isDeduction = true,
                status = "Hoàn tất"
            ),
            TransactionItem(
                title = "Thưởng hoàn tiền cọc VIP",
                time = "09:00 · 12/10/2026",
                amount = "+20.000 đ",
                isDeduction = false,
                status = "Thành công"
            )
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF7E8)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. VIP Profile Hero Card (Inspired by Salon Hero Card Layout)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF360814)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Brush.linearGradient(listOf(GoldSoft.copy(alpha = 0.6f), Color.Transparent)), RoundedCornerShape(22.dp))
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Khánh Linh",
                                color = Color.White,
                                fontFamily = FontFamily.Serif,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "linh.luxury@gmail.com\n0903 123 456",
                                color = Color(0xFFE5B8C2),
                                fontSize = 11.5.sp,
                                lineHeight = 16.sp
                            )

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF23040C))
                                    .padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                GlowIcon(icon = GlowIcons.Check, tint = Color(0xFF4ADE80), size = 10.dp)
                                Text(
                                    text = "Đã xác thực tài khoản chính chủ",
                                    color = Color(0xFFE5B8C2),
                                    fontSize = 10.sp
                                )
                            }
                        }

                        // Arched VIP Accent Image
                        Box(
                            modifier = Modifier
                                .size(width = 90.dp, height = 115.dp)
                                .clip(RoundedCornerShape(topStart = 45.dp, topEnd = 45.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
                                .border(1.5.dp, Brush.verticalGradient(listOf(GoldSoft, ChampagneGold.copy(alpha = 0.3f))), RoundedCornerShape(topStart = 45.dp, topEnd = 45.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.salon_luxury_interior),
                                contentDescription = "VIP Profile Avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xFF360814).copy(alpha = 0.6f))))
                            )
                        }
                    }
                }
            }
        }

        // 2. Wallet Balance & Points Row (Matching Salon Stats Cards Layout)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Card 1: Số Dư Ví GLOW Pay
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showTopUpDialog = true },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFDF4E3)),
                                contentAlignment = Alignment.Center
                            ) {
                                GlowIcon(icon = GlowIcons.Wallet, tint = Color(0xFF882C42), size = 18.dp)
                            }
                            Column {
                                Text(text = "SỐ DƯ VÍ GLOW", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7A686D))
                                Text(
                                    text = "%,d đ".format(walletBalance),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF360814)
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF360814))
                                .padding(vertical = 5.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            GlowIcon(icon = GlowIcons.Plus, tint = Color.White, size = 11.dp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Nạp Ví GLOW", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }

                // Card 2: Xu Tích Lũy VIP
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFF7ED)),
                                contentAlignment = Alignment.Center
                            ) {
                                GlowIcon(icon = GlowIcons.Coins, tint = ChampagneGold, size = 18.dp)
                            }
                            Column {
                                Text(text = "XU TÍCH LŨY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7A686D))
                                Text(
                                    text = "%,d Xu".format(rewardPoints),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF360814)
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFFFF7ED))
                                .padding(vertical = 5.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Đổi quà & Voucher", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF882C42))
                        }
                    }
                }
            }
        }

        // 3. Quick Shortcuts Row (Lịch hẹn, Yêu thích, Voucher, Đánh giá)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    QuickShortcutItem(icon = GlowIcons.Calendar, label = "Lịch hẹn", badge = "1 Active")
                    QuickShortcutItem(icon = GlowIcons.Heart, label = "Yêu thích", badge = "3 Salon")
                    QuickShortcutItem(icon = GlowIcons.Tag, label = "Voucher", badge = "4 Ưu đãi")
                    QuickShortcutItem(icon = GlowIcons.StarFilled, label = "Đánh giá", badge = "12 Lượt")
                }
            }
        }

        // 4. Lịch Sử Giao Dịch & Thanh Toán Cọc Card
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF9EFEF)),
                                contentAlignment = Alignment.Center
                            ) {
                                GlowIcon(icon = GlowIcons.Clock, tint = Color(0xFF882C42), size = 15.dp)
                            }
                            Text(
                                text = "Lịch sử giao dịch & cọc",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF360814)
                            )
                        }

                        Text(
                            text = "Xem tất cả",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF882C42),
                            modifier = Modifier.clickable { showHistoryDialog = true }
                        )
                    }

                    HorizontalDivider(color = Color(0xFFF3E8EC))

                    transactions.take(3).forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(if (item.isDeduction) Color(0xFFFFF1F2) else Color(0xFFF0FDF4)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    GlowIcon(
                                        icon = if (item.isDeduction) GlowIcons.ArrowUpRight else GlowIcons.ArrowDownLeft,
                                        tint = if (item.isDeduction) Color(0xFFE11D48) else Color(0xFF16A34A),
                                        size = 16.dp
                                    )
                                }
                                Column {
                                    Text(
                                        text = item.title,
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF24090F)
                                    )
                                    Text(
                                        text = item.time,
                                        fontSize = 11.sp,
                                        color = Color(0xFF7A686D)
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = item.amount,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (item.isDeduction) Color(0xFFE11D48) else Color(0xFF16A34A)
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFFBF7E8))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = item.status,
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF882C42)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Account Settings & Utilities
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Cài đặt & Tài khoản",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF360814)
                    )

                    HorizontalDivider(color = Color(0xFFF3E8EC))

                    ProfileSettingRow(
                        icon = GlowIcons.Wallet,
                        title = "Quản lý Ví GLOW & Phương thức cọc",
                        subtitle = "Thẻ ngân hàng, VietQR, Ví điện tử",
                        onClick = { showTopUpDialog = true }
                    )

                    HorizontalDivider(color = Color(0xFFF3E8EC))

                    ProfileSettingRow(
                        icon = GlowIcons.Shield,
                        title = "Chính sách bảo mật & Điều khoản cọc",
                        subtitle = "Quyền lợi hoàn cọc trong 24h",
                        onClick = {}
                    )

                    HorizontalDivider(color = Color(0xFFF3E8EC))

                    ProfileSettingRow(
                        icon = GlowIcons.User,
                        title = "Thông tin cá nhân & Sổ địa chỉ",
                        subtitle = "Khánh Linh · 0903 123 456",
                        onClick = {}
                    )

                    HorizontalDivider(color = Color(0xFFF3E8EC))

                    // Logout Button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLogout() }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFF1F2)),
                            contentAlignment = Alignment.Center
                        ) {
                            GlowIcon(icon = GlowIcons.Settings, tint = Color(0xFFE11D48), size = 16.dp)
                        }
                        Text(
                            text = "Đăng xuất tài khoản",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE11D48)
                        )
                    }
                }
            }
        }
    }

    // Top up Wallet Dialog Modal
    if (showTopUpDialog) {
        AlertDialog(
            onDismissRequest = { showTopUpDialog = false },
            title = {
                Text(text = "Nạp tiền vào Ví GLOW Pay", fontWeight = FontWeight.Bold, color = Color(0xFF360814))
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Chọn số tiền muốn nạp để thanh toán cọc nhanh chóng:", fontSize = 13.sp, color = Color(0xFF7A686D))

                    listOf(100000L, 200000L, 500000L, 1000000L).forEach { amount ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFBF7E8))
                                .border(1.dp, Color(0xFFE5B8C2), RoundedCornerShape(12.dp))
                                .clickable {
                                    walletBalance += amount
                                    transactions.add(
                                        0,
                                        TransactionItem(
                                            title = "Nạp tiền Ví GLOW Pay (VietQR)",
                                            time = "Vừa xong",
                                            amount = "+%,d đ".format(amount),
                                            isDeduction = false,
                                            status = "Thành công"
                                        )
                                    )
                                    showTopUpDialog = false
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "+%,d đ".format(amount),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF360814),
                                fontSize = 14.sp
                            )
                            GlowIcon(icon = GlowIcons.ChevronRight, tint = Color(0xFF882C42), size = 14.dp)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showTopUpDialog = false }) {
                    Text(text = "Hủy", color = Color(0xFF7A686D))
                }
            }
        )
    }

    // Full Transaction History Modal
    if (showHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showHistoryDialog = false },
            title = {
                Text(text = "Lịch sử giao dịch Ví GLOW", fontWeight = FontWeight.Bold, color = Color(0xFF360814))
            },
            text = {
                LazyColumn(
                    modifier = Modifier.height(300.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(transactions) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFFBF7E8))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = item.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                Text(text = item.time, fontSize = 10.sp, color = Color(0xFF7A686D))
                            }
                            Text(
                                text = item.amount,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (item.isDeduction) Color(0xFFE11D48) else Color(0xFF16A34A)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showHistoryDialog = false }) {
                    Text(text = "Đóng", color = Color(0xFF360814), fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun QuickShortcutItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    badge: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(0xFFF9EFEF)),
            contentAlignment = Alignment.Center
        ) {
            GlowIcon(icon = icon, tint = Color(0xFF882C42), size = 20.dp)
        }
        Text(text = label, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
        Text(text = badge, fontSize = 9.5.sp, color = Color(0xFF7A686D))
    }
}

@Composable
private fun ProfileSettingRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFBF7E8)),
                contentAlignment = Alignment.Center
            ) {
                GlowIcon(icon = icon, tint = Color(0xFF882C42), size = 16.dp)
            }
            Column {
                Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF24090F))
                Text(text = subtitle, fontSize = 11.sp, color = Color(0xFF7A686D))
            }
        }
        GlowIcon(icon = GlowIcons.ChevronRight, tint = Color(0xFF882C42), size = 14.dp)
    }
}
