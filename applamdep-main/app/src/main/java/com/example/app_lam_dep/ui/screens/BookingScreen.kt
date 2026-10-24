package com.example.app_lam_dep.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.app_lam_dep.model.BookingTicket
import com.example.app_lam_dep.ui.icons.GlowBackButton
import com.example.app_lam_dep.ui.icons.GlowIcon
import com.example.app_lam_dep.ui.icons.GlowIcons
import com.example.app_lam_dep.ui.theme.ChampagneGold
import com.example.app_lam_dep.ui.theme.DustyMerlot
import com.example.app_lam_dep.ui.theme.GoldSoft
import com.example.app_lam_dep.ui.theme.PinkPanther
import com.example.app_lam_dep.ui.theme.SuccessGreen
import com.example.app_lam_dep.ui.theme.TextMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    serviceName: String,
    salonName: String,
    servicePrice: String = "180.000 đ",
    requiresDeposit: Boolean = true, // Tùy shop có cần cọc hay không
    depositTiers: List<Long> = listOf(50000L, 100000L, 200000L, 300000L, 500000L), // 5 mệnh giá
    onBack: () -> Unit,
    onConfirmBooking: (BookingTicket) -> Unit,
    modifier: Modifier = Modifier
) {
    // Form Khách hàng điền thông tin
    var customerName by remember { mutableStateOf("Quý cô Khánh Linh") }
    var customerPhone by remember { mutableStateOf("0903 123 456") }
    var customerNote by remember { mutableStateOf("") }

    val dates = listOf("Hôm nay (19/10)", "Ngày mai (20/10)", "Thứ Tư (21/10)", "Thứ Năm (22/10)")
    var selectedDate by remember { mutableStateOf(dates[0]) }

    val timeSlots = listOf("09:30", "11:00", "13:45", "15:30", "16:45", "18:15")
    var selectedSlot by remember { mutableStateOf("15:30") }

    val specialists = listOf("Master Linh (VIP)", "Chuyên viên Sarah", "Chuyên viên Minh Thư")
    var selectedSpecialist by remember { mutableStateOf(specialists[0]) }

    // Chọn số tiền cọc (5 mệnh giá)
    var selectedDepositAmount by remember { mutableStateOf(depositTiers.firstOrNull() ?: 50000L) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF7E8))
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DustyMerlot)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GlowBackButton(onClick = onBack)
            Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(text = "Xác Nhận Đặt Lịch Hẹn", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    GlowIcon(icon = GlowIcons.Calendar, tint = PinkPanther, size = 15.dp)
                }
                Text(text = "$salonName • $serviceName", fontSize = 11.sp, color = PinkPanther)
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ──────────────── 1. FORM KHÁCH ĐIỀN THÔNG TIN ────────────────
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFEBDCD3)),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            GlowIcon(icon = GlowIcons.User, tint = DustyMerlot, size = 18.dp)
                            Text(text = "Thông Tin Quý Khách Đặt Hẹn", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                        }

                        // Họ và tên
                        OutlinedTextField(
                            value = customerName,
                            onValueChange = { customerName = it },
                            label = { Text("Họ và tên quý khách", fontSize = 12.sp) },
                            placeholder = { Text("Nhập họ tên...") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            leadingIcon = {
                                GlowIcon(icon = GlowIcons.User, tint = DustyMerlot.copy(alpha = 0.7f), size = 16.dp)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DustyMerlot,
                                unfocusedBorderColor = Color(0xFFEBDCD3),
                                focusedLabelColor = DustyMerlot
                            )
                        )

                        // Số điện thoại
                        OutlinedTextField(
                            value = customerPhone,
                            onValueChange = { customerPhone = it },
                            label = { Text("Số điện thoại nhận vé & mã QR", fontSize = 12.sp) },
                            placeholder = { Text("Nhập số điện thoại...") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            leadingIcon = {
                                GlowIcon(icon = GlowIcons.Phone, tint = DustyMerlot.copy(alpha = 0.7f), size = 16.dp)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DustyMerlot,
                                unfocusedBorderColor = Color(0xFFEBDCD3),
                                focusedLabelColor = DustyMerlot
                            )
                        )

                        // Ghi chú cho tiệm
                        OutlinedTextField(
                            value = customerNote,
                            onValueChange = { customerNote = it },
                            label = { Text("Ghi chú cho salon (Tùy chọn)", fontSize = 12.sp) },
                            placeholder = { Text("Ví dụ: Móng ngắn, da nhạy cảm, muốn thử mẫu ombre...") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            maxLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DustyMerlot,
                                unfocusedBorderColor = Color(0xFFEBDCD3),
                                focusedLabelColor = DustyMerlot
                            )
                        )
                    }
                }
            }

            // ──────────────── 2. CHỌN NGÀY & GIỜ HẸN ────────────────
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Chọn Ngày Hẹn", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(dates) { date ->
                            val isSel = date == selectedDate
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSel) DustyMerlot else Color.White)
                                    .border(1.dp, if (isSel) ChampagneGold else Color(0xFFEBDCD3), RoundedCornerShape(12.dp))
                                    .clickable { selectedDate = date }
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = date,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) Color.White else Color(0xFF24090F)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Chọn Khung Giờ Trống", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                    
                    val rows = timeSlots.chunked(3)
                    rows.forEach { rowSlots ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowSlots.forEach { slot ->
                                val isSel = slot == selectedSlot
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) DustyMerlot else Color.White)
                                        .border(1.dp, if (isSel) ChampagneGold else Color(0xFFEBDCD3), RoundedCornerShape(10.dp))
                                        .clickable { selectedSlot = slot }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = slot,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSel) Color.White else Color(0xFF24090F)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ──────────────── 3. CHỌN CHUYÊN VIÊN ────────────────
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Chuyên Viên Phục Vụ (Tùy chọn)", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                    specialists.forEach { spec ->
                        val isSel = spec == selectedSpecialist
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedSpecialist = spec },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = if (isSel) PinkPanther.copy(alpha = 0.35f) else Color.White),
                            border = BorderStroke(1.dp, if (isSel) DustyMerlot else Color(0xFFEBDCD3))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    GlowIcon(icon = GlowIcons.User, tint = DustyMerlot, size = 16.dp)
                                    Text(text = spec, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DustyMerlot)
                                }
                                RadioButton(
                                    selected = isSel,
                                    onClick = { selectedSpecialist = spec },
                                    colors = RadioButtonDefaults.colors(selectedColor = DustyMerlot)
                                )
                            }
                        }
                    }
                }
            }

            // ──────────────── 4. TÙY SHOP CẦN CỌC HAY KHÔNG & 5 MỆNH GIÁ CỌC ────────────────
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, if (requiresDeposit) ChampagneGold.copy(alpha = 0.8f) else SuccessGreen),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (!requiresDeposit) {
                            // SHOP KHÔNG CẦN CỌC
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SuccessGreen.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    GlowIcon(icon = GlowIcons.Check, tint = SuccessGreen, size = 16.dp)
                                }
                                Column {
                                    Text(
                                        text = "Tiệm Hỗ Trợ Giữ Chỗ Miễn Phí (Cọc 0đ)",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SuccessGreen
                                    )
                                    Text(
                                        text = "Shop này không yêu cầu đặt cọc trước. Quý khách chỉ thanh toán khi hoàn tất dịch vụ tại salon.",
                                        fontSize = 11.5.sp,
                                        color = TextMuted
                                    )
                                }
                            }
                        } else {
                            // SHOP YÊU CẦU CỌC VÀ CHO CHỌN 5 MỆNH GIÁ
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    GlowIcon(icon = GlowIcons.Wallet, tint = DustyMerlot, size = 18.dp)
                                    Text(
                                        text = "Chọn Mệnh Giá Đặt Cọc (5 Mức)",
                                        fontSize = 14.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DustyMerlot
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(PinkPanther.copy(alpha = 0.5f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("Shop quy định", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                                }
                            }

                            Text(
                                text = "Tiệm áp dụng 5 mệnh giá cọc để giữ chỗ chuyên viên. Quý cô vui lòng chọn mệnh giá phù hợp:",
                                fontSize = 11.5.sp,
                                color = TextMuted
                            )

                            // Dải 5 Mệnh giá cọc
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(depositTiers) { tierAmount ->
                                    val isSel = tierAmount == selectedDepositAmount
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isSel) DustyMerlot else Color(0xFFFBF7E8))
                                            .border(
                                                1.5.dp,
                                                if (isSel) ChampagneGold else Color(0xFFEBDCD3),
                                                RoundedCornerShape(12.dp)
                                            )
                                            .clickable { selectedDepositAmount = tierAmount }
                                            .padding(horizontal = 12.dp, vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = formatVnd(tierAmount),
                                                fontSize = 12.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSel) Color.White else DustyMerlot
                                            )
                                            Text(
                                                text = if (isSel) "Đã chọn" else "Mệnh giá",
                                                fontSize = 8.5.sp,
                                                color = if (isSel) PinkPanther else TextMuted
                                            )
                                        }
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFDF6EE))
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                GlowIcon(icon = GlowIcons.Shield, tint = DustyMerlot, size = 14.dp)
                                Text(
                                    text = "Chính sách: Hoàn cọc 100% tự động về Ví khi hủy trước giờ hẹn 2 tiếng.",
                                    fontSize = 11.sp,
                                    color = DustyMerlot
                                )
                            }
                        }
                    }
                }
            }

            // ──────────────── 5. TÓM TẮT ĐẶT LỊCH ────────────────
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFEBDCD3))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "Tóm Tắt Phiếu Đặt Hẹn", fontWeight = FontWeight.Bold, color = DustyMerlot)
                        HorizontalDivider(color = Color(0xFFEBDCD3))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Salon & Dịch vụ:", fontSize = 12.sp, color = TextMuted)
                            Text(text = "$salonName • $serviceName", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Khách hàng:", fontSize = 12.sp, color = TextMuted)
                            Text(text = "$customerName ($customerPhone)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Thời gian & Chuyên viên:", fontSize = 12.sp, color = TextMuted)
                            Text(text = "$selectedSlot • $selectedDate • $selectedSpecialist", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Khoản cọc giữ suất:", fontSize = 12.sp, color = TextMuted)
                            Text(
                                text = if (requiresDeposit) "${formatVnd(selectedDepositAmount)} (MoMo / QR)" else "0 đ (Miễn cọc)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (requiresDeposit) DustyMerlot else SuccessGreen
                            )
                        }
                    }
                }
            }
        }

        // ──────────────── 6. BOTTOM CONFIRM BAR ────────────────
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Button(
                onClick = {
                    val ticket = BookingTicket(
                        id = "#GLOW-" + (1000..9999).random(),
                        salonName = salonName,
                        serviceName = serviceName,
                        servicePrice = servicePrice,
                        date = selectedDate,
                        timeSlot = selectedSlot,
                        specialist = selectedSpecialist,
                        customerName = customerName.ifBlank { "Quý cô Khánh Linh" },
                        customerPhone = customerPhone.ifBlank { "0903 123 456" },
                        note = customerNote,
                        requiresDeposit = requiresDeposit,
                        depositAmount = if (requiresDeposit) selectedDepositAmount else 0L,
                        status = if (requiresDeposit) "ĐÃ XÁC NHẬN & ĐẶT CỌC" else "ĐÃ XÁC NHẬN GIỮ CHỖ (0Đ)"
                    )
                    onConfirmBooking(ticket)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DustyMerlot)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GlowIcon(icon = GlowIcons.Sparkle, tint = ChampagneGold, size = 16.dp)
                    Text(
                        text = if (requiresDeposit) 
                            "Xác Nhận Đặt Cọc ${formatVnd(selectedDepositAmount)} & Nhận Vé QR" 
                            else "Xác Nhận Giữ Chỗ Miễn Phí (0đ) & Nhận Vé QR",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

private fun formatVnd(amount: Long): String {
    return String.format("%,d đ", amount).replace(',', '.')
}
