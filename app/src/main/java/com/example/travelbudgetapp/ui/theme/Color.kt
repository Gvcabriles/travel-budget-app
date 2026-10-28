package com.example.travelbudgetapp.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val md_theme_light_primary = Color(0xFF00796B)
val md_theme_light_onPrimary = Color.White
val md_theme_dark_primary = Color(0xFF80CBC4)
val md_theme_dark_onPrimary = Color.Black

val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    secondary = Color(0xFF009688),
    background = Color(0xFFF6F6F6),
    surface = Color.White,
)

val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    secondary = Color(0xFF4DB6AC),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
)
