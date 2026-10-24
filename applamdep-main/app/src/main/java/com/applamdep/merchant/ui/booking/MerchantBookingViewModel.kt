package com.applamdep.merchant.ui.booking

import androidx.lifecycle.viewModelScope
import com.applamdep.customer.core.base.BaseViewModel
import com.applamdep.customer.core.base.UiState
import com.applamdep.customer.core.constants.AppConstants
import com.applamdep.customer.domain.model.Booking
import com.applamdep.merchant.domain.repository.MerchantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class MerchantBookingFilter {
    ALL, PENDING, CONFIRMED, IN_SERVICE, COMPLETED, CANCELLED
}

/**
 * 2. TÍNH CHẤT OOP: KẾ THỪA (Inheritance)
 * 4. TÍNH CHẤT OOP: TRỪU TƯỢNG (Abstraction)
 * ViewModel quản lý danh sách lịch hẹn của cửa hàng và trạng thái dịch vụ (Merchant Requirement #6 & #7)
 */
class MerchantBookingViewModel(
    private val merchantRepository: MerchantRepository
) : BaseViewModel<List<Booking>>() {

    private val _currentFilter = MutableStateFlow(MerchantBookingFilter.ALL)
    val currentFilter: StateFlow<MerchantBookingFilter> = _currentFilter.asStateFlow()

    private var rawList: List<Booking> = emptyList()

    fun loadBookings(shopId: String = "shop_lamour") {
        launchWithLoading {
            // Tự động kiểm tra hoàn thành sau 30 phút khi tải
            merchantRepository.checkAndAutoCompleteBookings(shopId)

            merchantRepository.getMerchantBookings(shopId).collect { list ->
                rawList = list
                applyFilter()
            }
        }
    }

    fun setFilter(filter: MerchantBookingFilter) {
        _currentFilter.value = filter
        applyFilter()
    }

    private fun applyFilter() {
        val filtered = when (_currentFilter.value) {
            MerchantBookingFilter.ALL -> rawList
            MerchantBookingFilter.PENDING -> rawList.filter { it.status == AppConstants.BOOKING_STATUS_PENDING }
            MerchantBookingFilter.CONFIRMED -> rawList.filter { it.status == AppConstants.BOOKING_STATUS_CONFIRMED }
            MerchantBookingFilter.IN_SERVICE -> rawList.filter { it.status == AppConstants.BOOKING_STATUS_IN_SERVICE }
            MerchantBookingFilter.COMPLETED -> rawList.filter { it.status == AppConstants.BOOKING_STATUS_COMPLETED }
            MerchantBookingFilter.CANCELLED -> rawList.filter { it.status == AppConstants.BOOKING_STATUS_CANCELLED }
        }
        updateState(UiState.Success(filtered))
    }

    fun updateStatus(
        bookingId: String,
        newStatus: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = merchantRepository.updateBookingStatus(bookingId, newStatus)
            if (result.isSuccess) {
                val msg = when (newStatus) {
                    AppConstants.BOOKING_STATUS_CONFIRMED -> "Đã xác nhận lịch hẹn thành công!"
                    AppConstants.BOOKING_STATUS_IN_SERVICE -> "Đã bắt đầu phục vụ khách hàng."
                    AppConstants.BOOKING_STATUS_COMPLETED -> "Đã hoàn thành dịch vụ! Tiền cọc đã được giải ngân về ví cửa hàng."
                    AppConstants.BOOKING_STATUS_CANCELLED -> "Đã từ chối lịch hẹn và hoàn cọc cho khách."
                    else -> "Cập nhật trạng thái thành công."
                }
                onSuccess(msg)
            } else {
                onError("Lỗi cập nhật: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    /**
     * Kích hoạt kiểm tra tự động hoàn thành dịch vụ (sau 30 phút từ lúc kết thúc)
     */
    fun triggerAutoComplete(shopId: String = "shop_lamour", onResult: (Int) -> Unit) {
        viewModelScope.launch {
            val res = merchantRepository.checkAndAutoCompleteBookings(shopId)
            val count = res.getOrDefault(0)
            onResult(count)
        }
    }
}
