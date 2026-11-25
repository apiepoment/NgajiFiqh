package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.ViewColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ReaderDesignTheme
import com.example.model.BlockType
import com.example.model.Bookmark
import com.example.model.Chapter
import com.example.model.GlossaryTerm
import com.example.model.Lesson
import com.example.model.LessonBlock
import com.example.model.ReaderMode
import com.example.model.StudyNote
import com.example.ui.theme.AmiriFontFamily
import kotlin.math.roundToInt

data class LessonSearchMatch(
    val lesson: Lesson,
    val matchedExcerpt: String?,
    val matchedPage: Int
)

fun searchFathulQoribLessons(
    query: String,
    lessons: List<Lesson>
): List<LessonSearchMatch> {
    val q = query.trim().lowercase()
    if (q.isEmpty()) return emptyList()
    return lessons.mapNotNull { lesson ->
        val titleMatch = lesson.title.lowercase().contains(q) ||
            lesson.kitabTitle.lowercase().contains(q) ||
            lesson.summary.lowercase().contains(q)
        val matchingBlock = lesson.blocks.firstOrNull { it.content.lowercase().contains(q) }
        if (titleMatch || matchingBlock != null) {
            LessonSearchMatch(
                lesson = lesson,
                matchedExcerpt = matchingBlock?.content ?: lesson.summary,
                matchedPage = matchingBlock?.sourcePage ?: lesson.sourcePage
            )
        } else {
            null
        }
    }
}

fun searchGlossaryTerms(
    query: String,
    terms: List<GlossaryTerm>
): List<GlossaryTerm> {
    val q = query.trim().lowercase()
    if (q.isEmpty()) return emptyList()
    return terms.filter { term ->
        term.term.lowercase().contains(q) ||
            term.arabicTerm.lowercase().contains(q) ||
            term.definition.lowercase().contains(q)
    }
}

fun searchChapters(
    query: String,
    chapters: List<Chapter>
): List<Chapter> {
    val q = query.trim().lowercase()
    if (q.isEmpty()) return emptyList()
    return chapters.filter { chapter ->
        chapter.title.lowercase().contains(q) ||
            chapter.subtitle.lowercase().contains(q)
    }
}

@Composable
fun ReaderSearchResultsCard(
    query: String,
    matchingChapters: List<Chapter>,
    matchingLessons: List<LessonSearchMatch>,
    matchingTerms: List<GlossaryTerm>,
    onSelectChapter: (Chapter) -> Unit,
    onSelectLesson: (Lesson) -> Unit,
    onSelectGlossaryTerm: (GlossaryTerm) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (matchingChapters.isEmpty() && matchingLessons.isEmpty() && matchingTerms.isEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "Tidak ditemukan hasil untuk \"$query\"",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "Coba kata kunci lain seperti wudlu, sholat, zakat, dua qullah, riba, atau khiyar.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        if (matchingChapters.isNotEmpty()) {
            Text(
                text = "Bab Kitab (${matchingChapters.size})",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            matchingChapters.forEach { chapter ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onSelectChapter(chapter) }
                        .testTag("search_chapter_${chapter.id}")
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${chapter.category.title} • Hlm. ${chapter.startPage}-${chapter.endPage}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(text = chapter.title, style = MaterialTheme.typography.titleSmall)
                        Text(
                            text = chapter.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        if (matchingTerms.isNotEmpty()) {
            Text(
                text = "Istilah Fiqih / Glosarium (${matchingTerms.size})",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            matchingTerms.forEach { term ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.45f)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onSelectGlossaryTerm(term) }
                        .testTag("search_term_${term.id}")
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = term.term,
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                            Text(
                                text = "Hlm. ${term.sourcePage}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                            Text(
                                text = term.arabicTerm,
                                fontFamily = AmiriFontFamily,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Text(
                            text = term.definition,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        if (matchingLessons.isNotEmpty()) {
            Text(
                text = "Materi & Teks Fathul Qorib (${matchingLessons.size})",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            matchingLessons.forEach { match ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onSelectLesson(match.lesson) }
                        .testTag("search_lesson_${match.lesson.id}")
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${match.lesson.kitabTitle} • Hlm. ${match.matchedPage}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(text = match.lesson.title, style = MaterialTheme.typography.titleSmall)
                        if (!match.matchedExcerpt.isNullOrBlank()) {
                            Text(
                                text = match.matchedExcerpt,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

sealed interface ReaderDisplayItem {
    val itemKey: String

    data class BilingualPair(
        val arabic: LessonBlock,
        val translation: LessonBlock
    ) : ReaderDisplayItem {
        override val itemKey: String = "pair_${arabic.id}_${translation.id}"
    }

    data class SingleBlock(
        val block: LessonBlock
    ) : ReaderDisplayItem {
        override val itemKey: String = "blk_${block.id}"
    }
}

fun buildReaderDisplayItems(
    blocks: List<LessonBlock>,
    mode: ReaderMode
): List<ReaderDisplayItem> {
    return when (mode) {
        ReaderMode.ARABIC -> {
            blocks.filter { it.blockType == BlockType.HEADING || it.blockType == BlockType.ARABIC }
                .map { ReaderDisplayItem.SingleBlock(it) }
        }
        ReaderMode.TRANSLATION -> {
            blocks.filter { it.blockType != BlockType.ARABIC }
                .map { ReaderDisplayItem.SingleBlock(it) }
        }
        ReaderMode.DUAL -> {
            val result = mutableListOf<ReaderDisplayItem>()
            var idx = 0
            while (idx < blocks.size) {
                val current = blocks[idx]
                val next = blocks.getOrNull(idx + 1)
                if (current.blockType == BlockType.ARABIC && next?.blockType == BlockType.TRANSLATION) {
                    result.add(ReaderDisplayItem.BilingualPair(current, next))
                    idx += 2
                } else {
                    result.add(ReaderDisplayItem.SingleBlock(current))
                    idx += 1
                }
            }
            result
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReadabilityControlsPanel(
    fontScale: Float,
    readerMode: ReaderMode,
    readerDesignTheme: ReaderDesignTheme = ReaderDesignTheme.DEFAULT,
    sideBySideEnabled: Boolean,
    onSetFontScale: (Float) -> Unit,
    onSelectDesignTheme: (ReaderDesignTheme) -> Unit = {},
    onToggleSideBySide: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val presets = listOf(
        "Standar" to 1.0f,
        "Besar" to 1.35f,
        "Ekstra" to 1.7f,
        "Jumbo" to 2.0f
    )

    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(R.string.reader_design_title),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            ReaderDesignSwatchSelector(
                selectedTheme = readerDesignTheme,
                onSelectTheme = onSelectDesignTheme
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.reader_text_size_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "${(fontScale * 100).roundToInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Slider(
                value = fontScale.coerceIn(0.85f, 2.0f),
                onValueChange = onSetFontScale,
                valueRange = 0.85f..2.0f,
                modifier = Modifier.testTag("reader_font_slider")
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                presets.forEach { (label, value) ->
                    val isSelected = kotlin.math.abs(fontScale - value) < 0.08f
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSetFontScale(value) },
                        label = { Text(label) },
                        modifier = Modifier.testTag("reader_preset_${label.lowercase()}")
                    )
                }
            }

            if (readerMode == ReaderMode.DUAL) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = sideBySideEnabled,
                        onClick = { onToggleSideBySide(true) },
                        leadingIcon = {
                            Icon(Icons.Default.ViewColumn, contentDescription = null, modifier = Modifier.size(16.dp))
                        },
                        label = { Text(stringResource(R.string.reader_layout_side_by_side)) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("reader_layout_side_by_side_chip")
                    )
                    FilterChip(
                        selected = !sideBySideEnabled,
                        onClick = { onToggleSideBySide(false) },
                        leadingIcon = {
                            Icon(Icons.Default.ViewAgenda, contentDescription = null, modifier = Modifier.size(16.dp))
                        },
                        label = { Text(stringResource(R.string.reader_layout_stacked)) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("reader_layout_stacked_chip")
                    )
                }
            }
        }
    }
}

@Composable
fun BilingualPairCard(
    arabic: LessonBlock,
    translation: LessonBlock,
    sideBySide: Boolean,
    fontScale: Float,
    bookmarkedPage: Int?,
    palette: ReaderPalette = rememberReaderPalette(ReaderDesignTheme.DEFAULT),
    onBookmarkPage: (Int) -> Unit,
    onHighlightToNote: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isThisPageBookmarked = bookmarkedPage == arabic.sourcePage
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isThisPageBookmarked) {
                palette.bookmarkedCardBackground
            } else {
                palette.cardBackground
            }
        ),
        border = BorderStroke(
            width = if (isThisPageBookmarked) 1.5.dp else 1.dp,
            color = if (isThisPageBookmarked) {
                palette.bookmarkedBorderColor
            } else {
                palette.borderColor
            }
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("bilingual_pair_${arabic.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = if (isThisPageBookmarked) {
                        palette.bookmarkedBadgeBackground
                    } else {
                        palette.badgeBackground
                    },
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = if (isThisPageBookmarked) {
                            "Bookmark Aktif • Hlm. ${arabic.sourcePage}"
                        } else {
                            "Arab & Terjemah • Hlm. ${arabic.sourcePage}"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isThisPageBookmarked) {
                            palette.bookmarkedBadgeText
                        } else {
                            palette.badgeText
                        },
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(
                        onClick = { onHighlightToNote(translation.content) },
                        modifier = Modifier.testTag("pair_note_button_${arabic.id}")
                    ) {
                        Text(
                            text = "Catat Faidah",
                            style = MaterialTheme.typography.labelMedium,
                            color = palette.primaryAccent
                        )
                    }
                    IconButton(
                        onClick = { onBookmarkPage(arabic.sourcePage) },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("pair_bookmark_button_${arabic.id}")
                    ) {
                        Icon(
                            imageVector = if (isThisPageBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = if (isThisPageBookmarked) {
                                stringResource(R.string.reader_bookmarked_text)
                            } else {
                                stringResource(R.string.reader_bookmark_text)
                            },
                            tint = if (isThisPageBookmarked) {
                                palette.bookmarkedBorderColor
                            } else {
                                palette.textSecondary
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (sideBySide) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    // Left column: Indonesian Translation (LTR)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Terjemahan Indonesia",
                            style = MaterialTheme.typography.labelMedium,
                            color = palette.secondaryAccent
                        )
                        Text(
                            text = translation.content,
                            fontSize = (15f * fontScale).sp,
                            lineHeight = (24f * fontScale).sp,
                            color = palette.textPrimary
                        )
                    }

                    VerticalDivider(
                        color = palette.dividerColor,
                        modifier = Modifier.fillMaxHeight()
                    )

                    // Right column: Arabic Matan / Syarah (RTL, Amiri)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Matan / Teks Arab",
                            style = MaterialTheme.typography.labelMedium,
                            color = palette.primaryAccent
                        )
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                            Text(
                                text = arabic.content,
                                fontFamily = AmiriFontFamily,
                                fontSize = (21.5f * fontScale).sp,
                                lineHeight = (40f * fontScale).sp,
                                color = palette.arabicText,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            } else {
                // Stacked dual layout for narrow screens / maximum line width
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Matan / Teks Arab",
                            style = MaterialTheme.typography.labelMedium,
                            color = palette.primaryAccent
                        )
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                            Text(
                                text = arabic.content,
                                fontFamily = AmiriFontFamily,
                                fontSize = (23f * fontScale).sp,
                                lineHeight = (44f * fontScale).sp,
                                color = palette.arabicText,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    HorizontalDivider(color = palette.dividerColor)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Terjemahan Indonesia",
                            style = MaterialTheme.typography.labelMedium,
                            color = palette.secondaryAccent
                        )
                        Text(
                            text = translation.content,
                            fontSize = (16f * fontScale).sp,
                            lineHeight = (26f * fontScale).sp,
                            color = palette.textPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LessonBlockCard(
    block: LessonBlock,
    fontScale: Float,
    bookmarkedPage: Int? = null,
    palette: ReaderPalette = rememberReaderPalette(ReaderDesignTheme.DEFAULT),
    onBookmarkPage: ((Int) -> Unit)? = null,
    onHighlightToNote: (String) -> Unit
) {
    val isThisPageBookmarked = bookmarkedPage == block.sourcePage
    when (block.blockType) {
        BlockType.HEADING -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = block.content,
                    fontSize = (20f * fontScale.coerceAtMost(1.5f)).sp,
                    lineHeight = (28f * fontScale.coerceAtMost(1.5f)).sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.primaryAccent,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    color = palette.badgeBackground,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Hlm. ${block.sourcePage}",
                        style = MaterialTheme.typography.labelSmall,
                        color = palette.badgeText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        BlockType.ARABIC -> {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isThisPageBookmarked) {
                        palette.bookmarkedCardBackground
                    } else {
                        palette.cardBackground
                    }
                ),
                border = BorderStroke(
                    width = if (isThisPageBookmarked) 1.5.dp else 1.dp,
                    color = if (isThisPageBookmarked) {
                        palette.bookmarkedBorderColor
                    } else {
                        palette.borderColor
                    }
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isThisPageBookmarked) {
                                "Bookmark Aktif • Matan Arab Hlm. ${block.sourcePage}"
                            } else {
                                "Matan / Teks Arab • Hlm. ${block.sourcePage}"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isThisPageBookmarked) {
                                palette.bookmarkedBadgeText
                            } else {
                                palette.primaryAccent
                            }
                        )
                        if (onBookmarkPage != null) {
                            IconButton(
                                onClick = { onBookmarkPage(block.sourcePage) },
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("block_bookmark_button_${block.id}")
                            ) {
                                Icon(
                                    imageVector = if (isThisPageBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = if (isThisPageBookmarked) {
                                        stringResource(R.string.reader_bookmarked_text)
                                    } else {
                                        stringResource(R.string.reader_bookmark_text)
                                    },
                                    tint = if (isThisPageBookmarked) {
                                        palette.bookmarkedBorderColor
                                    } else {
                                        palette.textSecondary
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        Text(
                            text = block.content,
                            fontFamily = AmiriFontFamily,
                            fontSize = (23f * fontScale).sp,
                            lineHeight = (44f * fontScale).sp,
                            color = palette.arabicText,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        BlockType.TRANSLATION -> {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isThisPageBookmarked) {
                        palette.bookmarkedCardBackground
                    } else {
                        palette.cardBackground
                    }
                ),
                border = if (isThisPageBookmarked) {
                    BorderStroke(1.5.dp, palette.bookmarkedBorderColor)
                } else {
                    BorderStroke(1.dp, palette.borderColor.copy(alpha = 0.65f))
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onHighlightToNote(block.content) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Terjemahan Indonesia",
                                style = MaterialTheme.typography.labelMedium,
                                color = palette.secondaryAccent
                            )
                            Text(
                                text = "Ketuk teks untuk catat • Hlm. ${block.sourcePage}",
                                style = MaterialTheme.typography.labelSmall,
                                color = palette.textSecondary
                            )
                        }
                        if (onBookmarkPage != null) {
                            IconButton(
                                onClick = { onBookmarkPage(block.sourcePage) },
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("block_bookmark_button_${block.id}")
                            ) {
                                Icon(
                                    imageVector = if (isThisPageBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = if (isThisPageBookmarked) {
                                        stringResource(R.string.reader_bookmarked_text)
                                    } else {
                                        stringResource(R.string.reader_bookmark_text)
                                    },
                                    tint = if (isThisPageBookmarked) {
                                        palette.bookmarkedBorderColor
                                    } else {
                                        palette.textSecondary
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = block.content,
                        fontSize = (16f * fontScale).sp,
                        lineHeight = (26f * fontScale).sp,
                        color = palette.textPrimary
                    )
                }
            }
        }

        BlockType.EXPLANATION -> {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = palette.syarahBackground
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = palette.primaryAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Penjelasan / Syarah (Hlm. ${block.sourcePage})",
                            style = MaterialTheme.typography.labelLarge,
                            color = palette.primaryAccent
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = block.content,
                        fontSize = (15f * fontScale).sp,
                        lineHeight = (24f * fontScale).sp,
                        color = palette.textPrimary
                    )
                }
            }
        }

        BlockType.QUOTE -> {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = palette.quoteBackground
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatQuote,
                        contentDescription = null,
                        tint = palette.quoteTextColor
                    )
                    Text(
                        text = block.content,
                        fontSize = (14.5f * fontScale).sp,
                        lineHeight = (23f * fontScale).sp,
                        fontStyle = FontStyle.Italic,
                        color = palette.quoteTextColor
                    )
                }
            }
        }

        BlockType.FOOTNOTE -> {
            Surface(
                color = palette.footnoteBackground,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = block.content,
                    fontSize = (13.5f * fontScale).sp,
                    lineHeight = (20f * fontScale).sp,
                    color = palette.textSecondary,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun SyllabusPane(
    lessons: List<Lesson>,
    selectedLessonId: String,
    completedIds: Set<String>,
    bookmarkedIds: Set<String>,
    onSelectLesson: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Daftar Materi Fathul Qorib",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${completedIds.size} dari ${lessons.size} materi selesai",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        LazyColumn(
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(lessons, key = { it.id }) { item ->
                val selected = item.id == selectedLessonId
                Surface(
                    color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSelectLesson(item.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = if (item.id in completedIds) Icons.Default.CheckCircle else Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${item.kitabTitle} • Hlm. ${item.sourcePage}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        if (item.id in bookmarkedIds) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = "Bookmark",
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotesForLessonSection(
    notes: List<StudyNote>,
    onAddNote: () -> Unit,
    onDeleteNote: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Catatan Pribadi (${notes.size})",
                    style = MaterialTheme.typography.titleSmall
                )
                TextButton(onClick = onAddNote) {
                    Text("+ Tambah Catatan")
                }
            }

            if (notes.isEmpty()) {
                Text(
                    text = "Belum ada catatan untuk materi ini. Ketuk teks terjemahan atau tombol catatan untuk menulis faidah.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                notes.forEach { note ->
                    Surface(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                if (!note.highlightText.isNullOrBlank()) {
                                    Text(
                                        text = "\"${note.highlightText}\"",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontStyle = FontStyle.Italic,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Text(
                                    text = note.content,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            IconButton(
                                onClick = { onDeleteNote(note.id) },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Hapus catatan",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReaderBookmarksSheetContent(
    bookmarks: List<Bookmark>,
    currentLessonId: String,
    onOpenBookmark: (Bookmark) -> Unit,
    onRemoveBookmark: (Bookmark) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.reader_bookmarks_list_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = if (bookmarks.isEmpty()) {
                    "Ketuk ikon pita pada teks kitab untuk menandai bacaan terakhir Anda."
                } else {
                    "${bookmarks.size} bacaan tersimpan • ketuk untuk langsung melanjutkan"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        if (bookmarks.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.BookmarkBorder,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = "Belum ada bookmark teks kitab.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(bookmarks, key = { "sheet_bm_${it.lessonId}" }) { bookmark ->
                    val isCurrent = bookmark.lessonId == currentLessonId
                    Surface(
                        color = if (isCurrent) {
                            MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                        } else {
                            MaterialTheme.colorScheme.background
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { onOpenBookmark(bookmark) }
                            .testTag("reader_sheet_bookmark_${bookmark.lessonId}")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${bookmark.chapterTitle} • Hlm. ${bookmark.sourcePage}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = bookmark.lessonTitle,
                                    style = MaterialTheme.typography.titleSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            IconButton(
                                onClick = { onRemoveBookmark(bookmark) },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BookmarkRemove,
                                    contentDescription = "Hapus bookmark",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Lanjutkan baca",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
