package com.applamdep.domain

import com.applamdep.customer.data.repository.FakeBookingRepository
import com.applamdep.customer.data.repository.FakeReviewRepository
import com.applamdep.customer.data.repository.FakeShopRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Kiểm thử toàn diện tính nhất quán & không có xung đột dữ liệu:
 * 1. 11 Salon từ Firebase RTDB tồn tại đầy đủ, thông tin địa chỉ & số điện thoại Đà Nẵng chuẩn xác.
 * 2. Cơ chế Alias tương thích ngược (shop_lamour <-> salon_01, shop_anyen <-> salon_09) hoạt động trơn tru.
 * 3. Dịch vụ, slots, reviews và booking của từng salon không bị xung đột chéo.
 */
class SalonIntegrationTest {

    private lateinit var shopRepository: FakeShopRepository
    private lateinit var bookingRepository: FakeBookingRepository
    private lateinit var reviewRepository: FakeReviewRepository

    @Before
    fun setUp() {
        shopRepository = FakeShopRepository()
        bookingRepository = FakeBookingRepository()
        reviewRepository = FakeReviewRepository()
    }

    @Test
    fun testAllElevenFirebaseSalonsExist() = runBlocking {
        val shops = shopRepository.getApprovedShops().first()

        // 11 salons from salon_01 to salon_11
        val salonIds = (1..11).map { String.format("salon_%02d", it) }

        for (id in salonIds) {
            val found = shops.find { it.id == id }
            assertNotNull("Salon $id must exist in repository", found)
            assertTrue("Salon $id name must not be blank", found!!.name.isNotBlank())
            assertTrue("Salon $id address must not be blank", found.address.isNotBlank())
            assertTrue("Salon $id must be in Da Nang area", found.lat > 15.0 && found.lng > 107.0)
        }
    }

    @Test
    fun testShopDetailAliasResolution() = runBlocking {
        // Querying salon_01 directly
        val shop01 = shopRepository.getShopById("salon_01").getOrThrow()
        assertEquals("salon_01", shop01.id)
        assertEquals("1994 Nailsroom", shop01.name)
        assertEquals("0762792671", shop01.phone)

        // Querying via legacy alias shop_lamour
        val aliasShop = shopRepository.getShopById("shop_lamour").getOrThrow()
        assertEquals("salon_01", aliasShop.id)
        assertEquals("1994 Nailsroom", aliasShop.name)

        // Querying salon_09 vs shop_anyen
        val shop09 = shopRepository.getShopById("salon_09").getOrThrow()
        assertEquals("salon_09", shop09.id)
        assertTrue(shop09.name.contains("Thì Là SPA", ignoreCase = true))

        val alias09 = shopRepository.getShopById("shop_anyen").getOrThrow()
        assertEquals("salon_09", alias09.id)
    }

    @Test
    fun testShopServicesNoConflicts() = runBlocking {
        val services01 = shopRepository.getShopServices("salon_01").first()
        assertFalse("salon_01 must have services", services01.isEmpty())

        val services02 = shopRepository.getShopServices("salon_02").first()
        assertFalse("salon_02 (BONNIE NAIL) must have services", services02.isEmpty())

        val services09 = shopRepository.getShopServices("salon_09").first()
        assertFalse("salon_09 (Thì Là SPA) must have services", services09.isEmpty())

        // Services of salon_01 and salon_02 must be separate
        val ids01 = services01.map { it.id }.toSet()
        val ids02 = services02.map { it.id }.toSet()
        val intersection = ids01.intersect(ids02)
        assertTrue("Services between salon_01 and salon_02 must not conflict", intersection.isEmpty())
    }

    @Test
    fun testShopBookingsAliasSupport() = runBlocking {
        val bookings01 = bookingRepository.getShopBookings("salon_01").first()
        assertFalse("salon_01 must have active bookings", bookings01.isEmpty())

        // Legacy query for shop_lamour must retrieve the same bookings without dropping
        val bookingsAlias = bookingRepository.getShopBookings("shop_lamour").first()
        assertEquals("Bookings queried by alias must match salon_01 count", bookings01.size, bookingsAlias.size)
    }

    @Test
    fun testReviewsSupportBothSalonIdAndAlias() = runBlocking {
        val reviews01 = reviewRepository.getShopReviews("salon_01").first()
        assertFalse("salon_01 must have reviews", reviews01.isEmpty())

        val reviewsAlias = reviewRepository.getShopReviews("shop_lamour").first()
        assertEquals("Reviews queried by alias must match salon_01 count", reviews01.size, reviewsAlias.size)
    }

    @Test
    fun testSocialLinksPresence() = runBlocking {
        val shop01 = shopRepository.getShopById("salon_01").getOrThrow()
        assertNotNull("salon_01 Facebook must be present", shop01.facebook)
        assertNotNull("salon_01 Instagram must be present", shop01.instagram)

        val shop02 = shopRepository.getShopById("salon_02").getOrThrow()
        assertNotNull("salon_02 TikTok must be present", shop02.tiktok)

        val shop07 = shopRepository.getShopById("salon_07").getOrThrow()
        assertNotNull("salon_07 Website must be present", shop07.website)
    }
}
