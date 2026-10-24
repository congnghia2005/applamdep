package com.applamdep.merchant.ui.dashboard

import androidx.lifecycle.viewModelScope
import com.applamdep.customer.core.base.BaseViewModel
import com.applamdep.customer.core.base.UiState
import com.applamdep.merchant.domain.model.MerchantProfile
import com.applamdep.merchant.domain.model.MerchantStats
import com.applamdep.merchant.domain.repository.MerchantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MerchantDashboardData(
    val profile: MerchantProfile,
    val stats: MerchantStats
)

/**
 * 2. TÍNH CHẤT OOP: KẾ THỪA (Inheritance) từ BaseViewModel
 * 4. TÍNH CHẤT OOP: TRỪU TƯỢNG (Abstraction)
 * ViewModel cho Dashboard đối tác cửa hàng
 */
class MerchantDashboardViewModel(
    private val merchantRepository: MerchantRepository
) : BaseViewModel<MerchantDashboardData>() {

    private val _autoCompletedCount = MutableStateFlow(0)
    val autoCompletedCount: StateFlow<Int> = _autoCompletedCount.asStateFlow()

    fun loadDashboard(shopId: String = "shop_lamour") {
        viewModelScope.launch {
            // Tự động kiểm tra và hoàn thành các ca làm đã kết thúc quá 30 phút theo Requirement 7
            val autoResult = merchantRepository.checkAndAutoCompleteBookings(shopId)
            autoResult.onSuccess { count ->
                _autoCompletedCount.value = count
            }

            launchWithLoading {
                merchantRepository.getMerchantProfile("owner_linh_01").collect { prof ->
                    if (prof != null) {
                        merchantRepository.getMerchantStats(shopId).collect { stats ->
                            updateState(UiState.Success(MerchantDashboardData(prof, stats)))
                        }
                    } else {
                        updateState(UiState.Error("Chưa tìm thấy hồ sơ cửa hàng."))
                    }
                }
            }
        }
    }
}
