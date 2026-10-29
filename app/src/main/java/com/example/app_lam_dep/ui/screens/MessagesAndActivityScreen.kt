package com.example.app_lam_dep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_lam_dep.ui.icons.GlowIcon
import com.example.app_lam_dep.ui.icons.GlowIcons
import com.example.app_lam_dep.ui.theme.DustyMerlot
import com.example.app_lam_dep.ui.theme.PinkPanther
import com.example.app_lam_dep.ui.theme.TextMuted

data class ConversationItem(
    val id: Int,
    val name: String,
    val snippet: String,
    val time: String,
    val tag: String, // "Khách hàng", "Salon", "Hệ thống"
    val unreadCount: Int = 0,
    val hasUnreadDot: Boolean = false,
    val isStore: Boolean = false,
    val isSystem: Boolean = false,
    val isOnline: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesAndActivityScreen(
    onChatClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(1) } // 0: Hoạt động, 1: Tin nhắn (Default matching Image 2)
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Tất cả") }

    val filterOptions = listOf("Tất cả", "Chưa đọc")

    // Customer conversations - only with Beauty Salons/Spas
    val conversations = listOf(
        ConversationItem(
            id = 1,
            name = "L'Amour Luxury Spa & Nail",
            snippet = "Dạ bên em đã xác nhận lịch hẹn cho bạn vào 15h30 hôm nay ạ. Cảm ơn bạn!",
            time = "09:47",
            tag = "Salon",
            unreadCount = 2,
            hasUnreadDot = true,
            isStore = true,
            isOnline = true
        ),
        ConversationItem(
            id = 2,
            name = "An Yên Thảo Dược Retreat",
            snippet = "Dạ em gửi bạn bảng giá dịch vụ gội đầu dưỡng sinh thảo dược ạ ✨",
            time = "Hôm qua",
            tag = "Salon",
            isStore = true,
            isOnline = true
        ),
        ConversationItem(
            id = 3,
            name = "La Beauté Beauty & Clinic",
            snippet = "Cảm ơn bạn đã quan tâm liệu trình chăm sóc da chuyên sâu tại La Beauté!",
            time = "Hôm qua",
            tag = "Salon",
            unreadCount = 1,
            hasUnreadDot = true,
            isStore = true,
            isOnline = false
        ),
        ConversationItem(
            id = 4,
            name = "The Glamour Hair Salon",
            snippet = "Dạ salon có thể phục vụ làm tóc vào khung giờ 16:30 chiều nay ạ.",
            time = "Thứ 6",
            tag = "Salon",
            isStore = true,
            isOnline = false
        )
    )

    val filteredConversations = conversations.filter { item ->
        val matchesSearch = searchQuery.isEmpty() ||
                item.name.contains(searchQuery, ignoreCase = true) ||
                item.snippet.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (selectedFilter) {
            "Chưa đọc" -> item.hasUnreadDot || item.unreadCount > 0
            else -> true
        }
        matchesSearch && matchesFilter
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF7E8))
    ) {
        // 1. Top Tabs Bar: Hoạt động | Tin nhắn + Compose Icon (Image 2)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
                // Tab "Hoạt động"
                Column(
                    modifier = Modifier.clickable { selectedTab = 0 },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Hoạt động",
                        fontSize = 17.sp,
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                        color = if (selectedTab == 0) Color(0xFF6B1228) else Color(0xFF7A686D)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (selectedTab == 0) Color(0xFF6B1228) else Color.Transparent)
                    )
                }

                // Tab "Tin nhắn" (Active)
                Column(
                    modifier = Modifier.clickable { selectedTab = 1 },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tin nhắn",
                        fontSize = 17.sp,
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                        color = if (selectedTab == 1) Color(0xFF6B1228) else Color(0xFF7A686D)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (selectedTab == 1) Color(0xFF6B1228) else Color.Transparent)
                    )
                }
            }

            // Compose new message button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                GlowIcon(
                    icon = GlowIcons.EditPencil,
                    tint = Color(0xFF6B1228),
                    size = 20.dp,
                    contentDescription = "Soạn tin nhắn"
                )
            }
        }

        if (selectedTab == 0) {
            // Activity Screen
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Cập nhật hoạt động & Nhắc hẹn",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF24090F)
                    )
                }
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF9EFEF)),
                                contentAlignment = Alignment.Center
                            ) {
                                GlowIcon(icon = GlowIcons.Calendar, tint = Color(0xFF6B1228), size = 20.dp)
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(text = "Nhắc lịch hẹn sắp tới", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF24090F))
                                Text(text = "L'Amour Luxury: Sơn gel Pháp vào lúc 15:30 chiều nay.", fontSize = 12.sp, color = Color(0xFF7A686D))
                                Text(text = "15 phút trước", fontSize = 10.5.sp, color = Color(0xFF9E8A90))
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
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF9EFEF)),
                                contentAlignment = Alignment.Center
                            ) {
                                GlowIcon(icon = GlowIcons.Coins, tint = Color(0xFF6B1228), size = 20.dp)
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(text = "Tạm giữ cọc thành công", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF24090F))
                                Text(text = "Khoản cọc 50.000đ đã được tạm giữ an toàn qua sàn.", fontSize = 12.sp, color = Color(0xFF7A686D))
                                Text(text = "2 giờ trước", fontSize = 10.5.sp, color = Color(0xFF9E8A90))
                            }
                        }
                    }
                }
            }
        } else {
            // 2. Search Box (Image 2)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xFFF5EFEF))
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GlowIcon(icon = GlowIcons.Search, tint = Color(0xFF8E7A80), size = 18.dp)
                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Tìm kiếm tin nhắn...",
                                color = Color(0xFF8E7A80),
                                fontSize = 13.sp
                            )
                        }
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // 3. Filter Pills Row (Image 2: Tất cả, Khách hàng, Salon, Hệ thống)
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterOptions) { filter ->
                    val isSelected = filter == selectedFilter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                if (isSelected) Color(0xFF62202F) else Color.White
                            )
                            .border(
                                width = if (isSelected) 0.dp else 1.dp,
                                color = if (isSelected) Color.Transparent else Color(0xFFEBDCD3),
                                shape = RoundedCornerShape(18.dp)
                            )
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 16.dp, vertical = 7.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = filter,
                            fontSize = 12.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF7A686D)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4. Conversation List (Image 2 exact items)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredConversations) { conv ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onChatClick(conv.name) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Red unread indicator dot (Image 2)
                            Box(
                                modifier = Modifier
                                    .width(10.dp)
                                    .padding(end = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (conv.hasUnreadDot) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFE11D48))
                                    )
                                }
                            }

                            // Avatar Medallion with optional Online green badge
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF9EFEF)),
                                contentAlignment = Alignment.Center
                            ) {
                                val icon = when {
                                    conv.isStore -> GlowIcons.Store
                                    conv.isSystem -> GlowIcons.Bell
                                    else -> GlowIcons.User
                                }
                                GlowIcon(
                                    icon = icon,
                                    tint = Color(0xFF6B1228),
                                    size = 22.dp,
                                    contentDescription = conv.name
                                )

                                // Green online badge
                                if (conv.isOnline) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .size(11.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF10B981))
                                            .border(1.5.dp, Color.White, CircleShape)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Message info & snippet
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text(
                                    text = conv.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF24090F)
                                )
                                Text(
                                    text = conv.snippet,
                                    fontSize = 12.sp,
                                    color = Color(0xFF7A686D),
                                    maxLines = 1
                                )

                                // Tag pill (Khách hàng / Salon / Hệ thống)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFFCEEEB))
                                        .padding(horizontal = 7.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = conv.tag,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF6B1228)
                                    )
                                }
                            }

                            // Time & Unread Badge
                            Column(
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = conv.time,
                                    fontSize = 11.sp,
                                    color = Color(0xFF9E8A90)
                                )

                                if (conv.unreadCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF62202F)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = conv.unreadCount.toString(),
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                } else {
                                    Spacer(modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }
            }
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
            "Dạ chào bạn, lịch hẹn chiều nay bên em đã chuẩn bị phòng dịch vụ sẵn sàng đón tiếp bạn rồi ạ! ✨",
            "Cảm ơn shop nhiều nhé, mình sẽ ghé đúng giờ ạ."
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBF7E8))
            .imePadding()
    ) {
        // Chat Header matching Haute Couture aesthetic
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF360814))
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                GlowIcon(icon = GlowIcons.ChevronLeft, tint = Color.White, size = 20.dp, contentDescription = "Quay lại")
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFCEEEB)),
                contentAlignment = Alignment.Center
            ) {
                GlowIcon(icon = GlowIcons.Crown, tint = Color(0xFF6B1228), size = 20.dp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = chatName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                Text(text = "Đang trực tuyến · Trả lời tức thì", fontSize = 11.sp, color = Color(0xFFF3C2CA))
            }

            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                GlowIcon(icon = GlowIcons.Phone, tint = Color.White, size = 20.dp, contentDescription = "Gọi thoại", modifier = Modifier.clickable { })
                GlowIcon(icon = GlowIcons.Video, tint = Color.White, size = 20.dp, contentDescription = "Gọi video", modifier = Modifier.clickable { })
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
                            .background(if (isMyMessage) Color(0xFF62202F) else Color.White)
                            .border(1.dp, if (isMyMessage) Color.Transparent else Color(0xFFEBDCD3), RoundedCornerShape(18.dp))
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

        // Bottom Message Input Bar
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
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Nhập tin nhắn...", color = Color(0xFF8E7A80), fontSize = 13.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF62202F),
                        unfocusedBorderColor = Color(0xFFEBDCD3),
                        focusedContainerColor = Color(0xFFFAF6F0),
                        unfocusedContainerColor = Color(0xFFFAF6F0)
                    ),
                    singleLine = true
                )

                if (messageText.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF62202F))
                            .clickable {
                                messages.add(messageText)
                                messageText = ""
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        GlowIcon(icon = GlowIcons.Check, tint = Color.White, size = 18.dp)
                    }
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        GlowIcon(icon = GlowIcons.Mic, tint = Color(0xFF7A686D), size = 20.dp, contentDescription = "Ghi âm", modifier = Modifier.clickable {})
                        GlowIcon(icon = GlowIcons.Smile, tint = Color(0xFF7A686D), size = 20.dp, contentDescription = "Biểu cảm", modifier = Modifier.clickable {})
                        GlowIcon(icon = GlowIcons.Image, tint = Color(0xFF7A686D), size = 20.dp, contentDescription = "Gửi ảnh", modifier = Modifier.clickable {})
                    }
                }
            }
        }
    }
}

