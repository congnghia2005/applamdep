package com.applamdep.merchant.ui.review

import androidx.lifecycle.viewModelScope
import com.applamdep.customer.core.base.BaseViewModel
import com.applamdep.customer.core.base.UiState
import com.applamdep.customer.domain.model.Review
import com.applamdep.merchant.domain.repository.MerchantRepository
import kotlinx.coroutines.launch

/**
 * 2. TÍNH CHẤT OOP: KẾ THỪA
 * 4. TÍNH CHẤT OOP: TRỪU TƯỢNG
 * ViewModel quản lý phản hồi và tố cáo đánh giá khách hàng (Merchant Requirement #8)
 */
class MerchantReviewViewModel(
    private val merchantRepository: MerchantRepository
) : BaseViewModel<List<Review>>() {

    fun loadReviews(shopId: String = "shop_lamour") {
        launchWithLoading {
            merchantRepository.getMerchantReviews(shopId).collect { list ->
                updateState(UiState.Success(list))
            }
        }
    }

    fun replyReview(reviewId: String, replyText: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (replyText.isBlank()) {
            onError("Nội dung phản hồi không được để trống.")
            return
        }
        viewModelScope.launch {
            val result = merchantRepository.replyReview(reviewId, replyText.trim())
            if (result.isSuccess) {
                onSuccess()
            } else {
                onError("Lỗi khi gửi phản hồi: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun reportReview(reviewId: String, reason: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (reason.isBlank()) {
            onError("Vui lòng nêu rõ lý do tố cáo vi phạm.")
            return
        }
        viewModelScope.launch {
            val result = merchantRepository.reportReview(reviewId, reason.trim())
            if (result.isSuccess) {
                onSuccess()
            } else {
                onError("Lỗi khi gửi yêu cầu tố cáo: ${result.exceptionOrNull()?.message}")
            }
        }
    }
}
