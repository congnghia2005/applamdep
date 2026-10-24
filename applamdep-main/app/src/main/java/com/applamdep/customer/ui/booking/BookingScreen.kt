package com.applamdep.customer.ui.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.applamdep.customer.core.base.UiState
import com.applamdep.customer.core.di.ViewModelFactory
import com.applamdep.customer.core.util.CurrencyFormatter
import com.applamdep.customer.domain.model.ShopSlot
import com.applamdep.customer.ui.components.*
import com.applamdep.customer.ui.theme.*

/**
 * MÀN HÌNH "CHỌN LỊCH" HẸN (NHÓM 3 - P0)
 * - RÀNG BUỘC BẮT BUỘC: Địa chỉ quán hiển thị trên ĐẦU TRANG
 * - Thay "giỏ hàng" bằng "Chọn lịch": tổng hợp dịch vụ đã chọn + khung giờ + tổng tiền + xác nhận
 * - Ẩn khung giờ trùng/đã qua/hết chỗ bằng SlotConflictResolver
 * - Form thông tin khách có validate
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    shopId: String,
    serviceId: String,
    onBack: () -> Unit,
    onBookingSuccess: (bookingId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookingViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formData by viewModel.formData.collectAsStateWithLifecycle()

    LaunchedEffect(shopId, serviceId) {
        viewModel.initBooking(shopId, serviceId)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = GrabBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Chọn lịch hẹn",
                        fontSize = 17.sp,
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is UiState.Loading -> GrabLoadingState()
                is UiState.Error -> GrabEmptyState(
                    message = (uiState as UiState.Error).message,
                    onRetry = { viewModel.initBooking(shopId, serviceId) }
                )
                else -> {
                    val data = formData
                    if (data != null) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            LazyColumn(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                contentPadding = PaddingValues(bottom = 24.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // 1. RÀNG BUỘC BẮT BUỘC: ĐỊA CHỈ QUÁN HIỂN THỊ TRÊN ĐẦU TRANG
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(GrabGreenLight)
                                            .padding(horizontal = 16.dp, vertical = 12.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.LocationOn,
                                                contentDescription = "Address",
                                                tint = GrabGreenDark,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text(
                                                    text = data.shop.name,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = GrabGreenDark
                                                )
                                                Text(
                                                    text = data.shop.address,
                                                    fontSize = 12.sp,
                                                    color = GrabTextPrimary
                                                )
                                            }
                                        }
                                    }
                                }

                                // 2. Dịch vụ đã chọn
                                item {
                                    GrabCard(modifier = Modifier.padding(horizontal = 16.dp)) {
                                        Text(
                                            text = "Dịch vụ đã chọn",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GrabTextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = data.selectedService.name,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = GrabTextPrimary
                                                )
                                                Text(
                                                    text = "Thời lượng thực hiện: ${data.selectedService.durationMinutes} phút",
                                                    fontSize = 12.sp,
                                                    color = GrabTextSecondary
                                                )
                                            }
                                            Text(
                                                text = CurrencyFormatter.formatVnd(data.selectedService.price),
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = GrabGreenDark
                                            )
                                        }
                                    }
                                }

                                // 3. Chọn ngày hẹn
                                item {
                                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                        Text(
                                            text = "Chọn ngày làm đẹp",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GrabTextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            items(data.availableDates) { date ->
                                                val isSelected = date == data.selectedDate
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(12.dp))
                                                        .background(if (isSelected) GrabGreen else GrabSurface)
                                                        .border(
                                                            width = 1.dp,
                                                            color = if (isSelected) GrabGreen else GrabBorder,
                                                            shape = RoundedCornerShape(12.dp)
                                                        )
                                                        .clickable { viewModel.onDateSelected(date) }
                                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                                ) {
                                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                        Text(
                                                            text = if (date == data.availableDates.first()) "Hôm nay" else "Ngày",
                                                            fontSize = 11.sp,
                                                            color = if (isSelected) Color.White.copy(alpha = 0.8f) else GrabTextSecondary
                                                        )
                                                        Text(
                                                            text = date.takeLast(5),
                                                            fontSize = 13.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = if (isSelected) Color.White else GrabTextPrimary
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                // 4. Chọn khung giờ hẹn (Đã lọc qua SlotConflictResolver)
                                item {
                                    GrabCard(modifier = Modifier.padding(horizontal = 16.dp)) {
                                        Text(
                                            text = "Khung giờ hẹn có sẵn",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GrabTextPrimary
                                        )
                                        Text(
                                            text = "Hệ thống tự động ẩn các khung giờ trùng hoặc đã hết chỗ",
                                            fontSize = 11.sp,
                                            color = GrabTextSecondary
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))

                                        if (data.availableSlots.isEmpty()) {
                                            Text(
                                                text = "Rất tiếc, ngày này không còn khung giờ trống. Vui lòng chọn ngày khác.",
                                                fontSize = 13.sp,
                                                color = GrabStatusError
                                            )
                                        } else {
                                            // Lưới các khung giờ
                                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                                data.availableSlots.chunked(3).forEach { rowSlots ->
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                    ) {
                                                        rowSlots.forEach { slot ->
                                                            val isSlotSelected = slot.id == data.selectedSlot?.id
                                                            Box(
                                                                modifier = Modifier
                                                                    .weight(1f)
                                                                    .clip(RoundedCornerShape(10.dp))
                                                                    .background(if (isSlotSelected) GrabGreenLight else GrabSurface)
                                                                    .border(
                                                                        width = 1.5.dp,
                                                                        color = if (isSlotSelected) GrabGreen else GrabBorder,
                                                                        shape = RoundedCornerShape(10.dp)
                                                                    )
                                                                    .clickable { viewModel.onSlotSelected(slot) }
                                                                    .padding(vertical = 10.dp),
                                                                contentAlignment = Alignment.Center
                                                            ) {
                                                                Text(
                                                                    text = slot.startTime,
                                                                    fontSize = 14.sp,
                                                                    fontWeight = if (isSlotSelected) FontWeight.Bold else FontWeight.Medium,
                                                                    color = if (isSlotSelected) GrabGreenDark else GrabTextPrimary
                                                                )
                                                            }
                                                        }
                                                        // Bù khoảng trống nếu hàng không đủ 3
                                                        repeat(3 - rowSlots.size) {
                                                            Spacer(modifier = Modifier.weight(1f))
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        if (data.slotError != null) {
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = data.slotError,
                                                color = GrabStatusError,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }

                                // 5. Chọn mệnh giá cọc (nếu shop yêu cầu cọc)
                                if (data.shop.requireDeposit && data.shop.depositOptions.isNotEmpty()) {
                                    item {
                                        GrabCard(modifier = Modifier.padding(horizontal = 16.dp)) {
                                            Text(
                                                text = "Chọn mệnh giá đặt cọc",
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = GrabTextPrimary
                                            )
                                            Text(
                                                text = "Tiền cọc sẽ được Sàn tạm giữ (HELD). Hoàn 100% về ví nếu hủy trước 2 tiếng.",
                                                fontSize = 11.sp,
                                                color = GrabTextSecondary
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))

                                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                items(data.shop.depositOptions) { tier ->
                                                    val isSelected = tier == data.selectedDepositAmount
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(8.dp))
                                                            .background(if (isSelected) Color(0xFFFFF0F0) else GrabSurface)
                                                            .border(
                                                                width = 1.dp,
                                                                color = if (isSelected) GrabStatusError else GrabBorder,
                                                                shape = RoundedCornerShape(8.dp)
                                                            )
                                                            .clickable { viewModel.onDepositSelected(tier) }
                                                            .padding(horizontal = 12.dp, vertical = 8.dp)
                                                    ) {
                                                        Text(
                                                            text = CurrencyFormatter.formatVnd(tier),
                                                            fontSize = 13.sp,
                                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                            color = if (isSelected) GrabStatusError else GrabTextPrimary
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                // 6. Form thông tin người đặt (Có validate)
                                item {
                                    GrabCard(modifier = Modifier.padding(horizontal = 16.dp)) {
                                        Text(
                                            text = "Thông tin khách hẹn",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GrabTextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))

                                        // Họ tên
                                        OutlinedTextField(
                                            value = data.customerName,
                                            onValueChange = viewModel::onNameChanged,
                                            label = { Text("Họ và tên") },
                                            isError = data.nameError != null,
                                            supportingText = data.nameError?.let { { Text(it, color = GrabStatusError) } },
                                            singleLine = true,
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(10.dp)
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        // Số điện thoại
                                        OutlinedTextField(
                                            value = data.customerPhone,
                                            onValueChange = viewModel::onPhoneChanged,
                                            label = { Text("Số điện thoại liên hệ") },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                            isError = data.phoneError != null,
                                            supportingText = data.phoneError?.let { { Text(it, color = GrabStatusError) } },
                                            singleLine = true,
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(10.dp)
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        // Ghi chú
                                        OutlinedTextField(
                                            value = data.customerNote,
                                            onValueChange = viewModel::onNoteChanged,
                                            label = { Text("Ghi chú đặc biệt (tùy chọn)") },
                                            placeholder = { Text("Ví dụ: da tay nhạy cảm, làm nhẹ nhàng...") },
                                            maxLines = 3,
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                    }
                                }
                            }

                            // 7. THAY "GIỎ HÀNG" BẰNG "CHỌN LỊCH" TỔNG HỢP Ở CHÂN TRANG
                            Surface(
                                color = GrabSurface,
                                shadowElevation = 10.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = if (data.shop.requireDeposit) "Tiền cọc giữ chỗ (Sàn tạm giữ)" else "Tổng tiền thanh toán tại tiệm",
                                                fontSize = 12.sp,
                                                color = GrabTextSecondary
                                            )
                                            val amountDisplay = if (data.shop.requireDeposit) {
                                                CurrencyFormatter.formatVnd(data.selectedDepositAmount)
                                            } else {
                                                CurrencyFormatter.formatVnd(data.selectedService.price)
                                            }
                                            Text(
                                                text = amountDisplay,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = if (data.shop.requireDeposit) GrabStatusError else GrabGreenDark
                                            )
                                        }

                                        if (data.selectedSlot != null) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(GrabGreenLight)
                                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                            ) {
                                                Text(
                                                    text = "${data.selectedSlot.startTime} · ${data.selectedDate.takeLast(5)}",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = GrabGreenDark
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    GrabPrimaryButton(
                                        text = "Xác nhận đặt lịch",
                                        isLoading = data.isSubmitting,
                                        onClick = {
                                            viewModel.submitBooking(
                                                customerId = "user_cust_01",
                                                onSuccess = { bookingId ->
                                                    // 3. THÔNG BÁO P1: Lên lịch nhắc hẹn 30 phút qua WorkManager
                                                    com.applamdep.customer.core.notification.NotificationHelper.scheduleBookingReminder(
                                                        context = context,
                                                        bookingId = bookingId,
                                                        shopName = data.shop.name,
                                                        bookingTime = data.selectedSlot?.startTime ?: "10:00",
                                                        appointmentTimestamp = System.currentTimeMillis() + (4 * 3600000L)
                                                    )
                                                    onBookingSuccess(bookingId)
                                                }
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
