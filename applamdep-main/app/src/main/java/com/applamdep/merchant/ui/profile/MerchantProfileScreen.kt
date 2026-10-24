package com.applamdep.merchant.ui.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.applamdep.customer.ui.components.GrabButton
import com.applamdep.customer.ui.components.GrabCard
import com.applamdep.customer.ui.components.GrabLoadingState
import com.applamdep.customer.ui.theme.*

/**
 * QUẢN LÝ HỒ SƠ CƠ SỞ & ĐĂNG KÝ XÉT DUYỆT (MERCHANT REQUIREMENT #1 & #2)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MerchantProfileScreen(
    onBack: () -> Unit,
    onNavigateToRegistration: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MerchantProfileViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = GrabBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Hồ sơ Cửa hàng & Xét duyệt",
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
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = state.message, color = GrabStatusError)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = onNavigateToRegistration) {
                            Text("Đăng ký hồ sơ cơ sở mới")
                        }
                    }
                }
            }
            is UiState.Success -> {
                val profile = state.data
                var shopName by remember { mutableStateOf(profile.shopName) }
                var ownerName by remember { mutableStateOf(profile.ownerName) }
                var phone by remember { mutableStateOf(profile.phone) }
                var email by remember { mutableStateOf(profile.email) }
                var address by remember { mutableStateOf(profile.address) }
                var description by remember { mutableStateOf(profile.description) }
                var openingHours by remember { mutableStateOf(profile.openingHours) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Trạng thái hồ sơ
                    GrabCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(if (profile.isApproved) GrabGreenLight else Color(0xFFFFF9E6)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (profile.isApproved) Icons.Default.Verified else Icons.Default.HourglassTop,
                                    contentDescription = null,
                                    tint = if (profile.isApproved) GrabGreenDark else Color(0xFFB77900)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (profile.isApproved) "Hồ sơ đã được phê duyệt" else "Đang chờ Admin xét duyệt",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (profile.isApproved) GrabGreenDark else Color(0xFFB77900)
                                )
                                Text(
                                    text = "Gói thành viên: ${profile.membershipPlan} · Đăng ký ngày 23/10/2026",
                                    fontSize = 12.sp,
                                    color = GrabTextSecondary
                                )
                            }
                        }
                    }

                    // Form chỉnh sửa hồ sơ
                    GrabCard {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Thông tin cơ sở làm đẹp",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = GrabTextPrimary
                            )

                            OutlinedTextField(
                                value = shopName,
                                onValueChange = { shopName = it },
                                label = { Text("Tên cơ sở / Salon") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = ownerName,
                                    onValueChange = { ownerName = it },
                                    label = { Text("Người đại diện") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = phone,
                                    onValueChange = { phone = it },
                                    label = { Text("Số điện thoại") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email liên hệ") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = address,
                                onValueChange = { address = it },
                                label = { Text("Địa chỉ chi tiết cơ sở") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = openingHours,
                                onValueChange = { openingHours = it },
                                label = { Text("Khung giờ đón khách") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = description,
                                onValueChange = { description = it },
                                label = { Text("Mô tả giới thiệu salon") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                            )
                        }
                    }

                    // Nút lưu
                    GrabButton(
                        text = "Lưu cập nhật hồ sơ",
                        onClick = {
                            val updated = profile.copy(
                                shopName = shopName.trim(),
                                ownerName = ownerName.trim(),
                                phone = phone.trim(),
                                email = email.trim(),
                                address = address.trim(),
                                description = description.trim(),
                                openingHours = openingHours.trim()
                            )
                            viewModel.updateProfile(
                                profile = updated,
                                onSuccess = {
                                    Toast.makeText(context, "Đã lưu thông tin hồ sơ salon!", Toast.LENGTH_SHORT).show()
                                },
                                onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Nút nộp hồ sơ tiệm mới khác
                    OutlinedButton(
                        onClick = onNavigateToRegistration,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.AddBusiness, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Gửi hồ sơ đăng ký cơ sở mới khác")
                    }
                }
            }
            else -> Unit
        }
    }
}
