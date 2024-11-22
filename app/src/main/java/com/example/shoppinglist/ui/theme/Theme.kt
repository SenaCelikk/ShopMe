package com.example.shoppinglist.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Wight,
    background = Black,
    surface = LightGray,
    onPrimary = GREEN,
    onSecondary = PurpleGrey80,
    onTertiary = RED,
    onBackground = Wight,
    onSurface = Wight,
)

@Composable
fun ShoppingListTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content
    )
}