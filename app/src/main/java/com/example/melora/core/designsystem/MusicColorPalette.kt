package com.example.melora.core.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class MusicPaletteColor(
    val hex: String,
    val name: String,
    val lightBg: Color,
    val darkBg: Color
)

object MusicColorPalette {
    val Sapphire = MusicPaletteColor("#2563EB", "Sapphire Ocean", Color(0xFFEFF6FF), Color(0xFF1E3A8A))
    val Cyan = MusicPaletteColor("#06B6D4", "Electric Cyan", Color(0xFFECFEFF), Color(0xFF164E63))
    val Rose = MusicPaletteColor("#F43F5E", "Coral Rose", Color(0xFFFFE4E6), Color(0xFF4C0519))
    val Purple = MusicPaletteColor("#8B5CF6", "Velvet Purple", Color(0xFFF5F3FF), Color(0xFF2E1065))
    val Emerald = MusicPaletteColor("#10B981", "Neon Emerald", Color(0xFFECFDF5), Color(0xFF064E3B))
    val Amber = MusicPaletteColor("#F59E0B", "Warm Amber", Color(0xFFFFFBEB), Color(0xFF78350F))
    val Slate = MusicPaletteColor("#64748B", "Cosmic Slate", Color(0xFFF1F5F9), Color(0xFF1E293B))

    val colors = listOf(Sapphire, Cyan, Rose, Purple, Emerald, Amber, Slate)

    @Composable
    fun getBackgroundColor(hex: String): Color {
        val isDark = LocalThemeIsDark.current
        val found = colors.firstOrNull { it.hex.equals(hex, ignoreCase = true) }
        if (found != null) {
            return if (isDark) found.darkBg else found.lightBg
        }
        return if (isDark) Sapphire.darkBg else Sapphire.lightBg
    }
}
