package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TextDecrease
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.TextIncrease
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.FathulQoribCatalog
import com.example.data.FathulQoribContent
import com.example.data.ReaderDesignTheme
import com.example.model.Bookmark
import com.example.model.GlossaryTerm
import com.example.model.Lesson
import com.example.model.LessonProgress
import com.example.model.ReaderMode
import com.example.ui.theme.AmiriFontFamily
import com.example.viewmodel.NgajiDashboardData
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LearningReaderScreen(
    data: NgajiDashboardData,
    selectedLessonId: String,
    targetScrollPage: Int?,
    readerMode: ReaderMode,
    readerDesignTheme: ReaderDesignTheme = ReaderDesignTheme.DEFAULT,
    fontScale: Float,
    onSelectLesson: (String, Int?) -> Unit,
    onClearTargetScrollPage: () -> Unit,
    onTrackLessonOpened: (Lesson, LessonProgress?) -> Unit,
    onChangeReaderMode: (ReaderMode) -> Unit,
    onChangeReaderDesignTheme: (ReaderDesignTheme) -> Unit = {},
    onAdjustFontScale: (Float) -> Unit,
    onSetFontScale: (Float) -> Unit,
    onMarkComplete: (Lesson, LessonProgress?) -> Unit,
    onToggleBookmark: (Lesson, Int, Boolean, Int?) -> Unit,
    onRemoveBookmark: (Bookmark) -> Unit,
    onSaveNote: (Lesson, String?, String) -> Unit,
    onDeleteNote: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val allLessons = FathulQoribContent.allLessons
    val currentIndex = allLessons.indexOfFirst { it.id == selectedLessonId }.takeIf { it >= 0 } ?: 0
    val lesson = allLessons[currentIndex]
    val prevLesson = allLessons.getOrNull(currentIndex - 1)
    val nextLesson = allLessons.getOrNull(currentIndex + 1)

    val completedIds = remember(data.progressList) {
        data.progressList.filter { it.status == "COMPLETED" }.map { it.lessonId }.toSet()
    }
    val progressRecord = remember(data.progressList, lesson.id) {
        data.progressList.firstOrNull { it.lessonId == lesson.id }
    }
    val isCompleted = lesson.id in completedIds
    val currentBookmark = remember(data.bookmarks, lesson.id) {
        data.bookmarks.firstOrNull { it.lessonId == lesson.id }
    }
    val isBookmarked = currentBookmark != null
    val currentBookmarkedPage = currentBookmark?.sourcePage?.toInt()
    val latestBookmark = data.bookmarks.firstOrNull()

    val lessonNotes = remember(data.notes, lesson.id) {
        data.notes.filter { it.lessonId == lesson.id }
    }

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var showSyllabusSheet by remember { mutableStateOf(false) }
    var showBookmarksSheet by remember { mutableStateOf(false) }
    var showReadabilityPanel by rememberSaveable { mutableStateOf(false) }
    var userSideBySidePref by rememberSaveable { mutableStateOf<Boolean?>(null) }
    var activeGlossaryTerm by remember { mutableStateOf<GlossaryTerm?>(null) }
    var showAddNoteDialog by remember { mutableStateOf(false) }
    var noteHighlightInput by remember { mutableStateOf("") }
    var noteContentInput by remember { mutableStateOf("") }

    val matchingChapters = remember(searchQuery) {
        searchChapters(searchQuery, FathulQoribCatalog.chapters)
    }
    val matchingLessons = remember(searchQuery) {
        searchFathulQoribLessons(searchQuery, allLessons)
    }
    val matchingTerms = remember(searchQuery) {
        searchGlossaryTerms(searchQuery, FathulQoribCatalog.glossaryTerms)
    }

    val displayItems = remember(lesson.blocks, readerMode) {
        buildReaderDisplayItems(lesson.blocks, readerMode)
    }
    val palette = rememberReaderPalette(readerDesignTheme)
    val listState = rememberLazyListState()

    LaunchedEffect(lesson.id) {
        onTrackLessonOpened(lesson, progressRecord)
    }

    LaunchedEffect(lesson.id, targetScrollPage, displayItems) {
        if (targetScrollPage != null) {
            val targetIdx = displayItems.indexOfFirst { item ->
                when (item) {
                    is ReaderDisplayItem.BilingualPair -> item.arabic.sourcePage == targetScrollPage
                    is ReaderDisplayItem.SingleBlock -> item.block.sourcePage == targetScrollPage
                }
            }
            if (targetIdx >= 0) {
                listState.animateScrollToItem(targetIdx)
            }
            onClearTargetScrollPage()
        } else {
            listState.scrollToItem(0)
        }
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isTabletSyllabusSplit = maxWidth >= 840.dp
        val readerPaneWidth = if (isTabletSyllabusSplit) maxWidth - 280.dp else maxWidth
        val sideBySideEnabled = userSideBySidePref ?: (readerPaneWidth >= 520.dp)

        Row(modifier = Modifier.fillMaxSize()) {
            // Left Split Syllabus Panel on Wide Tablets
            if (isTabletSyllabusSplit) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 1.dp,
                    modifier = Modifier
                        .width(280.dp)
                        .fillMaxHeight()
                ) {
                    SyllabusPane(
                        lessons = allLessons,
                        selectedLessonId = lesson.id,
                        completedIds = completedIds,
                        bookmarkedIds = data.bookmarks.map { it.lessonId }.toSet(),
                        onSelectLesson = { onSelectLesson(it, null) }
                    )
                }
            }

            // Main Fathul Qorib Reader Pane
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(palette.canvasBackground)
            ) {
                // Top Reader Header + Search Bar + Style Strip + Tabbed Mode Switcher
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                if (!isTabletSyllabusSplit) {
                                    IconButton(
                                        onClick = { showSyllabusSheet = true },
                                        modifier = Modifier
                                            .size(48.dp)
                                            .testTag("reader_syllabus_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                                            contentDescription = "Daftar bab dan materi"
                                        )
                                    }
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${lesson.kitabTitle} • Hlm. ${lesson.sourcePage}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = lesson.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        onToggleBookmark(
                                            lesson,
                                            currentBookmarkedPage ?: lesson.sourcePage,
                                            isBookmarked,
                                            null
                                        )
                                    },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .testTag("reader_bookmark_button")
                                ) {
                                    Icon(
                                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = if (isBookmarked) "Hapus bookmark" else "Simpan bookmark",
                                        tint = if (isBookmarked) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(
                                    onClick = { showBookmarksSheet = true },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .testTag("reader_bookmarks_list_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bookmarks,
                                        contentDescription = stringResource(R.string.reader_bookmarks_list_title),
                                        tint = if (data.bookmarks.isNotEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        noteHighlightInput = ""
                                        noteContentInput = ""
                                        showAddNoteDialog = true
                                    },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .testTag("reader_add_note_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.EditNote,
                                        contentDescription = stringResource(R.string.reader_add_note)
                                    )
                                }
                            }
                        }

                        // Top Search Bar for Chapters, Text Passages & Fiqh Terms
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.reader_search_hint),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null)
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(
                                        onClick = { searchQuery = "" },
                                        modifier = Modifier
                                            .size(48.dp)
                                            .testTag("reader_search_clear")
                                    ) {
                                        Icon(Icons.Default.Clear, contentDescription = "Hapus pencarian")
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                .testTag("reader_search_input")
                        )

                        // Quick Resume Latest Bookmark Pill (when user has a saved bookmark on another page/lesson)
                        if (latestBookmark != null && searchQuery.isBlank() && latestBookmark.lessonId != lesson.id) {
                            Surface(
                                color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        onSelectLesson(latestBookmark.lessonId, latestBookmark.sourcePage.toInt())
                                    }
                                    .testTag("reader_resume_bookmark_banner")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bookmark,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "${stringResource(R.string.reader_resume_last_read)}: ${latestBookmark.lessonTitle} (Hlm. ${latestBookmark.sourcePage})",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Buka bacaan terakhir",
                                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        // Quick Reader Design & Text Size Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AssistChip(
                                onClick = { showReadabilityPanel = !showReadabilityPanel },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Palette,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = "Desain: ${readerDesignTheme.shortLabel}",
                                        fontWeight = FontWeight.Medium
                                    )
                                },
                                modifier = Modifier
                                    .heightIn(min = 40.dp)
                                    .testTag("reader_design_toggle")
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { onAdjustFontScale(-0.15f) },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .testTag("reader_font_decrease")
                                ) {
                                    Icon(Icons.Default.TextDecrease, contentDescription = "Perkecil huruf")
                                }
                                AssistChip(
                                    onClick = { showReadabilityPanel = !showReadabilityPanel },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.TextFields,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = "${(fontScale * 100).roundToInt()}%",
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    },
                                    modifier = Modifier.testTag("reader_readability_toggle")
                                )
                                IconButton(
                                    onClick = { onAdjustFontScale(0.15f) },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .testTag("reader_font_increase")
                                ) {
                                    Icon(Icons.Default.TextIncrease, contentDescription = "Perbesar huruf")
                                }
                            }
                        }

                        // Expandable Reader Design, Large-Text Readability & Dual-Layout Control Drawer
                        AnimatedVisibility(visible = showReadabilityPanel) {
                            ReadabilityControlsPanel(
                                fontScale = fontScale,
                                readerMode = readerMode,
                                readerDesignTheme = readerDesignTheme,
                                sideBySideEnabled = sideBySideEnabled,
                                onSetFontScale = onSetFontScale,
                                onSelectDesignTheme = onChangeReaderDesignTheme,
                                onToggleSideBySide = { userSideBySidePref = it },
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        // Tabbed View Switcher for Dual (Side-by-Side) / Arabic / Indonesian Translation
                        val selectedTabIndex = ReaderMode.entries.indexOf(readerMode).coerceAtLeast(0)
                        SecondaryTabRow(
                            selectedTabIndex = selectedTabIndex,
                            containerColor = MaterialTheme.colorScheme.surface
                        ) {
                            ReaderMode.entries.forEach { mode ->
                                val tabLabel = when (mode) {
                                    ReaderMode.DUAL -> stringResource(R.string.reader_mode_dual)
                                    ReaderMode.ARABIC -> stringResource(R.string.reader_mode_arabic)
                                    ReaderMode.TRANSLATION -> stringResource(R.string.reader_mode_translation)
                                }
                                Tab(
                                    selected = readerMode == mode,
                                    onClick = { onChangeReaderMode(mode) },
                                    text = {
                                        Text(
                                            text = tabLabel,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    },
                                    modifier = Modifier.testTag("reader_mode_${mode.name.lowercase()}")
                                )
                            }
                        }
                    }
                }

                // Lesson Content Scroll Area (or live Search Results when query active)
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    if (searchQuery.isNotBlank()) {
                        item(key = "reader_search_results") {
                            ReaderSearchResultsCard(
                                query = searchQuery.trim(),
                                matchingChapters = matchingChapters,
                                matchingLessons = matchingLessons,
                                matchingTerms = matchingTerms,
                                onSelectChapter = { chapter ->
                                    val firstLesson = FathulQoribContent.lessonsForChapter(chapter.id).firstOrNull()
                                    if (firstLesson != null) {
                                        onSelectLesson(firstLesson.id, null)
                                    }
                                    searchQuery = ""
                                },
                                onSelectLesson = { matchedLesson ->
                                    onSelectLesson(matchedLesson.id, null)
                                    searchQuery = ""
                                },
                                onSelectGlossaryTerm = { term ->
                                    activeGlossaryTerm = term
                                }
                            )
                        }
                    } else {
                        items(displayItems, key = { it.itemKey }) { item ->
                            when (item) {
                                is ReaderDisplayItem.BilingualPair -> {
                                    BilingualPairCard(
                                        arabic = item.arabic,
                                        translation = item.translation,
                                        sideBySide = sideBySideEnabled,
                                        fontScale = fontScale,
                                        bookmarkedPage = currentBookmarkedPage,
                                        palette = palette,
                                        onBookmarkPage = { page ->
                                            onToggleBookmark(lesson, page, isBookmarked, currentBookmarkedPage)
                                        },
                                        onHighlightToNote = { excerpt ->
                                            noteHighlightInput = excerpt.take(280)
                                            noteContentInput = ""
                                            showAddNoteDialog = true
                                        }
                                    )
                                }

                                is ReaderDisplayItem.SingleBlock -> {
                                    LessonBlockCard(
                                        block = item.block,
                                        fontScale = fontScale,
                                        bookmarkedPage = currentBookmarkedPage,
                                        palette = palette,
                                        onBookmarkPage = { page ->
                                            onToggleBookmark(lesson, page, isBookmarked, currentBookmarkedPage)
                                        },
                                        onHighlightToNote = { excerpt ->
                                            noteHighlightInput = excerpt.take(280)
                                            noteContentInput = ""
                                            showAddNoteDialog = true
                                        }
                                    )
                                }
                            }
                        }

                        // Glossary Chips
                        val terms = lesson.glossaryIds.mapNotNull { FathulQoribCatalog.glossaryById(it) }
                        if (terms.isNotEmpty()) {
                            item {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = stringResource(R.string.reader_glossary_title),
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        FlowRow(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            terms.forEach { term ->
                                                AssistChip(
                                                    onClick = { activeGlossaryTerm = term },
                                                    label = { Text(term.term) },
                                                    leadingIcon = {
                                                        Icon(
                                                            Icons.Default.Info,
                                                            contentDescription = null,
                                                            modifier = Modifier.size(16.dp)
                                                        )
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Personal Study Notes for this Lesson
                        item {
                            NotesForLessonSection(
                                notes = lessonNotes,
                                onAddNote = {
                                    noteHighlightInput = ""
                                    noteContentInput = ""
                                    showAddNoteDialog = true
                                },
                                onDeleteNote = onDeleteNote
                            )
                        }

                        // Previous / Complete / Next Footer
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Button(
                                        onClick = { onMarkComplete(lesson, progressRecord) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isCompleted) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primary,
                                            contentColor = if (isCompleted) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimary
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .testTag("mark_lesson_complete_button")
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            if (isCompleted) stringResource(R.string.reader_completed_label)
                                            else stringResource(R.string.reader_mark_complete)
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = { prevLesson?.let { onSelectLesson(it.id, null) } },
                                            enabled = prevLesson != null,
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(48.dp)
                                                .testTag("reader_prev_button")
                                        ) {
                                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(stringResource(R.string.reader_prev_lesson))
                                        }
                                        Button(
                                            onClick = { nextLesson?.let { onSelectLesson(it.id, null) } },
                                            enabled = nextLesson != null,
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(48.dp)
                                                .testTag("reader_next_button")
                                        ) {
                                            Text(stringResource(R.string.reader_next_lesson))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showSyllabusSheet) {
        ModalBottomSheet(onDismissRequest = { showSyllabusSheet = false }) {
            SyllabusPane(
                lessons = allLessons,
                selectedLessonId = lesson.id,
                completedIds = completedIds,
                bookmarkedIds = data.bookmarks.map { it.lessonId }.toSet(),
                onSelectLesson = { id ->
                    onSelectLesson(id, null)
                    showSyllabusSheet = false
                },
                modifier = Modifier.height(480.dp)
            )
        }
    }

    if (showBookmarksSheet) {
        ModalBottomSheet(onDismissRequest = { showBookmarksSheet = false }) {
            ReaderBookmarksSheetContent(
                bookmarks = data.bookmarks,
                currentLessonId = lesson.id,
                onOpenBookmark = { bm ->
                    onSelectLesson(bm.lessonId, bm.sourcePage.toInt())
                    showBookmarksSheet = false
                },
                onRemoveBookmark = onRemoveBookmark,
                modifier = Modifier.height(440.dp)
            )
        }
    }

    activeGlossaryTerm?.let { term ->
        AlertDialog(
            onDismissRequest = { activeGlossaryTerm = null },
            title = { Text(term.term) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        Text(
                            text = term.arabicTerm,
                            fontFamily = AmiriFontFamily,
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Text(term.definition, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "Sumber: Fathul Qorib Hlm. ${term.sourcePage}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { activeGlossaryTerm = null }) {
                    Text("Tutup")
                }
            }
        )
    }

    if (showAddNoteDialog) {
        AlertDialog(
            onDismissRequest = { showAddNoteDialog = false },
            title = { Text("Catatan Belajar Pribadi") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = noteHighlightInput,
                        onValueChange = { noteHighlightInput = it },
                        label = { Text("Kutipan / Highlight (Opsional)") },
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = noteContentInput,
                        onValueChange = { noteContentInput = it },
                        label = { Text("Isi Catatan / Faidah") },
                        minLines = 3,
                        maxLines = 5,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("note_content_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (noteContentInput.isNotBlank()) {
                            onSaveNote(lesson, noteHighlightInput.ifBlank { null }, noteContentInput)
                            showAddNoteDialog = false
                        }
                    },
                    enabled = noteContentInput.isNotBlank(),
                    modifier = Modifier.testTag("save_note_button")
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddNoteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}
