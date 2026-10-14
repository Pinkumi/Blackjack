package com.example.card.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val TemaVerde = darkColorScheme(
    primary = VerdeBoton,
    secondary = VerdeTitulo,
    tertiary = VerdeCarta,
    background = VerdeFondo,
    surface = VerdeFondo,
    onPrimary = VerdeTextoBoton,
    onSecondary = VerdeTexto,
    onBackground = VerdeTexto,
    onSurface = VerdeTexto
)

private val TemaAzul = darkColorScheme(
    primary = AzulBoton,
    secondary = AzulTitulo,
    tertiary = AzulCarta,
    background = AzulFondo,
    surface = AzulFondo,
    onPrimary = AzulTextoBoton,
    onSecondary = AzulTexto,
    onBackground = AzulTexto,
    onSurface = AzulTexto
)

private val TemaRojo = darkColorScheme(
    primary = RojoBoton,
    secondary = RojoTitulo,
    tertiary = RojoCarta,
    background = RojoFondo,
    surface = RojoFondo,
    onPrimary = RojoTextoBoton,
    onSecondary = RojoTexto,
    onBackground = RojoTexto,
    onSurface = RojoTexto
)

@Composable
fun CardTheme(tema: Int = 0, content: @Composable () -> Unit) {
    val colores = when (tema) {
        1 -> TemaAzul
        2 -> TemaRojo
        else -> TemaVerde
    }

    MaterialTheme(
        colorScheme = colores,
        typography = Typography,
        content = content
    )
}