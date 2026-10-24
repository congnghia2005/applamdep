package com.applamdep.merchant.ui.profile

import androidx.lifecycle.viewModelScope
import com.applamdep.customer.core.base.BaseViewModel
import com.applamdep.customer.core.base.UiState
import com.applamdep.merchant.domain.model.MerchantProfile
import com.applamdep.merchant.domain.repository.MerchantRepository
import kotlinx.coroutines.launch

/**
 * 2. TÍNH CHẤT OOP: KẾ THỪA
 * 4. TÍNH CHẤT OOP: TRỪU TƯỢNG
 * ViewModel quản lý đăng ký cơ sở và cập nhật hồ sơ đối tác (Merchant Requirement #1 & #2)
 */
class MerchantProfileViewModel(
    private val merchantRepository: MerchantRepository
) : BaseViewModel<MerchantProfile>() {

    fun loadProfile(ownerId: String = "owner_linh_01") {
        launchWithLoading {
            merchantRepository.getMerchantProfile(ownerId).collect { prof ->
                if (prof != null) {
                    updateState(UiState.Success(prof))
                } else {
                    updateState(UiState.Error("Chưa có hồ sơ đối tác."))
                }
            }
        }
    }

    fun updateProfile(profile: MerchantProfile, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = merchantRepository.updateProfile(profile)
            if (result.isSuccess) {
                onSuccess()
            } else {
                onError("Không thể lưu thay đổi: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun registerNewShop(profile: MerchantProfile, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = merchantRepository.registerShop(profile)
            if (result.isSuccess) {
                onSuccess(result.getOrThrow())
            } else {
                onError("Đăng ký cơ sở thất bại: ${result.exceptionOrNull()?.message}")
            }
        }
    }
}
