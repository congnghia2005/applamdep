package com.applamdep.merchant.ui.finance

import com.applamdep.customer.core.base.BaseViewModel
import com.applamdep.customer.core.base.UiState
import com.applamdep.merchant.domain.model.MerchantFinancialSummary
import com.applamdep.merchant.domain.repository.MerchantRepository

/**
 * 2. TÍNH CHẤT OOP: KẾ THỪA
 * 4. TÍNH CHẤT OOP: TRỪU TƯỢNG
 * ViewModel theo dõi đặt cọc và bảo lãnh tài chính (Merchant Requirement #9)
 */
class MerchantFinanceViewModel(
    private val merchantRepository: MerchantRepository
) : BaseViewModel<MerchantFinancialSummary>() {

    fun loadFinance(shopId: String = "shop_lamour") {
        launchWithLoading {
            merchantRepository.getFinancialSummary(shopId).collect { summary ->
                updateState(UiState.Success(summary))
            }
        }
    }
}
