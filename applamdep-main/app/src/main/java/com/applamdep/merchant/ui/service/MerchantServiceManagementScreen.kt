package com.applamdep.merchant.ui.service

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.applamdep.customer.core.base.UiState
import com.applamdep.customer.core.di.ViewModelFactory
import com.applamdep.customer.core.util.CurrencyFormatter
import com.applamdep.customer.domain.model.ServiceItem
import com.applamdep.customer.ui.components.GrabButton
import com.applamdep.customer.ui.components.GrabCard
import com.applamdep.customer.ui.components.GrabEmptyState
import com.applamdep.customer.ui.components.GrabLoadingState
import com.applamdep.customer.ui.theme.*

/**
 * QUẢN LÝ DỊCH VỤ CỦA CỬA HÀNG (MERCHANT REQUIREMENT #3)
 * - Thêm, sửa, xóa dịch vụ
 * - Thiết lập giá và thời lượng phục vụ
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MerchantServiceManagementScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MerchantServiceViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var editingService by remember { mutableStateOf<ServiceItem?>(null) }
    var isAddingNew by remember { mutableStateOf(false) }
    var serviceToDelete by remember { mutableStateOf<ServiceItem?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadServices()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = GrabBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Quản lý Dịch vụ Salon",
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isAddingNew = true },
                containerColor = GrabGreen,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Thêm dịch vụ")
            }
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is UiState.Loading -> {
                GrabLoadingState(modifier = Modifier.padding(innerPadding))
            }
            is UiState.Error -> {
                GrabEmptyState(
                    title = "Lỗi tải dịch vụ",
                    subtitle = state.message,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            is UiState.Success -> {
                val services = state.data
                if (services.isEmpty()) {
                    GrabEmptyState(
                        icon = Icons.Default.DesignServices,
                        title = "Chưa có dịch vụ nào",
                        subtitle = "Bấm dấu + bên dưới để thêm dịch vụ làm đẹp đầu tiên cho salon của bạn.",
                        modifier = Modifier.padding(innerPadding)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(services, key = { it.id }) { service ->
                            ServiceManageCard(
                                service = service,
                                onEdit = { editingService = service },
                                onDelete = { serviceToDelete = service }
                            )
                        }
                    }
                }
            }
            else -> Unit
        }

        // Dialog Thêm hoặc Sửa dịch vụ
        if (isAddingNew || editingService != null) {
            val initial = editingService ?: ServiceItem(
                id = "",
                shopId = "shop_lamour",
                name = "",
                description = "",
                price = 150000L,
                durationMinutes = 45,
                category = "Nail Art & Care",
                imageUrl = "https://images.unsplash.com/photo-1604654894610-df63bc536371?w=500&auto=format&fit=crop"
            )

            ServiceEditDialog(
                initialService = initial,
                isNew = isAddingNew,
                onDismiss = {
                    isAddingNew = false
                    editingService = null
                },
                onSave = { savedItem ->
                    if (isAddingNew) {
                        viewModel.addService(
                            service = savedItem,
                            onSuccess = {
                                Toast.makeText(context, "Thêm dịch vụ mới thành công!", Toast.LENGTH_SHORT).show()
                                isAddingNew = false
                            },
                            onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                        )
                    } else {
                        viewModel.updateService(
                            service = savedItem,
                            onSuccess = {
                                Toast.makeText(context, "Cập nhật dịch vụ thành công!", Toast.LENGTH_SHORT).show()
                                editingService = null
                            },
                            onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                        )
                    }
                }
            )
        }

        // Dialog xác nhận xóa
        serviceToDelete?.let { s ->
            AlertDialog(
                onDismissRequest = { serviceToDelete = null },
                title = { Text(text = "Xóa dịch vụ?", fontWeight = FontWeight.Bold) },
                text = { Text(text = "Bạn có chắc muốn xóa dịch vụ '${s.name}' khỏi bảng giá không?") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteService(
                                serviceId = s.id,
                                onSuccess = {
                                    Toast.makeText(context, "Đã xóa dịch vụ.", Toast.LENGTH_SHORT).show()
                                    serviceToDelete = null
                                },
                                onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GrabStatusError)
                    ) {
                        Text(text = "Xóa")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { serviceToDelete = null }) {
                        Text(text = "Hủy", color = GrabTextSecondary)
                    }
                }
            )
        }
    }
}

@Composable
private fun ServiceManageCard(
    service: ServiceItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    GrabCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = service.imageUrl ?: "https://images.unsplash.com/photo-1604654894610-df63bc536371?w=500&auto=format&fit=crop",
                contentDescription = service.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = GrabTextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${CurrencyFormatter.formatVnd(service.price)}  ·  ${service.durationMinutes} phút",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = GrabGreenDark
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Nhóm: ${service.category}",
                    fontSize = 11.sp,
                    color = GrabTextSecondary
                )
            }

            // Nút sửa & xóa
            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = GrabGreenDark,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = GrabStatusError,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ServiceEditDialog(
    initialService: ServiceItem,
    isNew: Boolean,
    onDismiss: () -> Unit,
    onSave: (ServiceItem) -> Unit
) {
    var name by remember { mutableStateOf(initialService.name) }
    var priceText by remember { mutableStateOf(initialService.price.toString()) }
    var durationText by remember { mutableStateOf(initialService.durationMinutes.toString()) }
    var category by remember { mutableStateOf(initialService.category) }
    var description by remember { mutableStateOf(initialService.description) }

    val categories = listOf("Nail Art & Care", "Lash & Brow", "Spa & Massage", "Hair Salon", "Skincare")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isNew) "Thêm dịch vụ mới" else "Chỉnh sửa dịch vụ",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = GrabTextPrimary
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên dịch vụ") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it },
                        label = { Text("Giá niêm yết (VNĐ)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1.2f)
                    )

                    OutlinedTextField(
                        value = durationText,
                        onValueChange = { durationText = it },
                        label = { Text("Thời lượng (phút)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(0.8f)
                    )
                }

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Danh mục dịch vụ") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Mô tả chi tiết") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val p = priceText.toLongOrNull() ?: 100000L
                    val d = durationText.toIntOrNull() ?: 45
                    val updated = initialService.copy(
                        name = name.trim(),
                        price = p,
                        durationMinutes = d,
                        category = category.trim(),
                        description = description.trim()
                    )
                    onSave(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = GrabGreen),
                enabled = name.isNotBlank()
            ) {
                Text(text = if (isNew) "Tạo mới" else "Lưu")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Hủy", color = GrabTextSecondary)
            }
        }
    )
}
