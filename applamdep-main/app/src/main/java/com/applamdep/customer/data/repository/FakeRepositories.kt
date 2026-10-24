package com.applamdep.customer.data.repository

import com.applamdep.customer.core.base.BaseRepository
import com.applamdep.customer.core.constants.AppConstants
import com.applamdep.customer.core.network.FirebaseApiClient
import com.applamdep.customer.domain.model.*
import com.applamdep.customer.domain.repository.BookingRepository
import com.applamdep.customer.domain.repository.ReviewRepository
import com.applamdep.customer.domain.repository.ShopRepository
import com.applamdep.customer.domain.repository.WalletRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * 2. TÍNH CHẤT OOP: KẾ THỪA (Kế thừa BaseRepository)
 * 4. TÍNH CHẤT OOP: TRỪU TƯỢNG (Cài đặt các Repository Interface)
 *
 * Cung cấp dữ liệu 11 Cửa hàng từ Firebase Realtime Database:
 * Endpoint: https://applamdep-default-rtdb.asia-southeast1.firebasedatabase.app/salons.json
 * - Đồng bộ trực tiếp từ Firebase RTDB
 * - 11 Salon thực tế tại Đà Nẵng (Hải Châu, Sơn Trà, Cẩm Lệ, Ngũ Hành Sơn)
 * - Tích hợp đầy đủ dịch vụ, khung giờ, liên kết mạng xã hội (Facebook, Instagram, TikTok, Website)
 */
class FakeShopRepository : BaseRepository(), ShopRepository {

    private val sampleShops = listOf(
        Shop(
            id = "salon_01",
            ownerId = "merchant_01",
            name = "1994 Nailsroom",
            address = "172/5 Trần Phú, Hải Châu, Đà Nẵng",
            lat = 16.0658,
            lng = 108.2230,
            phone = "0762792671",
            openingHours = "9h - 20h",
            description = "Tiệm làm móng nghệ thuật phong cách Hàn Quốc và dưỡng móng cao cấp tại trung tâm Hải Châu, Đà Nẵng.",
            images = listOf(
                "https://images.unsplash.com/photo-1604654894610-df63bc536371?w=800",
                "https://images.unsplash.com/photo-1560750588-73207b1ef5b8?w=800"
            ),
            rating = 4.9,
            reviewCount = 142,
            status = AppConstants.SHOP_STATUS_APPROVED,
            plan = AppConstants.PLAN_VIP,
            planExpiryMillis = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000L),
            requireDeposit = true,
            depositOptions = listOf(50000L, 100000L, 200000L, 300000L, 500000L),
            amenities = listOf("Máy lạnh", "Trà thảo mộc", "Wifi tốc độ cao", "Phòng VIP riêng", "Thanh toán VietQR"),
            facebook = "https://www.facebook.com/profile.php?id=100083563112383",
            instagram = "https://www.instagram.com/1998_nails.room"
        ),
        Shop(
            id = "salon_02",
            ownerId = "merchant_02",
            name = "BONNIE NAIL",
            address = "260/18 Dương Đình Nghệ, Sơn Trà, Đà Nẵng",
            lat = 16.0682,
            lng = 108.2395,
            phone = "0912154434",
            openingHours = "8h - 20h",
            description = "Chuyên vẽ móng Ombre nghệ thuật, đắp móng gel sang trọng, nối mi tự nhiên gần bãi biển Mỹ Khê.",
            images = listOf(
                "https://images.unsplash.com/photo-1519014816548-bf5fe059798b?w=800",
                "https://images.unsplash.com/photo-1560750588-73207b1ef5b8?w=800"
            ),
            rating = 4.8,
            reviewCount = 98,
            status = AppConstants.SHOP_STATUS_APPROVED,
            plan = AppConstants.PLAN_VIP,
            planExpiryMillis = System.currentTimeMillis() + (45L * 24 * 60 * 60 * 1000L),
            requireDeposit = true,
            depositOptions = listOf(50000L, 100000L, 200000L),
            amenities = listOf("Wifi", "Nước uống", "Chỗ đậu xe máy", "Máy lạnh"),
            facebook = "https://www.facebook.com/profile.php?id=61590247413035",
            tiktok = "https://www.tiktok.com/@bonnie.nail"
        ),
        Shop(
            id = "salon_03",
            ownerId = "merchant_03",
            name = "GÀ SPA",
            address = "Hải Châu, Đà Nẵng",
            lat = 16.0630,
            lng = 108.2225,
            phone = "0901 311 113",
            openingHours = "8h30 - 19h30",
            description = "Thương hiệu chăm sóc da và điều trị mụn chuẩn y khoa uy tín hàng đầu, liệu trình thải độc Oxy tươi chuyên sâu.",
            images = listOf(
                "https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?w=800"
            ),
            rating = 4.8,
            reviewCount = 215,
            status = AppConstants.SHOP_STATUS_APPROVED,
            plan = AppConstants.PLAN_NORMAL,
            planExpiryMillis = System.currentTimeMillis() + (60L * 24 * 60 * 60 * 1000L),
            requireDeposit = false,
            depositOptions = emptyList(),
            amenities = listOf("Bác sĩ tư vấn", "Thiết bị y khoa", "Điều hòa")
        ),
        Shop(
            id = "salon_04",
            ownerId = "merchant_04",
            name = "HIEN HAIR STUDIO",
            address = "183 Tố Hữu, Cẩm Lệ, Đà Nẵng",
            lat = 16.0280,
            lng = 108.2090,
            phone = "0332 678 156",
            openingHours = "8h30 - 19h30",
            description = "Salon tạo mẫu tóc chuyên nghiệp, uốn sóng lơi chuẩn Hàn, nhuộm Balayage thời thượng và phục hồi tóc hư tổn.",
            images = listOf(
                "https://images.unsplash.com/photo-1562322140-8baeececf3df?w=800",
                "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?w=800"
            ),
            rating = 4.9,
            reviewCount = 180,
            status = AppConstants.SHOP_STATUS_APPROVED,
            plan = AppConstants.PLAN_VIP,
            planExpiryMillis = System.currentTimeMillis() + (60L * 24 * 60 * 60 * 1000L),
            requireDeposit = true,
            depositOptions = listOf(100000L, 200000L, 500000L),
            amenities = listOf("Ghế massage gội đầu", "Wifi 5G", "Trà & Cafe", "Chỗ để xe"),
            facebook = "https://www.facebook.com/p/Hienz-Hair-Studio-61554487566304/",
            tiktok = "https://www.tiktok.com/@hienz_hair_studio"
        ),
        Shop(
            id = "salon_05",
            ownerId = "merchant_05",
            name = "KỲ DUYÊN TRỊ MỤN",
            address = "282 Nguyễn Phước Nguyên, Đà Nẵng",
            lat = 16.0590,
            lng = 108.1820,
            phone = "0905 926 017",
            openingHours = "8h00 - 19h00",
            description = "Cơ sở chuyên sâu điều trị các loại mụn viêm, mụn ẩn, sẹo rỗ đông y, giúp tái sinh làn da mịn màng.",
            images = listOf(
                "https://images.unsplash.com/photo-1512290900672-1f5beab6c340?w=800"
            ),
            rating = 4.7,
            reviewCount = 112,
            status = AppConstants.SHOP_STATUS_APPROVED,
            plan = AppConstants.PLAN_NORMAL,
            planExpiryMillis = System.currentTimeMillis() + (60L * 24 * 60 * 60 * 1000L),
            requireDeposit = false,
            depositOptions = emptyList(),
            amenities = listOf("Dược mỹ phẩm chính hãng", "Tư vấn 1:1"),
            facebook = "https://www.facebook.com/ky.duyen.478645",
            tiktok = "https://www.tiktok.com/@kyduyen_29"
        ),
        Shop(
            id = "salon_06",
            ownerId = "merchant_06",
            name = "MNH THUY MAKEUP",
            address = "01 Đinh Văn Chấp, Cẩm Lệ, Đà Nẵng",
            lat = 16.0240,
            lng = 108.2130,
            phone = "077 9417 168",
            openingHours = "8h - 20h",
            description = "Studio trang điểm cô dâu, dạ tiệc, kỷ yếu phong cách trong trẻo Douyin & phương Tây quyến rũ.",
            images = listOf(
                "https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?w=800"
            ),
            rating = 4.9,
            reviewCount = 156,
            status = AppConstants.SHOP_STATUS_APPROVED,
            plan = AppConstants.PLAN_VIP,
            planExpiryMillis = System.currentTimeMillis() + (90L * 24 * 60 * 60 * 1000L),
            requireDeposit = true,
            depositOptions = listOf(100000L, 200000L, 300000L),
            amenities = listOf("Mỹ phẩm High-end", "Phòng thay đồ", "Ánh sáng Studio chuyên nghiệp"),
            facebook = "https://www.facebook.com/mnh.thuy.2025",
            tiktok = "https://www.tiktok.com/@mnhthuy.makeup"
        ),
        Shop(
            id = "salon_07",
            ownerId = "merchant_07",
            name = "Nails 1996",
            address = "3 Dương Tụ Quán, Ngũ Hành Sơn, Đà Nẵng",
            lat = 16.0370,
            lng = 108.2430,
            phone = "083.383.5555",
            openingHours = "9h - 20h",
            description = "Studio móng nghệ thuật cao cấp, không gian sang trọng, dịch vụ chuyên nghiệp hàng đầu tại Ngũ Hành Sơn.",
            images = listOf(
                "https://images.unsplash.com/photo-1632345031435-8727f6897d53?w=800"
            ),
            rating = 4.8,
            reviewCount = 89,
            status = AppConstants.SHOP_STATUS_APPROVED,
            plan = AppConstants.PLAN_VIP,
            planExpiryMillis = System.currentTimeMillis() + (60L * 24 * 60 * 60 * 1000L),
            requireDeposit = true,
            depositOptions = listOf(50000L, 100000L, 200000L),
            amenities = listOf("Sơn hữu cơ an toàn", "Bảo hành 7 ngày", "Trà hoa cúc"),
            facebook = "https://www.facebook.com/1996.NailStudio",
            website = "https://1996nailstudio.com/vi"
        ),
        Shop(
            id = "salon_08",
            ownerId = "merchant_08",
            name = "SI MÊ BEAUTY",
            address = "58 Nguyễn Đôn Tiết, Hải Châu, Đà Nẵng",
            lat = 16.0820,
            lng = 108.2190,
            phone = "0777603852",
            openingHours = "8h - 21h",
            description = "Trung tâm làm đẹp toàn diện, triệt lông diode laser thế hệ mới, cấy collagen căng bóng tầng sâu.",
            images = listOf(
                "https://images.unsplash.com/photo-1540555700478-4be289fbecef?w=800"
            ),
            rating = 4.8,
            reviewCount = 94,
            status = AppConstants.SHOP_STATUS_APPROVED,
            plan = AppConstants.PLAN_NORMAL,
            planExpiryMillis = System.currentTimeMillis() + (60L * 24 * 60 * 60 * 1000L),
            requireDeposit = false,
            depositOptions = emptyList(),
            amenities = listOf("Phòng riêng tư", "Máy lạnh", "Tư vấn da miễn phí"),
            facebook = "https://www.facebook.com/simebeauty.haichau.danang",
            tiktok = "https://www.tiktok.com/@simebeauty81"
        ),
        Shop(
            id = "salon_09",
            ownerId = "merchant_09",
            name = "Thì Là SPA - Gội Đầu Dưỡng Sinh, Massage Thư Giãn",
            address = "57 Nguyễn Thị Minh Khai, Hải Châu, Đà Nẵng",
            lat = 16.0710,
            lng = 108.2170,
            phone = "0773622699",
            openingHours = "9h15 - 20h30",
            description = "Không gian dưỡng sinh thư thái với nước gội bồ kết nấu tươi cô đặc, massage trị liệu cổ vai gáy và ngâm chân thảo mộc đá nóng.",
            images = listOf(
                "https://images.unsplash.com/photo-1544161515-4ab6ce6db874?w=800",
                "https://images.unsplash.com/photo-1540555700478-4be289fbecef?w=800"
            ),
            rating = 5.0,
            reviewCount = 260,
            status = AppConstants.SHOP_STATUS_APPROVED,
            plan = AppConstants.PLAN_VIP,
            planExpiryMillis = System.currentTimeMillis() + (90L * 24 * 60 * 60 * 1000L),
            requireDeposit = true,
            depositOptions = listOf(50000L, 100000L, 200000L),
            amenities = listOf("Bồ kết nấu tươi", "Phòng xông tinh dầu", "Trà gừng mật ong", "Phòng VIP đôi")
        ),
        Shop(
            id = "salon_10",
            ownerId = "merchant_10",
            name = "Tun Beauty Salon & Spa",
            address = "238 Trần Hưng Đạo, Sơn Trà, Đà Nẵng",
            lat = 16.0610,
            lng = 108.2310,
            phone = "083.383.5555",
            openingHours = "10h - 21h",
            description = "Tổ hợp chăm sóc tóc và spa thư giãn bên bờ sông Hàn thơ mộng, ngắm trọn cầu Rồng Đà Nẵng.",
            images = listOf(
                "https://images.unsplash.com/photo-1560066984-138dadb4c035?w=800"
            ),
            rating = 4.8,
            reviewCount = 130,
            status = AppConstants.SHOP_STATUS_APPROVED,
            plan = AppConstants.PLAN_VIP,
            planExpiryMillis = System.currentTimeMillis() + (60L * 24 * 60 * 60 * 1000L),
            requireDeposit = true,
            depositOptions = listOf(50000L, 100000L, 200000L),
            amenities = listOf("View sông Hàn", "Chỗ đỗ ô tô", "Điều hòa"),
            facebook = "https://www.facebook.com/TunBeautySalonSpa/"
        ),
        Shop(
            id = "salon_11",
            ownerId = "merchant_11",
            name = "1m65.nail",
            address = "48 Lê Đình Dương, Hải Châu, Đà Nẵng",
            lat = 16.0615,
            lng = 108.2205,
            phone = "0935043679",
            openingHours = "9h - 20h",
            description = "Tiệm nail phong cách Nhật - Hàn, chuyên vẽ hoạt hình anime tỉ mỉ và phụ kiện charm xinh độc quyền.",
            images = listOf(
                "https://images.unsplash.com/photo-1604654894610-df63bc536371?w=800"
            ),
            rating = 4.9,
            reviewCount = 175,
            status = AppConstants.SHOP_STATUS_APPROVED,
            plan = AppConstants.PLAN_VIP,
            planExpiryMillis = System.currentTimeMillis() + (60L * 24 * 60 * 60 * 1000L),
            requireDeposit = true,
            depositOptions = listOf(50000L, 100000L, 200000L),
            amenities = listOf("Sơn gel cao cấp Nhật", "Kho charm hơn 1000 mẫu", "Điều hòa"),
            facebook = "https://www.facebook.com/profile.php?id=100083393916454",
            instagram = "https://www.instagram.com/1m65.nail"
        )
    )

    private val sampleServices = listOf(
        // salon_01: 1994 Nailsroom
        ServiceItem("srv_01", "salon_01", "Sơn Gel Thạch Hàn Quốc + Cắt Da", "Sơn gel cao cấp kèm tẩy da chết và dưỡng ẩm móng", 160000L, 45, "Làm Nail Art"),
        ServiceItem("srv_02", "salon_01", "Nail Art Mắt Mèo Cực Quang VIP", "Hiệu ứng mắt mèo đổi màu kết hợp đính đá Swarovski", 250000L, 60, "Làm Nail Art"),
        ServiceItem("srv_03", "salon_01", "Nối Mi Lụa Thiết Kế Tự Nhiên", "Mi lụa mềm tự nhiên không cộm, không cay mắt", 220000L, 50, "Nối Mi"),
        ServiceItem("srv_04", "salon_01", "Chăm Sóc & Dưỡng Móng Organic", "Cắt da, ủ dưỡng móng chuyên sâu collagen", 120000L, 30, "Làm Nail Art"),

        // salon_02: BONNIE NAIL
        ServiceItem("srv_05", "salon_02", "Vẽ Móng Nghệ Thuật Ombre Pastel", "Vẽ cọ nét hoa văn ombre chuyển màu tự nhiên", 180000L, 45, "Làm Nail Art"),
        ServiceItem("srv_06", "salon_02", "Đắp Móng Gel Form Hạnh Nhân", "Đắp móng gel bền đẹp kèm tạo form thanh thoát", 280000L, 60, "Làm Nail Art"),
        ServiceItem("srv_07", "salon_02", "Tháo Móng & Dưỡng Keratin", "Tháo móng nhẹ nhàng không xước, phục hồi keratin", 100000L, 30, "Làm Nail Art"),

        // salon_03: GÀ SPA
        ServiceItem("srv_08", "salon_03", "Lấy Nhân Mụn Y Khoa Chuẩn Da Liễu", "Lấy sạch nhân mụn tăm bông vô trùng không để lại sẹo", 250000L, 60, "Spa Trị Liệu"),
        ServiceItem("srv_09", "salon_03", "Điện Di Vitamin C Sáng Mịn Da", "Đưa tinh chất vitamin C sâu vào biểu bì mờ thâm", 300000L, 45, "Chăm sóc da"),
        ServiceItem("srv_10", "salon_03", "Thải Độc Da CO2 Thải Chì Kháng Khuẩn", "Mặt nạ CO2 thải độc da kích thích tăng sinh tế bào", 350000L, 60, "Spa Trị Liệu"),

        // salon_04: HIEN HAIR STUDIO
        ServiceItem("srv_11", "salon_04", "Cắt Tạo Kiểu Layer / Wolf Cut Chuẩn Form", "Stylist tư vấn dáng mặt và cắt tạo kiểu thời thượng", 150000L, 40, "Hair Salon"),
        ServiceItem("srv_12", "salon_04", "Uốn Sóng Lơi Hàn Quốc Bồng Bềnh", "Uốn sóng lơi tự nhiên giữ nếp bền lâu", 450000L, 90, "Hair Salon"),
        ServiceItem("srv_13", "salon_04", "Nhuộm Nâu Tây / Balayage Tôn Da", "Nhuộm màu chuẩn tone không tẩy tóc hư tổn", 550000L, 90, "Hair Salon"),
        ServiceItem("srv_14", "salon_04", "Phục Hồi Tóc Keratin Chuyên Sâu", "Ủ nhiệt phục hồi các sợi tóc khô xơ chẻ ngọn", 350000L, 60, "Hair Salon"),

        // salon_05: KỲ DUYÊN TRỊ MỤN
        ServiceItem("srv_15", "salon_05", "Trị Mụn Thảo Dược Đông Y Tận Gốc", "Liệu trình thuốc nam đông y lành tính kháng khuẩn", 200000L, 60, "Spa Trị Liệu"),
        ServiceItem("srv_16", "salon_05", "Trị Thâm Sẹo Tế Bào Gốc Phục Hồi", "Cấy tinh chất tế bào gốc làm đầy vùng sẹo rỗ", 400000L, 75, "Spa Trị Liệu"),

        // salon_06: MNH THUY MAKEUP
        ServiceItem("srv_17", "salon_06", "Trang Điểm Cô Dâu Đón Dâu & Tiệc Cưới", "Trang điểm cô dâu tone trong trẻo kèm tạo kiểu tóc", 600000L, 75, "Makeup Tiệc"),
        ServiceItem("srv_18", "salon_06", "Trang Điểm & Làm Tóc Dạ Hội Cao Cấp", "Tone Tây / Douyin tôn đường nét gương mặt", 300000L, 50, "Makeup Tiệc"),
        ServiceItem("srv_19", "salon_06", "Makeup Chụp Ảnh Ngoại Cảnh / Kỷ Yếu", "Giữ lớp nền lâu trôi suốt cả ngày chụp ảnh", 250000L, 45, "Makeup Tiệc"),

        // salon_07: Nails 1996
        ServiceItem("srv_20", "salon_07", "Sơn Gel Thiết Kế Phong Cách Châu Âu", "Tone màu nude và gel kim tuyến sang chảnh", 220000L, 50, "Làm Nail Art"),
        ServiceItem("srv_21", "salon_07", "Đắp Bột Móng Nghệ Thuật Đính Đá", "Kỹ thuật đắp bột chắc chắn, đính đá tỉ mỉ", 350000L, 75, "Làm Nail Art"),

        // salon_08: SI MÊ BEAUTY
        ServiceItem("srv_22", "salon_08", "Chăm Sóc Da Căng Bóng Thủy Điểm", "Massage nâng cơ và cấp ẩm sâu cho da", 320000L, 60, "Chăm sóc da"),
        ServiceItem("srv_23", "salon_08", "Cấy Collagen Tươi Trẻ Hóa Tầng Sâu", "Bổ sung collagen tươi đàn hồi và săn chắc", 450000L, 75, "Chăm sóc da"),
        ServiceItem("srv_24", "salon_08", "Triệt Lông Vĩnh Viễn Diode Laser", "Công nghệ triệt lạnh không đau rát", 180000L, 30, "Chăm sóc da"),

        // salon_09: Thì Là SPA
        ServiceItem("srv_25", "salon_09", "Gội Đầu Dưỡng Sinh Thảo Dược 7 Bước", "Nước gội bồ kết cô đặc, bấm huyệt cổ vai gáy", 150000L, 45, "Dưỡng sinh"),
        ServiceItem("srv_26", "salon_09", "Massage Trị Liệu Cổ Vai Gáy Chuyên Sâu", "Đả thông kinh lạc, giải tỏa căng cứng mỏi cơ", 250000L, 50, "Dưỡng sinh"),
        ServiceItem("srv_27", "salon_09", "Massage Body Đá Nóng & Ngâm Chân Thảo Mộc", "Thư giãn các khối cơ, hồi phục năng lượng toàn thân", 380000L, 75, "Dưỡng sinh"),

        // salon_10: Tun Beauty Salon & Spa
        ServiceItem("srv_28", "salon_10", "Cắt Tóc & Gội Massage Thư Giãn", "Cắt tóc tạo kiểu kèm gội đầu thư giãn", 120000L, 40, "Hair Salon"),
        ServiceItem("srv_29", "salon_10", "Uốn Nhuộm Combo Chăm Sóc Tóc Cao Cấp", "Combo uốn nhuộm phục hồi trọn gói", 480000L, 90, "Hair Salon"),

        // salon_11: 1m65.nail
        ServiceItem("srv_30", "salon_11", "Vẽ Hoạt Hình Anime & Gắn Charm Theo Yêu Cầu", "Vẽ móng nhân vật anime siêu chi tiết kèm charm xinh", 220000L, 60, "Làm Nail Art"),
        ServiceItem("srv_31", "salon_11", "Sơn Gel Thạch Trong Trẻo Kiểu Nhật", "Màu sắc pastel trong trẻo tự nhiên chuẩn Nhật", 180000L, 45, "Làm Nail Art"),
        ServiceItem("srv_32", "salon_11", "Nail Box Thiết Kế Thủ Công Theo Size Tay", "Mẫu nail box thiết kế đo ni vừa vặn tiện lợi", 150000L, 30, "Làm Nail Art")
    )

    private val shopsFlow = MutableStateFlow(sampleShops)

    init {
        // Đồng bộ dữ liệu mới nhất từ Firebase Realtime Database
        fetchSalonsFromFirebase()
    }

    private fun fetchSalonsFromFirebase() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val remoteSalons = FirebaseApiClient.salonService.getSalons()
                if (remoteSalons.isNotEmpty()) {
                    val currentList = shopsFlow.value
                    val updated = currentList.map { shop ->
                        val remoteDto = remoteSalons[shop.id]
                        if (remoteDto != null) {
                            shop.copy(
                                name = remoteDto.name?.takeIf { it.isNotBlank() } ?: shop.name,
                                address = if (remoteDto.address.isNullOrBlank() || remoteDto.address.contains("Đang cập nhật", ignoreCase = true)) {
                                    shop.address
                                } else {
                                    remoteDto.address
                                },
                                phone = remoteDto.phone?.takeIf { it.isNotBlank() } ?: shop.phone,
                                openingHours = remoteDto.hours?.takeIf { it.isNotBlank() } ?: shop.openingHours,
                                facebook = remoteDto.facebook ?: shop.facebook,
                                instagram = remoteDto.instagram ?: shop.instagram,
                                tiktok = remoteDto.tiktok ?: shop.tiktok,
                                website = remoteDto.website ?: shop.website
                            )
                        } else {
                            shop
                        }
                    }
                    shopsFlow.value = updated
                }
            } catch (e: Exception) {
                // Sử dụng dữ liệu cục bộ 11 salon nếu không có kết nối internet
            }
        }
    }

    override fun getApprovedShops(): Flow<List<Shop>> {
        return shopsFlow.map { list ->
            // Chỉ lấy shop APPROVED và còn hạn, ưu tiên VIP lên đầu
            list.filter { it.status == AppConstants.SHOP_STATUS_APPROVED && it.isPlanValid }
                .sortedWith(
                    compareByDescending<Shop> { it.isVipActive }
                        .thenByDescending { it.rating }
                )
        }
    }

    override fun searchShops(query: String): Flow<List<Shop>> {
        return shopsFlow.map { list ->
            val cleanQuery = query.trim().lowercase()
            list.filter { shop ->
                shop.status == AppConstants.SHOP_STATUS_APPROVED && shop.isPlanValid &&
                        (shop.name.lowercase().contains(cleanQuery) ||
                                shop.address.lowercase().contains(cleanQuery) ||
                                sampleServices.any { (it.shopId == shop.id || (shop.id == "salon_01" && it.shopId == "shop_lamour")) && it.name.lowercase().contains(cleanQuery) })
            }.sortedWith(
                compareByDescending<Shop> { it.isVipActive }
                    .thenByDescending { it.rating }
            )
        }
    }

    override suspend fun getShopById(shopId: String): Result<Shop> = safeCall {
        val targetId = when (shopId) {
            "shop_lamour" -> "salon_01"
            "shop_anyen" -> "salon_09"
            else -> shopId
        }
        shopsFlow.value.find { it.id == targetId || it.id == shopId }
            ?: sampleShops.find { it.id == targetId || it.id == shopId }
            ?: sampleShops.first()
    }

    override fun getShopServices(shopId: String): Flow<List<ServiceItem>> {
        val targetId = when (shopId) {
            "shop_lamour" -> "salon_01"
            "shop_anyen" -> "salon_09"
            else -> shopId
        }
        val list = sampleServices.filter { (it.shopId == targetId || it.shopId == shopId) && it.isActive }
        return MutableStateFlow(if (list.isEmpty()) sampleServices.take(4) else list)
    }

    override fun getShopSlots(shopId: String, date: String): Flow<List<ShopSlot>> {
        val sampleSlots = listOf(
            ShopSlot("slot_01", shopId, date, "09:00", maxCapacity = 2, bookedCount = 2, isAvailable = false), // Hết chỗ
            ShopSlot("slot_02", shopId, date, "10:00", maxCapacity = 3, bookedCount = 1, isAvailable = true),
            ShopSlot("slot_03", shopId, date, "11:30", maxCapacity = 3, bookedCount = 0, isAvailable = true),
            ShopSlot("slot_04", shopId, date, "14:00", maxCapacity = 3, bookedCount = 1, isAvailable = true),
            ShopSlot("slot_05", shopId, date, "15:30", maxCapacity = 2, bookedCount = 0, isAvailable = true),
            ShopSlot("slot_06", shopId, date, "17:00", maxCapacity = 2, bookedCount = 0, isAvailable = true),
            ShopSlot("slot_07", shopId, date, "18:30", maxCapacity = 3, bookedCount = 2, isAvailable = true)
        )
        return MutableStateFlow(sampleSlots)
    }
}

/**
 * Fake Booking Repository
 */
class FakeBookingRepository : BaseRepository(), BookingRepository {
    private val bookings = MutableStateFlow<List<Booking>>(
        listOf(
            Booking(
                id = "BK-8921",
                customerId = "user_cust_01",
                customerName = "Khánh Linh",
                customerPhone = "0903 123 456",
                customerNote = "Sơn tone hồng thạch, da tay nhạy cảm",
                shopId = "salon_01",
                shopName = "1994 Nailsroom",
                shopAddress = "172/5 Trần Phú, Hải Châu, Đà Nẵng",
                serviceId = "srv_01",
                serviceName = "Sơn Gel Hữu Cơ + Dưỡng Móng",
                servicePrice = 180000L,
                bookingDate = "2026-10-23",
                bookingTime = "15:30",
                appointmentTimestamp = System.currentTimeMillis() + (4 * 60 * 60 * 1000L), // Còn 4 tiếng (hợp lệ hủy hoàn cọc)
                durationMinutes = 45,
                requiresDeposit = true,
                depositAmount = 50000L,
                depositTransactionId = "TX-DEP-001",
                status = AppConstants.BOOKING_STATUS_CONFIRMED
            ),
            // Thêm booking đã hoàn thành để khách hàng trải nghiệm ngay chức năng ĐÁNH GIÁ (Nhóm 5)
            Booking(
                id = "BK-8840",
                customerId = "user_cust_01",
                customerName = "Khánh Linh",
                customerPhone = "0903 123 456",
                customerNote = "Gội đầu dưỡng sinh thảo dược",
                shopId = "salon_09",
                shopName = "Thì Là SPA - Gội Đầu Dưỡng Sinh",
                shopAddress = "57 Nguyễn Thị Minh Khai, Hải Châu, Đà Nẵng",
                serviceId = "srv_03",
                serviceName = "Gội Đầu Dưỡng Sinh Thảo Dược 60p",
                servicePrice = 250000L,
                bookingDate = "2026-10-22",
                bookingTime = "14:00",
                appointmentTimestamp = System.currentTimeMillis() - (24 * 60 * 60 * 1000L),
                durationMinutes = 60,
                requiresDeposit = true,
                depositAmount = 50000L,
                depositTransactionId = "TX-DEP-000",
                status = AppConstants.BOOKING_STATUS_COMPLETED,
                isReviewed = false
            ),
            // Thêm booking của khách khác tại shop để Merchant Portal kiểm thử quản lý booking
            Booking(
                id = "BK-9012",
                customerId = "user_cust_02",
                customerName = "Phương Thảo",
                customerPhone = "0912 888 999",
                customerNote = "Khách làm móng cô dâu phong cách Hàn Quốc",
                shopId = "shop_lamour",
                shopName = "L'Amour Haute Nail & Lash Lounge",
                shopAddress = "128 Pasteur, Bến Nghé, Quận 1, TP.HCM",
                serviceId = "srv_01",
                serviceName = "Sơn Gel Hữu Cơ Pháp + Dưỡng Móng",
                servicePrice = 180000L,
                bookingDate = "2026-10-23",
                bookingTime = "10:30",
                appointmentTimestamp = System.currentTimeMillis() - (2 * 60 * 60 * 1000L), // Quá 30p sau hẹn -> Tự động hoàn thành
                durationMinutes = 45,
                requiresDeposit = true,
                depositAmount = 50000L,
                depositTransactionId = "TX-DEP-002",
                status = AppConstants.BOOKING_STATUS_CONFIRMED
            )
        )
    )

    override suspend fun createBooking(booking: Booking): Result<String> = safeCall {
        val currentList = bookings.value.toMutableList()
        currentList.add(0, booking)
        bookings.value = currentList
        booking.id
    }

    override fun getCustomerBookings(customerId: String): Flow<List<Booking>> {
        return bookings.map { list -> list.filter { it.customerId == customerId } }
    }

    override fun getShopBookings(shopId: String): Flow<List<Booking>> {
        val targetId = when (shopId) {
            "shop_lamour" -> "salon_01"
            "shop_anyen" -> "salon_09"
            else -> shopId
        }
        return bookings.map { list ->
            list.filter { it.shopId == targetId || it.shopId == shopId || (targetId == "salon_01" && it.shopId == "shop_lamour") }
        }
    }

    override suspend fun getBookingById(bookingId: String): Result<Booking> = safeCall {
        bookings.value.find { it.id == bookingId } ?: throw NoSuchElementException("Không tìm thấy booking: $bookingId")
    }

    override suspend fun updateBookingStatus(bookingId: String, status: String): Result<Unit> = safeCall {
        val updated = bookings.value.map { b ->
            if (b.id == bookingId) {
                b.copy(status = status)
            } else b
        }
        bookings.value = updated
    }

    override suspend fun markBookingReviewed(bookingId: String): Result<Unit> = safeCall {
        val updated = bookings.value.map { b ->
            if (b.id == bookingId) {
                b.copy(isReviewed = true)
            } else b
        }
        bookings.value = updated
    }

    override suspend fun cancelBooking(bookingId: String, reason: String): Result<Unit> = safeCall {
        val updated = bookings.value.map { b ->
            if (b.id == bookingId) {
                b.copy(
                    status = AppConstants.BOOKING_STATUS_CANCELLED,
                    cancelReason = reason,
                    cancelledAt = System.currentTimeMillis()
                )
            } else b
        }
        bookings.value = updated
    }
}

/**
 * Fake Wallet Repository
 */
class FakeWalletRepository : BaseRepository(), WalletRepository {
    private val walletFlow = MutableStateFlow(Wallet("user_cust_01", balance = 3450000L))
    private val transactionsFlow = MutableStateFlow<List<Transaction>>(
        listOf(
            DepositTransaction(
                id = "TX-DEP-001",
                userId = "user_cust_01",
                amount = 50000L,
                status = AppConstants.TX_STATUS_HELD, // Sàn đang giữ cọc
                bookingId = "BK-8921",
                shopId = "salon_01"
            ),
            TopUpTransaction(
                id = "TX-TOP-002",
                userId = "user_cust_01",
                amount = 500000L,
                status = AppConstants.TX_STATUS_SUCCESS
            )
        )
    )

    override fun getWalletBalance(userId: String): Flow<Wallet?> = walletFlow

    override fun getTransactionHistory(userId: String): Flow<List<Transaction>> = transactionsFlow

    override suspend fun createTopUpRequest(userId: String, amount: Long): Result<Transaction> = safeCall {
        val txId = "TX-TOP-" + UUID.randomUUID().toString().take(8).uppercase()
        val tx = TopUpTransaction(
            id = txId,
            userId = userId,
            amount = amount,
            status = AppConstants.TX_STATUS_PENDING,
            qrUrl = "${AppConstants.DEFAULT_MOCK_PAYMENT_URL}?txId=$txId&amount=$amount"
        )
        val list = transactionsFlow.value.toMutableList()
        list.add(0, tx)
        transactionsFlow.value = list
        tx
    }

    override suspend fun holdDeposit(
        userId: String,
        bookingId: String,
        shopId: String,
        amount: Long
    ): Result<Transaction> = safeCall {
        val currentBalance = walletFlow.value.balance
        if (currentBalance < amount) {
            throw IllegalStateException("Số dư ví không đủ để thanh toán cọc ($amount đ). Vui lòng nạp thêm.")
        }
        // Trừ tiền số dư ví của khách
        walletFlow.value = walletFlow.value.copy(balance = currentBalance - amount)

        val txId = "TX-DEP-" + UUID.randomUUID().toString().take(8).uppercase()
        val tx = DepositTransaction(
            id = txId,
            userId = userId,
            amount = amount,
            status = AppConstants.TX_STATUS_HELD, // Sàn tạm giữ
            bookingId = bookingId,
            shopId = shopId
        )
        val list = transactionsFlow.value.toMutableList()
        list.add(0, tx)
        transactionsFlow.value = list
        tx
    }

    override suspend fun refundDepositToWallet(
        userId: String,
        bookingId: String,
        amount: Long,
        reason: String
    ): Result<Transaction> = safeCall {
        // Hoàn tiền cộng lại vào ví khách
        walletFlow.value = walletFlow.value.copy(balance = walletFlow.value.balance + amount)

        val txId = "TX-REF-" + UUID.randomUUID().toString().take(8).uppercase()
        val tx = RefundTransaction(
            id = txId,
            userId = userId,
            amount = amount,
            status = AppConstants.TX_STATUS_REFUNDED,
            originalDepositTxId = "TX-DEP-OLD",
            bookingId = bookingId,
            refundReason = reason
        )
        val list = transactionsFlow.value.toMutableList()
        list.add(0, tx)
        transactionsFlow.value = list
        tx
    }

    override fun observeTransactionStatus(transactionId: String): Flow<String> {
        return transactionsFlow.map { list ->
            list.find { it.id == transactionId }?.status ?: AppConstants.TX_STATUS_PENDING
        }
    }

    override suspend fun confirmTransactionSuccess(transactionId: String): Result<Unit> = safeCall {
        var topUpAmount = 0L
        val updated = transactionsFlow.value.map { tx ->
            if (tx.id == transactionId) {
                if (tx is TopUpTransaction) {
                    topUpAmount = tx.amount
                    tx.copy(status = AppConstants.TX_STATUS_SUCCESS)
                } else tx
            } else tx
        }
        transactionsFlow.value = updated

        // Nếu là giao dịch nạp tiền, cộng số dư ví
        if (topUpAmount > 0) {
            walletFlow.value = walletFlow.value.copy(balance = walletFlow.value.balance + topUpAmount)
        }
    }
}

/**
 * Fake Review Repository
 */
class FakeReviewRepository : BaseRepository(), ReviewRepository {
    private val reviewsFlow = MutableStateFlow(
        listOf(
            Review(
                id = "REV-01",
                bookingId = "BK-OLD-1",
                shopId = "salon_01",
                customerId = "user_cust_02",
                customerName = "Thảo Nhi",
                rating = 5,
                comment = "Tiệm làm móng rất tỉ mỉ, tone màu hồng thạch cực xinh. Chuyên viên nhẹ nhàng chu đáo!",
                status = "ACTIVE"
            ),
            Review(
                id = "REV-02",
                bookingId = "BK-OLD-2",
                shopId = "salon_01",
                customerId = "user_cust_03",
                customerName = "Bảo Trâm",
                rating = 5,
                comment = "Không gian sang trọng tại Đà Nẵng, phục vụ rất chu đáo. Sẽ quay lại nhiều lần.",
                status = "ACTIVE"
            )
        )
    )

    override fun getShopReviews(shopId: String): Flow<List<Review>> {
        val targetId = when (shopId) {
            "shop_lamour" -> "salon_01"
            "shop_anyen" -> "salon_09"
            else -> shopId
        }
        return reviewsFlow.map { list ->
            list.filter { (it.shopId == targetId || it.shopId == shopId || (targetId == "salon_01" && it.shopId == "shop_lamour")) && it.status != "REMOVED" }
        }
    }

    override fun getCustomerReviews(customerId: String): Flow<List<Review>> {
        return reviewsFlow.map { list ->
            list.filter { it.customerId == customerId }
        }
    }

    override suspend fun submitReview(review: Review): Result<Unit> = safeCall {
        val list = reviewsFlow.value.toMutableList()
        list.add(0, review)
        reviewsFlow.value = list
    }

    override suspend fun replyReview(reviewId: String, reply: String): Result<Unit> = safeCall {
        val updated = reviewsFlow.value.map { r ->
            if (r.id == reviewId) {
                r.copy(
                    replyComment = reply,
                    repliedAt = System.currentTimeMillis()
                )
            } else r
        }
        reviewsFlow.value = updated
    }

    override suspend fun reportReview(reviewId: String, reason: String): Result<Unit> = safeCall {
        val updated = reviewsFlow.value.map { r ->
            if (r.id == reviewId) {
                r.copy(
                    status = "REMOVE_REQUESTED",
                    reportReason = reason,
                    reportedAt = System.currentTimeMillis()
                )
            } else r
        }
        reviewsFlow.value = updated
    }
}
