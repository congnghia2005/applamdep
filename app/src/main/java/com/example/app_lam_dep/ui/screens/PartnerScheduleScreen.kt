package com.example.app_lam_dep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import com.example.app_lam_dep.ui.icons.GlowIcon
import com.example.app_lam_dep.ui.icons.GlowIcons
import com.example.app_lam_dep.ui.icons.IconMedallion
import com.example.app_lam_dep.ui.theme.ApplamdepTheme
import com.example.app_lam_dep.ui.theme.ChampagneGold
import com.example.app_lam_dep.ui.theme.DustyMerlot
import com.example.app_lam_dep.ui.theme.PinkPanther
import com.example.app_lam_dep.ui.theme.SuccessGreen
import com.example.app_lam_dep.ui.theme.TextMuted
import com.example.app_lam_dep.ui.theme.WarningOrange

data class SlotItem(val time: String, var status: String, var customer: String, var maxSlots: Int = 2, var bookedSlots: Int = 1)

@Composable
fun PartnerScheduleScreen(
    modifier: Modifier = Modifier
) {
    var selectedCalendarView by remember { mutableStateOf(0) } // 0: Lịch Tuần, 1: Lịch Tháng, 2: Lịch Ngày
    var selectedDayIdx by remember { mutableStateOf(0) }
    var editingSlot by remember { mutableStateOf<SlotItem?>(null) }

    val days = listOf(
        "T2 (Hôm nay)\n07/10", "Thứ Ba\n08/10", "Thứ Tư\n09/10",
        "Thứ Năm\n10/10", "Thứ Sáu\n11/10", "Thứ Bảy\n12/10", "Chủ Nhật\n13/10"
    )

    val slotsByDay = remember {
        mutableStateMapOf(
            0 to mutableStateListOf(
                SlotItem("09:00 - 10:15", "booked", "Quý cô Hà An (Đã cọc 50k)", 2, 1),
                SlotItem("10:30 - 11:45", "available", "", 3, 0),
                SlotItem("13:00 - 14:15", "available", "", 2, 0),
                SlotItem("15:30 - 16:45", "booked", "Quý cô Khánh Linh (Đã cọc 50k)", 2, 1),
                SlotItem("17:00 - 18:15", "available", "", 2, 0),
                SlotItem("18:30 - 19:45", "locked", "", 2, 0)
            ),
            1 to mutableStateListOf(
                SlotItem("09:00 - 10:15", "available", "", 2, 0),
                SlotItem("10:30 - 11:45", "booked", "Quý cô Mai Phương", 2, 1)
            )
        )
    }

    val currentSlots = slotsByDay.getOrPut(selectedDayIdx) {
        mutableStateListOf(
            SlotItem("09:00 - 10:15", "available", "", 2, 0),
            SlotItem("10:30 - 11:45", "available", "", 2, 0)
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF7E8))
    ) {
        // Salon Top Profile Header Card (matching screenshot)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconMedallion(
                    icon = GlowIcons.Crown,
                    size = 52.dp,
                    iconSize = 24.dp,
                    tint = ChampagneGold,
                    contentDescription = "Salon Crown"
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "L'Amour Haute Nail & Lash Lounge", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF3A101A))
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        GlowIcon(icon = GlowIcons.MapPin, tint = DustyMerlot.copy(alpha = 0.7f), size = 12.dp)
                        Text(text = "45 Đồng Khởi, Phường Bến Nghé, Quận 1", fontSize = 11.sp, color = TextMuted, maxLines = 1)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(PinkPanther.copy(alpha = 0.5f))
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            GlowIcon(icon = GlowIcons.Crown, tint = DustyMerlot, size = 10.dp)
                            Text(text = "Gói VIP Sponsor (Còn 24 ngày)", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                        }
                    }
                }
            }
        }

        // Sub-tabs: Lịch Tuần | Lịch Tháng | Lịch Ngày
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalendarTabButton("Lịch Tuần", selectedCalendarView == 0, GlowIcons.Calendar) { selectedCalendarView = 0 }
            CalendarTabButton("Lịch Tháng", selectedCalendarView == 1, GlowIcons.Calendar) { selectedCalendarView = 1 }
            CalendarTabButton("Lịch Ngày", selectedCalendarView == 2, GlowIcons.Clock) { selectedCalendarView = 2 }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Content based on selected calendar view
        if (selectedCalendarView == 0) {
            // Lịch Tuần (Weekly Matrix)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "Lịch Hoạt Động (7 Ngày x Khung Giờ)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3A101A))
                                    Text(text = "Chạm vào ô để Khóa/Mở slot nhận khách hoặc cài đặt số lượng slot", fontSize = 10.5.sp, color = TextMuted)
                                }
                                Button(
                                    onClick = {},
                                    colors = ButtonDefaults.buttonColors(containerColor = DustyMerlot),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        GlowIcon(icon = GlowIcons.Save, tint = Color.White, size = 12.dp)
                                        Text("Lưu Lịch", fontSize = 10.sp, color = Color.White)
                                    }
                                }
                            }

                            // Legend
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                LegendItem("Mở nhận khách", SuccessGreen)
                                LegendItem("Đã khóa (Bận)", Color.Gray)
                                LegendItem("Khách đã cọc", WarningOrange)
                            }

                            // 7-Day Day Selector
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(days.size) { idx ->
                                    val isSel = idx == selectedDayIdx
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isSel) DustyMerlot else Color(0xFFFBF7E8))
                                            .clickable { selectedDayIdx = idx }
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = days[idx],
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSel) Color.White else Color(0xFF3A101A),
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Slot Grid items
                items(currentSlots.size) { sIdx ->
                    val slot = currentSlots[sIdx]
                    val isBooked = slot.status == "booked"
                    val isLocked = slot.status == "locked"

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { editingSlot = slot },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                isBooked -> Color(0xFFFDF2EF)
                                isLocked -> Color(0xFFF5EEEC)
                                else -> Color.White
                            }
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = slot.time, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3A101A))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(
                                            when {
                                                isBooked -> WarningOrange.copy(alpha = 0.2f)
                                                isLocked -> Color.Gray.copy(alpha = 0.2f)
                                                else -> SuccessGreen.copy(alpha = 0.2f)
                                            }
                                        )
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = when {
                                            isBooked -> "Đã cọc 30% (${slot.bookedSlots}/${slot.maxSlots} slots)"
                                            isLocked -> "Đang KHÓA (${slot.maxSlots} slots)"
                                            else -> "Mở nhận khách (${slot.maxSlots} slots)"
                                        },
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when {
                                            isBooked -> WarningOrange
                                            isLocked -> Color.Gray
                                            else -> SuccessGreen
                                        }
                                    )
                                }
                            }

                            if (isBooked) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    GlowIcon(icon = GlowIcons.User, tint = DustyMerlot, size = 13.dp)
                                    Text(text = slot.customer, fontSize = 12.sp, color = DustyMerlot, fontWeight = FontWeight.Medium)
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Chạm để cấu hình slot / khóa giờ", fontSize = 10.sp, color = TextMuted)
                                Button(
                                    onClick = {
                                        if (slot.status == "available") slot.status = "locked"
                                        else if (slot.status == "locked") slot.status = "available"
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isLocked) SuccessGreen else DustyMerlot
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Text(
                                        text = if (isLocked) "Mở giờ này" else "Khóa giờ",
                                        fontSize = 10.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else if (selectedCalendarView == 1) {
            // Lịch Tháng (Monthly View)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "Lịch Tháng 10/2026", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                        GlowIcon(icon = GlowIcons.Calendar, tint = ChampagneGold, size = 16.dp)
                    }
                    Text(text = "Hiển thị tổng quan mật độ booking trong tháng. Chọn ngày để xem chi tiết slot.", fontSize = 12.sp, color = TextMuted)

                    val daysInMonth = (1..31).toList()
                    val weeks = daysInMonth.chunked(7)
                    weeks.forEach { week ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            week.forEach { day ->
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (day == 7 || day == 15) DustyMerlot else Color(0xFFFBF7E8))
                                        .clickable { },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$day",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (day == 7 || day == 15) Color.White else Color(0xFF3A101A)
                                    )
                                }
                            }
                            repeat(7 - week.size) {
                                Spacer(modifier = Modifier.size(38.dp))
                            }
                        }
                    }
                }
            }
        } else {
            // Lịch Ngày (Daily Hourly View)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(text = "Lịch Chi Tiết Theo Giờ Trong Ngày ⏰", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                    Text(text = "Quản lý chi tiết từng khung giờ và số lượng slot nhận khách tối đa.", fontSize = 12.sp, color = TextMuted)

                    currentSlots.forEach { slot ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFBF7E8), RoundedCornerShape(10.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = slot.time, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DustyMerlot)
                                Text(text = "Tối đa: ${slot.maxSlots} slots • Đã đặt: ${slot.bookedSlots}", fontSize = 11.sp, color = TextMuted)
                            }
                            Button(
                                onClick = { editingSlot = slot },
                                colors = ButtonDefaults.buttonColors(containerColor = DustyMerlot),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(30.dp)
                            ) {
                                Text("Sửa Slot", fontSize = 10.sp, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }

    // Slot Editing Dialog
    if (editingSlot != null) {
        val slot = editingSlot!!
        var slotCount by remember { mutableStateOf(slot.maxSlots.toString()) }

        AlertDialog(
            onDismissRequest = { editingSlot = null },
            title = { Text("Cấu Hình Khung Giờ ${slot.time}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DustyMerlot) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Nhập số lượng slot nhận khách tối đa:", fontSize = 13.sp, color = TextMuted)
                    OutlinedTextField(
                        value = slotCount,
                        onValueChange = { slotCount = it },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        slot.maxSlots = slotCount.toIntOrNull() ?: 2
                        editingSlot = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DustyMerlot)
                ) {
                    Text("Lưu Cấu Hình", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { editingSlot = null }) {
                    Text("Hủy", color = TextMuted)
                }
            }
        )
    }
}

@Composable
private fun CalendarTabButton(text: String, isSelected: Boolean, icon: ImageVector? = null, onClick: () -> Unit) {
    val bg = if (isSelected) DustyMerlot else Color.White
    val fg = if (isSelected) Color.White else DustyMerlot

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (icon != null) {
                GlowIcon(icon = icon, tint = fg, size = 13.dp)
            }
            Text(text = text, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = fg)
        }
    }
}

@Composable
private fun LegendItem(text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(text = text, fontSize = 10.sp, color = TextMuted)
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
fun PartnerScheduleScreenPreview() {
    ApplamdepTheme {
        PartnerScheduleScreen()
    }
}
