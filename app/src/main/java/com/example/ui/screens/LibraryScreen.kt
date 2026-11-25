package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.FathulQoribCatalog
import com.example.data.FathulQoribContent
import com.example.model.EditorialStatus
import com.example.model.KitabCategory
import com.example.model.KitabSection
import com.example.model.Lesson
import com.example.ui.theme.AmiriFontFamily
import com.example.viewmodel.NgajiDashboardData

@Composable
fun LibraryScreen(
    data: NgajiDashboardData,
    onOpenLesson: (String) -> Unit,
    onOpenLessonAtPage: (String, Int) -> Unit = { id, _ -> onOpenLesson(id) },
    modifier: Modifier = Modifier
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf(KitabCategory.ALL) }
    var expandedKitabId by rememberSaveable { mutableStateOf("kitab_thaharah") }

    val completedIds = remember(data.progressList) {
        data.progressList.filter { it.status == "COMPLETED" }.map { it.lessonId }.toSet()
    }
    val bookmarkedIds = remember(data.bookmarks) {
        data.bookmarks.map { it.lessonId }.toSet()
    }

    val filteredKitabs = remember(searchQuery, selectedCategory) {
        val q = searchQuery.trim().lowercase()
        FathulQoribCatalog.kitabSections.filter { kitab ->
            val matchesCategory = selectedCategory == KitabCategory.ALL || kitab.category == selectedCategory
            val matchesQuery = q.isEmpty() ||
                kitab.title.lowercase().contains(q) ||
                kitab.summary.lowercase().contains(q) ||
                kitab.babTitles.any { it.lowercase().contains(q) }
            matchesCategory && matchesQuery
        }
    }

    val matchingLessons = remember(searchQuery, selectedCategory) {
        val q = searchQuery.trim().lowercase()
        if (q.isEmpty()) emptyList()
        else FathulQoribContent.allLessons.filter { lesson ->
            lesson.title.lowercase().contains(q) ||
                lesson.summary.lowercase().contains(q) ||
                lesson.kitabTitle.lowercase().contains(q) ||
                lesson.blocks.any { it.content.lowercase().contains(q) }
        }
    }

    val matchingGlossaryTerms = remember(searchQuery) {
        searchGlossaryTerms(searchQuery, FathulQoribCatalog.glossaryTerms)
    }

    val verifiedCmsLessons = remember(data.editorialSubmissions) {
        data.editorialSubmissions.filter { it.status == EditorialStatus.VERIFIED.name }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.library_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Struktur kurikulum terverifikasi dari 380 halaman Kitab Fathul Qorib",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(stringResource(R.string.library_search_hint)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Hapus pencarian")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("library_search_input")
            )
        }

        // Category Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(KitabCategory.entries) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat.title) },
                        modifier = Modifier.testTag("filter_chip_${cat.name.lowercase()}")
                    )
                }
            }
        }

        // Featured Book Card (only Fathul Qorib — no fake unfilled books!)
        if (searchQuery.isBlank()) {
            val latestBookmark = data.bookmarks.firstOrNull()
            if (latestBookmark != null) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.55f)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                onOpenLessonAtPage(latestBookmark.lessonId, latestBookmark.sourcePage.toInt())
                            }
                            .testTag("library_resume_bookmark_card")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${stringResource(R.string.reader_resume_last_read)} • Hlm. ${latestBookmark.sourcePage}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                                Text(
                                    text = latestBookmark.lessonTitle,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Lanjutkan bacaan terakhir",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            item {
                FeaturedBookDetailCard(
                    onStartLearning = {
                        if (latestBookmark != null) {
                            onOpenLessonAtPage(latestBookmark.lessonId, latestBookmark.sourcePage.toInt())
                        } else {
                            val firstUnfinished = FathulQoribContent.allLessons.firstOrNull { it.id !in completedIds }
                                ?: FathulQoribContent.allLessons.first()
                            onOpenLesson(firstUnfinished.id)
                        }
                    }
                )
            }
        }

        // Direct search results for Lessons & Terms
        if (matchingGlossaryTerms.isNotEmpty()) {
            item {
                Text(
                    text = "Istilah Fiqih (${matchingGlossaryTerms.size})",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            items(matchingGlossaryTerms, key = { "lib_term_${it.id}" }) { term ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.45f)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(term.term, style = MaterialTheme.typography.titleSmall)
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
                        Text(term.definition, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        if (matchingLessons.isNotEmpty()) {
            item {
                Text(
                    text = "Hasil Pencarian Materi (${matchingLessons.size})",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            items(matchingLessons, key = { "search_${it.id}" }) { lesson ->
                LessonRowItem(
                    lesson = lesson,
                    isCompleted = lesson.id in completedIds,
                    isBookmarked = lesson.id in bookmarkedIds,
                    onClick = { onOpenLesson(lesson.id) }
                )
            }
        }

        // Verified Community/Editorial Additions from Firestore
        if (verifiedCmsLessons.isNotEmpty() && searchQuery.isBlank()) {
            item {
                Text(
                    text = "Tambahan Materi Terverifikasi Ustaz (${verifiedCmsLessons.size})",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            items(verifiedCmsLessons, key = { "cms_${it.id}" }) { sub ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${sub.chapterTitle} • Hlm. ${sub.sourcePage}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(sub.lessonTitle, style = MaterialTheme.typography.titleSmall)
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                            Text(
                                text = sub.arabicText,
                                fontFamily = AmiriFontFamily,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Text(sub.translation, style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = "Disusun: ${sub.authorName} • Diverifikasi: ${sub.reviewerName ?: "Ustaz"}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Daftar Kitab & Bab (16 Kitab sections from Fathul Qorib PDF)
        item {
            Text(
                text = "${stringResource(R.string.library_chapters_title)} (${filteredKitabs.size})",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        items(filteredKitabs, key = { it.id }) { kitab ->
            val chaptersForKitab = FathulQoribCatalog.chapters.filter { it.kitabId == kitab.id }
            val lessonsForKitab = chaptersForKitab.flatMap { FathulQoribContent.lessonsForChapter(it.id) }
            KitabAccordionCard(
                kitab = kitab,
                lessons = lessonsForKitab,
                completedIds = completedIds,
                bookmarkedIds = bookmarkedIds,
                expanded = expandedKitabId == kitab.id || searchQuery.isNotBlank(),
                onToggleExpand = {
                    expandedKitabId = if (expandedKitabId == kitab.id) "" else kitab.id
                },
                onOpenLesson = onOpenLesson
            )
        }
    }
}

@Composable
private fun FeaturedBookDetailCard(onStartLearning: () -> Unit) {
    val book = FathulQoribCatalog.mainBook
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_cover_fathul_qorib),
                    contentDescription = stringResource(R.string.cd_book_cover),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(92.dp)
                        .height(126.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = stringResource(R.string.library_featured_badge),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }
                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        Text(
                            text = book.arabicTitle,
                            fontFamily = AmiriFontFamily,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = "Syarih: ${book.author}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Matan: ${book.originalMatanAuthor}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Text(
                text = book.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${book.publisher} • ${book.totalPages} Hal",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = onStartLearning,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("library_start_learning_button")
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.library_start_learning))
                }
            }
        }
    }
}

@Composable
private fun KitabAccordionCard(
    kitab: KitabSection,
    lessons: List<Lesson>,
    completedIds: Set<String>,
    bookmarkedIds: Set<String>,
    expanded: Boolean,
    onToggleExpand: () -> Unit,
    onOpenLesson: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${kitab.sequence}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Hlm. ${kitab.startPage}-${kitab.endPage}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        if (lessons.isNotEmpty()) {
                            val done = lessons.count { it.id in completedIds }
                            Text(
                                text = "$done/${lessons.size} materi",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Text(
                        text = kitab.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        Text(
                            text = kitab.arabicTitle,
                            fontFamily = AmiriFontFamily,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Tutup rincian" else "Buka rincian",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Text(
                        text = kitab.summary,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Cakupan Bab dalam Kitab:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    kitab.babTitles.forEach { bab ->
                        Text(
                            text = "• $bab",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (lessons.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Materi Pembelajaran Interaktif:",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        lessons.forEach { lesson ->
                            LessonRowItem(
                                lesson = lesson,
                                isCompleted = lesson.id in completedIds,
                                isBookmarked = lesson.id in bookmarkedIds,
                                onClick = { onOpenLesson(lesson.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonRowItem(
    lesson: Lesson,
    isCompleted: Boolean,
    isBookmarked: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("lesson_item_${lesson.id}")
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.AutoMirrored.Filled.MenuBook,
                contentDescription = null,
                tint = if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(20.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Hlm. ${lesson.sourcePage} • ${lesson.estimatedMinutes} menit",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isBookmarked) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = "Bookmark",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
