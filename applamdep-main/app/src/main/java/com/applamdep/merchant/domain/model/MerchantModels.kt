package com.applamdep.merchant.domain.model

import com.applamdep.customer.domain.model.Booking

/**
 * 1. TÍNH CHẤT OOP: ĐÓNG GÓI (ENCAPSULATION)
 * Model hồ sơ đối tác cửa hàng làm đẹp (Salon / Spa / Nail)
 */
data class MerchantProfile(
    val id: String,
    val ownerId: String,
    val shopName: String,
    val ownerName: String,
    val phone: String,
    val email: String,
    val address: String,
    val lat: Double = 10.7769,
    val lng: Double = 106.7009,
    val description: String,
    val openingHours: String = "09:00 - 21:00",
    val avatarUrl: String? = null,
    val coverImageUrl: String? = null,
    val licenseNumber: String = "",
    val status: String = "APPROVED",          // PENDING (Chờ duyệt), APPROVED (Đã duyệt), REJECTED
    val membershipPlan: String = "VIP",       // NORMAL, VIP
    val planExpiryMillis: Long = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000L),
    val requireDeposit: Boolean = true,
    val depositOptions: List<Long> = listOf(50000L, 100000L, 200000L),
    val amenities: List<String> = listOf("Wifi tốc độ cao", "Phòng VIP riêng tư", "Trà thảo mộc", "Chỗ đậu xe hơi"),
    val createdAt: Long = System.currentTimeMillis()
) {
    val isApproved: Boolean get() = status.equals("APPROVED", ignoreCase = true)
    val isVip: Boolean get() = membershipPlan.equals("VIP", ignoreCase = true)
}

/**
 * Lịch làm việc và ngày nghỉ của tiệm
 */
data class MerchantSchedule(
    val shopId: String,
    val workingDays: List<String> = listOf("Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"),
    val openTime: String = "09:00",
    val closeTime: String = "21:00",
    val breakStartTime: String = "12:00",
    val breakEndTime: String = "13:00",
    val offDates: List<String> = emptyList(), // Các ngày nghỉ đột xuất (YYYY-MM-DD)
    val defaultSlotCapacity: Int = 3         // Sức chứa mặc định mỗi khung giờ (3 thợ/ghế)
)

/**
 * Thống kê tổng quan hoạt động đối tác
 */
data class MerchantStats(
    val totalBookingsToday: Int = 6,
    val pendingBookings: Int = 2,
    val confirmedBookings: Int = 3,
    val inServiceBookings: Int = 1,
    val completedBookings: Int = 18,
    val cancelledBookings: Int = 1,
    val estimatedRevenueToday: Long = 1850000L,
    val totalRevenueMonth: Long = 42600000L,
    val averageRating: Double = 4.9,
    val totalReviews: Int = 48
)

/**
 * Theo dõi đặt cọc và giải ngân (Escrow Deposit Tracking)
 */
data class MerchantFinancialSummary(
    val heldEscrowBalance: Long = 150000L,        // Tiền cọc sàn đang giữ (chờ làm xong dịch vụ)
    val releasedTotalBalance: Long = 3450000L,    // Tiền cọc đã giải ngân cho tiệm
    val refundedTotalBalance: Long = 100000L,     // Tiền cọc hoàn về cho khách do hủy hợp lệ (>2h)
    val bookingTransactions: List<MerchantEscrowItem> = emptyList()
)

data class MerchantEscrowItem(
    val id: String,
    val bookingId: String,
    val customerName: String,
    val serviceName: String,
    val depositAmount: Long,
    val status: String, // HELD, RELEASED_TO_SHOP, REFUNDED
    val time: String
)
