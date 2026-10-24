package com.applamdep.merchant.ui.dashboard

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.applamdep.customer.core.base.UiState
import com.applamdep.customer.core.di.ViewModelFactory
import com.applamdep.customer.core.util.CurrencyFormatter
import com.applamdep.customer.ui.components.GrabCard
import com.applamdep.customer.ui.components.GrabEmptyState
import com.applamdep.customer.ui.components.GrabLoadingState
import com.applamdep.customer.ui.theme.*

/**
 * DASHBOARD CỔNG ĐỐI TÁC CỬA HÀNG (MERCHANT PORTAL - 10 CHỨC NĂNG QUY CHUẨN)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MerchantDashboardScreen(
    onNavigateToBookings: () -> Unit,
    onNavigateToServices: () -> Unit,
    onNavigateToSchedule: () -> Unit,
    onNavigateToReviews: () -> Unit,
    onNavigateToFinance: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MerchantDashboardViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val autoCount by viewModel.autoCompletedCount.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadDashboard()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = GrabBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(GrabGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Storefront,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Merchant Portal",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = GrabTextPrimary
                            )
                            Text(
                                text = "Kênh Quản Trị Cửa Hàng",
                                fontSize = 11.sp,
                                color = GrabTextSecondary
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
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
                    title = "Lỗi tải thông tin",
                    subtitle = state.message,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            is UiState.Success -> {
                val data = state.data
                val profile = data.profile
                val stats = data.stats

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. Header cơ sở đối tác & Huy hiệu gói
                    GrabCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(GrabGreenLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Spa,
                                    contentDescription = null,
                                    tint = GrabGreenDark,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = profile.shopName,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GrabTextPrimary
                                    )
                                    if (profile.isVip) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(Color(0xFFFFB800))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "VIP",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = "Chủ tiệm: ${profile.ownerName} · ${profile.phone}",
                                    fontSize = 12.sp,
                                    color = GrabTextSecondary
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(if (profile.isApproved) GrabGreen else Color(0xFFE5A100))
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (profile.isApproved) "Đã duyệt hoạt động" else "Chờ xét duyệt hồ sơ",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (profile.isApproved) GrabGreenDark else Color(0xFFB77900)
                                    )
                                }
                            }
                        }
                    }

                    // Thông báo tự động hoàn thành (Requirement #7)
                    if (autoCount > 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = GrabGreenDark
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Đã tự động chuyển $autoCount lịch hẹn sang trạng thái HOÀN THÀNH (sau 30p kết thúc) và giải ngân tiền cọc.",
                                    fontSize = 12.sp,
                                    color = GrabGreenDark,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }

                    // 2. Các chỉ số thống kê cơ bản (Requirement #10)
                    Text(
                        text = "Hiệu quả hôm nay",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = GrabTextPrimary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricCard(
                            title = "Lịch hôm nay",
                            value = "${stats.totalBookingsToday}",
                            subtitle = "${stats.pendingBookings} chờ duyệt",
                            icon = Icons.Default.CalendarToday,
                            color = GrabGreenDark,
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            title = "Doanh thu ước tính",
                            value = CurrencyFormatter.formatVnd(stats.estimatedRevenueToday),
                            subtitle = "Tháng: ${CurrencyFormatter.formatVnd(stats.totalRevenueMonth)}",
                            icon = Icons.Default.MonetizationOn,
                            color = Color(0xFF0070BA),
                            modifier = Modifier.weight(1.3f)
                        )
                        MetricCard(
                            title = "Đánh giá",
                            value = "${stats.averageRating} ★",
                            subtitle = "${stats.totalReviews} lượt",
                            icon = Icons.Default.Star,
                            color = Color(0xFFFFB800),
                            modifier = Modifier.weight(0.9f)
                        )
                    }

                    // 3. Lưới 6 phân hệ quản lý đối tác
                    Text(
                        text = "Công cụ quản lý cửa hàng",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = GrabTextPrimary
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ActionTile(
                                title = "Lịch hẹn khách",
                                subtitle = "Xem, duyệt & trạng thái",
                                icon = Icons.Default.EventNote,
                                badge = "${stats.pendingBookings + stats.confirmedBookings}",
                                onClick = onNavigateToBookings,
                                modifier = Modifier.weight(1f)
                            )
                            ActionTile(
                                title = "Dịch vụ & Bảng giá",
                                subtitle = "Thêm, sửa, xóa dịch vụ",
                                icon = Icons.Default.DesignServices,
                                onClick = onNavigateToServices,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ActionTile(
                                title = "Ca làm & Khung giờ",
                                subtitle = "Giờ mở cửa, slot trống",
                                icon = Icons.Default.Schedule,
                                onClick = onNavigateToSchedule,
                                modifier = Modifier.weight(1f)
                            )
                            ActionTile(
                                title = "Đánh giá & Phản hồi",
                                subtitle = "Trả lời khách, tố cáo",
                                icon = Icons.Default.RateReview,
                                onClick = onNavigateToReviews,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ActionTile(
                                title = "Theo dõi Tiền cọc",
                                subtitle = "Bảo lãnh & giải ngân",
                                icon = Icons.Default.AccountBalanceWallet,
                                onClick = onNavigateToFinance,
                                modifier = Modifier.weight(1f)
                            )
                            ActionTile(
                                title = "Hồ sơ & Đăng ký",
                                subtitle = "Thông tin tiệm, cơ sở",
                                icon = Icons.Default.Store,
                                onClick = onNavigateToProfile,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
            else -> Unit
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(GrabSurface)
            .padding(12.dp)
    ) {
        Column {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = GrabTextPrimary
            )
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = GrabTextSecondary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = GrabTextHint
            )
        }
    }
}

@Composable
private fun ActionTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badge: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(GrabSurface)
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(GrabGreenLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = GrabGreenDark,
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (badge != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(GrabStatusError)
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = GrabTextHint,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = GrabTextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = GrabTextSecondary
            )
        }
    }
}
