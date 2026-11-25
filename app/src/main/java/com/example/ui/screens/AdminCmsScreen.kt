package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.data.FathulQoribCatalog
import com.example.model.EditorialStatus
import com.example.model.EditorialSubmission
import com.example.model.UserProfile
import com.example.model.UserRole
import com.example.ui.theme.AmiriFontFamily
import com.example.viewmodel.NgajiDashboardData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCmsScreen(
    data: NgajiDashboardData,
    currentUserId: String,
    isBusy: Boolean,
    onBack: () -> Unit,
    onSaveEditorialDraft: (
        existingId: String?,
        chapterId: String,
        chapterTitle: String,
        lessonTitle: String,
        arabicText: String,
        translation: String,
        explanation: String,
        footnote: String?,
        sourcePage: Int,
        submitForReview: Boolean
    ) -> Unit,
    onReviewSubmission: (EditorialSubmission, Boolean, String) -> Unit,
    onDeleteDraft: (String) -> Unit,
    onUpdateUserRole: (String, UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val profile = data.profile
    val role = UserRole.fromRaw(profile?.role)
    val isAdmin = data.isBootstrappedAdmin || role == UserRole.ADMIN || role == UserRole.SUPER_ADMIN
    val isReviewer = isAdmin || role == UserRole.REVIEWER_USTAZ
    val isEditor = isReviewer || role == UserRole.CONTENT_EDITOR

    var selectedTab by remember { mutableIntStateOf(0) }
    var editingId by remember { mutableStateOf<String?>(null) }
    var showEditorForm by remember { mutableStateOf(false) }

    val defaultChapter = FathulQoribCatalog.chapters.first()
    var selectedChapter by remember { mutableStateOf(defaultChapter) }
    var chapterDropdownExpanded by remember { mutableStateOf(false) }
    var lessonTitleInput by remember { mutableStateOf("") }
    var sourcePageInput by remember { mutableStateOf("4") }
    var arabicInput by remember { mutableStateOf("") }
    var translationInput by remember { mutableStateOf("") }
    var explanationInput by remember { mutableStateOf("") }
    var footnoteInput by remember { mutableStateOf("") }

    fun populateEditor(item: EditorialSubmission?) {
        if (item == null) {
            editingId = null
            selectedChapter = defaultChapter
            lessonTitleInput = ""
            sourcePageInput = "4"
            arabicInput = ""
            translationInput = ""
            explanationInput = ""
            footnoteInput = ""
        } else {
            editingId = item.id
            selectedChapter = FathulQoribCatalog.chapters.firstOrNull { it.id == item.chapterId } ?: defaultChapter
            lessonTitleInput = item.lessonTitle
            sourcePageInput = item.sourcePage.toString()
            arabicInput = item.arabicText
            translationInput = item.translation
            explanationInput = item.explanation
            footnoteInput = item.footnote.orEmpty()
        }
        showEditorForm = true
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Header bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("cms_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali ke Profil"
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "CMS & Review Ustaz",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Alur editorial Fathul Qorib • ${role.labelId}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        val tabs = listOf("Materi CMS", "Review Ustaz", "Pengguna & Log")
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { idx, title ->
                Tab(
                    selected = selectedTab == idx,
                    onClick = { selectedTab = idx },
                    text = { Text(title, maxLines = 1) },
                    modifier = Modifier.testTag("cms_tab_$idx")
                )
            }
        }

        when (selectedTab) {
            0 -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        if (!isEditor) {
                            RoleRestrictionBanner(
                                title = "Mode Baca Saja (${role.labelId})",
                                message = "Hanya akun dengan peran Content Editor, Reviewer / Ustaz, atau Admin yang dapat menyusun dan mengajukan materi baru ke CMS."
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Daftar Konten Tambahan (${data.editorialSubmissions.size})",
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Button(
                                    onClick = {
                                        if (showEditorForm) showEditorForm = false
                                        else populateEditor(null)
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .height(48.dp)
                                        .testTag("toggle_cms_editor_button")
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (showEditorForm) "Tutup Form" else "Buat Materi")
                                }
                            }
                        }
                    }

                    if (isEditor && showEditorForm) {
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = if (editingId == null) "Susun Materi Baru (Fathul Qorib)" else "Perbarui Draft Materi",
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    ExposedDropdownMenuBox(
                                        expanded = chapterDropdownExpanded,
                                        onExpandedChange = { chapterDropdownExpanded = !chapterDropdownExpanded }
                                    ) {
                                        OutlinedTextField(
                                            value = selectedChapter.title,
                                            onValueChange = {},
                                            readOnly = true,
                                            label = { Text("Bab Kitab Fathul Qorib") },
                                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = chapterDropdownExpanded) },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                        )
                                        ExposedDropdownMenu(
                                            expanded = chapterDropdownExpanded,
                                            onDismissRequest = { chapterDropdownExpanded = false }
                                        ) {
                                            FathulQoribCatalog.chapters.forEach { chapter ->
                                                DropdownMenuItem(
                                                    text = { Text("${chapter.title} (hlm. ${chapter.startPage}-${chapter.endPage})") },
                                                    onClick = {
                                                        selectedChapter = chapter
                                                        sourcePageInput = chapter.startPage.toString()
                                                        chapterDropdownExpanded = false
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        OutlinedTextField(
                                            value = lessonTitleInput,
                                            onValueChange = { if (it.length <= 160) lessonTitleInput = it },
                                            label = { Text("Judul Materi (min. 3 huruf)") },
                                            singleLine = true,
                                            modifier = Modifier
                                                .weight(2f)
                                                .testTag("cms_title_input")
                                        )
                                        OutlinedTextField(
                                            value = sourcePageInput,
                                            onValueChange = { input ->
                                                if (input.all { it.isDigit() } && input.length <= 3) {
                                                    sourcePageInput = input
                                                }
                                            },
                                            label = { Text("Hlm (1-380)") },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            singleLine = true,
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("cms_page_input")
                                        )
                                    }

                                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                                        OutlinedTextField(
                                            value = arabicInput,
                                            onValueChange = { if (it.length <= 4000) arabicInput = it },
                                            label = { Text("Teks Arab Matan / Syarah") },
                                            minLines = 2,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("cms_arabic_input")
                                        )
                                    }

                                    OutlinedTextField(
                                        value = translationInput,
                                        onValueChange = { if (it.length <= 4000) translationInput = it },
                                        label = { Text("Terjemah Indonesia") },
                                        minLines = 2,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("cms_translation_input")
                                    )

                                    OutlinedTextField(
                                        value = explanationInput,
                                        onValueChange = { if (it.length <= 4000) explanationInput = it },
                                        label = { Text("Syarah / Penjelasan") },
                                        minLines = 2,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("cms_explanation_input")
                                    )

                                    OutlinedTextField(
                                        value = footnoteInput,
                                        onValueChange = { if (it.length <= 1000) footnoteInput = it },
                                        label = { Text("Catatan Kaki / Dalil (Opsional)") },
                                        minLines = 1,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    val pageNumber = (sourcePageInput.toIntOrNull() ?: selectedChapter.startPage).coerceIn(1, 380)
                                    val canSubmit = !isBusy &&
                                        lessonTitleInput.trim().length >= 3 &&
                                        arabicInput.isNotBlank() &&
                                        translationInput.isNotBlank() &&
                                        explanationInput.isNotBlank()

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                onSaveEditorialDraft(
                                                    editingId,
                                                    selectedChapter.id,
                                                    selectedChapter.title,
                                                    lessonTitleInput,
                                                    arabicInput,
                                                    translationInput,
                                                    explanationInput,
                                                    footnoteInput.ifBlank { null },
                                                    pageNumber,
                                                    false
                                                )
                                                showEditorForm = false
                                            },
                                            enabled = canSubmit,
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(48.dp)
                                                .testTag("cms_save_draft_button")
                                        ) {
                                            Text("Simpan Draft")
                                        }
                                        Button(
                                            onClick = {
                                                onSaveEditorialDraft(
                                                    editingId,
                                                    selectedChapter.id,
                                                    selectedChapter.title,
                                                    lessonTitleInput,
                                                    arabicInput,
                                                    translationInput,
                                                    explanationInput,
                                                    footnoteInput.ifBlank { null },
                                                    pageNumber,
                                                    true
                                                )
                                                showEditorForm = false
                                            },
                                            enabled = canSubmit,
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(48.dp)
                                                .testTag("cms_submit_review_button")
                                        ) {
                                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Ajukan Review")
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (data.editorialSubmissions.isEmpty()) {
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Belum ada materi CMS tambahan. Seluruh bab inti Kitab Fathul Qorib (380 halaman) sudah tersedia di menu Kitab.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    } else {
                        items(data.editorialSubmissions, key = { it.id }) { sub ->
                            val status = EditorialStatus.fromRaw(sub.status)
                            val canEditOwn = sub.authorId == currentUserId &&
                                (status == EditorialStatus.DRAFT || status == EditorialStatus.REJECTED)
                            EditorialSubmissionCard(
                                submission = sub,
                                status = status,
                                footer = {
                                    if (canEditOwn || (isAdmin && (status == EditorialStatus.DRAFT || status == EditorialStatus.REJECTED))) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            if (canEditOwn) {
                                                TextButton(onClick = { populateEditor(sub) }) {
                                                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Edit")
                                                }
                                            }
                                            TextButton(
                                                onClick = { onDeleteDraft(sub.id) },
                                                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                                            ) {
                                                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Hapus")
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }

            1 -> {
                val reviewQueue = data.editorialSubmissions.filter {
                    it.status == EditorialStatus.REVIEW.name
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        if (!isReviewer) {
                            RoleRestrictionBanner(
                                title = "Khusus Reviewer / Ustaz",
                                message = "Verifikasi materi hanya dapat dilakukan oleh pengguna dengan peran Reviewer / Ustaz atau Admin yang bukan penyusun materi tersebut."
                            )
                        } else {
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.RateReview,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Text(
                                        text = "Antrean Quality Control (${reviewQueue.size} materi). Pemisahan tugas editorial aktif: penyusun tidak dapat menyetujui materinya sendiri.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }
                    }

                    if (reviewQueue.isEmpty()) {
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Tidak ada pengajuan materi yang menunggu review saat ini.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    } else {
                        items(reviewQueue, key = { "rev_${it.id}" }) { submission ->
                            var noteInput by remember(submission.id) { mutableStateOf("") }
                            val isSameAuthor = submission.authorId == currentUserId
                            val canReviewThis = isReviewer && !isSameAuthor && !isBusy

                            EditorialSubmissionCard(
                                submission = submission,
                                status = EditorialStatus.REVIEW,
                                footer = {
                                    if (isSameAuthor) {
                                        Surface(
                                            color = MaterialTheme.colorScheme.errorContainer,
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(10.dp),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Warning,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onErrorContainer,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Text(
                                                    text = "Pemisahan tugas editorial: penyusun tidak dapat menyetujui atau menolak materi miliknya sendiri.",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onErrorContainer
                                                )
                                            }
                                        }
                                    } else if (isReviewer) {
                                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            OutlinedTextField(
                                                value = noteInput,
                                                onValueChange = { if (it.length <= 1000) noteInput = it },
                                                label = { Text("Catatan Review Ustaz") },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                OutlinedButton(
                                                    onClick = { onReviewSubmission(submission, false, noteInput) },
                                                    enabled = canReviewThis,
                                                    colors = ButtonDefaults.outlinedButtonColors(
                                                        contentColor = MaterialTheme.colorScheme.error
                                                    ),
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(48.dp)
                                                        .testTag("reject_submission_${submission.id}")
                                                ) {
                                                    Text("Tolak / Revisi")
                                                }
                                                Button(
                                                    onClick = { onReviewSubmission(submission, true, noteInput) },
                                                    enabled = canReviewThis,
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(48.dp)
                                                        .testTag("approve_submission_${submission.id}")
                                                ) {
                                                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Setujui")
                                                }
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (!isAdmin) {
                        item {
                            RoleRestrictionBanner(
                                title = "Akses Terbatas Admin",
                                message = "Manajemen peran pengguna dan log audit menyeluruh hanya tersedia untuk Admin dan Super Admin."
                            )
                        }
                    } else {
                        item {
                            Text(
                                text = "Manajemen Peran Pengguna (${data.allUsers.size})",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        if (data.allUsers.isEmpty()) {
                            item {
                                Text(
                                    text = "Daftar pengguna hanya terlihat oleh Admin Utama terverifikasi.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            items(data.allUsers, key = { "usr_${it.userId}" }) { user ->
                                UserRoleManagementCard(
                                    user = user,
                                    isSelf = user.userId == currentUserId,
                                    onSelectRole = { newRole ->
                                        onUpdateUserRole(user.userId, newRole)
                                    }
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Audit Log Aktivitas (${data.auditLogs.size})",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    if (data.auditLogs.isEmpty()) {
                        item {
                            Text(
                                text = "Belum ada riwayat aktivitas editorial atau perubahan peran.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        items(data.auditLogs, key = { "log_${it.id}" }) { log ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            color = MaterialTheme.colorScheme.secondaryContainer,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = log.action,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                        Text(
                                            text = log.actorName,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Text(
                                        text = log.summary,
                                        style = MaterialTheme.typography.bodySmall
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

@Composable
private fun EditorialSubmissionCard(
    submission: EditorialSubmission,
    status: EditorialStatus,
    footer: @Composable () -> Unit
) {
    val badgeBg = when (status) {
        EditorialStatus.VERIFIED -> MaterialTheme.colorScheme.primaryContainer
        EditorialStatus.REVIEW -> MaterialTheme.colorScheme.tertiaryContainer
        EditorialStatus.REJECTED -> MaterialTheme.colorScheme.errorContainer
        EditorialStatus.DRAFT -> MaterialTheme.colorScheme.surfaceVariant
    }
    val badgeFg = when (status) {
        EditorialStatus.VERIFIED -> MaterialTheme.colorScheme.onPrimaryContainer
        EditorialStatus.REVIEW -> MaterialTheme.colorScheme.onTertiaryContainer
        EditorialStatus.REJECTED -> MaterialTheme.colorScheme.onErrorContainer
        EditorialStatus.DRAFT -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${submission.chapterTitle} • Hlm. ${submission.sourcePage}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Surface(color = badgeBg, shape = RoundedCornerShape(50)) {
                    Text(
                        text = status.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = badgeFg,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = submission.lessonTitle,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Penyusun: ${submission.authorName}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = submission.arabicText,
                        fontFamily = AmiriFontFamily,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Text(
                text = submission.translation,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = submission.explanation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (!submission.reviewNote.isNullOrBlank()) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Text(
                    text = "Catatan Ustaz (${submission.reviewerName ?: "Reviewer"}): ${submission.reviewNote}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            footer()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserRoleManagementCard(
    user: UserProfile,
    isSelf: Boolean,
    onSelectRole: (UserRole) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val currentRole = UserRole.fromRaw(user.role)

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user.displayName + if (isSelf) " (Anda)" else "",
                        style = MaterialTheme.typography.titleSmall
                    )
                    if (user.email.isNotBlank()) {
                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.Gavel,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = currentRole.labelId,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Peran Akun") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    UserRole.entries.forEach { roleOption ->
                        DropdownMenuItem(
                            text = { Text(roleOption.labelId) },
                            onClick = {
                                expanded = false
                                if (roleOption != currentRole) {
                                    onSelectRole(roleOption)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RoleRestrictionBanner(
    title: String,
    message: String
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleSmall)
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
