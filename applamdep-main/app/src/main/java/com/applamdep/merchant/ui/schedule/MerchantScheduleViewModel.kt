package com.applamdep.merchant.ui.schedule

import androidx.lifecycle.viewModelScope
import com.applamdep.customer.core.base.BaseViewModel
import com.applamdep.customer.core.base.UiState
import com.applamdep.customer.domain.model.ShopSlot
import com.applamdep.merchant.domain.model.MerchantSchedule
import com.applamdep.merchant.domain.repository.MerchantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MerchantScheduleData(
    val schedule: MerchantSchedule,
    val slots: List<ShopSlot>
)

/**
 * 2. TÍNH CHẤT OOP: KẾ THỪA
 * 4. TÍNH CHẤT OOP: TRỪU TƯỢNG
 * ViewModel quản lý lịch làm việc và khung giờ trống (Merchant Requirement #4 & #5)
 */
class MerchantScheduleViewModel(
    private val merchantRepository: MerchantRepository
) : BaseViewModel<MerchantScheduleData>() {

    private val _selectedDate = MutableStateFlow("2026-10-23")
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    fun loadSchedule(shopId: String = "shop_lamour") {
        launchWithLoading {
            merchantRepository.getSchedule(shopId).collect { sched ->
                merchantRepository.getSlots(shopId, _selectedDate.value).collect { slots ->
                    updateState(UiState.Success(MerchantScheduleData(sched, slots)))
                }
            }
        }
    }

    fun setSelectedDate(date: String, shopId: String = "shop_lamour") {
        _selectedDate.value = date
        loadSchedule(shopId)
    }

    fun updateSchedule(schedule: MerchantSchedule, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = merchantRepository.updateSchedule(schedule)
            if (result.isSuccess) {
                onSuccess()
            } else {
                onError("Không thể lưu cấu hình ca làm: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun toggleSlotAvailability(slotId: String, isAvailable: Boolean, onSuccess: () -> Unit) {
        viewModelScope.launch {
            merchantRepository.toggleSlotAvailability(slotId, isAvailable)
            onSuccess()
        }
    }

    fun updateSlotCapacity(slot: ShopSlot, newCapacity: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val updated = slot.copy(maxCapacity = newCapacity)
            merchantRepository.updateSlot(updated)
            onSuccess()
        }
    }
}
