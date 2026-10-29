package com.example.app_lam_dep.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_lam_dep.model.AiShopRecommendation
import com.example.app_lam_dep.ui.icons.GlowIcon
import com.example.app_lam_dep.ui.icons.GlowIcons
import com.example.app_lam_dep.ui.icons.IconMedallion
import com.example.app_lam_dep.ui.theme.ChampagneGold
import com.example.app_lam_dep.ui.theme.DustyMerlot
import com.example.app_lam_dep.ui.theme.GoldSoft
import com.example.app_lam_dep.ui.theme.PinkPanther
import com.example.app_lam_dep.ui.theme.SuccessGreen
import com.example.app_lam_dep.ui.theme.TextMuted
import kotlin.math.roundToInt

data class ChatMessage(val isAi: Boolean, val text: String, val recommendations: List<AiShopRecommendation> = emptyList())

@Composable
fun DraggableFloatingAiButton(
    onNavigateToSalon: (String) -> Unit,
    onSelectServiceForBooking: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAiSheet by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
    val buttonSizePx = with(density) { 60.dp.toPx() }

    // Start near right edge, vertically center-bottom
    var offsetX by remember { mutableStateOf(screenWidthPx - buttonSizePx - 20f) }
    var offsetY by remember { mutableStateOf(screenHeightPx * 0.65f) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Floating Draggable Circular Button
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .size(60.dp)
                .shadow(12.dp, CircleShape, spotColor = DustyMerlot)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF2E0913), DustyMerlot, Color(0xFF882C42), ChampagneGold)
                    )
                )
                .border(2.dp, Brush.sweepGradient(listOf(ChampagneGold, GoldSoft, PinkPanther, ChampagneGold)), CircleShape)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX = (offsetX + dragAmount.x).coerceIn(16f, screenWidthPx - buttonSizePx - 16f)
                        offsetY = (offsetY + dragAmount.y).coerceIn(100f, screenHeightPx - buttonSizePx - 100f)
                    }
                }
                .clickable { showAiSheet = true },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                GlowIcon(icon = GlowIcons.Sparkle, tint = GoldSoft, size = 24.dp)
                Text(
                    text = "AI BOT",
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
            }

            // Small Live indicator dot
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(9.dp)
                    .clip(CircleShape)
                    .background(SuccessGreen)
                    .border(1.5.dp, Color.White, CircleShape)
            )
        }
    }

    if (showAiSheet) {
        AiShopAdvisorSheet(
            onDismiss = { showAiSheet = false },
            onNavigateToSalon = { sName ->
                showAiSheet = false
                onNavigateToSalon(sName)
            },
            onSelectServiceForBooking = { sName, sShop ->
                showAiSheet = false
                onSelectServiceForBooking(sName, sShop)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiShopAdvisorSheet(
    onDismiss: () -> Unit,
    onNavigateToSalon: (String) -> Unit,
    onSelectServiceForBooking: (String, String) -> Unit
) {
    var queryText by remember { mutableStateOf("") }

    val sampleRecommendations = listOf(
        AiShopRecommendation(
            id = "1",
            salonName = "L'Amour Haute Nail & Lash Lounge",
            serviceSuggestion = "Nail Art Mắt Mèo Cực Quang (Hot)",
            rating = 4.99,
            distance = "0.8 km • Q.1",
            requiresDeposit = true,
            depositInfo = "Có cọc (Shop set 5 mức từ 50k)",
            matchReason = "Điểm đánh giá cao nhất, chuyên viên tay nghề Master Linh"
        ),
        AiShopRecommendation(
            id = "2",
            salonName = "An Yên Thảo Dược & Luxury Retreat",
            serviceSuggestion = "Gội Đầu Dưỡng Sinh Cung Đình 75p",
            rating = 4.97,
            distance = "1.5 km • Q.3",
            requiresDeposit = false,
            depositInfo = "Không cần cọc (Giữ chỗ 0đ)",
            matchReason = "Phù hợp thư giãn cuối tuần, tiệm hỗ trợ đặt giữ chỗ không cần cọc"
        ),
        AiShopRecommendation(
            id = "3",
            salonName = "The Royal Atelier Nail & Spa",
            serviceSuggestion = "Form Móng Hoàng Gia Đính Đá",
            rating = 4.95,
            distance = "1.2 km • Q.1",
            requiresDeposit = true,
            depositInfo = "Có cọc (Mức linh hoạt)",
            matchReason = "Gói VIP Sponsored được khách hàng đánh giá 5 sao tuần qua"
        )
    )

    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage(
                    isAi = true,
                    text = "Xin chào bạn! Em là Trợ lý AI GlowPrestige ✨. Em có thể giúp bạn tìm salon theo sở thích, khoảng cách, hoặc lọc theo tiệm KHÔNG CẦN CỌC hay CÓ CỌC theo mệnh giá phù hợp.",
                    recommendations = sampleRecommendations.take(2)
                )
            )
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFFBF7E8)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DustyMerlot)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(ChampagneGold, GoldSoft))),
                    contentAlignment = Alignment.Center
                ) {
                    GlowIcon(icon = GlowIcons.Sparkle, tint = DustyMerlot, size = 20.dp)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Trợ Lý AI Đề Xuất Shop & Dịch Vụ",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Gợi ý thông minh theo nhu cầu, khoảng cách & mức cọc",
                        fontSize = 11.sp,
                        color = PinkPanther
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SuccessGreen.copy(alpha = 0.2f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = "AI Online", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                }
            }

            // Quick Prompt Chips
            val promptChips = listOf(
                "💅 Tìm tiệm Nail gần nhất",
                "🌿 Spa không cần cọc",
                "👑 Salon VIP đánh giá cao",
                "⚡ Tiệm trống lịch chiều nay"
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(promptChips) { chip ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .border(1.dp, ChampagneGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .clickable {
                                val filterNoDeposit = chip.contains("không cần cọc", ignoreCase = true)
                                val responseRecs = if (filterNoDeposit) {
                                    sampleRecommendations.filter { !it.requiresDeposit }
                                } else {
                                    sampleRecommendations
                                }
                                val newMsgList = messages.toMutableList()
                                newMsgList.add(ChatMessage(isAi = false, text = chip))
                                newMsgList.add(
                                    ChatMessage(
                                        isAi = true,
                                        text = if (filterNoDeposit) 
                                            "Dạ đây là các tiệm được đề xuất KHÔNG CẦN CỌC TRƯỚC (Giữ chỗ 0đ), bạn chỉ cần đến và tận hưởng:" 
                                            else "Dạ em đã lọc các salon ưu tú nhất theo yêu cầu của bạn:",
                                        recommendations = responseRecs
                                    )
                                )
                                messages = newMsgList
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = chip, fontSize = 11.5.sp, fontWeight = FontWeight.Medium, color = DustyMerlot)
                    }
                }
            }

            // Message Stream
            LazyColumn(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { msg ->
                    if (msg.isAi) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(DustyMerlot),
                                    contentAlignment = Alignment.Center
                                ) {
                                    GlowIcon(icon = GlowIcons.Sparkle, tint = ChampagneGold, size = 15.dp)
                                }
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = msg.text,
                                        fontSize = 12.5.sp,
                                        color = Color(0xFF24090F),
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }

                            // If message has recommendations, render cards
                            msg.recommendations.forEach { rec ->
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(2.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 36.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = rec.salonName,
                                                fontSize = 13.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = DustyMerlot,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                                            ) {
                                                GlowIcon(icon = GlowIcons.StarFilled, tint = ChampagneGold, size = 12.dp)
                                                Text(text = "${rec.rating}", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                                            }
                                        }

                                        Text(text = "✨ Gợi ý: ${rec.serviceSuggestion}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF3A101A))
                                        Text(text = "📍 ${rec.distance} • ${rec.matchReason}", fontSize = 11.sp, color = TextMuted)

                                        // Deposit badge
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(if (rec.requiresDeposit) PinkPanther.copy(alpha = 0.5f) else SuccessGreen.copy(alpha = 0.15f))
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = if (rec.requiresDeposit) "📌 ${rec.depositInfo}" else "🎉 Không cần cọc • Giữ chỗ miễn phí",
                                                fontSize = 10.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (rec.requiresDeposit) DustyMerlot else SuccessGreen
                                            )
                                        }

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            OutlinedButton(
                                                onClick = { onNavigateToSalon(rec.salonName) },
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.weight(1f).height(32.dp),
                                                contentPadding = PaddingValues(0.dp)
                                            ) {
                                                Text("Xem Shop", fontSize = 11.sp, color = DustyMerlot, fontWeight = FontWeight.Bold)
                                            }
                                            Button(
                                                onClick = { onSelectServiceForBooking(rec.serviceSuggestion, rec.salonName) },
                                                colors = ButtonDefaults.buttonColors(containerColor = DustyMerlot),
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.weight(1f).height(32.dp),
                                                contentPadding = PaddingValues(0.dp)
                                            ) {
                                                Text("Đặt lịch ngay", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // User message
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(DustyMerlot)
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(text = msg.text, color = Color.White, fontSize = 12.5.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Chat Input Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = queryText,
                    onValueChange = { queryText = it },
                    placeholder = { Text("Hỏi AI (Ví dụ: tiệm nail gần đây, spa không cọc...)", fontSize = 11.5.sp, color = TextMuted) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = DustyMerlot,
                        unfocusedBorderColor = Color(0xFFEBDCD3)
                    ),
                    singleLine = true
                )

                Button(
                    onClick = {
                        if (queryText.isNotBlank()) {
                            val userText = queryText
                            queryText = ""
                            val isNoDeposit = userText.contains("không cọc", ignoreCase = true) || userText.contains("miễn cọc", ignoreCase = true)
                            val matched = if (isNoDeposit) sampleRecommendations.filter { !it.requiresDeposit } else sampleRecommendations
                            val newMsgList = messages.toMutableList()
                            newMsgList.add(ChatMessage(isAi = false, text = userText))
                            newMsgList.add(
                                ChatMessage(
                                    isAi = true,
                                    text = "Em đã phân tích yêu cầu \"$userText\" và chọn lọc ra các salon làm đẹp uy tín nhất:",
                                    recommendations = matched
                                )
                            )
                            messages = newMsgList
                        }
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = DustyMerlot),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(46.dp)
                ) {
                    GlowIcon(icon = GlowIcons.Sparkle, tint = ChampagneGold, size = 20.dp)
                }
            }
        }
    }
}
