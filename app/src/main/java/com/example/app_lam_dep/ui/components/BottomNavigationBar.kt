package com.example.app_lam_dep.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_lam_dep.ui.icons.GlowIcon
import com.example.app_lam_dep.ui.icons.GlowIcons
import com.example.app_lam_dep.ui.theme.DustyMerlot
import com.example.app_lam_dep.ui.theme.PinkPanther
import com.example.app_lam_dep.ui.theme.TextMuted

private data class NavTab(val label: String, val icon: ImageVector)

@Composable
fun BottomNavigationBar(
    isPartnerMode: Boolean,
    currentTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = if (!isPartnerMode) {
        // Customer Tabs: Khám phá, Lịch, Tin nhắn, Tôi
        listOf(
            NavTab("Khám phá", GlowIcons.Sparkle),
            NavTab("Lịch", GlowIcons.Calendar),
            NavTab("Tin nhắn", GlowIcons.Chat),
            NavTab("Tôi", GlowIcons.User)
        )
    } else {
        // Partner Tabs: Trang chủ, Lịch, Tin nhắn, Tôi
        listOf(
            NavTab("Trang chủ", GlowIcons.Home),
            NavTab("Lịch", GlowIcons.Calendar),
            NavTab("Tin nhắn", GlowIcons.Chat),
            NavTab("Tôi", GlowIcons.Settings)
        )
    }

    NavigationBar(
        modifier = modifier,
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        tabs.forEachIndexed { index, tab ->
            val selected = currentTab == index
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(index) },
                // Tint do NavigationBarItemDefaults.colors() quyết định (đỏ rượu khi chọn, xám ấm khi không)
                icon = { GlowIcon(icon = tab.icon, tint = LocalContentColor.current, size = 22.dp, contentDescription = tab.label) },
                label = {
                    Text(
                        text = tab.label,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = navigationColors()
            )
        }
    }
}

@Composable
private fun navigationColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = DustyMerlot,
    selectedTextColor = DustyMerlot,
    unselectedIconColor = TextMuted.copy(alpha = 0.75f),
    unselectedTextColor = TextMuted.copy(alpha = 0.75f),
    indicatorColor = PinkPanther.copy(alpha = 0.45f)
)
