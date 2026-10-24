package com.applamdep.merchant.ui.schedule

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.applamdep.customer.core.base.UiState
import com.applamdep.customer.core.di.ViewModelFactory
import com.applamdep.customer.domain.model.ShopSlot
import com.applamdep.customer.ui.components.GrabButton
import com.applamdep.customer.ui.components.GrabCard
import com.applamdep.customer.ui.components.GrabLoadingState
import com.applamdep.customer.ui.theme.*

/**
 * QUẢN LÝ LỊCH LÀM VIỆC & KHUNG GIỜ TRỐNG (MERCHANT REQUIREMENT #4 & #5)
 * - Thiết lập ngày làm, giờ mở cửa/đóng cửa, thời gian nghỉ
 * - Cập nhật slot có thể nhận khách, sức chứa và bật/tắt nhận khách thủ công
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MerchantScheduleScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MerchantScheduleViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf(0) } // 0: Khung giờ slots, 1: Ca làm việc & Ngày nghỉ

    LaunchedEffect(Unit) {
        viewModel.loadSchedule()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = GrabBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Ca làm & Khung giờ Slot",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = GrabTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = GrabTextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GrabSurface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Tab chuyển đổi
            TabRow(
                selectedTabIndex = activeTab,
                containerColor = GrabSurface,
                contentColor = GrabGreen
            ) {
                Tab(
                    selected = activeTab == 0,
                    onClick = { activeTab = 0 },
                    text = { Text("Quản lý Slot nhận khách", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = activeTab == 1,
                    onClick = { activeTab = 1 },
                    text = { Text("Cấu hình ca làm việc", fontWeight = FontWeight.Bold) }
                )
            }

            when (val state = uiState) {
                is UiState.Loading -> {
                    GrabLoadingState(modifier = Modifier.weight(1f))
                }
                is UiState.Error -> {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(text = state.message, color = GrabStatusError)
                    }
                }
                is UiState.Success -> {
                    val schedule = state.data.schedule
                    val slots = state.data.slots

                    if (activeTab == 0) {
                        // 1. Quản lý Slots
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Chọn ngày kiểm tra slot:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = GrabTextSecondary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Thanh chọn ngày
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("2026-10-23", "2026-10-24", "2026-10-25", "2026-10-26").forEach { date ->
                                    val isSelected = date == selectedDate
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) GrabGreen else GrabSurface)
                                            .border(1.dp, if (isSelected) GrabGreen else GrabBorder, RoundedCornerShape(8.dp))
                                            .clickable { viewModel.setSelectedDate(date) }
                                            .padding(horizontal = 14.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = if (date == "2026-10-23") "Hôm nay ($date)" else date,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) Color.White else GrabTextPrimary
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(slots, key = { it.id }) { slot ->
                                    SlotManageCard(
                                        slot = slot,
                                        onToggleAvailability = { isAvailable ->
                                            viewModel.toggleSlotAvailability(slot.id, isAvailable) {
                                                Toast.makeText(context, if (isAvailable) "Đã mở nhận khách lúc ${slot.startTime}" else "Đã khóa slot lúc ${slot.startTime}", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        onCapacityChange = { newCap ->
                                            viewModel.updateSlotCapacity(slot, newCap) {
                                                Toast.makeText(context, "Sức chứa lúc ${slot.startTime}: $newCap khách", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    } else {
                        // 2. Cấu hình Ca làm việc
                        ScheduleConfigForm(
                            schedule = schedule,
                            onSave = { updated ->
                                viewModel.updateSchedule(
                                    schedule = updated,
                                    onSuccess = { Toast.makeText(context, "Lưu cấu hình ca làm việc thành công!", Toast.LENGTH_SHORT).show() },
                                    onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                else -> Unit
            }
        }
    }
}

@Composable
private fun SlotManageCard(
    slot: ShopSlot,
    onToggleAvailability: (Boolean) -> Unit,
    onCapacityChange: (Int) -> Unit
) {
    GrabCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (slot.isAvailable) GrabGreenLight else Color(0xFFF0F0F0)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = if (slot.isAvailable) GrabGreenDark else GrabTextHint,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = slot.startTime,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (slot.isAvailable) GrabTextPrimary else GrabTextHint
                    )
                    Text(
                        text = "Đã đặt: ${slot.bookedCount}/${slot.maxCapacity} khách",
                        fontSize = 12.sp,
                        color = if (slot.bookedCount >= slot.maxCapacity) GrabStatusError else GrabGreenDark,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Bộ điều khiển: Tăng giảm sức chứa + Switch bật tắt slot
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Nút giảm sức chứa
                IconButton(
                    onClick = { if (slot.maxCapacity > 1) onCapacityChange(slot.maxCapacity - 1) },
                    enabled = slot.isAvailable && slot.maxCapacity > 1
                ) {
                    Icon(imageVector = Icons.Default.RemoveCircleOutline, contentDescription = "Giảm", tint = GrabGreenDark)
                }

                Text(
                    text = "${slot.maxCapacity}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = GrabTextPrimary
                )

                // Nút tăng sức chứa
                IconButton(
                    onClick = { if (slot.maxCapacity < 10) onCapacityChange(slot.maxCapacity + 1) },
                    enabled = slot.isAvailable && slot.maxCapacity < 10
                ) {
                    Icon(imageVector = Icons.Default.AddCircleOutline, contentDescription = "Tăng", tint = GrabGreenDark)
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Switch Bật/Tắt slot nhận khách
                Switch(
                    checked = slot.isAvailable,
                    onCheckedChange = onToggleAvailability,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = GrabGreen
                    )
                )
            }
        }
    }
}

@Composable
private fun ScheduleConfigForm(
    schedule: com.applamdep.merchant.domain.model.MerchantSchedule,
    onSave: (com.applamdep.merchant.domain.model.MerchantSchedule) -> Unit,
    modifier: Modifier = Modifier
) {
    var openTime by remember { mutableStateOf(schedule.openTime) }
    var closeTime by remember { mutableStateOf(schedule.closeTime) }
    var breakStart by remember { mutableStateOf(schedule.breakStartTime) }
    var breakEnd by remember { mutableStateOf(schedule.breakEndTime) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        GrabCard {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Giờ mở cửa & Giờ đóng cửa",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = GrabTextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = openTime,
                        onValueChange = { openTime = it },
                        label = { Text("Mở cửa") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = closeTime,
                        onValueChange = { closeTime = it },
                        label = { Text("Đóng cửa") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        GrabCard {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Thời gian nghỉ giữa ca (Nghỉ trưa)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = GrabTextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = breakStart,
                        onValueChange = { breakStart = it },
                        label = { Text("Bắt đầu nghỉ") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = breakEnd,
                        onValueChange = { breakEnd = it },
                        label = { Text("Kết thúc nghỉ") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        GrabButton(
            text = "Lưu cấu hình ca làm việc",
            onClick = {
                val updated = schedule.copy(
                    openTime = openTime.trim(),
                    closeTime = closeTime.trim(),
                    breakStartTime = breakStart.trim(),
                    breakEndTime = breakEnd.trim()
                )
                onSave(updated)
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
