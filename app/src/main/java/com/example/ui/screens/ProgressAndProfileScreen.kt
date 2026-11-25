package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import com.example.R
import com.example.auth.signOutUser
import com.example.data.AppSettings
import com.example.data.AppThemeMode
import com.example.data.FathulQoribContent
import com.example.data.ReaderDesignTheme
import com.example.model.Bookmark
import com.example.model.ReaderMode
import com.example.model.UserRole
import com.example.viewmodel.NgajiDashboardData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

private data class WeeklyDayPoint(
    val label: String,
    val minutes: Int,
    val isToday: Boolean
)

private data class AchievementBadge(
    val id: String,
    val title: String,
    val subtitle: String,
    val unlocked: Boolean
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProgressAndProfileScreen(
    data: NgajiDashboardData,
    appSettings: AppSettings = AppSettings(),
    customPhotoPath: String? = null,
    isBusy: Boolean = false,
    onOpenLesson: (String) -> Unit,
    onMarkChapterCompleted: (String) -> Unit,
    onRemoveBookmark: (Bookmark) -> Unit,
    onDeleteNote: (String) -> Unit,
    onUpdateProfileSettings: (Long, String) -> Unit,
    onPickProfilePhoto: (Uri) -> Unit = {},
    onRemoveProfilePhoto: () -> Unit = {},
    onChangeThemeMode: (AppThemeMode) -> Unit = {},
    onChangeDefaultReaderMode: (ReaderMode) -> Unit = {},
    onChangeReaderDesignTheme: (ReaderDesignTheme) -> Unit = {},
    onChangeFontScale: (Float) -> Unit = {},
    onToggleBookmarkBanner: (Boolean) -> Unit = {},
    onOpenCmsPortal: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }
    val fallbackPhotoUrl = remember { Firebase.auth.currentUser?.photoUrl?.toString() }
    val headerPhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let { onPickProfilePhoto(it) }
        }
    )

    val profile = data.profile
    val role = UserRole.fromRaw(profile?.role)
    val allLessons = FathulQoribContent.allLessons
    val completedLessons = remember(data.progressList) {
        data.progressList.filter { it.status == "COMPLETED" }
    }
    val completedCount = completedLessons.size
    val totalLessons = allLessons.size.coerceAtLeast(1)

    val avgScore = remember(data.quizAttempts) {
        if (data.quizAttempts.isEmpty()) 0
        else data.quizAttempts.map { it.score.toInt() }.average().roundToInt()
    }
    val totalStudyMinutes = remember(data.progressList, profile?.studyMinutesToday) {
        val fromLessons = (data.progressList.sumOf { it.studySeconds } / 60L).toInt()
        val today = (profile?.studyMinutesToday ?: 0L).toInt()
        maxOf(fromLessons, today)
    }
    val streakCount = (profile?.streakCount ?: 1L).toInt()
    val dailyTarget = (profile?.dailyTargetMinutes ?: 20L).toInt().coerceIn(5, 240)
    val studyToday = (profile?.studyMinutesToday ?: 0L).toInt()

    var showEditProfileSheet by remember { mutableStateOf(false) }
    var showSettingsSheet by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    var savedTab by remember { mutableIntStateOf(0) } // 0 = Bookmarks, 1 = Catatan

    val weeklyPoints = remember(data.progressList, data.quizAttempts, studyToday) {
        buildWeeklyPoints(data, studyToday)
    }

    val badges = remember(completedLessons, data.quizAttempts, streakCount) {
        val completedIds = completedLessons.map { it.lessonId }.toSet()
        val thaharahDone = FathulQoribContent.lessonsForChapter("ch_thaharah_air").all { it.id in completedIds }
        val sholatDone = FathulQoribContent.lessonsForChapter("ch_sholat_rukun").all { it.id in completedIds }
        val passedQuizCount = data.quizAttempts.count { it.passed }
        listOf(
            AchievementBadge(
                id = "badge_thaharah",
                title = "Pemula Thaharah",
                subtitle = "Menyelesaikan seluruh materi pembagian air & thaharah",
                unlocked = thaharahDone || completedIds.isNotEmpty()
            ),
            AchievementBadge(
                id = "badge_sholat",
                title = "Khatam Bab Sholat",
                subtitle = "Menuntaskan materi syarat dan 18 rukun sholat",
                unlocked = sholatDone
            ),
            AchievementBadge(
                id = "badge_quiz",
                title = "Pejuang Kuis",
                subtitle = "Lulus minimal 1 kali evaluasi pemahaman bab",
                unlocked = passedQuizCount >= 1
            ),
            AchievementBadge(
                id = "badge_istiqomah",
                title = "Istiqomah Ngaji",
                subtitle = "Menjaga target & konsistensi belajar",
                unlocked = streakCount >= 3 || completedCount >= 4
            )
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Profile Header Card
        item {
            val displayName = profile?.displayName?.ifBlank { "Santri NgajiFiqih" } ?: "Santri NgajiFiqih"
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    ProfileAvatar(
                        displayName = displayName,
                        customPhotoPath = customPhotoPath,
                        fallbackPhotoUrl = fallbackPhotoUrl,
                        size = 58.dp,
                        showCameraBadge = true,
                        onClick = {
                            headerPhotoPicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.testTag("profile_avatar_button")
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = displayName,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        val profileEmail = profile?.email
                        if (!profileEmail.isNullOrBlank()) {
                            Text(
                                text = profileEmail,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = role.labelId,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            if (data.isBootstrappedAdmin) {
                                Surface(
                                    color = MaterialTheme.colorScheme.tertiaryContainer,
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = "Admin Utama",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                    OutlinedButton(
                        onClick = { showEditProfileSheet = true },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        modifier = Modifier
                            .heightIn(min = 48.dp)
                            .testTag("open_edit_profile_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.profile_edit_title),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Edit", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }

        // 1b. Profile Action Menu (Edit Profile, Settings, CMS, Logout)
        item {
            ProfileMenuCard(
                profile = profile,
                appSettings = appSettings,
                dailyTargetMinutes = dailyTarget,
                onOpenEditProfile = { showEditProfileSheet = true },
                onOpenSettings = { showSettingsSheet = true },
                onOpenCmsPortal = onOpenCmsPortal,
                onRequestLogout = { showLogoutDialog = true }
            )
        }

        // 2. StudyAnalytics Dashboard (Chapter Completion Percentage & Progress Indicators)
        item {
            StudyAnalytics(
                data = data,
                onOpenLesson = onOpenLesson,
                onMarkChapterCompleted = onMarkChapterCompleted
            )
        }

        // 3. Analytics Summary Grid
        item {
            Text(
                text = stringResource(R.string.profile_progress_title),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val isWide = maxWidth >= 600.dp
                if (isWide) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatTile(
                            icon = Icons.AutoMirrored.Filled.MenuBook,
                            value = "$completedCount / $totalLessons",
                            label = "Materi Selesai",
                            modifier = Modifier.weight(1f)
                        )
                        StatTile(
                            icon = Icons.Default.Quiz,
                            value = "$avgScore%",
                            label = "Rata-Rata Kuis",
                            modifier = Modifier.weight(1f)
                        )
                        StatTile(
                            icon = Icons.Default.Schedule,
                            value = "$totalStudyMinutes mnt",
                            label = "Total Waktu",
                            modifier = Modifier.weight(1f)
                        )
                        StatTile(
                            icon = Icons.Default.LocalFireDepartment,
                            value = "$streakCount Hari",
                            label = "Streak Belajar",
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            StatTile(
                                icon = Icons.AutoMirrored.Filled.MenuBook,
                                value = "$completedCount / $totalLessons",
                                label = "Materi Selesai",
                                modifier = Modifier.weight(1f)
                            )
                            StatTile(
                                icon = Icons.Default.Quiz,
                                value = "$avgScore%",
                                label = "Rata-Rata Kuis",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            StatTile(
                                icon = Icons.Default.Schedule,
                                value = "$totalStudyMinutes mnt",
                                label = "Total Waktu",
                                modifier = Modifier.weight(1f)
                            )
                            StatTile(
                                icon = Icons.Default.LocalFireDepartment,
                                value = "$streakCount Hari",
                                label = "Streak Belajar",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // 3. Weekly Activity Bar Chart (Custom Canvas)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.profile_weekly_activity),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "Target: $dailyTarget mnt/hari",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    WeeklyActivityBarChart(
                        points = weeklyPoints,
                        dailyTargetMinutes = dailyTarget,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        weeklyPoints.forEach { pt ->
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${pt.minutes}m",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (pt.isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = if (pt.isToday) FontWeight.Bold else FontWeight.Normal
                                )
                                Text(
                                    text = pt.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (pt.isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Achievement Badges
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = "Lencana Pencapaian",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        badges.forEach { badge ->
                            Surface(
                                color = if (badge.unlocked) MaterialTheme.colorScheme.tertiaryContainer
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = if (badge.unlocked) Icons.Default.Verified else Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = if (badge.unlocked) MaterialTheme.colorScheme.onTertiaryContainer
                                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Column {
                                        Text(
                                            text = badge.title,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = if (badge.unlocked) MaterialTheme.colorScheme.onTertiaryContainer
                                            else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = if (badge.unlocked) "Tercapai" else "Belum tercapai",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Bookmarks & Personal Study Notes Switcher
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = stringResource(R.string.profile_bookmarks_notes),
                    style = MaterialTheme.typography.titleMedium
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = savedTab == 0,
                        onClick = { savedTab = 0 },
                        leadingIcon = {
                            Icon(Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(16.dp))
                        },
                        label = { Text("Bookmark (${data.bookmarks.size})") },
                        modifier = Modifier.testTag("tab_bookmarks")
                    )
                    FilterChip(
                        selected = savedTab == 1,
                        onClick = { savedTab = 1 },
                        leadingIcon = {
                            Icon(Icons.Default.NoteAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                        },
                        label = { Text("Catatan (${data.notes.size})") },
                        modifier = Modifier.testTag("tab_notes")
                    )
                }
            }
        }

        if (savedTab == 0) {
            if (data.bookmarks.isEmpty()) {
                item {
                    EmptySavedStateCard(
                        title = "Belum Ada Bookmark",
                        subtitle = "Tandai materi penting di Layar Belajar agar mudah dibuka kembali kapan saja."
                    )
                }
            } else {
                items(data.bookmarks, key = { "bm_${it.lessonId}" }) { bookmark ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { onOpenLesson(bookmark.lessonId) }
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                        }
                    }
                }
            }
        } else {
            if (data.notes.isEmpty()) {
                item {
                    EmptySavedStateCard(
                        title = "Belum Ada Catatan Pribadi",
                        subtitle = "Tambahkan catatan belajar saat membaca syarah Fathul Qorib untuk merangkum faedah penting."
                    )
                }
            } else {
                items(data.notes, key = { "nt_${it.id}" }) { note ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { onOpenLesson(note.lessonId) }
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NoteAlt,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "${note.lessonTitle} • Hlm. ${note.sourcePage}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                if (!note.highlightText.isNullOrBlank()) {
                                    Text(
                                        text = "\"${note.highlightText}\"",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
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

    if (showEditProfileSheet) {
        EditProfileModalSheet(
            profile = profile,
            role = role,
            dailyTargetMinutes = dailyTarget,
            customPhotoPath = customPhotoPath,
            fallbackPhotoUrl = fallbackPhotoUrl,
            isBusy = isBusy,
            onPickPhoto = onPickProfilePhoto,
            onRemovePhoto = onRemoveProfilePhoto,
            onDismiss = { showEditProfileSheet = false },
            onSave = onUpdateProfileSettings
        )
    }

    if (showSettingsSheet) {
        SettingsModalSheet(
            appSettings = appSettings,
            onChangeThemeMode = onChangeThemeMode,
            onChangeDefaultReaderMode = onChangeDefaultReaderMode,
            onChangeReaderDesignTheme = onChangeReaderDesignTheme,
            onChangeFontScale = onChangeFontScale,
            onToggleBookmarkBanner = onToggleBookmarkBanner,
            onDismiss = { showSettingsSheet = false }
        )
    }

    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirmLogout = {
                showLogoutDialog = false
                signOutUser(credentialManager = credentialManager, scope = scope)
            }
        )
    }
}

@Composable
private fun StatTile(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun WeeklyActivityBarChart(
    points: List<WeeklyDayPoint>,
    dailyTargetMinutes: Int,
    modifier: Modifier = Modifier
) {
    val barColor = MaterialTheme.colorScheme.secondary
    val todayBarColor = MaterialTheme.colorScheme.primary
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val targetLineColor = MaterialTheme.colorScheme.tertiary

    Canvas(modifier = modifier) {
        if (points.isEmpty()) return@Canvas
        val maxMinutes = maxOf(
            dailyTargetMinutes,
            points.maxOf { it.minutes },
            15
        ).toFloat()

        val barCount = points.size
        val slotWidth = size.width / barCount
        val barWidth = (slotWidth * 0.46f).coerceAtLeast(12f)
        val topPadding = 12f
        val bottomPadding = 6f
        val chartHeight = (size.height - topPadding - bottomPadding).coerceAtLeast(24f)

        // Draw target reference line
        val targetRatio = (dailyTargetMinutes.toFloat() / maxMinutes).coerceIn(0f, 1f)
        val targetY = topPadding + chartHeight * (1f - targetRatio)
        drawLine(
            color = targetLineColor,
            start = Offset(0f, targetY),
            end = Offset(size.width, targetY),
            strokeWidth = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
        )

        points.forEachIndexed { index, point ->
            val xLeft = index * slotWidth + (slotWidth - barWidth) / 2f
            // Track background
            drawRoundRect(
                color = trackColor,
                topLeft = Offset(xLeft, topPadding),
                size = Size(barWidth, chartHeight),
                cornerRadius = CornerRadius(12f, 12f)
            )
            // Value bar
            val ratio = (point.minutes.toFloat() / maxMinutes).coerceIn(0f, 1f)
            val barHeight = (chartHeight * ratio).coerceAtLeast(if (point.minutes > 0) 10f else 0f)
            if (barHeight > 0f) {
                drawRoundRect(
                    color = if (point.isToday) todayBarColor else barColor,
                    topLeft = Offset(xLeft, topPadding + (chartHeight - barHeight)),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(12f, 12f)
                )
            }
        }
    }
}

@Composable
private fun EmptySavedStateCard(
    title: String,
    subtitle: String
) {
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
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_empty_state),
                contentDescription = stringResource(R.string.cd_empty_state),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun buildWeeklyPoints(
    data: NgajiDashboardData,
    studyToday: Int
): List<WeeklyDayPoint> {
    val dayNames = mapOf(
        Calendar.SUNDAY to "Aha",
        Calendar.MONDAY to "Sen",
        Calendar.TUESDAY to "Sel",
        Calendar.WEDNESDAY to "Rab",
        Calendar.THURSDAY to "Kam",
        Calendar.FRIDAY to "Jum",
        Calendar.SATURDAY to "Sab"
    )
    val buckets = IntArray(7)
    val todayCal = Calendar.getInstance()
    todayCal.set(Calendar.HOUR_OF_DAY, 0)
    todayCal.set(Calendar.MINUTE, 0)
    todayCal.set(Calendar.SECOND, 0)
    todayCal.set(Calendar.MILLISECOND, 0)
    val todayStartMillis = todayCal.timeInMillis
    val dayMillis = 24L * 60L * 60L * 1000L

    data.progressList.forEach { progress ->
        val ts = progress.updatedAt?.toDate()?.time ?: return@forEach
        val daysAgo = ((todayStartMillis - normalizeDayMillis(ts)) / dayMillis).toInt()
        if (daysAgo in 0..6) {
            val idx = 6 - daysAgo
            buckets[idx] += (progress.studySeconds / 60L).toInt().coerceAtLeast(1)
        }
    }

    data.quizAttempts.forEach { attempt ->
        val ts = attempt.createdAt?.toDate()?.time ?: return@forEach
        val daysAgo = ((todayStartMillis - normalizeDayMillis(ts)) / dayMillis).toInt()
        if (daysAgo in 0..6) {
            val idx = 6 - daysAgo
            buckets[idx] += (attempt.durationSeconds / 60L).toInt().coerceAtLeast(2)
        }
    }

    buckets[6] = maxOf(buckets[6], studyToday)

    return (0..6).map { idx ->
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, idx - 6)
        val dow = cal.get(Calendar.DAY_OF_WEEK)
        val label = if (idx == 6) "Ini" else (dayNames[dow] ?: String.format(Locale.US, "H-%d", 6 - idx))
        WeeklyDayPoint(
            label = label,
            minutes = buckets[idx],
            isToday = idx == 6
        )
    }
}

private fun normalizeDayMillis(timeMillis: Long): Long {
    val cal = Calendar.getInstance()
    cal.timeInMillis = timeMillis
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}
