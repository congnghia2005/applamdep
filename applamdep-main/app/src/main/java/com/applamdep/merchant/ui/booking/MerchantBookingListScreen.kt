package com.applamdep.merchant.ui.booking

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
import com.applamdep.customer.core.constants.AppConstants
import com.applamdep.customer.core.di.ViewModelFactory
import com.applamdep.customer.core.util.CurrencyFormatter
import com.applamdep.customer.domain.model.Booking
import com.applamdep.customer.ui.components.GrabCard
import com.applamdep.customer.ui.components.GrabEmptyState
import com.applamdep.customer.ui.components.GrabLoadingState
import com.applamdep.customer.ui.theme.*

/**
 * QUẢN LÝ LỊCH HẸN & TRẠNG THÁI DỊCH VỤ (MERCHANT REQUIREMENT #6 & #7)
 * - Xác nhận, từ chối, bắt đầu phục vụ và đánh dấu hoàn thành dịch vụ
 * - Cơ chế tự động hoàn thành sau 30 phút khi kết thúc dịch vụ
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MerchantBookingListScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MerchantBookingViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentFilter by viewModel.currentFilter.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadBookings()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = GrabBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Quản lý Lịch hẹn Salon",
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
                actions = {
                    // Nút quét tự động hoàn thành sau 30 phút theo Requirement #7
                    IconButton(
                        onClick = {
                            viewModel.triggerAutoComplete { count ->
                                if (count > 0) {
                                    Toast.makeText(context, "Đã tự động chuyển $count lịch hẹn quá 30p sang HOÀN THÀNH!", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(context, "Không có lịch hẹn nào quá hạn 30p chưa hoàn thành.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Autorenew,
                            contentDescription = "Quét tự động hoàn thành",
                            tint = GrabGreenDark
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
            // Thanh cuộn ngang các tab lọc trạng thái
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GrabSurface)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterTabChip("Tất cả", currentFilter == MerchantBookingFilter.ALL) {
                    viewModel.setFilter(MerchantBookingFilter.ALL)
                }
                FilterTabChip("Chờ duyệt", currentFilter == MerchantBookingFilter.PENDING) {
                    viewModel.setFilter(MerchantBookingFilter.PENDING)
                }
                FilterTabChip("Đã xác nhận", currentFilter == MerchantBookingFilter.CONFIRMED) {
                    viewModel.setFilter(MerchantBookingFilter.CONFIRMED)
                }
                FilterTabChip("Đang phục vụ", currentFilter == MerchantBookingFilter.IN_SERVICE) {
                    viewModel.setFilter(MerchantBookingFilter.IN_SERVICE)
                }
                FilterTabChip("Đã xong", currentFilter == MerchantBookingFilter.COMPLETED) {
                    viewModel.setFilter(MerchantBookingFilter.COMPLETED)
                }
                FilterTabChip("Đã hủy", currentFilter == MerchantBookingFilter.CANCELLED) {
                    viewModel.setFilter(MerchantBookingFilter.CANCELLED)
                }
            }

            HorizontalDivider(color = GrabBorder)

            when (val state = uiState) {
                is UiState.Loading -> {
                    GrabLoadingState(modifier = Modifier.weight(1f))
                }
                is UiState.Error -> {
                    GrabEmptyState(
                        title = "Có lỗi xảy ra",
                        subtitle = state.message,
                        modifier = Modifier.weight(1f)
                    )
                }
                is UiState.Success -> {
                    val bookings = state.data
                    if (bookings.isEmpty()) {
                        GrabEmptyState(
                            icon = Icons.Default.EventBusy,
                            title = "Không có lịch hẹn",
                            subtitle = "Chưa có lịch hẹn nào trong danh mục này.",
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(bookings, key = { it.id }) { booking ->
                                MerchantBookingCard(
                                    booking = booking,
                                    onConfirm = {
                                        viewModel.updateStatus(
                                            booking.id,
                                            AppConstants.BOOKING_STATUS_CONFIRMED,
                                            onSuccess = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() },
                                            onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                                        )
                                    },
                                    onStartService = {
                                        viewModel.updateStatus(
                                            booking.id,
                                            AppConstants.BOOKING_STATUS_IN_SERVICE,
                                            onSuccess = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() },
                                            onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                                        )
                                    },
                                    onComplete = {
                                        viewModel.updateStatus(
                                            booking.id,
                                            AppConstants.BOOKING_STATUS_COMPLETED,
                                            onSuccess = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() },
                                            onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                                        )
                                    },
                                    onReject = {
                                        viewModel.updateStatus(
                                            booking.id,
                                            AppConstants.BOOKING_STATUS_CANCELLED,
                                            onSuccess = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() },
                                            onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
                else -> Unit
            }
        }
    }
}

@Composable
private fun FilterTabChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) GrabGreen else GrabBackground)
            .border(1.dp, if (isSelected) GrabGreen else GrabBorder, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else GrabTextPrimary
        )
    }
}

@Composable
private fun MerchantBookingCard(
    booking: Booking,
    onConfirm: () -> Unit,
    onStartService: () -> Unit,
    onComplete: () -> Unit,
    onReject: () -> Unit
) {
    GrabCard {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: Mã & Trạng thái
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mã đơn: ${booking.id}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = GrabTextPrimary
                )
                BookingBadge(status = booking.status)
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = GrabBorder)
            Spacer(modifier = Modifier.height(10.dp))

            // Thông tin khách & Dịch vụ
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = GrabGreenDark,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${booking.customerName} · ${booking.customerPhone}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = GrabTextPrimary
                )
            }

            if (booking.customerNote.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ghi chú: ${booking.customerNote}",
                    fontSize = 12.sp,
                    color = GrabTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = booking.serviceName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = GrabTextPrimary
                )
                Text(
                    text = CurrencyFormatter.formatVnd(booking.servicePrice),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = GrabGreenDark
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Giờ hẹn
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = GrabTextSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${booking.bookingTime}  ·  ${booking.bookingDate} (${booking.durationMinutes} phút)",
                    fontSize = 12.sp,
                    color = GrabTextSecondary
                )
            }

            // Tiền cọc sàn bảo lãnh
            if (booking.requiresDeposit && booking.depositAmount > 0) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = if (booking.status == AppConstants.BOOKING_STATUS_COMPLETED) GrabGreenDark else Color(0xFF0070BA),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (booking.status == AppConstants.BOOKING_STATUS_COMPLETED)
                            "Tiền cọc ${CurrencyFormatter.formatVnd(booking.depositAmount)}: ĐÃ GIẢI NGÂN CHO SHOP"
                        else
                            "Tiền cọc ${CurrencyFormatter.formatVnd(booking.depositAmount)}: SÀN ĐANG BẢO LÃNH (HELD)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (booking.status == AppConstants.BOOKING_STATUS_COMPLETED) GrabGreenDark else Color(0xFF0070BA)
                    )
                }
            }

            // NÚT HÀNH ĐỘNG THEO TRẠNG THÁI (REQUIREMENT #6 & #7)
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = GrabBorder)
            Spacer(modifier = Modifier.height(8.dp))

            when (booking.status) {
                AppConstants.BOOKING_STATUS_PENDING -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onReject,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = GrabStatusError)
                        ) {
                            Text(text = "Từ chối", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = onConfirm,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GrabGreen)
                        ) {
                            Text(text = "Xác nhận lịch", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                AppConstants.BOOKING_STATUS_CONFIRMED -> {
                    Button(
                        onClick = onStartService,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0070BA))
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Khách đã đến · Bắt đầu phục vụ", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
                AppConstants.BOOKING_STATUS_IN_SERVICE -> {
                    Button(
                        onClick = onComplete,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GrabGreen)
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Hoàn thành dịch vụ (Giải ngân cọc)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
                AppConstants.BOOKING_STATUS_COMPLETED -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (booking.isReviewed) "★ Khách đã gửi đánh giá" else "Chờ khách gửi đánh giá",
                            fontSize = 12.sp,
                            color = if (booking.isReviewed) Color(0xFFFFB800) else GrabTextHint,
                            fontWeight = FontWeight.Medium
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = "ĐÃ GIẢI NGÂN", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GrabGreenDark)
                        }
                    }
                }
                AppConstants.BOOKING_STATUS_CANCELLED -> {
                    Text(
                        text = "Lý do hủy: ${booking.cancelReason ?: "Cửa hàng/Khách đã hủy"}",
                        fontSize = 12.sp,
                        color = GrabStatusError
                    )
                }
            }
        }
    }
}

@Composable
private fun BookingBadge(status: String) {
    val (bgColor, textColor, text) = when (status) {
        AppConstants.BOOKING_STATUS_PENDING -> Triple(Color(0xFFFFF9E6), Color(0xFFB77900), "CHỜ DUYỆT")
        AppConstants.BOOKING_STATUS_CONFIRMED -> Triple(Color(0xFFE6F0FF), Color(0xFF0070BA), "ĐÃ XÁC NHẬN")
        AppConstants.BOOKING_STATUS_IN_SERVICE -> Triple(Color(0xFFF3E5F5), Color(0xFF7B1FA2), "ĐANG LÀM")
        AppConstants.BOOKING_STATUS_COMPLETED -> Triple(GrabGreenLight, GrabGreenDark, "HOÀN THÀNH")
        AppConstants.BOOKING_STATUS_CANCELLED -> Triple(Color(0xFFFDE8E8), Color(0xFFC81E1E), "ĐÃ HỦY")
        else -> Triple(Color(0xFFF0F0F0), Color(0xFF555555), status)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(text = text, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
    }
}
