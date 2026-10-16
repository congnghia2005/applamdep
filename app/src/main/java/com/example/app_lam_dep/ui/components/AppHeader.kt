package com.example.app_lam_dep.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_lam_dep.ui.icons.GlowIcon
import com.example.app_lam_dep.ui.icons.GlowIcons
import com.example.app_lam_dep.ui.theme.ChampagneGold
import com.example.app_lam_dep.ui.theme.DustyMerlot
import com.example.app_lam_dep.ui.theme.GoldSoft
import com.example.app_lam_dep.ui.theme.PinkPanther

@Composable
fun AppHeader(
    currentRole: Int, // 0: Customer, 1: Partner, 2: Admin
    onRoleChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(DustyMerlot)
            .statusBarsPadding() // Proper padding below status bar
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Logo & Title (Matching web prototype brand-crest)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(listOf(Color.White.copy(alpha = 0.16f), Color.White.copy(alpha = 0.04f))))
                    .border(1.dp, Brush.linearGradient(listOf(GoldSoft, ChampagneGold.copy(alpha = 0.4f))), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                GlowIcon(icon = GlowIcons.Diamond, tint = GoldSoft, size = 20.dp, contentDescription = "Glow Prestige")
            }
            Column {
                Text(
                    text = "GLOW PRESTIGE",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = when (currentRole) {
                        1 -> "Khu vực Đối tác Salon"
                        2 -> "Quản trị viên (Admin)"
                        else -> "Haute Couture Beauty"
                    },
                    color = PinkPanther,
                    fontSize = 11.sp
                )
            }
        }

        // 3-Role Switcher Pills (Matching web prototype role-selector-pills)
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF1E060C).copy(alpha = 0.55f))
                .padding(3.dp)
        ) {
            RoleTabButton(
                text = "Khách Hàng",
                isSelected = currentRole == 0,
                onClick = { onRoleChange(0) }
            )
            RoleTabButton(
                text = "Salon",
                isSelected = currentRole == 1,
                onClick = { onRoleChange(1) }
            )
            RoleTabButton(
                text = "Admin",
                isSelected = currentRole == 2,
                onClick = { onRoleChange(2) }
            )
        }
    }
}

@Composable
private fun RoleTabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFFFBF7E8) else Color.Transparent
    val textColor = if (isSelected) DustyMerlot else Color(0xFFD1B8BE)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
