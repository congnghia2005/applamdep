package com.example.app_lam_dep.ui.screens

import androidx.compose.foundation.background
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
import com.example.app_lam_dep.ui.icons.GlowBackButton
import com.example.app_lam_dep.ui.icons.GlowIcon
import com.example.app_lam_dep.ui.icons.GlowIcons
import com.example.app_lam_dep.ui.theme.ChampagneGold
import com.example.app_lam_dep.ui.theme.DustyMerlot
import com.example.app_lam_dep.ui.theme.PinkPanther
import com.example.app_lam_dep.ui.theme.TextMuted

@Composable
fun BookingScreen(
    serviceName: String,
    salonName: String,
    onBack: () -> Unit,
    onConfirmBooking: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dates = listOf("Hôm nay (07/10)", "Thứ Ba (08/10)", "Thứ Tư (09/10)", "Thứ Năm (10/10)")
    var selectedDate by remember { mutableStateOf(dates[0]) }

    val afternoonSlots = listOf("13:00", "13:45", "14:30", "15:30", "16:15")
    var selectedSlot by remember { mutableStateOf("15:30") }

    val specialists = listOf("Master Linh (VIP)", "Chuyên viên Sarah", "Chuyên viên Minh Thư")
    var selectedSpecialist by remember { mutableStateOf(specialists[0]) }

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
                    Text(text = "Xác Nhận Đặt Lịch", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
            // Date Selection
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

            // Time Slot Picker
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Chọn Khung Giờ Trống", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                    Text(text = "Buổi chiều", fontSize = 12.sp, color = TextMuted)
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        afternoonSlots.take(4).forEach { slot ->
                            val isSel = slot == selectedSlot
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSel) DustyMerlot else Color.White)
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

            // Specialist Selection
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Chọn Chuyên Viên (Optional)", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                    specialists.forEach { spec ->
                        val isSel = spec == selectedSpecialist
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedSpecialist = spec },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = if (isSel) PinkPanther.copy(alpha = 0.3f) else Color.White)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = spec, fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = DustyMerlot)
                                RadioButton(selected = isSel, onClick = { selectedSpecialist = spec })
                            }
                        }
                    }
                }
            }

            // Summary Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "Tóm Tắt Đặt Lịch & Cọc 30%", fontWeight = FontWeight.Bold, color = DustyMerlot)
                        HorizontalDivider(color = Color(0xFFEBDCD3))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Dịch vụ:", fontSize = 12.5.sp, color = TextMuted)
                            Text(text = serviceName, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Thời gian:", fontSize = 12.5.sp, color = TextMuted)
                            Text(text = "$selectedSlot • $selectedDate", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24090F))
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Cọc giữ suất 30%:", fontSize = 12.5.sp, color = TextMuted)
                            Text(text = "50.000 đ (MoMo / QR)", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = DustyMerlot)
                        }
                    }
                }
            }
        }

        // Bottom Action Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Button(
                onClick = onConfirmBooking,
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
                    Text(text = "Xác Nhận Đặt Cọc 50.000đ & Nhận Vé VIP", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
