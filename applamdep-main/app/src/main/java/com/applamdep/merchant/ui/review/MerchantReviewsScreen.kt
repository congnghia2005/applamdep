package com.applamdep.merchant.ui.review

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.applamdep.customer.core.base.UiState
import com.applamdep.customer.core.di.ViewModelFactory
import com.applamdep.customer.domain.model.Review
import com.applamdep.customer.ui.components.GrabCard
import com.applamdep.customer.ui.components.GrabEmptyState
import com.applamdep.customer.ui.components.GrabLoadingState
import com.applamdep.customer.ui.theme.*

/**
 * QUẢN LÝ ĐÁNH GIÁ, PHẢN HỒI VÀ TỐ CÁO (MERCHANT REQUIREMENT #8)
 * - Xem đánh giá từ khách hàng
 * - Viết phản hồi chính thức từ cửa hàng
 * - Báo cáo / Tố cáo đánh giá vi phạm gửi lên Admin Web Portal
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MerchantReviewsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MerchantReviewViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var replyingReview by remember { mutableStateOf<Review?>(null) }
    var reportingReview by remember { mutableStateOf<Review?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadReviews()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = GrabBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Đánh giá & Phản hồi Khách hàng",
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
                    title = "Lỗi tải đánh giá",
                    subtitle = state.message,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            is UiState.Success -> {
                val reviews = state.data
                if (reviews.isEmpty()) {
                    GrabEmptyState(
                        icon = Icons.Default.RateReview,
                        title = "Chưa có đánh giá nào",
                        subtitle = "Chưa có khách hàng nào gửi nhận xét cho salon của bạn.",
                        modifier = Modifier.padding(innerPadding)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(reviews, key = { it.id }) { review ->
                            MerchantReviewItemCard(
                                review = review,
                                onReplyClick = { replyingReview = review },
                                onReportClick = { reportingReview = review }
                            )
                        }
                    }
                }
            }
            else -> Unit
        }

        // Dialog Phản hồi đánh giá
        replyingReview?.let { rev ->
            var replyText by remember { mutableStateOf(rev.replyComment ?: "") }
            AlertDialog(
                onDismissRequest = { replyingReview = null },
                title = { Text(text = "Phản hồi khách hàng", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text(
                            text = "Đánh giá từ ${rev.customerName}: \"${rev.comment}\"",
                            fontSize = 13.sp,
                            color = GrabTextSecondary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = replyText,
                            onValueChange = { replyText = it },
                            placeholder = { Text("Nhập lời cảm ơn hoặc giải đáp thắc mắc của salon...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.replyReview(
                                reviewId = rev.id,
                                replyText = replyText,
                                onSuccess = {
                                    Toast.makeText(context, "Đã gửi phản hồi đến khách hàng!", Toast.LENGTH_SHORT).show()
                                    replyingReview = null
                                },
                                onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GrabGreen),
                        enabled = replyText.isNotBlank()
                    ) {
                        Text("Gửi phản hồi")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { replyingReview = null }) {
                        Text("Hủy", color = GrabTextSecondary)
                    }
                }
            )
        }

        // Dialog Tố cáo đánh giá vi phạm (gửi Admin)
        reportingReview?.let { rev ->
            var reason by remember { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { reportingReview = null },
                title = { Text(text = "Tố cáo đánh giá vi phạm", fontWeight = FontWeight.Bold, color = GrabStatusError) },
                text = {
                    Column {
                        Text(
                            text = "Yêu cầu gỡ bỏ đánh giá của ${rev.customerName} sẽ được chuyển tiếp lên Admin xét duyệt.",
                            fontSize = 13.sp,
                            color = GrabTextSecondary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = reason,
                            onValueChange = { reason = it },
                            placeholder = { Text("Nêu rõ lý do (Đánh giá sai sự thật, xúc phạm, spam đối thủ...)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.reportReview(
                                reviewId = rev.id,
                                reason = reason,
                                onSuccess = {
                                    Toast.makeText(context, "Đã gửi báo cáo vi phạm lên Admin Portal xét duyệt!", Toast.LENGTH_LONG).show()
                                    reportingReview = null
                                },
                                onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GrabStatusError),
                        enabled = reason.isNotBlank()
                    ) {
                        Text("Gửi tố cáo")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { reportingReview = null }) {
                        Text("Hủy", color = GrabTextSecondary)
                    }
                }
            )
        }
    }
}

@Composable
private fun MerchantReviewItemCard(
    review: Review,
    onReplyClick: () -> Unit,
    onReportClick: () -> Unit
) {
    GrabCard {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(GrabGreenLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = review.customerName.take(1).uppercase(),
                            fontWeight = FontWeight.Bold,
                            color = GrabGreenDark
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = review.customerName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = GrabTextPrimary
                        )
                        Text(
                            text = "Mã booking: ${review.bookingId}",
                            fontSize = 11.sp,
                            color = GrabTextHint
                        )
                    }
                }

                if (review.status == "REMOVE_REQUESTED") {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFFF0F0))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "ĐANG TỐ CÁO",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GrabStatusError
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Hiển thị sao
            Row(verticalAlignment = Alignment.CenterVertically) {
                for (i in 1..5) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = if (i <= review.rating) Color(0xFFFFB800) else GrabBorder,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${review.rating}/5 sao",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFB800)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = review.comment,
                fontSize = 13.sp,
                color = GrabTextPrimary,
                lineHeight = 18.sp
            )

            // Tags
            if (review.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    review.tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(GrabGreenLight)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(text = tag, fontSize = 11.sp, color = GrabGreenDark)
                        }
                    }
                }
            }

            // Ảnh đính kèm
            if (review.images.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(review.images) { img ->
                        AsyncImage(
                            model = img,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
            }

            // Phản hồi của tiệm nếu đã có
            if (!review.replyComment.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF7F8F9))
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "Phản hồi chính thức của Salon:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GrabGreenDark
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = review.replyComment,
                            fontSize = 12.sp,
                            color = GrabTextSecondary
                        )
                    }
                }
            }

            // Hàng nút: Phản hồi & Tố cáo
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = GrabBorder)
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onReportClick,
                    colors = ButtonDefaults.textButtonColors(contentColor = GrabStatusError)
                ) {
                    Icon(imageVector = Icons.Default.ReportProblem, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Tố cáo vi phạm", fontSize = 12.sp)
                }

                Button(
                    onClick = onReplyClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GrabGreen),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp)
                ) {
                    Icon(imageVector = Icons.Default.Reply, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (review.replyComment.isNullOrBlank()) "Phản hồi" else "Chỉnh sửa phản hồi",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
