package com.applamdep.customer.ui.shop

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.applamdep.customer.core.base.UiState
import com.applamdep.customer.core.di.ViewModelFactory
import com.applamdep.customer.core.util.CurrencyFormatter
import com.applamdep.customer.domain.model.ServiceItem
import com.applamdep.customer.ui.components.*
import com.applamdep.customer.ui.theme.*

/**
 * MÀN HÌNH CHI TIẾT CỬA HÀNG (NHÓM 2 - P0)
 * - RÀNG BUỘC ĐẶC BIỆT: Địa chỉ quán hiển thị rõ ràng trên ĐẦU TRANG
 * - Pager ảnh lướt ngang
 * - Bảng giá dịch vụ kèm thời lượng
 * - Chính sách cọc đầy đủ (5 mệnh giá)
 * - Đánh giá nhận xét của khách
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopDetailScreen(
    shopId: String,
    onBack: () -> Unit,
    onSelectServiceForBooking: (shopId: String, serviceId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ShopDetailViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(shopId) {
        viewModel.loadShopDetail(shopId)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = GrabBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Chi tiết cửa hàng",
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
            when (val state = uiState) {
                is UiState.Loading -> {
                    GrabLoadingState()
                }
                is UiState.Error -> {
                    GrabEmptyState(
                        message = state.message,
                        onRetry = { viewModel.loadShopDetail(shopId) }
                    )
                }
                is UiState.Success -> {
                    val detail = state.data
                    val shop = detail.shop
                    val services = detail.services
                    val reviews = detail.reviews

                    Column(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(bottom = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // 1. RÀNG BUỘC BẮT BUỘC: ĐỊA CHỈ QUÁN NẰM TRÊN ĐẦU TRANG
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(GrabGreenLight)
                                        .padding(horizontal = 16.dp, vertical = 12.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = "Location",
                                            tint = GrabGreenDark,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = "Địa chỉ cơ sở:",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = GrabGreenDark
                                            )
                                            Text(
                                                text = shop.address,
                                                fontSize = 13.sp,
                                                color = GrabTextPrimary,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }

                            // 2. Pager ảnh quán
                            item {
                                val images = shop.images.ifEmpty {
                                    listOf("https://images.unsplash.com/photo-1560750588-73207b1ef5b8?w=800")
                                }
                                val pagerState = rememberPagerState(pageCount = { images.size })

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                ) {
                                    HorizontalPager(
                                        state = pagerState,
                                        modifier = Modifier.fillMaxSize()
                                    ) { page ->
                                        AsyncImage(
                                            model = images[page],
                                            contentDescription = "Shop Image $page",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    // Chỉ số trang ảnh (vd: 1/3)
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(12.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color.Black.copy(alpha = 0.6f))
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "${pagerState.currentPage + 1}/${images.size}",
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            // 3. Thông tin hồ sơ quán
                            item {
                                GrabCard(modifier = Modifier.padding(horizontal = 16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = shop.name,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GrabTextPrimary,
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (shop.isVipActive) {
                                            GrabVipBadge()
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Điểm đánh giá & Giờ mở cửa
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = "Rating",
                                                tint = GrabAccentGold,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "${shop.rating}",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = GrabTextPrimary
                                            )
                                            Text(
                                                text = " (${shop.reviewCount} đánh giá)",
                                                fontSize = 13.sp,
                                                color = GrabTextSecondary
                                            )
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Schedule,
                                                contentDescription = "Hours",
                                                tint = GrabTextSecondary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = shop.openingHours,
                                                fontSize = 13.sp,
                                                color = GrabTextSecondary
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Số điện thoại liên hệ
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Phone,
                                            contentDescription = "Phone",
                                            tint = GrabGreen,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Hotline: ${shop.phone}",
                                            fontSize = 13.sp,
                                            color = GrabTextPrimary,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }

                                    // Mạng xã hội / Kênh liên hệ (Đồng bộ Firebase RTDB)
                                    val hasSocial = !shop.facebook.isNullOrBlank() || !shop.instagram.isNullOrBlank() || !shop.tiktok.isNullOrBlank() || !shop.website.isNullOrBlank()
                                    if (hasSocial) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = "Kênh:",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = GrabTextSecondary
                                            )
                                            shop.facebook?.takeIf { it.isNotBlank() }?.let { fbUrl ->
                                                SocialBadge(
                                                    label = "Facebook",
                                                    bgColor = Color(0xFF1877F2).copy(alpha = 0.12f),
                                                    textColor = Color(0xFF1877F2),
                                                    onClick = {
                                                        try {
                                                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(fbUrl)))
                                                        } catch (_: Exception) {}
                                                    }
                                                )
                                            }
                                            shop.instagram?.takeIf { it.isNotBlank() }?.let { igUrl ->
                                                SocialBadge(
                                                    label = "Instagram",
                                                    bgColor = Color(0xFFE1306C).copy(alpha = 0.12f),
                                                    textColor = Color(0xFFE1306C),
                                                    onClick = {
                                                        try {
                                                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(igUrl)))
                                                        } catch (_: Exception) {}
                                                    }
                                                )
                                            }
                                            shop.tiktok?.takeIf { it.isNotBlank() }?.let { ttUrl ->
                                                SocialBadge(
                                                    label = "TikTok",
                                                    bgColor = Color(0xFF000000).copy(alpha = 0.08f),
                                                    textColor = Color(0xFF111111),
                                                    onClick = {
                                                        try {
                                                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ttUrl)))
                                                        } catch (_: Exception) {}
                                                    }
                                                )
                                            }
                                            shop.website?.takeIf { it.isNotBlank() }?.let { webUrl ->
                                                SocialBadge(
                                                    label = "Website",
                                                    bgColor = Color(0xFF0088CC).copy(alpha = 0.12f),
                                                    textColor = Color(0xFF0088CC),
                                                    onClick = {
                                                        try {
                                                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(webUrl)))
                                                        } catch (_: Exception) {}
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))
                                    HorizontalDivider(color = GrabBorder)
                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Mô tả quán
                                    Text(
                                        text = "Giới thiệu:",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GrabTextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = shop.description,
                                        fontSize = 13.sp,
                                        color = GrabTextSecondary,
                                        lineHeight = 18.sp
                                    )

                                    if (shop.amenities.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = "Tiện ích:",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GrabTextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            items(shop.amenities) { amenity ->
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(GrabBackground)
                                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                                ) {
                                                    Text(
                                                        text = amenity,
                                                        fontSize = 11.sp,
                                                        color = GrabTextSecondary
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // 4. CHÍNH SÁCH ĐẶT CỌC CỦA SHOP
                            item {
                                GrabCard(modifier = Modifier.padding(horizontal = 16.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AccountBalanceWallet,
                                            contentDescription = "Deposit Policy",
                                            tint = if (shop.requireDeposit) GrabStatusError else GrabGreen,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = "Chính sách đặt cọc của quán",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GrabTextPrimary
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (shop.requireDeposit) {
                                        Text(
                                            text = "Quán yêu cầu đặt cọc trước để giữ chỗ khung giờ hẹn. Sàn sẽ tạm giữ tiền cọc này cho đến khi bạn tới làm dịch vụ.",
                                            fontSize = 13.sp,
                                            color = GrabTextSecondary
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Các mệnh giá cọc do tiệm áp dụng:",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = GrabTextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            items(shop.depositOptions) { tier ->
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(Color(0xFFFFF0F0))
                                                        .border(1.dp, Color(0xFFFFD1D1), RoundedCornerShape(8.dp))
                                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                                ) {
                                                    Text(
                                                        text = CurrencyFormatter.formatVnd(tier),
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = GrabStatusError
                                                    )
                                                }
                                            }
                                        }
                                    } else {
                                        Text(
                                            text = "Quán này KHÔNG yêu cầu đặt cọc. Bạn có thể tự do đặt lịch và thanh toán toàn bộ chi phí trực tiếp tại quán sau khi làm xong dịch vụ.",
                                            fontSize = 13.sp,
                                            color = GrabGreenDark
                                        )
                                    }
                                }
                            }

                            // 5. BẢNG GIÁ DỊCH VỤ
                            item {
                                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                    Text(
                                        text = "Bảng giá dịch vụ",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GrabTextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }

                            items(
                                items = services,
                                key = { it.id }
                            ) { service ->
                                ServicePriceCardItem(
                                    service = service,
                                    onSelect = {
                                        onSelectServiceForBooking(shop.id, service.id)
                                    },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }

                            // 6. ĐÁNH GIÁ CỦA KHÁCH HÀNG
                            item {
                                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Đánh giá từ khách hàng (${reviews.size})",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GrabTextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))

                                    if (reviews.isEmpty()) {
                                        Text(
                                            text = "Chưa có nhận xét nào.",
                                            fontSize = 13.sp,
                                            color = GrabTextSecondary
                                        )
                                    } else {
                                        reviews.forEach { review ->
                                            GrabCard(modifier = Modifier.padding(bottom = 8.dp)) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = review.customerName,
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = GrabTextPrimary
                                                    )
                                                    Row {
                                                        repeat(review.rating) {
                                                            Icon(
                                                                imageVector = Icons.Default.Star,
                                                                contentDescription = null,
                                                                tint = GrabAccentGold,
                                                                modifier = Modifier.size(14.dp)
                                                            )
                                                        }
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = review.comment,
                                                    fontSize = 13.sp,
                                                    color = GrabTextSecondary
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // 7. Thanh nút Sticky Bottom Bar cố định ở dưới
                        Surface(
                            color = GrabSurface,
                            shadowElevation = 8.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                GrabPrimaryButton(
                                    text = "Chọn lịch ngay",
                                    onClick = {
                                        val firstService = services.firstOrNull()
                                        if (firstService != null) {
                                            onSelectServiceForBooking(shop.id, firstService.id)
                                        }
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

/**
 * Card hiển thị từng dịch vụ trong bảng giá
 */
@Composable
private fun ServicePriceCardItem(
    service: ServiceItem,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    GrabCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = GrabTextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = service.description,
                    fontSize = 12.sp,
                    color = GrabTextSecondary,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = CurrencyFormatter.formatVnd(service.price),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GrabGreenDark
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "•  ${service.durationMinutes} phút",
                        fontSize = 12.sp,
                        color = GrabTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Nút Chọn lịch
            Button(
                onClick = onSelect,
                colors = ButtonDefaults.buttonColors(containerColor = GrabGreenLight),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Chọn",
                    color = GrabGreenDark,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SocialBadge(
    label: String,
    bgColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
        )
    }
}

