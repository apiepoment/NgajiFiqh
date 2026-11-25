package com.example.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppSettings
import com.example.data.AppSettingsDataStore
import com.example.data.AppThemeMode
import com.example.data.FathulQoribContent
import com.example.data.NgajiFiqihRepository
import com.example.data.ReaderDesignTheme
import com.example.model.AuditLog
import com.example.model.Bookmark
import com.example.model.EditorialSubmission
import com.example.model.LessonProgress
import com.example.model.QuizAttempt
import com.example.model.ReaderMode
import com.example.model.StudyNote
import com.example.model.UserProfile
import com.example.model.UserRole
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

data class NgajiDashboardData(
    val profile: UserProfile?,
    val progressList: List<LessonProgress>,
    val bookmarks: List<Bookmark>,
    val notes: List<StudyNote>,
    val quizAttempts: List<QuizAttempt>,
    val editorialSubmissions: List<EditorialSubmission>,
    val auditLogs: List<AuditLog>,
    val allUsers: List<UserProfile>,
    val isBootstrappedAdmin: Boolean
)

class NgajiFiqihViewModel(
    private val repository: NgajiFiqihRepository,
    private val currentUserId: String,
    private val settingsStore: AppSettingsDataStore? = null
) : ViewModel() {

    private val _selectedLessonId = MutableStateFlow(FathulQoribContent.allLessons.first().id)
    val selectedLessonId: StateFlow<String> = _selectedLessonId.asStateFlow()

    private val _targetScrollPage = MutableStateFlow<Int?>(null)
    val targetScrollPage: StateFlow<Int?> = _targetScrollPage.asStateFlow()

    private val _readerMode = MutableStateFlow(ReaderMode.DUAL)
    val readerMode: StateFlow<ReaderMode> = _readerMode.asStateFlow()

    private val _arabicFontScale = MutableStateFlow(1.0f)
    val arabicFontScale: StateFlow<Float> = _arabicFontScale.asStateFlow()

    val appSettings: StateFlow<AppSettings> = (settingsStore?.settingsFlow ?: flowOf(AppSettings()))
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = AppSettings()
        )

    val customProfilePhotoPath: StateFlow<String?> = (settingsStore?.profilePhotoPathFlow(currentUserId) ?: flowOf(null))
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    private val _feedbackMessage = MutableStateFlow<String?>(null)
    val feedbackMessage: StateFlow<String?> = _feedbackMessage.asStateFlow()

    private val _isBusy = MutableStateFlow(false)
    val isBusy: StateFlow<Boolean> = _isBusy.asStateFlow()

    private var lastTrackedLessonId: String? = null

    init {
        viewModelScope.launch {
            settingsStore?.let { store ->
                runCatching { store.settingsFlow.first() }.onSuccess { saved ->
                    _readerMode.value = saved.defaultReaderMode
                    _arabicFontScale.value = saved.arabicFontScale
                }
            }
            repository.ensureUserProfile(currentUserId)
                .onFailure { _feedbackMessage.value = it.localizedMessage ?: "Gagal memuat profil" }
        }
    }

    private fun <T> Flow<T>.unwrapFirestoreErrors(fallbackMsg: String): Flow<Result<T>> =
        this.map { Result.success(it) }
            .catch { error ->
                val firestoreError = generateSequence(error) { it.cause }
                    .filterIsInstance<FirebaseFirestoreException>()
                    .firstOrNull()
                if (firestoreError == null && error is CancellationException) throw error
                val root = firestoreError ?: error
                emit(Result.failure(Exception(root.message ?: fallbackMsg, root)))
            }

    val dashboardState: StateFlow<UiState<NgajiDashboardData>> = combine(
        repository.observeUserProfile(currentUserId).unwrapFirestoreErrors("Gagal memuat profil"),
        repository.observeProgress(currentUserId).unwrapFirestoreErrors("Gagal memuat progres"),
        repository.observeBookmarks(currentUserId).unwrapFirestoreErrors("Gagal memuat bookmark"),
        repository.observeNotes(currentUserId).unwrapFirestoreErrors("Gagal memuat catatan"),
        repository.observeQuizAttempts(currentUserId).unwrapFirestoreErrors("Gagal memuat riwayat kuis"),
        repository.observeEditorialSubmissions(currentUserId).unwrapFirestoreErrors("Gagal memuat CMS"),
        repository.observeAuditLogs(currentUserId).unwrapFirestoreErrors("Gagal memuat log"),
        repository.observeAllUsersIfAdmin(currentUserId).unwrapFirestoreErrors("Gagal memuat daftar pengguna")
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        val profileRes = values[0] as Result<UserProfile?>
        @Suppress("UNCHECKED_CAST")
        val progressRes = values[1] as Result<List<LessonProgress>>
        @Suppress("UNCHECKED_CAST")
        val bookmarksRes = values[2] as Result<List<Bookmark>>
        @Suppress("UNCHECKED_CAST")
        val notesRes = values[3] as Result<List<StudyNote>>
        @Suppress("UNCHECKED_CAST")
        val quizzesRes = values[4] as Result<List<QuizAttempt>>
        @Suppress("UNCHECKED_CAST")
        val editorialRes = values[5] as Result<List<EditorialSubmission>>
        @Suppress("UNCHECKED_CAST")
        val auditRes = values[6] as Result<List<AuditLog>>
        @Suppress("UNCHECKED_CAST")
        val usersRes = values[7] as Result<List<UserProfile>>

        val firstError = listOf(profileRes, progressRes, bookmarksRes, notesRes, quizzesRes)
            .firstOrNull { it.isFailure }?.exceptionOrNull()

        if (firstError != null) {
            UiState.Error(firstError.message ?: "Terjadi kendala saat menyinkronkan data")
        } else {
            UiState.Success(
                NgajiDashboardData(
                    profile = profileRes.getOrNull(),
                    progressList = progressRes.getOrDefault(emptyList()),
                    bookmarks = bookmarksRes.getOrDefault(emptyList()),
                    notes = notesRes.getOrDefault(emptyList()),
                    quizAttempts = quizzesRes.getOrDefault(emptyList()),
                    editorialSubmissions = editorialRes.getOrDefault(emptyList()),
                    auditLogs = auditRes.getOrDefault(emptyList()),
                    allUsers = usersRes.getOrDefault(emptyList()),
                    isBootstrappedAdmin = repository.isBootstrappedAdmin()
                )
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = UiState.Loading
    )

    fun selectLesson(lessonId: String, targetPage: Int? = null) {
        _selectedLessonId.value = lessonId
        _targetScrollPage.value = targetPage
    }

    fun clearTargetScrollPage() {
        _targetScrollPage.value = null
    }

    fun trackLessonOpened(lessonId: String, chapterId: String, existing: LessonProgress?) {
        if (lastTrackedLessonId == lessonId) return
        lastTrackedLessonId = lessonId
        viewModelScope.launch {
            repository.saveLessonProgress(
                userId = currentUserId,
                lessonId = lessonId,
                chapterId = chapterId,
                completed = false,
                readMode = _readerMode.value,
                addedSeconds = 0L,
                existingProgress = existing
            )
        }
    }

    fun setReaderMode(mode: ReaderMode) {
        _readerMode.value = mode
        viewModelScope.launch {
            settingsStore?.setDefaultReaderMode(mode)
        }
    }

    fun adjustFontScale(delta: Float) {
        val next = (_arabicFontScale.value + delta).coerceIn(0.85f, 2.0f)
        _arabicFontScale.value = next
        viewModelScope.launch {
            settingsStore?.setArabicFontScale(next)
        }
    }

    fun setFontScale(scale: Float) {
        val next = scale.coerceIn(0.85f, 2.0f)
        _arabicFontScale.value = next
        viewModelScope.launch {
            settingsStore?.setArabicFontScale(next)
        }
    }

    fun setThemeMode(mode: AppThemeMode) {
        viewModelScope.launch {
            settingsStore?.setThemeMode(mode)
            _feedbackMessage.value = "Tema diubah ke ${mode.label}"
        }
    }

    fun setShowBookmarkBannerOnHome(show: Boolean) {
        viewModelScope.launch {
            settingsStore?.setShowBookmarkBannerOnHome(show)
        }
    }

    fun setReaderDesignTheme(theme: ReaderDesignTheme) {
        viewModelScope.launch {
            settingsStore?.setReaderDesignTheme(theme)
            _feedbackMessage.value = "Desain baca diubah ke ${theme.label}"
        }
    }

    fun updateProfilePhoto(uri: Uri) {
        val store = settingsStore ?: return
        viewModelScope.launch {
            _isBusy.value = true
            val res = store.saveProfilePhotoFromUri(currentUserId, uri)
            _isBusy.value = false
            res.onSuccess {
                _feedbackMessage.value = "Foto profil berhasil diperbarui"
            }.onFailure {
                _feedbackMessage.value = it.localizedMessage ?: "Gagal memproses foto profil"
            }
        }
    }

    fun removeProfilePhoto() {
        val store = settingsStore ?: return
        viewModelScope.launch {
            _isBusy.value = true
            val res = store.clearProfilePhoto(currentUserId)
            _isBusy.value = false
            res.onSuccess {
                _feedbackMessage.value = "Foto profil dikembalikan ke avatar inisial"
            }.onFailure {
                _feedbackMessage.value = it.localizedMessage ?: "Gagal menghapus foto profil"
            }
        }
    }

    fun clearFeedback() {
        _feedbackMessage.value = null
    }

    fun markLessonProgress(
        lessonId: String,
        chapterId: String,
        completed: Boolean,
        addedSeconds: Long,
        existing: LessonProgress?
    ) {
        viewModelScope.launch {
            _isBusy.value = true
            val result = repository.saveLessonProgress(
                userId = currentUserId,
                lessonId = lessonId,
                chapterId = chapterId,
                completed = completed,
                readMode = _readerMode.value,
                addedSeconds = addedSeconds,
                existingProgress = existing
            )
            _isBusy.value = false
            result.onSuccess {
                if (completed) {
                    _feedbackMessage.value = "Alhamdulillah, materi ditandai selesai!"
                }
            }.onFailure {
                _feedbackMessage.value = it.localizedMessage ?: "Gagal menyimpan progres belajar"
            }
        }
    }

    fun markChapterCompleted(
        chapterId: String,
        existingProgressList: List<LessonProgress>
    ) {
        viewModelScope.launch {
            _isBusy.value = true
            val result = repository.markChapterCompleted(
                userId = currentUserId,
                chapterId = chapterId,
                existingProgressList = existingProgressList
            )
            _isBusy.value = false
            result.onSuccess { count ->
                _feedbackMessage.value = if (count > 0) {
                    "Bab ditandai khatam & tersimpan ke Firestore!"
                } else {
                    "Seluruh materi pada bab ini sudah selesai."
                }
            }.onFailure {
                _feedbackMessage.value = it.localizedMessage ?: "Gagal menyimpan progres bab"
            }
        }
    }

    fun toggleBookmark(
        lessonId: String,
        chapterId: String,
        lessonTitle: String,
        chapterTitle: String,
        sourcePage: Int,
        currentlyBookmarked: Boolean,
        existingBookmarkPage: Int? = null
    ) {
        viewModelScope.launch {
            val result = repository.toggleBookmark(
                userId = currentUserId,
                lessonId = lessonId,
                chapterId = chapterId,
                lessonTitle = lessonTitle,
                chapterTitle = chapterTitle,
                sourcePage = sourcePage,
                currentlyBookmarked = currentlyBookmarked,
                existingBookmarkPage = existingBookmarkPage
            )
            result.onSuccess { saved ->
                _feedbackMessage.value = if (saved) {
                    "Bacaan halaman $sourcePage disimpan ke Bookmark"
                } else {
                    "Bookmark dihapus"
                }
            }.onFailure {
                _feedbackMessage.value = it.localizedMessage ?: "Gagal memperbarui bookmark"
            }
        }
    }

    fun saveStudyNote(
        lessonId: String,
        lessonTitle: String,
        highlightText: String?,
        content: String,
        sourcePage: Int
    ) {
        viewModelScope.launch {
            val result = repository.addNote(
                userId = currentUserId,
                lessonId = lessonId,
                lessonTitle = lessonTitle,
                highlightText = highlightText,
                content = content,
                sourcePage = sourcePage
            )
            result.onSuccess {
                _feedbackMessage.value = "Catatan belajar berhasil disimpan"
            }.onFailure {
                _feedbackMessage.value = it.localizedMessage ?: "Gagal menyimpan catatan"
            }
        }
    }

    fun deleteStudyNote(noteId: String) {
        viewModelScope.launch {
            repository.deleteNote(currentUserId, noteId)
                .onSuccess { _feedbackMessage.value = "Catatan dihapus" }
                .onFailure { _feedbackMessage.value = it.localizedMessage ?: "Gagal menghapus catatan" }
        }
    }

    fun submitQuizAttempt(
        quizId: String,
        quizTitle: String,
        chapterId: String,
        score: Int,
        correctCount: Int,
        totalQuestions: Int,
        passed: Boolean,
        durationSeconds: Int
    ) {
        viewModelScope.launch {
            repository.recordQuizAttempt(
                userId = currentUserId,
                quizId = quizId,
                quizTitle = quizTitle,
                chapterId = chapterId,
                score = score,
                correctCount = correctCount,
                totalQuestions = totalQuestions,
                passed = passed,
                durationSeconds = durationSeconds
            ).onFailure {
                _feedbackMessage.value = it.localizedMessage ?: "Gagal menyimpan hasil kuis"
            }
        }
    }

    fun updateProfileSettings(dailyTargetMinutes: Long, displayName: String) {
        val trimmedName = displayName.trim()
        if (trimmedName.isEmpty()) {
            _feedbackMessage.value = "Nama tampilan tidak boleh kosong"
            return
        }
        viewModelScope.launch {
            _isBusy.value = true
            val res = repository.updateDailyTarget(currentUserId, dailyTargetMinutes, trimmedName)
            _isBusy.value = false
            res.onSuccess { _feedbackMessage.value = "Profil & target ngaji berhasil disimpan" }
                .onFailure { _feedbackMessage.value = it.localizedMessage ?: "Gagal memperbarui profil" }
        }
    }

    fun updateUserRole(targetUserId: String, role: UserRole, actorName: String) {
        viewModelScope.launch {
            repository.updateUserRoleAsAdmin(targetUserId, role, actorName)
                .onSuccess { _feedbackMessage.value = "Peran pengguna diubah menjadi ${role.labelId}" }
                .onFailure { _feedbackMessage.value = it.localizedMessage ?: "Gagal mengubah peran pengguna" }
        }
    }

    fun saveEditorialDraft(
        authorName: String,
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
    ) {
        viewModelScope.launch {
            _isBusy.value = true
            val res = repository.saveEditorialDraft(
                userId = currentUserId,
                authorName = authorName,
                existingId = existingId,
                chapterId = chapterId,
                chapterTitle = chapterTitle,
                lessonTitle = lessonTitle,
                arabicText = arabicText,
                translation = translation,
                explanation = explanation,
                footnote = footnote,
                sourcePage = sourcePage,
                submitForReview = submitForReview
            )
            _isBusy.value = false
            res.onSuccess {
                _feedbackMessage.value = if (submitForReview) {
                    "Materi diajukan ke antrean Review Ustaz"
                } else {
                    "Draft materi berhasil disimpan"
                }
            }.onFailure {
                _feedbackMessage.value = it.localizedMessage ?: "Gagal menyimpan materi CMS"
            }
        }
    }

    fun reviewEditorialSubmission(
        submission: EditorialSubmission,
        reviewerName: String,
        approve: Boolean,
        reviewNote: String
    ) {
        viewModelScope.launch {
            _isBusy.value = true
            val res = repository.reviewSubmission(
                submission = submission,
                reviewerId = currentUserId,
                reviewerName = reviewerName,
                approve = approve,
                reviewNote = reviewNote
            )
            _isBusy.value = false
            res.onSuccess {
                _feedbackMessage.value = if (approve) "Materi diverifikasi & dipublikasikan" else "Materi dikembalikan untuk revisi"
            }.onFailure {
                _feedbackMessage.value = it.localizedMessage ?: "Gagal memproses review materi"
            }
        }
    }

    fun deleteEditorialDraft(submissionId: String, actorName: String) {
        viewModelScope.launch {
            repository.deleteEditorialDraft(submissionId, actorName)
                .onSuccess { _feedbackMessage.value = "Draft berhasil dihapus" }
                .onFailure { _feedbackMessage.value = it.localizedMessage ?: "Gagal menghapus draft" }
        }
    }
}
