package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.FathulQoribCatalog
import com.example.data.FathulQoribContent
import com.example.model.Chapter
import com.example.viewmodel.NgajiDashboardData
import kotlin.math.roundToInt

data class ChapterAnalyticsItem(
    val chapter: Chapter,
    val completedLessons: Int,
    val totalLessons: Int,
    val percent: Float,
    val isCompleted: Boolean,
    val nextLessonId: String?
)

data class StudyAnalyticsSummary(
    val completedChaptersCount: Int,
    val totalChaptersCount: Int,
    val chapterCompletionPercent: Float,
    val completedLessonsCount: Int,
    val totalLessonsCount: Int,
    val lessonCompletionPercent: Float,
    val chapterItems: List<ChapterAnalyticsItem>
)

fun computeStudyAnalytics(data: NgajiDashboardData): StudyAnalyticsSummary {
    val completedLessonIds = data.progressList
        .filter { it.status == "COMPLETED" }
        .map { it.lessonId }
        .toSet()

    val chapters = FathulQoribCatalog.chapters
    val chapterItems = chapters.map { chapter ->
        val lessons = FathulQoribContent.lessonsForChapter(chapter.id)
        val total = lessons.size.coerceAtLeast(1)
        val done = lessons.count { it.id in completedLessonIds }
        val pct = (done.toFloat() / total.toFloat()).coerceIn(0f, 1f)
        val isComplete = lessons.isNotEmpty() && done >= lessons.size
        val nextLesson = lessons.firstOrNull { it.id !in completedLessonIds } ?: lessons.firstOrNull()
        ChapterAnalyticsItem(
            chapter = chapter,
            completedLessons = done,
            totalLessons = lessons.size,
            percent = pct,
            isCompleted = isComplete,
            nextLessonId = nextLesson?.id
        )
    }

    val totalChapters = chapters.size.coerceAtLeast(1)
    val completedChapters = chapterItems.count { it.isCompleted }
    val chapterPct = (completedChapters.toFloat() / totalChapters.toFloat()).coerceIn(0f, 1f)

    val allLessons = FathulQoribContent.allLessons
    val totalLessons = allLessons.size.coerceAtLeast(1)
    val completedLessons = completedLessonIds.size.coerceAtMost(totalLessons)
    val lessonPct = (completedLessons.toFloat() / totalLessons.toFloat()).coerceIn(0f, 1f)

    return StudyAnalyticsSummary(
        completedChaptersCount = completedChapters,
        totalChaptersCount = totalChapters,
        chapterCompletionPercent = chapterPct,
        completedLessonsCount = completedLessons,
        totalLessonsCount = totalLessons,
        lessonCompletionPercent = lessonPct,
        chapterItems = chapterItems
    )
}

@Composable
fun StudyAnalytics(
    data: NgajiDashboardData,
    onOpenLesson: (String) -> Unit,
    onMarkChapterCompleted: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val summary = remember(data.progressList) { computeStudyAnalytics(data) }
    val animatedChapterPct by animateFloatAsState(
        targetValue = summary.chapterCompletionPercent,
        animationSpec = tween(durationMillis = 550),
        label = "chapter_completion_anim"
    )
    val animatedLessonPct by animateFloatAsState(
        targetValue = summary.lessonCompletionPercent,
        animationSpec = tween(durationMillis = 550),
        label = "lesson_completion_anim"
    )
    val chapterPctDisplay = (summary.chapterCompletionPercent * 100).roundToInt()

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("study_analytics_dashboard")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header + Firestore Sync Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Analytics,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "StudyAnalytics • Progres Bab",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDone,
                            contentDescription = "Tersimpan di Firestore",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Firestore",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Overall Chapter Completion Circular + Linear Progress Indicator Card
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier.size(84.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = { animatedChapterPct },
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            strokeWidth = 8.dp,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier
                                .size(84.dp)
                                .testTag("study_analytics_circular_progress")
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$chapterPctDisplay%",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Bab",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "${summary.completedChaptersCount} dari ${summary.totalChaptersCount} Bab Selesai",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${summary.completedLessonsCount} dari ${summary.totalLessonsCount} materi (${(summary.lessonCompletionPercent * 100).roundToInt()}%) selesai",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        LinearProgressIndicator(
                            progress = { animatedLessonPct },
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .testTag("study_analytics_overall_linear_progress")
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // Per-Chapter Completion Breakdown
            Text(
                text = "Rincian Penyelesaian Bab Fathul Qorib",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                summary.chapterItems.forEach { item ->
                    val pctInt = (item.percent * 100).roundToInt()
                    Surface(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                item.nextLessonId?.let { onOpenLesson(it) }
                            }
                            .testTag("analytics_chapter_${item.chapter.id}")
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${item.chapter.category.title} • Hlm. ${item.chapter.startPage}-${item.chapter.endPage}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = item.chapter.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = if (item.isCompleted) {
                                        MaterialTheme.colorScheme.primaryContainer
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant
                                    },
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = if (item.isCompleted) "Khatam 100%" else "$pctInt%",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (item.isCompleted) {
                                            MaterialTheme.colorScheme.onPrimaryContainer
                                        } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        },
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            LinearProgressIndicator(
                                progress = { item.percent },
                                color = if (item.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                strokeCap = StrokeCap.Round,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(7.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .testTag("analytics_progress_${item.chapter.id}")
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${item.completedLessons}/${item.totalLessons} materi selesai",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (!item.isCompleted) {
                                        OutlinedButton(
                                            onClick = { onMarkChapterCompleted(item.chapter.id) },
                                            shape = RoundedCornerShape(10.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                                            modifier = Modifier
                                                .heightIn(min = 48.dp)
                                                .testTag("mark_chapter_complete_${item.chapter.id}")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Tandai Selesai", style = MaterialTheme.typography.labelMedium)
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                    }
                                    TextButton(
                                        onClick = { item.nextLessonId?.let { onOpenLesson(it) } },
                                        modifier = Modifier
                                            .heightIn(min = 48.dp)
                                            .testTag("open_chapter_${item.chapter.id}")
                                    ) {
                                        Text(
                                            text = if (item.isCompleted) "Ulangi" else "Baca",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp)
                                        )
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
