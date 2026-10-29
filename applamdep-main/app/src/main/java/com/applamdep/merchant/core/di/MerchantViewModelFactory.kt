package com.applamdep.merchant.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applamdep.merchant.ui.profile.MerchantProfileViewModel
import com.applamdep.merchant.ui.register.MerchantRegisterViewModel

/**
 * 4. TÍNH CHẤT OOP: TRỪU TƯỢNG (ABSTRACTION)
 * ViewModelProvider.Factory khởi tạo các ViewModel độc lập cho Merchant Portal
 * mà không để UI phụ thuộc trực tiếp vào triển khai cơ sở dữ liệu.
 */
class MerchantViewModelFactory(
    private val container: MerchantContainer = DefaultMerchantContainer()
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MerchantRegisterViewModel::class.java) -> {
                MerchantRegisterViewModel(
                    shopRepository = container.shopRepository,
                    planRepository = container.planRepository
                ) as T
            }
            modelClass.isAssignableFrom(MerchantProfileViewModel::class.java) -> {
                MerchantProfileViewModel(
                    shopRepository = container.shopRepository
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        val DefaultFactory = MerchantViewModelFactory(DefaultMerchantContainer())
    }
}
