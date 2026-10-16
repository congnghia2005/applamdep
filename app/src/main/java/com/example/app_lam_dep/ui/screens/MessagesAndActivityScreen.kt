package com.example.app_lam_dep.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_lam_dep.ui.icons.GlowIcon
import com.example.app_lam_dep.ui.icons.GlowIcons
import com.example.app_lam_dep.ui.icons.IconMedallion
import com.example.app_lam_dep.ui.theme.ChampagneGold
import com.example.app_lam_dep.ui.theme.DustyMerlot
import com.example.app_lam_dep.ui.theme.GoldSoft
import com.example.app_lam_dep.ui.theme.PinkPanther
import com.example.app_lam_dep.ui.theme.TextMuted

@Composable
fun MessagesAndActivityScreen(
    onChatClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedMainTab by remember { mutableStateOf(1) } // Default to Chat tab (1)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF7E8))
    ) {
        // Top Toggle Bar (Activities | Chat matching screenshot)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Activities",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (selectedMainTab == 0) Color.Black else TextMuted,
                modifier = Modifier.clickable { selectedMainTab = 0 }
            )
            Text(
                text = "Chat",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (selectedMainTab == 1) Color.Black else TextMuted,
                modifier = Modifier.clickable { selectedMainTab = 1 }
            )
        }

        if (selectedMainTab == 0) {
            // Activities / Thông báo screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Activity", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))

                val categories = listOf("All", "Category", "Category", "Category")
                var selectedCat by remember { mutableStateOf("All") }
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { cat ->
                        val isSel = cat == selectedCat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSel) Color(0xFF1E060C) else Color.White)
                                .clickable { selectedCat = cat }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(text = cat, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color(0xFF24090F))
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        ActivityRowItem(
                            name = "starryskies23",
                            action = "Started following you",
                            time = "1d",
                            showUnreadDot = true,
                            hasButton = true
                        )
                    }
                    item {
                        ActivityRowItem(
                            name = "nebulanomad",
                            action = "Liked your post",
                            time = "1d",
                            showUnreadDot = true,
                            hasThumb = true
                        )
                    }
                }
            }
        } else {
            // Chat List screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Messages", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        ChatRowItem(
                            name = "L'Amour Luxury Spa & Nail",
                            lastMessage = "Dạ quý cô ơi, lịch hẹn chiều nay đã sẵn sàng...",
                            time = "10:45",
                            onClick = { onChatClick("L'Amour Luxury Spa & Nail") }
                        )
                    }
                    item {
                        ChatRowItem(
                            name = "An Yên Thảo Dược Retreat",
                            lastMessage = "Shop đã chuẩn bị phòng VIP đá nóng ạ.",
                            time = "Hôm qua",
                            onClick = { onChatClick("An Yên Thảo Dược Retreat") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivityRowItem(
    name: String,
    action: String,
    time: String,
    showUnreadDot: Boolean = false,
    hasButton: Boolean = false,
    hasThumb: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (showUnreadDot) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
            )
        } else {
            Spacer(modifier = Modifier.width(6.dp))
        }

        IconMedallion(icon = GlowIcons.Crown, size = 44.dp, iconSize = 20.dp)

        Column(modifier = Modifier.weight(1f)) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF24090F))
                Text(text = time, fontSize = 12.sp, color = TextMuted)
            }
            Spacer(modifier = Modifier.height(1.dp))
            Text(text = action, fontSize = 12.sp, color = TextMuted, maxLines = 2)
        }

        if (hasButton) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E060C)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.height(30.dp)
            ) {
                Text(text = "Button", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        if (hasThumb) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(DustyMerlot),
                contentAlignment = Alignment.Center
            ) {
                GlowIcon(icon = GlowIcons.Sparkle, tint = GoldSoft, size = 18.dp)
            }
        }
    }
}

@Composable
private fun ChatRowItem(
    name: String,
    lastMessage: String,
    time: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconMedallion(icon = GlowIcons.NailPolish, size = 48.dp, iconSize = 22.dp)

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF24090F))
                Text(text = time, fontSize = 11.sp, color = TextMuted)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = lastMessage, fontSize = 12.5.sp, color = TextMuted, maxLines = 1)
        }
    }
}

@Composable
fun ChatDetailScreen(
    chatName: String,
    onBack: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            "Dạ quý cô ơi, lịch hẹn 15:30 chiều nay bên em đã chuẩn bị phòng VIP rồi ạ! ✨",
            "Cảm ơn shop nhé, mình sẽ đến đúng giờ."
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBF7E8))
            .imePadding()
    ) {
        // Chat Header matching second screenshot
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GlowIcon(
                icon = GlowIcons.ChevronLeft,
                tint = DustyMerlot,
                size = 26.dp,
                contentDescription = "Quay lại",
                modifier = Modifier.clickable { onBack() }
            )
            IconMedallion(icon = GlowIcons.Crown, size = 40.dp, iconSize = 18.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(text = chatName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF24090F))
                Text(text = "Active 11m ago", fontSize = 11.sp, color = TextMuted)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                GlowIcon(icon = GlowIcons.Phone, tint = DustyMerlot, size = 21.dp, contentDescription = "Gọi thoại", modifier = Modifier.clickable { })
                GlowIcon(icon = GlowIcons.Video, tint = DustyMerlot, size = 22.dp, contentDescription = "Gọi video", modifier = Modifier.clickable { })
            }
        }

        // Messages list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages.size) { idx ->
                val msg = messages[idx]
                val isMyMessage = idx % 2 != 0
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isMyMessage) Arrangement.End else Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (isMyMessage) DustyMerlot else Color.White)
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = msg,
                            fontSize = 13.5.sp,
                            color = if (isMyMessage) Color.White else Color(0xFF24090F)
                        )
                    }
                }
            }
        }

        // Bottom Message Input Bar matching second screenshot (Message..., mic, emoji, gallery icons)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Message...", color = TextMuted) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DustyMerlot,
                        unfocusedBorderColor = Color(0xFFEBDCD3),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    GlowIcon(icon = GlowIcons.Mic, tint = TextMuted, size = 21.dp, contentDescription = "Ghi âm", modifier = Modifier.clickable {})
                    GlowIcon(icon = GlowIcons.Smile, tint = TextMuted, size = 21.dp, contentDescription = "Biểu cảm", modifier = Modifier.clickable {})
                    GlowIcon(icon = GlowIcons.Image, tint = TextMuted, size = 21.dp, contentDescription = "Gửi ảnh", modifier = Modifier.clickable {})
                }
            }
        }
    }
}
