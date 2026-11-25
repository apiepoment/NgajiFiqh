package com.example

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.auth.AuthScreen
import com.example.data.AppSettingsDataStore
import com.example.data.NgajiFiqihRepository
import com.example.ui.screens.AdminCmsScreen
import com.example.ui.screens.HomeDashboardScreen
import com.example.ui.screens.LearningReaderScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.ProgressAndProfileScreen
import com.example.ui.screens.QuizHubScreen
import com.example.viewmodel.NgajiFiqihViewModel
import com.example.viewmodel.UiState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

enum class NgajiRoute(
    val routeId: String,
    val labelResId: Int,
    val icon: ImageVector
) {
    HOME("home", R.string.nav_home, Icons.Default.Home),
    LIBRARY("library", R.string.nav_library, Icons.AutoMirrored.Filled.LibraryBooks),
    READER("reader", R.string.nav_reader, Icons.AutoMirrored.Filled.MenuBook),
    QUIZ("quiz", R.string.nav_quiz, Icons.Default.Quiz),
    PROFILE("profile", R.string.nav_profile, Icons.Default.Person),
    CMS("cms", R.string.profile_cms_portal, Icons.Default.Person)
}

private fun authStateFlow(): Flow<FirebaseUser?> = callbackFlow {
    val auth = Firebase.auth
    val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        trySend(firebaseAuth.currentUser)
    }
    auth.addAuthStateListener(listener)
    trySend(auth.currentUser)
    awaitClose { auth.removeAuthStateListener(listener) }
}

@Composable
fun NgajiFiqihApp(
    settingsStore: AppSettingsDataStore? = null
) {
    val authFlow = remember { authStateFlow() }
    val currentUser by authFlow.collectAsStateWithLifecycle(initialValue = Firebase.auth.currentUser)

    if (currentUser == null) {
        AuthScreen(onAuthSuccess = {})
    } else {
        val uid = currentUser!!.uid
        key(uid) {
            val viewModel: NgajiFiqihViewModel = viewModel(
                key = uid,
                factory = viewModelFactory {
                    initializer {
                        val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]!!
                        val repository = NgajiFiqihRepository(app)
                        val resolvedStore = settingsStore ?: AppSettingsDataStore(app)
                        NgajiFiqihViewModel(repository, uid, resolvedStore)
                    }
                }
            )
            AuthenticatedNgajiShell(
                currentUserId = uid,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun AuthenticatedNgajiShell(
    currentUserId: String,
    viewModel: NgajiFiqihViewModel
) {
    val dashboardState by viewModel.dashboardState.collectAsStateWithLifecycle()
    val selectedLessonId by viewModel.selectedLessonId.collectAsStateWithLifecycle()
    val targetScrollPage by viewModel.targetScrollPage.collectAsStateWithLifecycle()
    val readerMode by viewModel.readerMode.collectAsStateWithLifecycle()
    val arabicFontScale by viewModel.arabicFontScale.collectAsStateWithLifecycle()
    val appSettings by viewModel.appSettings.collectAsStateWithLifecycle()
    val customPhotoPath by viewModel.customProfilePhotoPath.collectAsStateWithLifecycle()
    val feedbackMessage by viewModel.feedbackMessage.collectAsStateWithLifecycle()
    val isBusy by viewModel.isBusy.collectAsStateWithLifecycle()

    var currentRoute by rememberSaveable { mutableStateOf(NgajiRoute.HOME) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(feedbackMessage) {
        val msg = feedbackMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearFeedback()
        }
    }

    if (currentRoute != NgajiRoute.HOME) {
        BackHandler {
            currentRoute = if (currentRoute == NgajiRoute.CMS) NgajiRoute.PROFILE else NgajiRoute.HOME
        }
    }

    val primaryTabs = listOf(
        NgajiRoute.HOME,
        NgajiRoute.LIBRARY,
        NgajiRoute.READER,
        NgajiRoute.QUIZ,
        NgajiRoute.PROFILE
    )

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useRail = maxWidth >= 720.dp

        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                if (!useRail && currentRoute != NgajiRoute.CMS) {
                    NavigationBar {
                        primaryTabs.forEach { tab ->
                            NavigationBarItem(
                                selected = currentRoute == tab,
                                onClick = { currentRoute = tab },
                                icon = {
                                    Icon(
                                        imageVector = tab.icon,
                                        contentDescription = stringResource(tab.labelResId)
                                    )
                                },
                                label = { Text(stringResource(tab.labelResId)) },
                                modifier = Modifier.testTag("nav_${tab.routeId}")
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (useRail && currentRoute != NgajiRoute.CMS) {
                    NavigationRail(modifier = Modifier.fillMaxHeight()) {
                        primaryTabs.forEach { tab ->
                            NavigationRailItem(
                                selected = currentRoute == tab,
                                onClick = { currentRoute = tab },
                                icon = {
                                    Icon(
                                        imageVector = tab.icon,
                                        contentDescription = stringResource(tab.labelResId)
                                    )
                                },
                                label = { Text(stringResource(tab.labelResId)) },
                                modifier = Modifier.testTag("rail_${tab.routeId}")
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    val contentModifier = Modifier
                        .fillMaxSize()
                        .widthIn(max = 960.dp)

                    when (val state = dashboardState) {
                        UiState.Loading -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(40.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Menyiapkan ruang belajar NgajiFiqih...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 12.dp)
                                )
                            }
                        }

                        is UiState.Error -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Gagal Memuat Data",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = state.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )
                                Button(onClick = { currentRoute = NgajiRoute.HOME }) {
                                    Text("Coba Muat Ulang")
                                }
                            }
                        }

                        is UiState.Success -> {
                            val data = state.data
                            val actorName = data.profile?.displayName?.ifBlank { "Pengguna" } ?: "Pengguna"

                            when (currentRoute) {
                                NgajiRoute.HOME -> {
                                    HomeDashboardScreen(
                                        data = data,
                                        showBookmarkBanner = appSettings.showBookmarkBannerOnHome,
                                        onOpenLesson = { lessonId ->
                                            viewModel.selectLesson(lessonId)
                                            currentRoute = NgajiRoute.READER
                                        },
                                        onOpenLessonAtPage = { lessonId, page ->
                                            viewModel.selectLesson(lessonId, page)
                                            currentRoute = NgajiRoute.READER
                                        },
                                        onOpenLibrary = { currentRoute = NgajiRoute.LIBRARY },
                                        onOpenQuizTab = { currentRoute = NgajiRoute.QUIZ },
                                        onOpenProgressProfile = { currentRoute = NgajiRoute.PROFILE },
                                        modifier = contentModifier
                                    )
                                }

                                NgajiRoute.LIBRARY -> {
                                    LibraryScreen(
                                        data = data,
                                        onOpenLesson = { lessonId ->
                                            viewModel.selectLesson(lessonId)
                                            currentRoute = NgajiRoute.READER
                                        },
                                        onOpenLessonAtPage = { lessonId, page ->
                                            viewModel.selectLesson(lessonId, page)
                                            currentRoute = NgajiRoute.READER
                                        },
                                        modifier = contentModifier
                                    )
                                }

                                NgajiRoute.READER -> {
                                    LearningReaderScreen(
                                        data = data,
                                        selectedLessonId = selectedLessonId,
                                        targetScrollPage = targetScrollPage,
                                        readerMode = readerMode,
                                        readerDesignTheme = appSettings.readerDesignTheme,
                                        fontScale = arabicFontScale,
                                        onSelectLesson = { id, page -> viewModel.selectLesson(id, page) },
                                        onClearTargetScrollPage = { viewModel.clearTargetScrollPage() },
                                        onTrackLessonOpened = { lesson, existing ->
                                            viewModel.trackLessonOpened(lesson.id, lesson.chapterId, existing)
                                        },
                                        onChangeReaderMode = { viewModel.setReaderMode(it) },
                                        onChangeReaderDesignTheme = { viewModel.setReaderDesignTheme(it) },
                                        onAdjustFontScale = { viewModel.adjustFontScale(it) },
                                        onSetFontScale = { viewModel.setFontScale(it) },
                                        onMarkComplete = { lesson, existing ->
                                            viewModel.markLessonProgress(
                                                lessonId = lesson.id,
                                                chapterId = lesson.chapterId,
                                                completed = true,
                                                addedSeconds = lesson.estimatedMinutes * 60L,
                                                existing = existing
                                            )
                                        },
                                        onToggleBookmark = { lesson, page, currentlyBookmarked, existingPage ->
                                            viewModel.toggleBookmark(
                                                lessonId = lesson.id,
                                                chapterId = lesson.chapterId,
                                                lessonTitle = lesson.title,
                                                chapterTitle = lesson.kitabTitle,
                                                sourcePage = page,
                                                currentlyBookmarked = currentlyBookmarked,
                                                existingBookmarkPage = existingPage
                                            )
                                        },
                                        onRemoveBookmark = { bookmark ->
                                            viewModel.toggleBookmark(
                                                lessonId = bookmark.lessonId,
                                                chapterId = bookmark.chapterId,
                                                lessonTitle = bookmark.lessonTitle,
                                                chapterTitle = bookmark.chapterTitle,
                                                sourcePage = bookmark.sourcePage.toInt(),
                                                currentlyBookmarked = true
                                            )
                                        },
                                        onSaveNote = { lesson, highlight, content ->
                                            viewModel.saveStudyNote(
                                                lessonId = lesson.id,
                                                lessonTitle = lesson.title,
                                                highlightText = highlight,
                                                content = content,
                                                sourcePage = lesson.sourcePage
                                            )
                                        },
                                        onDeleteNote = { noteId ->
                                            viewModel.deleteStudyNote(noteId)
                                        },
                                        modifier = contentModifier
                                    )
                                }

                                NgajiRoute.QUIZ -> {
                                    QuizHubScreen(
                                        data = data,
                                        onRecordAttempt = { quiz, score, correctCount, passed, durationSeconds ->
                                            viewModel.submitQuizAttempt(
                                                quizId = quiz.id,
                                                quizTitle = quiz.title,
                                                chapterId = quiz.chapterId,
                                                score = score,
                                                correctCount = correctCount,
                                                totalQuestions = quiz.questions.size,
                                                passed = passed,
                                                durationSeconds = durationSeconds
                                            )
                                        },
                                        modifier = contentModifier
                                    )
                                }

                                NgajiRoute.PROFILE -> {
                                    ProgressAndProfileScreen(
                                        data = data,
                                        appSettings = appSettings,
                                        customPhotoPath = customPhotoPath,
                                        isBusy = isBusy,
                                        onOpenLesson = { lessonId ->
                                            val bmPage = data.bookmarks.firstOrNull { it.lessonId == lessonId }?.sourcePage?.toInt()
                                            viewModel.selectLesson(lessonId, bmPage)
                                            currentRoute = NgajiRoute.READER
                                        },
                                        onMarkChapterCompleted = { chapterId ->
                                            viewModel.markChapterCompleted(chapterId, data.progressList)
                                        },
                                        onRemoveBookmark = { bookmark ->
                                            viewModel.toggleBookmark(
                                                lessonId = bookmark.lessonId,
                                                chapterId = bookmark.chapterId,
                                                lessonTitle = bookmark.lessonTitle,
                                                chapterTitle = bookmark.chapterTitle,
                                                sourcePage = bookmark.sourcePage.toInt(),
                                                currentlyBookmarked = true
                                            )
                                        },
                                        onDeleteNote = { noteId ->
                                            viewModel.deleteStudyNote(noteId)
                                        },
                                        onUpdateProfileSettings = { targetMinutes, displayName ->
                                            viewModel.updateProfileSettings(targetMinutes, displayName)
                                        },
                                        onPickProfilePhoto = { uri -> viewModel.updateProfilePhoto(uri) },
                                        onRemoveProfilePhoto = { viewModel.removeProfilePhoto() },
                                        onChangeThemeMode = { viewModel.setThemeMode(it) },
                                        onChangeDefaultReaderMode = { viewModel.setReaderMode(it) },
                                        onChangeReaderDesignTheme = { viewModel.setReaderDesignTheme(it) },
                                        onChangeFontScale = { viewModel.setFontScale(it) },
                                        onToggleBookmarkBanner = { viewModel.setShowBookmarkBannerOnHome(it) },
                                        onOpenCmsPortal = {
                                            currentRoute = NgajiRoute.CMS
                                        },
                                        modifier = contentModifier
                                    )
                                }

                                NgajiRoute.CMS -> {
                                    AdminCmsScreen(
                                        data = data,
                                        currentUserId = currentUserId,
                                        isBusy = isBusy,
                                        onBack = { currentRoute = NgajiRoute.PROFILE },
                                        onSaveEditorialDraft = { existingId, chapterId, chapterTitle, lessonTitle, arabic, translation, explanation, footnote, sourcePage, submitForReview ->
                                            viewModel.saveEditorialDraft(
                                                authorName = actorName,
                                                existingId = existingId,
                                                chapterId = chapterId,
                                                chapterTitle = chapterTitle,
                                                lessonTitle = lessonTitle,
                                                arabicText = arabic,
                                                translation = translation,
                                                explanation = explanation,
                                                footnote = footnote,
                                                sourcePage = sourcePage,
                                                submitForReview = submitForReview
                                            )
                                        },
                                        onReviewSubmission = { submission, approve, reviewNote ->
                                            viewModel.reviewEditorialSubmission(
                                                submission = submission,
                                                reviewerName = actorName,
                                                approve = approve,
                                                reviewNote = reviewNote
                                            )
                                        },
                                        onDeleteDraft = { submissionId ->
                                            viewModel.deleteEditorialDraft(submissionId, actorName)
                                        },
                                        onUpdateUserRole = { targetUserId, newRole ->
                                            viewModel.updateUserRole(targetUserId, newRole, actorName)
                                        },
                                        modifier = contentModifier
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
