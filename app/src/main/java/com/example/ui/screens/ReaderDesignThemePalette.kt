package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ReaderDesignTheme
import com.example.ui.theme.AmiriFontFamily

data class ReaderPalette(
    val canvasBackground: Color,
    val cardBackground: Color,
    val bookmarkedCardBackground: Color,
    val syarahBackground: Color,
    val quoteBackground: Color,
    val quoteTextColor: Color,
    val footnoteBackground: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val arabicText: Color,
    val primaryAccent: Color,
    val secondaryAccent: Color,
    val borderColor: Color,
    val bookmarkedBorderColor: Color,
    val dividerColor: Color,
    val badgeBackground: Color,
    val badgeText: Color,
    val bookmarkedBadgeBackground: Color,
    val bookmarkedBadgeText: Color
)

@Composable
fun rememberReaderPalette(theme: ReaderDesignTheme): ReaderPalette {
    val scheme = MaterialTheme.colorScheme
    return when (theme) {
        ReaderDesignTheme.DEFAULT -> ReaderPalette(
            canvasBackground = scheme.background,
            cardBackground = scheme.surface,
            bookmarkedCardBackground = scheme.tertiaryContainer.copy(alpha = 0.25f),
            syarahBackground = scheme.primaryContainer.copy(alpha = 0.35f),
            quoteBackground = scheme.tertiaryContainer.copy(alpha = 0.55f),
            quoteTextColor = scheme.onTertiaryContainer,
            footnoteBackground = scheme.surfaceVariant.copy(alpha = 0.65f),
            textPrimary = scheme.onSurface,
            textSecondary = scheme.onSurfaceVariant,
            arabicText = scheme.onSurface,
            primaryAccent = scheme.primary,
            secondaryAccent = scheme.secondary,
            borderColor = scheme.primary.copy(alpha = 0.22f),
            bookmarkedBorderColor = scheme.tertiary,
            dividerColor = scheme.outlineVariant,
            badgeBackground = scheme.primaryContainer,
            badgeText = scheme.onPrimaryContainer,
            bookmarkedBadgeBackground = scheme.tertiaryContainer,
            bookmarkedBadgeText = scheme.onTertiaryContainer
        )

        ReaderDesignTheme.KITAB_KUNING -> ReaderPalette(
            canvasBackground = Color(0xFFF6E7BE),
            cardBackground = Color(0xFFFDF3D7),
            bookmarkedCardBackground = Color(0xFFFBE7AE),
            syarahBackground = Color(0xFFEEE0B4),
            quoteBackground = Color(0xFFF3DDA2),
            quoteTextColor = Color(0xFF44310A),
            footnoteBackground = Color(0xFFEADAB0),
            textPrimary = Color(0xFF2B2010),
            textSecondary = Color(0xFF614C29),
            arabicText = Color(0xFF1F1609),
            primaryAccent = Color(0xFF1B5E3A),
            secondaryAccent = Color(0xFF8A5A12),
            borderColor = Color(0xFFD5BC7C),
            bookmarkedBorderColor = Color(0xFFB37B12),
            dividerColor = Color(0xFFDDC894),
            badgeBackground = Color(0xFFE5D29A),
            badgeText = Color(0xFF3E2B07),
            bookmarkedBadgeBackground = Color(0xFFD9A638),
            bookmarkedBadgeText = Color(0xFF2A1D03)
        )

        ReaderDesignTheme.SEPIA -> ReaderPalette(
            canvasBackground = Color(0xFFEDE3D2),
            cardBackground = Color(0xFFF8F2E7),
            bookmarkedCardBackground = Color(0xFFF3E4CA),
            syarahBackground = Color(0xFFEAE0CE),
            quoteBackground = Color(0xFFEAD9BF),
            quoteTextColor = Color(0xFF4B3621),
            footnoteBackground = Color(0xFFE3D8C5),
            textPrimary = Color(0xFF35281C),
            textSecondary = Color(0xFF6D5844),
            arabicText = Color(0xFF271C12),
            primaryAccent = Color(0xFF7B4B27),
            secondaryAccent = Color(0xFF2F6B4F),
            borderColor = Color(0xFFD4C4AD),
            bookmarkedBorderColor = Color(0xFFB06D2D),
            dividerColor = Color(0xFFDDD0BC),
            badgeBackground = Color(0xFFE5D5BE),
            badgeText = Color(0xFF47311B),
            bookmarkedBadgeBackground = Color(0xFFD49A5A),
            bookmarkedBadgeText = Color(0xFF2B1B0A)
        )

        ReaderDesignTheme.NIGHT_FOCUS -> ReaderPalette(
            canvasBackground = Color(0xFF0D1411),
            cardBackground = Color(0xFF16211C),
            bookmarkedCardBackground = Color(0xFF212E22),
            syarahBackground = Color(0xFF1A2A23),
            quoteBackground = Color(0xFF262A1B),
            quoteTextColor = Color(0xFFF2DDA6),
            footnoteBackground = Color(0xFF1E2924),
            textPrimary = Color(0xFFE5EEE9),
            textSecondary = Color(0xFFAABBB2),
            arabicText = Color(0xFFF5F8F6),
            primaryAccent = Color(0xFF7ADBA7),
            secondaryAccent = Color(0xFFF0C978),
            borderColor = Color(0xFF2C3E35),
            bookmarkedBorderColor = Color(0xFFF0C978),
            dividerColor = Color(0xFF2B3B33),
            badgeBackground = Color(0xFF1E3A2D),
            badgeText = Color(0xFFBDF7D4),
            bookmarkedBadgeBackground = Color(0xFF4F3E12),
            bookmarkedBadgeText = Color(0xFFFDF0CE)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReaderDesignSwatchSelector(
    selectedTheme: ReaderDesignTheme,
    onSelectTheme: (ReaderDesignTheme) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ReaderDesignTheme.entries.forEach { theme ->
            val palette = rememberReaderPalette(theme)
            val isSelected = selectedTheme == theme
            Surface(
                color = palette.cardBackground,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else palette.borderColor
                ),
                modifier = Modifier
                    .widthIn(min = 140.dp)
                    .heightIn(min = 56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onSelectTheme(theme) }
                    .testTag("reader_design_${theme.name}")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(palette.canvasBackground)
                            .border(1.dp, palette.borderColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Dipilih",
                                tint = palette.primaryAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Text(
                                text = "ع",
                                fontFamily = AmiriFontFamily,
                                fontSize = 16.sp,
                                color = palette.arabicText,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Column {
                        Text(
                            text = theme.label,
                            style = MaterialTheme.typography.labelLarge,
                            color = palette.textPrimary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = theme.shortLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = palette.textSecondary,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
