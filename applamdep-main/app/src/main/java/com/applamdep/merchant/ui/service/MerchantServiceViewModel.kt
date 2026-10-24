package com.applamdep.merchant.ui.service

import androidx.lifecycle.viewModelScope
import com.applamdep.customer.core.base.BaseViewModel
import com.applamdep.customer.core.base.UiState
import com.applamdep.customer.domain.model.ServiceItem
import com.applamdep.merchant.domain.repository.MerchantRepository
import kotlinx.coroutines.launch

/**
 * 2. TÍNH CHẤT OOP: KẾ THỪA
 * 4. TÍNH CHẤT OOP: TRỪU TƯỢNG
 * ViewModel quản lý danh mục dịch vụ (Thêm, Sửa, Xóa, Thiết lập giá & thời lượng - Merchant Requirement #3)
 */
class MerchantServiceViewModel(
    private val merchantRepository: MerchantRepository
) : BaseViewModel<List<ServiceItem>>() {

    fun loadServices(shopId: String = "shop_lamour") {
        launchWithLoading {
            merchantRepository.getServices(shopId).collect { list ->
                updateState(UiState.Success(list))
            }
        }
    }

    fun addService(service: ServiceItem, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = merchantRepository.addService(service)
            if (result.isSuccess) {
                onSuccess()
            } else {
                onError("Không thể thêm dịch vụ: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun updateService(service: ServiceItem, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = merchantRepository.updateService(service)
            if (result.isSuccess) {
                onSuccess()
            } else {
                onError("Không thể cập nhật dịch vụ: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun deleteService(serviceId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = merchantRepository.deleteService(serviceId)
            if (result.isSuccess) {
                onSuccess()
            } else {
                onError("Không thể xóa dịch vụ: ${result.exceptionOrNull()?.message}")
            }
        }
    }
}
