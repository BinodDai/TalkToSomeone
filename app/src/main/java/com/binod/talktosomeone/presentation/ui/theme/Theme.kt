package com.binod.talktosomeone.presentation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryVariantLight,
    onPrimaryContainer = OnPrimaryLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    tertiary = Info,
    onTertiary = OnPrimaryLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = Gray50,
    onSurfaceVariant = Gray700,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorLight,
    onErrorContainer = OnErrorLight,
    outline = Gray300,
    outlineVariant = Gray200,
    scrim = Gray900,
    inverseSurface = Gray800,
    inverseOnSurface = BackgroundLight,
    inversePrimary = PrimaryDark,
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryVariantDark,
    onPrimaryContainer = OnPrimaryDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    tertiary = Info,
    onTertiary = OnPrimaryDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = Gray800,
    onSurfaceVariant = Gray300,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorDark,
    onErrorContainer = OnErrorDark,
    outline = Gray600,
    outlineVariant = Gray700,
    scrim = OnBackgroundDark,
    inverseSurface = Gray50,
    inverseOnSurface = BackgroundDark,
    inversePrimary = PrimaryLight,
)

@Composable
fun TalkToSomeoneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    CompositionLocalProvider(LocalDimensions provides Dimensions()) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(20.dp)
)

data class Dimensions(
    val default: Dp = 0.dp,
    val spaceExtraSmall: Dp = 4.dp,
    val spaceSmall: Dp = 8.dp,
    val spaceMedium: Dp = 16.dp,
    val spaceLarge: Dp = 24.dp,
    val spaceExtraLarge: Dp = 32.dp,

    // Padding
    val paddingExtraExtraSmall: Dp = 2.dp,
    val paddingExtraSmall: Dp = 4.dp,
    val paddingSmall: Dp = 8.dp,
    val padding12: Dp = 12.dp,
    val paddingMedium: Dp = 16.dp,
    val paddingLarge: Dp = 24.dp,
    val paddingExtraLarge: Dp = 32.dp,

    // Margins
    val marginSmall: Dp = 8.dp,
    val marginMedium: Dp = 16.dp,
    val marginLarge: Dp = 24.dp,
    val marginExtraLarge: Dp = 32.dp,

    // Component Sizes
    val buttonHeight: Dp = 48.dp,
    val buttonHeightSmall: Dp = 36.dp,
    val buttonHeightLarge: Dp = 56.dp,

    val cardElevation: Dp = 4.dp,
    val cardElevationPressed: Dp = 2.dp,

    // Border Radius
    val radiusSmall: Dp = 8.dp,
    val radiusMedium: Dp = 12.dp,
    val radiusLarge: Dp = 16.dp,
    val radiusExtraLarge: Dp = 20.dp,
    val radiusCircular: Dp = 50.dp,

    // Icon Sizes
    val iconSmall: Dp = 16.dp,
    val iconMedium: Dp = 24.dp,
    val iconLarge: Dp = 32.dp,
    val iconExtraLarge: Dp = 48.dp,

    // Avatar Sizes
    val avatarSmall: Dp = 32.dp,
    val avatarMedium: Dp = 40.dp,
    val avatarLarge: Dp = 64.dp,
    val avatarExtraLarge: Dp = 96.dp,

    // Card Sizes
    val cardMinHeight: Dp = 120.dp,
    val cardMaxWidth: Dp = 400.dp,

    // Bottom Navigation
    val bottomNavHeight: Dp = 80.dp,
    val topBarHeight: Dp = 56.dp,

    // Message Bubble
    val messageBubbleMaxWidth: Dp = 280.dp,
    val messageBubbleMinHeight: Dp = 40.dp,

    // Connection Cards
    val connectionCardHeight: Dp = 120.dp,
    val connectionCardWidth: Dp = 160.dp,
)

val LocalDimensions = compositionLocalOf { Dimensions() }

val dimensions: Dimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalDimensions.current