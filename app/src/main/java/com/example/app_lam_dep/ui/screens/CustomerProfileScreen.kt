package com.example.app_lam_dep.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_lam_dep.ui.icons.GlowIcons
import com.example.app_lam_dep.ui.icons.IconMedallion
import com.example.app_lam_dep.ui.theme.ChampagneGold
import com.example.app_lam_dep.ui.theme.DustyMerlot
import com.example.app_lam_dep.ui.theme.PinkPanther
import com.example.app_lam_dep.ui.theme.TextMuted

@Composable
fun CustomerProfileScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF7E8)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconMedallion(
                    icon = GlowIcons.Crown,
                    size = 64.dp,
                    iconSize = 32.dp,
                    tint = ChampagneGold,
                    contentDescription = "Avatar VIP"
                )
                Column {
                    Text(
                        text = "Quý cô Khánh Linh",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DustyMerlot
                    )
                    Text(
                        text = "linh.luxury@gmail.com • 0903 123 456",
                        fontSize = 13.sp,
                        color = TextMuted
                    )
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
                    Text(text = "Cài đặt tài khoản", fontWeight = FontWeight.Bold, color = DustyMerlot)
                    HorizontalDivider()
                    Text(text = "Quản lý phương thức thanh toán cọc", fontSize = 14.sp, color = TextMuted)
                    HorizontalDivider()
                    Text(text = "Chính sách bảo mật & Điều khoản", fontSize = 14.sp, color = TextMuted)
                    HorizontalDivider()
                    Text(text = "Đăng xuất tài khoản", fontSize = 14.sp, color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
