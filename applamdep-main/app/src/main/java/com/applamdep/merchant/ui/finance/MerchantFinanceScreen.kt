package com.applamdep.merchant.ui.finance

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.applamdep.customer.core.base.UiState
import com.applamdep.customer.core.di.ViewModelFactory
import com.applamdep.customer.core.util.CurrencyFormatter
import com.applamdep.merchant.domain.model.MerchantEscrowItem
import com.applamdep.customer.ui.components.GrabCard
import com.applamdep.customer.ui.components.GrabEmptyState
import com.applamdep.customer.ui.components.GrabLoadingState
import com.applamdep.customer.ui.theme.*

/**
 * THEO DÕI ĐẶT CỌC & BẢO LÃNH TÀI CHÍNH (MERCHANT REQUIREMENT #9)
 * - Quản lý trạng thái giao dịch đặt cọc liên quan đến booking
 * - Minh bạch giữa tiền đang giữ (HELD), tiền giải ngân (RELEASED) và tiền hoàn (REFUNDED)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MerchantFinanceScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MerchantFinanceViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadFinance()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = GrabBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Theo dõi Tiền cọc & Giải ngân",
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
        when (val state = uiState) {
            is UiState.Loading -> {
                GrabLoadingState(modifier = Modifier.padding(innerPadding))
            }
            is UiState.Error -> {
                GrabEmptyState(
                    title = "Lỗi tải dữ liệu",
                    subtitle = state.message,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            is UiState.Success -> {
                val finance = state.data
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 3 Khối chỉ số tài chính
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FinanceStatBox(
                            title = "Sàn giữ (HELD)",
                            amount = finance.heldEscrowBalance,
                            subtitle = "Chờ hoàn thành dịch vụ",
                            color = Color(0xFF0070BA),
                            modifier = Modifier.weight(1f)
                        )
                        FinanceStatBox(
                            title = "Đã giải ngân",
                            amount = finance.releasedTotalBalance,
                            subtitle = "Đã chuyển vào ví tiệm",
                            color = GrabGreenDark,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    FinanceStatBox(
                        title = "Tổng cọc đã hoàn về ví khách (REFUNDED)",
                        amount = finance.refundedTotalBalance,
                        subtitle = "Khách hủy lịch hợp lệ trước thời hạn 2 tiếng theo quy định chính sách sàn",
                        color = GrabStatusError,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Chi tiết các khoản cọc booking",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = GrabTextPrimary
                    )

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(finance.bookingTransactions, key = { it.id }) { item ->
                            EscrowTransactionItemCard(item = item)
                        }
                    }
                }
            }
            else -> Unit
        }
    }
}

@Composable
private fun FinanceStatBox(
    title: String,
    amount: Long,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(GrabSurface)
            .padding(14.dp)
    ) {
        Column {
            Text(
                text = title,
                fontSize = 12.sp,
                color = GrabTextSecondary,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = CurrencyFormatter.formatVnd(amount),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = GrabTextHint,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
private fun EscrowTransactionItemCard(item: MerchantEscrowItem) {
    GrabCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${item.customerName} · ${item.serviceName}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = GrabTextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Mã đơn: ${item.bookingId}  ·  ${item.time}",
                    fontSize = 11.sp,
                    color = GrabTextHint
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = CurrencyFormatter.formatVnd(item.depositAmount),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (item.status) {
                        "RELEASED_TO_SHOP" -> GrabGreenDark
                        "HELD" -> Color(0xFF0070BA)
                        else -> GrabStatusError
                    }
                )
                Spacer(modifier = Modifier.height(2.dp))
                EscrowStatusBadge(status = item.status)
            }
        }
    }
}

@Composable
private fun EscrowStatusBadge(status: String) {
    val (bgColor, textColor, label) = when (status) {
        "RELEASED_TO_SHOP" -> Triple(GrabGreenLight, GrabGreenDark, "ĐÃ GIẢI NGÂN")
        "HELD" -> Triple(Color(0xFFE6F0FF), Color(0xFF0070BA), "SÀN TẠM GIỮ")
        "REFUNDED" -> Triple(Color(0xFFFDE8E8), Color(0xFFC81E1E), "ĐÃ HOÀN CỌC")
        else -> Triple(Color(0xFFF0F0F0), Color(0xFF555555), status)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bgColor)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = textColor)
    }
}
