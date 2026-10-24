package com.applamdep.customer.ui.booking

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.applamdep.customer.domain.policy.CancellationDecision
import com.applamdep.customer.ui.components.GrabCard
import com.applamdep.customer.ui.components.GrabEmptyState
import com.applamdep.customer.ui.components.GrabLoadingState
import com.applamdep.customer.ui.theme.*

/**
 * MÀN HÌNH LỊCH SỬ ĐẶT LỊCH & QUẢN LÝ HỦY LỊCH (NHÓM 3 - P0)
 * - Hiển thị các trạng thái: PENDING, CONFIRMED, IN_SERVICE, COMPLETED, CANCELLED
 * - Nghiệp vụ hủy trước 2 tiếng: Hiển thị hộp thoại hoàn cọc về ví qua CancellationPolicy
 * - Dưới 2 tiếng: Vô hiệu hóa nút Hủy và hiển thị lý do
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingHistoryScreen(
    onBack: (() -> Unit)? = null,
    onNavigateToReview: ((bookingId: String) -> Unit)? = null,
    onViewCustomerReviews: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    viewModel: BookingHistoryViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentFilter by viewModel.filter.collectAsStateWithLifecycle()

    var bookingToCancel by remember { mutableStateOf<Pair<Booking, CancellationDecision>?>(null) }
    var cancelReasonText by remember { mutableStateOf("Bận việc đột xuất") }

    LaunchedEffect(Unit) {
        viewModel.loadCustomerBookings("user_cust_01")
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = GrabBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lịch hẹn của tôi",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = GrabTextPrimary
                    )
                },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = GrabTextPrimary
                            )
                        }
                    }
                },
                actions = {
                    if (onViewCustomerReviews != null) {
                        TextButton(onClick = onViewCustomerReviews) {
                            Icon(
                                imageVector = Icons.Default.RateReview,
                                contentDescription = null,
                                tint = GrabGreenDark,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Đánh giá của tôi",
                                fontSize = 12.sp,
                                color = GrabGreenDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
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
            // Thanh chuyển tab bộ lọc: Tất cả, Sắp tới, Đã xong/Hủy
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GrabSurface)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterTabButton(
                    label = "Tất cả",
                    isSelected = currentFilter == BookingFilterType.ALL,
                    onClick = { viewModel.setFilter(BookingFilterType.ALL) },
                    modifier = Modifier.weight(1f)
                )
                FilterTabButton(
                    label = "Sắp tới",
                    isSelected = currentFilter == BookingFilterType.UPCOMING,
                    onClick = { viewModel.setFilter(BookingFilterType.UPCOMING) },
                    modifier = Modifier.weight(1f)
                )
                FilterTabButton(
                    label = "Đã xong / Hủy",
                    isSelected = currentFilter == BookingFilterType.COMPLETED_OR_CANCELLED,
                    onClick = { viewModel.setFilter(BookingFilterType.COMPLETED_OR_CANCELLED) },
                    modifier = Modifier.weight(1.3f)
                )
            }

            // Danh sách lịch hẹn
            Box(modifier = Modifier.fillMaxSize()) {
                when (val state = uiState) {
                    is UiState.Loading -> GrabLoadingState()
                    is UiState.Error -> GrabEmptyState(
                        message = state.message,
                        onRetry = { viewModel.loadCustomerBookings("user_cust_01") }
                    )
                    is UiState.Success -> {
                        val bookings = state.data
                        if (bookings.isEmpty()) {
                            GrabEmptyState(message = "Bạn chưa có lịch hẹn nào trong danh mục này.")
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                items(
                                    items = bookings,
                                    key = { it.id }
                                ) { booking ->
                                    val decision = viewModel.checkCancellationEligibility(booking)

                                    BookingTicketItem(
                                        booking = booking,
                                        cancellationDecision = decision,
                                        onCancelClick = {
                                            bookingToCancel = Pair(booking, decision)
                                        },
                                        onReviewClick = {
                                            onNavigateToReview?.invoke(booking.id)
                                        }
                                    )
                                }
                            }
                        }
                    }
                    else -> Unit
                }
            }

            // HỘP THOẠI XÁC NHẬN HỦY LỊCH (QUY TẮC HOÀN CỌC VÍ TRƯỚC 2 TIẾNG)
            bookingToCancel?.let { (booking, decision) ->
                AlertDialog(
                    onDismissRequest = { bookingToCancel = null },
                    containerColor = GrabSurface,
                    shape = RoundedCornerShape(16.dp),
                    title = {
                        Text(
                            text = "Xác nhận hủy lịch hẹn",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = GrabTextPrimary
                        )
                    },
                    text = {
                        Column {
                            Text(
                                text = decision.message,
                                fontSize = 13.sp,
                                color = if (decision.isRefundEligible) GrabGreenDark else GrabStatusError,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = cancelReasonText,
                                onValueChange = { cancelReasonText = it },
                                label = { Text("Lý do hủy lịch") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                val b = booking
                                bookingToCancel = null
                                viewModel.cancelBooking(
                                    booking = b,
                                    reason = cancelReasonText,
                                    onSuccess = { msg ->
                                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                    },
                                    onError = { err ->
                                        Toast.makeText(context, err, Toast.LENGTH_LONG).show()
                                    }
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GrabStatusError),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Đồng ý hủy", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { bookingToCancel = null }) {
                            Text("Giữ lại lịch", color = GrabTextSecondary)
                        }
                    }
                )
            }
        }
    }
}

/**
 * Thẻ hiển thị vé đặt lịch
 */
@Composable
private fun BookingTicketItem(
    booking: Booking,
    cancellationDecision: CancellationDecision,
    onCancelClick: () -> Unit,
    onReviewClick: () -> Unit
) {
    GrabCard {
        // Dòng header: Mã vé + Trạng thái
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#${booking.id}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = GrabTextPrimary
            )

            BookingStatusChip(status = booking.status)
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = GrabBorder)
        Spacer(modifier = Modifier.height(8.dp))

        // Tên shop & Dịch vụ
        Text(
            text = booking.shopName,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = GrabTextPrimary
        )
        Text(
            text = booking.shopAddress,
            fontSize = 12.sp,
            color = GrabTextSecondary
        )

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

        // Thời gian hẹn
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = "Time",
                tint = GrabGreenDark,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${booking.bookingTime}  ·  ${booking.bookingDate}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = GrabGreenDark
            )
        }

        // Cọc nếu có
        if (booking.requiresDeposit && booking.depositAmount > 0) {
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = "Deposit",
                    tint = GrabStatusError,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Đã cọc: ${CurrencyFormatter.formatVnd(booking.depositAmount)} (Sàn giữ HELD)",
                    fontSize = 12.sp,
                    color = GrabStatusError,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Dòng hành động: Hủy lịch hoặc Đánh giá
        val isCancellableStatus = booking.status == AppConstants.BOOKING_STATUS_CONFIRMED || booking.status == AppConstants.BOOKING_STATUS_PENDING

        if (isCancellableStatus) {
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = GrabBorder)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!cancellationDecision.canCancel) {
                    Text(
                        text = "Quá hạn hủy (< 2h)",
                        fontSize = 12.sp,
                        color = GrabStatusError,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Text(
                        text = "Được hoàn 100% cọc về ví",
                        fontSize = 12.sp,
                        color = GrabGreenDark,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                }

                // NÚT HỦY LỊCH: Vô hiệu hóa nếu dưới 2 tiếng theo quy định
                OutlinedButton(
                    onClick = onCancelClick,
                    enabled = cancellationDecision.canCancel,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = GrabStatusError,
                        disabledContentColor = GrabTextHint
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp)
                ) {
                    Text(text = "Hủy lịch", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        } else if (booking.status == AppConstants.BOOKING_STATUS_COMPLETED && !booking.isReviewed) {
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onReviewClick,
                colors = ButtonDefaults.buttonColors(containerColor = GrabGreen),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Viết đánh giá dịch vụ", color = Color.White, fontSize = 13.sp)
            }
        } else if (booking.status == AppConstants.BOOKING_STATUS_COMPLETED && booking.isReviewed) {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = onReviewClick,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFB800),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Đã gửi đánh giá · Xem nhận xét",
                    color = GrabGreenDark,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun BookingStatusChip(status: String) {
    val (bgColor, textColor, text) = when (status) {
        AppConstants.BOOKING_STATUS_CONFIRMED -> Triple(GrabGreenLight, GrabGreenDark, "ĐÃ XÁC NHẬN")
        AppConstants.BOOKING_STATUS_PENDING -> Triple(Color(0xFFFFF9E6), Color(0xFFB77900), "CHỜ XỬ LÝ")
        AppConstants.BOOKING_STATUS_IN_SERVICE -> Triple(Color(0xFFE6F0FF), Color(0xFF0056B3), "ĐANG LÀM")
        AppConstants.BOOKING_STATUS_COMPLETED -> Triple(Color(0xFFF0F0F0), Color(0xFF555555), "HOÀN THÀNH")
        AppConstants.BOOKING_STATUS_CANCELLED -> Triple(Color(0xFFFDE8E8), Color(0xFFC81E1E), "ĐÃ HỦY")
        else -> Triple(Color(0xFFF0F0F0), Color(0xFF555555), status)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
private fun FilterTabButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) GrabGreenLight else GrabSurface)
            .border(
                width = 1.dp,
                color = if (isSelected) GrabGreen else GrabBorder,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) GrabGreenDark else GrabTextSecondary
        )
    }
}
