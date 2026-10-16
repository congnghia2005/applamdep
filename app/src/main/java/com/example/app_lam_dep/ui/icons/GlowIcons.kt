package com.example.app_lam_dep.ui.icons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.app_lam_dep.ui.theme.ChampagneGold
import com.example.app_lam_dep.ui.theme.DustyMerlot
import com.example.app_lam_dep.ui.theme.GoldSoft
import com.example.app_lam_dep.ui.theme.IconBlush
import com.example.app_lam_dep.ui.theme.IconCream

/**
 * GLOW PRESTIGE ICON SET
 *
 * Bộ icon vẽ tay dạng "fine line" dành riêng cho app làm đẹp cao cấp:
 * - Lưới 24x24, nét 1.6, đầu nét & góc bo tròn → mảnh, thanh lịch, đồng bộ.
 * - Đơn sắc, nhận màu qua `tint` → luôn khớp palette Burgundy / Champagne Gold.
 * - Thay thế toàn bộ emoji (vốn hiển thị khác nhau trên mỗi máy, sặc sỡ, thiếu sang trọng).
 */
object GlowIcons {

    // ───────────── Thương hiệu ─────────────
    val Diamond by lazy {
        glowIcon("Diamond") {
            line("M6 3h12l4 6-10 12L2 9z")
            line("M2 9h20")
            line("M10 3 8 9l4 12 4-12-2-6")
        }
    }

    val Crown by lazy {
        glowIcon("Crown") {
            line("M3 7.5l4.5 4L12 4.5l4.5 7L21 7.5l-2 10.5H5z")
            line("M5 21h14")
        }
    }

    val Sparkle by lazy {
        glowIcon("Sparkle") {
            line("M11 4c.6 4.6 2.9 6.9 7.5 7.5-4.6.6-6.9 2.9-7.5 7.5-.6-4.6-2.9-6.9-7.5-7.5C8.1 10.9 10.4 8.6 11 4z")
            line("M19 2.5v3")
            line("M17.5 4h3")
        }
    }

    // ───────────── Danh mục dịch vụ làm đẹp ─────────────
    /** Lọ sơn móng – Nail Art */
    val NailPolish by lazy {
        glowIcon("NailPolish") {
            line("M10.5 2h3a.5.5 0 0 1 .5.5V7h-4V2.5a.5.5 0 0 1 .5-.5z")
            line("M9 7h6v2.5H9z")
            line("M8 9.5h8a2 2 0 0 1 2 2V20a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2v-8.5a2 2 0 0 1 2-2z")
            line("M6 15h12")
        }
    }

    /** Hoa sen – Spa trị liệu */
    val Lotus by lazy {
        glowIcon("Lotus") {
            line("M12 4.5c-2 2.4-3 4.8-3 7.2 0 2.3 1.2 4.1 3 5.3 1.8-1.2 3-3 3-5.3 0-2.4-1-4.8-3-7.2z")
            line("M12 17c-1.6-.2-4.3-1-5.9-3.1C5 12.5 4.5 10.9 4.5 9.5c1.7.2 3.4.9 4.6 2")
            line("M12 17c1.6-.2 4.3-1 5.9-3.1 1.1-1.4 1.6-3 1.6-4.4-1.7.2-3.4.9-4.6 2")
            line("M3 20.5c2.2 0 2.8-1.3 4.5-1.3s2.3 1.3 4.5 1.3 2.8-1.3 4.5-1.3 2.3 1.3 4.5 1.3")
        }
    }

    /** Lọ serum nhỏ giọt – Chăm sóc da */
    val Serum by lazy {
        glowIcon("Serum") {
            line("M10.5 2h3a1 1 0 0 1 1 1v3h-5V3a1 1 0 0 1 1-1z")
            line("M8.5 6h7v2.5h-7z")
            line("M8 8.5h8a1 1 0 0 1 1 1V20a2 2 0 0 1-2 2H9a2 2 0 0 1-2-2V9.5a1 1 0 0 1 1-1z")
            line("M12 12.5c-1 1.3-1.5 2.2-1.5 3a1.5 1.5 0 0 0 3 0c0-.8-.5-1.7-1.5-3z")
        }
    }

    /** Kéo – Hair Salon */
    val Scissors by lazy {
        glowIcon("Scissors") {
            line("M6 3a3 3 0 1 0 0 6a3 3 0 1 0 0-6z")
            line("M6 15a3 3 0 1 0 0 6a3 3 0 1 0 0-6z")
            line("M8.12 8.12 12 12")
            line("M20 4 8.12 15.88")
            line("M14.8 14.8 20 20")
        }
    }

    /** Thỏi son – Makeup */
    val Lipstick by lazy {
        glowIcon("Lipstick") {
            line("M10 10V5.6L14 3v7")
            line("M9 10h6v3H9z")
            line("M8 13h8v7a2 2 0 0 1-2 2h-4a2 2 0 0 1-2-2z")
            line("M8 16.5h8")
        }
    }

    /** Lá – Dưỡng sinh */
    val Leaf by lazy {
        glowIcon("Leaf") {
            line("M11 20A7 7 0 0 1 9.8 6.1C15.5 5 17 4.48 19 2c1 2 2 4.18 2 8 0 5.5-4.78 10-10 10z")
            line("M2 21c0-3 1.85-5.36 5.08-6C9.5 14.52 12 13 13 12")
        }
    }

    // ───────────── Điều hướng ─────────────
    val Home by lazy {
        glowIcon("Home") {
            line("M3 10a2 2 0 0 1 .7-1.5l7-6a2 2 0 0 1 2.6 0l7 6A2 2 0 0 1 21 10v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z")
            line("M15 21v-7a1 1 0 0 0-1-1h-4a1 1 0 0 0-1 1v7")
        }
    }

    val Calendar by lazy {
        glowIcon("Calendar") {
            line("M8 2v4")
            line("M16 2v4")
            line("M5 4h14a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2z")
            line("M3 10h18")
            line("M8 14h.01")
            line("M12 14h.01")
            line("M16 14h.01")
            line("M8 18h.01")
            line("M12 18h.01")
        }
    }

    val Chat by lazy {
        glowIcon("Chat") {
            line("M7.9 20A9 9 0 1 0 4 16.1L2 22z")
            line("M8 12h.01")
            line("M12 12h.01")
            line("M16 12h.01")
        }
    }

    val User by lazy {
        glowIcon("User") {
            line("M12 3a4 4 0 1 0 0 8a4 4 0 1 0 0-8z")
            line("M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2")
        }
    }

    /** Thanh trượt cài đặt – thanh lịch hơn bánh răng */
    val Settings by lazy {
        glowIcon("Settings") {
            line("M20 7h-9")
            line("M14 17H4")
            line("M7 4a3 3 0 1 0 0 6a3 3 0 1 0 0-6z")
            line("M17 14a3 3 0 1 0 0 6a3 3 0 1 0 0-6z")
        }
    }

    val ChevronLeft by lazy { glowIcon("ChevronLeft") { line("M15 18l-6-6 6-6", width = 2f) } }
    val ChevronRight by lazy { glowIcon("ChevronRight") { line("M9 18l6-6-6-6", width = 2f) } }
    val ChevronDown by lazy { glowIcon("ChevronDown") { line("M6 9l6 6 6-6", width = 2f) } }

    val Filter by lazy {
        glowIcon("Filter") {
            line("M4 6h16")
            line("M7 12h10")
            line("M10 18h4")
        }
    }

    val Tag by lazy {
        glowIcon("Tag") {
            line("M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z")
            line("M7 7h.01", width = 2.4f)
        }
    }

    val Bolt by lazy {
        glowIcon("Bolt") {
            line("M13 2L3 14h9l-1 8 10-12h-9l1-8z")
        }
    }

    // ───────────── Tiện ích ─────────────
    val Search by lazy {
        glowIcon("Search") {
            line("M11 3a8 8 0 1 0 0 16a8 8 0 1 0 0-16z")
            line("M21 21l-4.3-4.3")
        }
    }

    /** Máy ảnh – tìm kiếm bằng hình ảnh mẫu móng/tóc */
    val Camera by lazy {
        glowIcon("Camera") {
            line("M14.5 4h-5L7 7H4a2 2 0 0 0-2 2v9a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2h-3z")
            line("M12 10a3 3 0 1 0 0 6a3 3 0 1 0 0-6z")
        }
    }

    val Bell by lazy {
        glowIcon("Bell") {
            line("M6 8a6 6 0 0 1 12 0c0 7 3 9 3 9H3s3-2 3-9")
            line("M10.3 21a1.94 1.94 0 0 0 3.4 0")
        }
    }

    val MapPin by lazy {
        glowIcon("MapPin") {
            line("M20 10c0 6-8 12-8 12S4 16 4 10a8 8 0 0 1 16 0z")
            line("M12 7a3 3 0 1 0 0 6a3 3 0 1 0 0-6z")
        }
    }

    /** Sao đặc – dùng cho điểm đánh giá */
    val StarFilled by lazy {
        glowIcon("StarFilled") {
            solid("M12 2.5l2.94 5.96 6.56.95-4.75 4.63 1.12 6.54L12 17.5l-5.87 3.08 1.12-6.54L2.5 9.41l6.56-.95z")
        }
    }

    val Wallet by lazy {
        glowIcon("Wallet") {
            line("M19 7V4a1 1 0 0 0-1-1H5a2 2 0 0 0 0 4h15a1 1 0 0 1 1 1v4h-3a2 2 0 0 0 0 4h3a1 1 0 0 0 1-1v-2a1 1 0 0 0-1-1")
            line("M3 5v14a2 2 0 0 0 2 2h15a1 1 0 0 0 1-1v-4")
        }
    }

    val Phone by lazy {
        glowIcon("Phone") {
            line("M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72c.13.96.36 1.9.7 2.81a2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45c.91.34 1.85.57 2.81.7A2 2 0 0 1 22 16.92z")
        }
    }

    val Video by lazy {
        glowIcon("Video") {
            line("M16 13l5.2 3.5a.5.5 0 0 0 .8-.4V7.9a.5.5 0 0 0-.8-.4L16 11")
            line("M4 6h10a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2z")
        }
    }

    val Mic by lazy {
        glowIcon("Mic") {
            line("M12 2a3 3 0 0 1 3 3v7a3 3 0 0 1-6 0V5a3 3 0 0 1 3-3z")
            line("M19 10v2a7 7 0 0 1-14 0v-2")
            line("M12 19v3")
        }
    }

    val Smile by lazy {
        glowIcon("Smile") {
            line("M12 2a10 10 0 1 0 0 20a10 10 0 1 0 0-20z")
            line("M8 14s1.5 2 4 2 4-2 4-2")
            line("M9 9h.01", width = 2.2f)
            line("M15 9h.01", width = 2.2f)
        }
    }

    val Image by lazy {
        glowIcon("Image") {
            line("M5 3h14a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2z")
            line("M9 7a2 2 0 1 0 0 4a2 2 0 1 0 0-4z")
            line("M21 15l-3.1-3.1a2 2 0 0 0-2.8 0L6 21")
        }
    }

    val Save by lazy {
        glowIcon("Save") {
            line("M15.2 3a2 2 0 0 1 1.4.6l3.8 3.8a2 2 0 0 1 .6 1.4V19a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2z")
            line("M17 21v-7a1 1 0 0 0-1-1H8a1 1 0 0 0-1 1v7")
            line("M7 3v4a1 1 0 0 0 1 1h7")
        }
    }

    val Alert by lazy {
        glowIcon("Alert") {
            line("M21.7 18l-8-14a2 2 0 0 0-3.4 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.7-3z")
            line("M12 9v4")
            line("M12 17h.01", width = 2.2f)
        }
    }

    /** Lá cờ – báo cáo / tố cáo */
    val Flag by lazy {
        glowIcon("Flag") {
            line("M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1z")
            line("M4 22v-7")
        }
    }

    val Shield by lazy {
        glowIcon("Shield") {
            line("M20 13c0 5-3.5 7.5-7.66 8.95a1 1 0 0 1-.67-.01C7.5 20.5 4 18 4 13V6a1 1 0 0 1 1-1c2 0 4.5-1.2 6.24-2.72a1.17 1.17 0 0 1 1.52 0C14.51 3.81 17 5 19 5a1 1 0 0 1 1 1z")
            line("M9 12l2 2 4-4")
        }
    }

    val Check by lazy { glowIcon("Check") { line("M20 6 9 17l-5-5", width = 2.2f) } }

    val QrCode by lazy {
        glowIcon("QrCode") {
            line("M4 3h4a1 1 0 0 1 1 1v4a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1z")
            line("M16 3h4a1 1 0 0 1 1 1v4a1 1 0 0 1-1 1h-4a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1z")
            line("M4 15h4a1 1 0 0 1 1 1v4a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1v-4a1 1 0 0 1 1-1z")
            line("M21 16h-3a2 2 0 0 0-2 2v3")
            line("M21 21v.01")
            line("M12 7v3a2 2 0 0 1-2 2H7")
            line("M3 12h.01")
            line("M12 3h.01")
            line("M12 16v.01")
            line("M16 12h1")
            line("M21 12v.01")
            line("M12 21v-1")
        }
    }

    val Clock by lazy {
        glowIcon("Clock") {
            line("M12 2a10 10 0 1 0 0 20a10 10 0 1 0 0-20z")
            line("M12 6v6l4 2")
        }
    }

    val Apple by lazy {
        glowIcon("Apple") {
            solid("M16.37 12.6c-.02-2.3 1.88-3.4 1.96-3.46-1.07-1.56-2.73-1.77-3.32-1.8-1.41-.14-2.76.83-3.47.83-.72 0-1.82-.81-3-.79-1.54.02-2.96.9-3.76 2.28-1.6 2.78-.41 6.9 1.15 9.16.76 1.1 1.67 2.34 2.86 2.3 1.15-.05 1.58-.74 2.97-.74 1.38 0 1.77.74 2.98.72 1.23-.02 2.01-1.12 2.76-2.23.87-1.28 1.23-2.52 1.25-2.58-.03-.01-2.39-.92-2.41-3.65zM14.1 5.86c.63-.77 1.06-1.83.94-2.89-.91.04-2.01.61-2.66 1.37-.58.67-1.1 1.76-.96 2.8 1.01.08 2.05-.52 2.68-1.28z")
        }
    }

    /** Logo Google 4 màu – hiển thị bằng [GlowBrandIcon] (không tint) */
    val Google by lazy {
        glowIcon("Google") {
            solid("M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z", Color(0xFF4285F4))
            solid("M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z", Color(0xFF34A853))
            solid("M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l3.66-2.84z", Color(0xFFFBBC05))
            solid("M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z", Color(0xFFEA4335))
        }
    }
}

// ─────────────────────────── Builder nội bộ ───────────────────────────

private class GlowIconScope(private val builder: ImageVector.Builder) {
    /** Nét mảnh bo tròn – phong cách chủ đạo của bộ icon */
    fun line(pathData: String, width: Float = 1.6f) {
        builder.addPath(
            pathData = addPathNodes(pathData),
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = width,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        )
    }

    /** Hình đặc (sao, logo thương hiệu) */
    fun solid(pathData: String, color: Color = Color.Black) {
        builder.addPath(pathData = addPathNodes(pathData), fill = SolidColor(color))
    }
}

private fun glowIcon(name: String, block: GlowIconScope.() -> Unit): ImageVector {
    val builder = ImageVector.Builder(
        name = "Glow.$name",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    )
    GlowIconScope(builder).block()
    return builder.build()
}

// ─────────────────────────── Composable tiện ích ───────────────────────────

/** Icon đơn sắc theo palette của app. */
@Composable
fun GlowIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    tint: Color = DustyMerlot,
    size: Dp = 20.dp,
    contentDescription: String? = null
) {
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier.size(size)
    )
}

/** Icon giữ nguyên màu gốc (logo thương hiệu nhiều màu). */
@Composable
fun GlowBrandIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    contentDescription: String? = null
) {
    Image(imageVector = icon, contentDescription = contentDescription, modifier = modifier.size(size))
}

/**
 * "Huy chương" icon: nền kem → hồng phấn, viền vàng champagne mảnh 1dp.
 * Dùng cho danh mục, avatar, nút tròn… để tạo cảm giác trang sức cao cấp.
 */
@Composable
fun IconMedallion(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = 52.dp,
    iconSize: Dp = size * 0.46f,
    tint: Color = DustyMerlot,
    shape: Shape = CircleShape,
    background: Brush = Brush.linearGradient(listOf(IconCream, IconBlush)),
    borderBrush: Brush = Brush.linearGradient(listOf(ChampagneGold.copy(alpha = 0.85f), GoldSoft.copy(alpha = 0.35f))),
    contentDescription: String? = null
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(background)
            .border(1.dp, borderBrush, shape),
        contentAlignment = Alignment.Center
    ) {
        GlowIcon(icon = icon, tint = tint, size = iconSize, contentDescription = contentDescription)
    }
}

/** Nút quay lại chuẩn cho header nền đỏ rượu: khung kính mờ bo góc + chevron mảnh. */
@Composable
fun GlowBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Color.White,
    background: Color = Color.White.copy(alpha = 0.14f)
) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(11.dp))
            .background(background)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        GlowIcon(icon = GlowIcons.ChevronLeft, tint = tint, size = 20.dp, contentDescription = "Quay lại")
    }
}

/** Dãy sao đánh giá: sao đạt màu vàng champagne, sao còn lại mờ. */
@Composable
fun StarRating(
    rating: Int,
    modifier: Modifier = Modifier,
    max: Int = 5,
    size: Dp = 14.dp,
    activeColor: Color = ChampagneGold,
    inactiveColor: Color = GoldSoft.copy(alpha = 0.45f)
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        repeat(max) { index ->
            GlowIcon(
                icon = GlowIcons.StarFilled,
                tint = if (index < rating) activeColor else inactiveColor,
                size = size
            )
        }
    }
}
