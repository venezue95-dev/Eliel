package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.vpn.model.AppThemeMode

// 1. Cyber Dark (ET Obsidian Default)
private val CyberDarkColorScheme = darkColorScheme(
    primary = CyberCyan,
    onPrimary = Color(0xFF00363D),
    primaryContainer = Color(0xFF004F58),
    onPrimaryContainer = Color(0xFF97F0FF),
    secondary = NeonEmerald,
    onSecondary = Color(0xFF003919),
    secondaryContainer = Color(0xFF005227),
    onSecondaryContainer = Color(0xFF66FFA3),
    tertiary = ElectricAmber,
    background = DarkBackground,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = DarkBorder
)

// 2. AMOLED Pitch Black
private val AmoledBlackColorScheme = darkColorScheme(
    primary = CyberCyan,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF00363D),
    onPrimaryContainer = CyberCyan,
    secondary = NeonEmerald,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF003919),
    onSecondaryContainer = NeonEmerald,
    tertiary = ElectricAmber,
    background = AmoledBackground,
    onBackground = Color.White,
    surface = AmoledSurface,
    onSurface = Color.White,
    surfaceVariant = AmoledSurfaceVariant,
    onSurfaceVariant = Color(0xFFA3A3A3),
    outline = AmoledBorder
)

// 3. Clean White & Deep Midnight Navy (#00012F) Palette
private val WhiteNavyColorScheme = lightColorScheme(
    primary = DeepMidnightNavy,
    onPrimary = Color.White,
    primaryContainer = DeepNavyContainer,
    onPrimaryContainer = DeepMidnightNavy,
    secondary = Color(0xFF0284C7),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F2FE),
    onSecondaryContainer = Color(0xFF0369A1),
    tertiary = NeonEmerald,
    onTertiary = Color.White,
    background = LightWhiteBackground,
    onBackground = TextPrimaryWhiteNavy,
    surface = LightWhiteSurface,
    onSurface = TextPrimaryWhiteNavy,
    surfaceVariant = LightWhiteSurfaceVariant,
    onSurfaceVariant = TextSecondaryWhiteNavy,
    outline = LightWhiteBorder,
    outlineVariant = Color(0xFFE2E8F0)
)

// 4. Matrix Terminal
private val NeonMatrixColorScheme = darkColorScheme(
    primary = MatrixGreen,
    onPrimary = Color(0xFF002211),
    primaryContainer = Color(0xFF004422),
    onPrimaryContainer = Color(0xFF66FFA3),
    secondary = Color(0xFF34D399),
    onSecondary = Color(0xFF002211),
    secondaryContainer = Color(0xFF064E3B),
    onSecondaryContainer = Color(0xFFA7F3D0),
    tertiary = ElectricAmber,
    background = MatrixBackground,
    onBackground = Color(0xFFECFDF5),
    surface = MatrixSurface,
    onSurface = Color(0xFFECFDF5),
    surfaceVariant = MatrixSurfaceVariant,
    onSurfaceVariant = Color(0xFF6EE7B7),
    outline = MatrixBorder
)

// 5. Deep Tactical Navy
private val DeepNavyColorScheme = darkColorScheme(
    primary = TacticalTeal,
    onPrimary = Color(0xFF002B2B),
    primaryContainer = Color(0xFF004D4D),
    onPrimaryContainer = Color(0xFF80FFE8),
    secondary = Color(0xFF38BDF8),
    onSecondary = Color(0xFF082F49),
    secondaryContainer = Color(0xFF075985),
    onSecondaryContainer = Color(0xFFBAE6FD),
    tertiary = ElectricAmber,
    background = NavyBackground,
    onBackground = Color(0xFFF1F5F9),
    surface = NavySurface,
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = NavySurfaceVariant,
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = NavyBorder
)

fun getThemeColorScheme(themeMode: AppThemeMode, isSystemDark: Boolean = false): ColorScheme {
    return when (themeMode) {
        AppThemeMode.SYSTEM -> if (isSystemDark) CyberDarkColorScheme else WhiteNavyColorScheme
        AppThemeMode.WHITE_NAVY -> WhiteNavyColorScheme
        AppThemeMode.CYBER_DARK -> CyberDarkColorScheme
        AppThemeMode.AMOLED_BLACK -> AmoledBlackColorScheme
        AppThemeMode.NEON_MATRIX -> NeonMatrixColorScheme
        AppThemeMode.DEEP_NAVY -> DeepNavyColorScheme
    }
}

@Composable
fun MyApplicationTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val colorScheme = getThemeColorScheme(themeMode, isSystemDark)
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
