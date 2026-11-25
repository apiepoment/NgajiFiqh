package com.example.data

import android.content.Context
import com.example.R
import com.example.model.AuditLog
import com.example.model.Bookmark
import com.example.model.EditorialStatus
import com.example.model.EditorialSubmission
import com.example.model.LessonProgress
import com.example.model.QuizAttempt
import com.example.model.ReaderMode
import com.example.model.StudyNote
import com.example.model.UserProfile
import com.example.model.UserRole
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NgajiFiqihRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth = Firebase.auth
) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    companion object {
        const val BOOTSTRAPPED_ADMIN_EMAIL = "apiepoment97@gmail.com"
    }

    private fun requireUserId(): String =
        auth.currentUser?.uid
            ?: throw IllegalStateException("Pengguna harus masuk dengan Google terlebih dahulu.")

    fun isBootstrappedAdmin(): Boolean {
        val user = auth.currentUser ?: return false
        return user.isEmailVerified && user.email == BOOTSTRAPPED_ADMIN_EMAIL
    }

    private fun todayDateString(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    private fun <T> handleStreamError(error: Throwable, op: OperationType, path: String, uid: String): Nothing {
        val firestoreError = generateSequence(error) { it.cause }
            .filterIsInstance<FirebaseFirestoreException>()
            .firstOrNull()
        if (firestoreError == null && error is CancellationException) throw error
        val exceptionToLog = firestoreError ?: (error as? Exception)
        if (exceptionToLog != null && auth.currentUser?.uid == uid) {
            handleFirestoreError(exceptionToLog, op, path)
        }
        throw error
    }

    suspend fun ensureUserProfile(userId: String): Result<Unit> {
        val path = "users/$userId"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Sesi pengguna berubah." }
            val docRef = db.collection("users").document(uid)
            val snapshot = docRef.get().await()
            if (!snapshot.exists()) {
                val firebaseUser = auth.currentUser
                val name = firebaseUser?.displayName?.takeIf { it.isNotBlank() }?.take(80) ?: "Santri NgajiFiqih"
                val email = firebaseUser?.email?.take(120) ?: ""
                val initialRole = if (isBootstrappedAdmin()) UserRole.SUPER_ADMIN.name else UserRole.STUDENT.name
                val payload = mapOf(
                    "userId" to uid,
                    "displayName" to name,
                    "email" to email,
                    "role" to initialRole,
                    "dailyTargetMinutes" to 20L,
                    "streakCount" to 1L,
                    "studyMinutesToday" to 0L,
                    "lastStudyDate" to todayDateString(),
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                ).filterValues { it != null }
                docRef.set(payload).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    fun observeUserProfile(userId: String): Flow<UserProfile?> = flow {
        val path = "users/$userId"
        emitAll(
            db.collection("users").document(userId)
                .snapshots()
                .map { snapshot ->
                    if (snapshot.exists()) {
                        snapshot.toObject(UserProfile::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                    } else null
                }
                .catch { handleStreamError<UserProfile?>(it, OperationType.GET, path, userId) }
        )
    }

    fun observeAllUsersIfAdmin(userId: String): Flow<List<UserProfile>> = flow {
        if (!isBootstrappedAdmin()) {
            emit(emptyList())
            return@flow
        }
        val path = "users"
        emitAll(
            db.collection("users")
                .snapshots()
                .map { snap ->
                    snap.toObjects(UserProfile::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                        .sortedBy { it.displayName }
                }
                .catch { handleStreamError<List<UserProfile>>(it, OperationType.LIST, path, userId) }
        )
    }

    suspend fun updateDailyTarget(userId: String, minutes: Long, displayName: String): Result<Unit> {
        val path = "users/$userId"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Sesi pengguna berubah." }
            val boundedMinutes = minutes.coerceIn(5L, 240L)
            val safeName = displayName.trim().ifBlank { "Santri NgajiFiqih" }.take(80)
            db.collection("users").document(uid)
                .update(
                    mapOf(
                        "displayName" to safeName,
                        "dailyTargetMinutes" to boundedMinutes,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, path)
            Result.failure(e)
        }
    }

    suspend fun updateUserRoleAsAdmin(targetUserId: String, newRole: UserRole, actorName: String): Result<Unit> {
        val path = "users/$targetUserId"
        return try {
            val uid = requireUserId()
            require(targetUserId.isNotBlank())
            db.collection("users").document(targetUserId)
                .update(
                    mapOf(
                        "role" to newRole.name,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            recordAuditLog(uid, actorName, "UPDATE_ROLE", targetUserId, "Mengubah peran pengguna menjadi ${newRole.labelId}")
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, path)
            Result.failure(e)
        }
    }

    fun observeProgress(userId: String): Flow<List<LessonProgress>> = flow {
        val path = "users/$userId/progress"
        emitAll(
            db.collection("users").document(userId).collection("progress")
                .snapshots()
                .map { it.toObjects(LessonProgress::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE) }
                .catch { handleStreamError<List<LessonProgress>>(it, OperationType.LIST, path, userId) }
        )
    }

    suspend fun saveLessonProgress(
        userId: String,
        lessonId: String,
        chapterId: String,
        completed: Boolean,
        readMode: ReaderMode,
        addedSeconds: Long,
        existingProgress: LessonProgress?
    ): Result<Unit> {
        val path = "users/$userId/progress/$lessonId"
        return try {
            val uid = requireUserId()
            check(uid == userId)
            val docRef = db.collection("users").document(uid).collection("progress").document(lessonId)
            val newStatus = if (completed || existingProgress?.status == "COMPLETED") "COMPLETED" else "IN_PROGRESS"
            val totalSeconds = ((existingProgress?.studySeconds ?: 0L) + addedSeconds).coerceIn(0L, 86400L)

            if (existingProgress == null) {
                val payload = mutableMapOf<String, Any>(
                    "userId" to uid,
                    "lessonId" to lessonId,
                    "chapterId" to chapterId,
                    "status" to newStatus,
                    "lastReadMode" to readMode.name,
                    "studySeconds" to totalSeconds,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                if (newStatus == "COMPLETED") {
                    payload["completedAt"] = FieldValue.serverTimestamp()
                }
                docRef.set(payload).await()
            } else {
                val updates = mutableMapOf<String, Any>(
                    "chapterId" to chapterId,
                    "status" to newStatus,
                    "lastReadMode" to readMode.name,
                    "studySeconds" to totalSeconds,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                if (completed && existingProgress.completedAt == null) {
                    updates["completedAt"] = FieldValue.serverTimestamp()
                }
                docRef.update(updates).await()
            }

            // Update daily minutes on user profile
            val minutesIncrement = (addedSeconds / 60L).coerceAtLeast(if (completed) 2L else 0L)
            if (minutesIncrement > 0L) {
                val userRef = db.collection("users").document(uid)
                val snap = userRef.get().await()
                val prevDate = snap.getString("lastStudyDate") ?: ""
                val today = todayDateString()
                val prevMinutes = snap.getLong("studyMinutesToday") ?: 0L
                val prevStreak = snap.getLong("streakCount") ?: 1L
                val nextMinutes = if (prevDate == today) (prevMinutes + minutesIncrement).coerceAtMost(1440L) else minutesIncrement
                val nextStreak = if (prevDate.isNotEmpty() && prevDate != today) (prevStreak + 1L).coerceAtMost(3650L) else prevStreak.coerceAtLeast(1L)
                userRef.update(
                    mapOf(
                        "studyMinutesToday" to nextMinutes,
                        "streakCount" to nextStreak,
                        "lastStudyDate" to today,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    suspend fun markChapterCompleted(
        userId: String,
        chapterId: String,
        existingProgressList: List<LessonProgress>
    ): Result<Int> {
        val path = "users/$userId/progress"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Sesi pengguna berubah." }
            val chapterLessons = FathulQoribContent.lessonsForChapter(chapterId)
            var updatedCount = 0
            chapterLessons.forEach { lesson ->
                val existing = existingProgressList.firstOrNull { it.lessonId == lesson.id }
                if (existing?.status != "COMPLETED") {
                    val res = saveLessonProgress(
                        userId = uid,
                        lessonId = lesson.id,
                        chapterId = chapterId,
                        completed = true,
                        readMode = ReaderMode.DUAL,
                        addedSeconds = lesson.estimatedMinutes * 60L,
                        existingProgress = existing
                    )
                    if (res.isFailure) {
                        return Result.failure(res.exceptionOrNull() ?: IllegalStateException("Gagal menyimpan progres bab."))
                    }
                    updatedCount++
                }
            }
            Result.success(updatedCount)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    fun observeBookmarks(userId: String): Flow<List<Bookmark>> = flow {
        val path = "users/$userId/bookmarks"
        emitAll(
            db.collection("users").document(userId).collection("bookmarks")
                .snapshots()
                .map { snap ->
                    snap.toObjects(Bookmark::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                        .sortedByDescending {
                            maxOf(it.updatedAt?.seconds ?: 0L, it.createdAt?.seconds ?: 0L)
                        }
                }
                .catch { handleStreamError<List<Bookmark>>(it, OperationType.LIST, path, userId) }
        )
    }

    suspend fun toggleBookmark(
        userId: String,
        lessonId: String,
        chapterId: String,
        lessonTitle: String,
        chapterTitle: String,
        sourcePage: Int,
        currentlyBookmarked: Boolean,
        existingBookmarkPage: Int? = null
    ): Result<Boolean> {
        val path = "users/$userId/bookmarks/$lessonId"
        return try {
            val uid = requireUserId()
            check(uid == userId)
            val docRef = db.collection("users").document(uid).collection("bookmarks").document(lessonId)
            val boundedPage = sourcePage.coerceIn(1, 500).toLong()
            if (currentlyBookmarked && (existingBookmarkPage == null || existingBookmarkPage == sourcePage)) {
                docRef.delete().await()
                Result.success(false)
            } else if (currentlyBookmarked) {
                docRef.update(
                    mapOf(
                        "chapterId" to chapterId,
                        "lessonTitle" to lessonTitle.take(160),
                        "chapterTitle" to chapterTitle.take(120),
                        "sourcePage" to boundedPage,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
                Result.success(true)
            } else {
                val payload = mapOf(
                    "userId" to uid,
                    "lessonId" to lessonId,
                    "chapterId" to chapterId,
                    "lessonTitle" to lessonTitle.take(160),
                    "chapterTitle" to chapterTitle.take(120),
                    "sourcePage" to boundedPage,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                docRef.set(payload).await()
                Result.success(true)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    fun observeNotes(userId: String): Flow<List<StudyNote>> = flow {
        val path = "users/$userId/notes"
        emitAll(
            db.collection("users").document(userId).collection("notes")
                .snapshots()
                .map { snap ->
                    snap.toObjects(StudyNote::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                        .sortedByDescending { it.updatedAt?.seconds ?: 0L }
                }
                .catch { handleStreamError<List<StudyNote>>(it, OperationType.LIST, path, userId) }
        )
    }

    suspend fun addNote(
        userId: String,
        lessonId: String,
        lessonTitle: String,
        highlightText: String?,
        content: String,
        sourcePage: Int
    ): Result<Unit> {
        val path = "users/$userId/notes"
        return try {
            val uid = requireUserId()
            check(uid == userId)
            val cleanContent = content.trim().take(2000)
            require(cleanContent.isNotEmpty()) { "Catatan tidak boleh kosong." }
            val cleanHighlight = highlightText?.trim()?.takeIf { it.isNotEmpty() }?.take(500)
            val docRef = db.collection("users").document(uid).collection("notes").document()
            val payload = mapOf(
                "userId" to uid,
                "lessonId" to lessonId,
                "lessonTitle" to lessonTitle.take(160),
                "highlightText" to cleanHighlight,
                "content" to cleanContent,
                "sourcePage" to sourcePage.coerceIn(1, 500).toLong(),
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            ).filterValues { it != null }
            docRef.set(payload).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    suspend fun deleteNote(userId: String, noteId: String): Result<Unit> {
        val path = "users/$userId/notes/$noteId"
        return try {
            val uid = requireUserId()
            check(uid == userId)
            db.collection("users").document(uid).collection("notes").document(noteId).delete().await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, path)
            Result.failure(e)
        }
    }

    fun observeQuizAttempts(userId: String): Flow<List<QuizAttempt>> = flow {
        val path = "users/$userId/quizAttempts"
        emitAll(
            db.collection("users").document(userId).collection("quizAttempts")
                .snapshots()
                .map { snap ->
                    snap.toObjects(QuizAttempt::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                        .sortedByDescending { it.createdAt?.seconds ?: 0L }
                }
                .catch { handleStreamError<List<QuizAttempt>>(it, OperationType.LIST, path, userId) }
        )
    }

    suspend fun recordQuizAttempt(
        userId: String,
        quizId: String,
        quizTitle: String,
        chapterId: String,
        score: Int,
        correctCount: Int,
        totalQuestions: Int,
        passed: Boolean,
        durationSeconds: Int
    ): Result<Unit> {
        val path = "users/$userId/quizAttempts"
        return try {
            val uid = requireUserId()
            check(uid == userId)
            val docRef = db.collection("users").document(uid).collection("quizAttempts").document()
            val payload = mapOf(
                "userId" to uid,
                "quizId" to quizId,
                "quizTitle" to quizTitle.take(160),
                "chapterId" to chapterId,
                "score" to score.coerceIn(0, 100).toLong(),
                "correctCount" to correctCount.coerceIn(0, 100).toLong(),
                "totalQuestions" to totalQuestions.coerceIn(1, 100).toLong(),
                "passed" to passed,
                "durationSeconds" to durationSeconds.coerceIn(0, 7200).toLong(),
                "createdAt" to FieldValue.serverTimestamp()
            )
            docRef.set(payload).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    fun observeEditorialSubmissions(userId: String): Flow<List<EditorialSubmission>> = flow {
        val path = "editorialSubmissions"
        if (isBootstrappedAdmin()) {
            emitAll(
                db.collection("editorialSubmissions")
                    .snapshots()
                    .map { snap ->
                        snap.toObjects(EditorialSubmission::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                            .sortedByDescending { it.updatedAt?.seconds ?: 0L }
                    }
                    .catch { handleStreamError<List<EditorialSubmission>>(it, OperationType.LIST, path, userId) }
            )
        } else {
            val verifiedFlow = db.collection("editorialSubmissions")
                .whereEqualTo("status", EditorialStatus.VERIFIED.name)
                .snapshots()
                .map { it.toObjects(EditorialSubmission::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE) }
            val ownFlow = db.collection("editorialSubmissions")
                .whereEqualTo("authorId", userId)
                .snapshots()
                .map { it.toObjects(EditorialSubmission::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE) }
            emitAll(
                combine(verifiedFlow, ownFlow) { verified, own ->
                    (verified + own).distinctBy { it.id }.sortedByDescending { it.updatedAt?.seconds ?: 0L }
                }.catch { handleStreamError<List<EditorialSubmission>>(it, OperationType.LIST, path, userId) }
            )
        }
    }

    suspend fun saveEditorialDraft(
        userId: String,
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
    ): Result<Unit> {
        val path = "editorialSubmissions"
        return try {
            val uid = requireUserId()
            check(uid == userId)
            val cleanTitle = lessonTitle.trim().take(160)
            val cleanArabic = arabicText.trim().take(4000)
            val cleanTranslation = translation.trim().take(4000)
            val cleanExplanation = explanation.trim().take(4000)
            require(cleanTitle.length >= 3) { "Judul materi minimal 3 karakter." }
            require(cleanArabic.isNotEmpty() && cleanTranslation.isNotEmpty() && cleanExplanation.isNotEmpty()) {
                "Teks Arab, terjemahan, dan syarah wajib diisi."
            }
            val cleanFootnote = footnote?.trim()?.takeIf { it.isNotEmpty() }?.take(1000)
            val targetStatus = if (submitForReview) EditorialStatus.REVIEW.name else EditorialStatus.DRAFT.name
            val boundedPage = sourcePage.coerceIn(1, 380).toLong()

            if (existingId.isNullOrBlank()) {
                val docRef = db.collection("editorialSubmissions").document()
                val payload = mapOf(
                    "authorId" to uid,
                    "authorName" to authorName.trim().ifBlank { "Editor" }.take(80),
                    "chapterId" to chapterId,
                    "chapterTitle" to chapterTitle.take(120),
                    "lessonTitle" to cleanTitle,
                    "arabicText" to cleanArabic,
                    "translation" to cleanTranslation,
                    "explanation" to cleanExplanation,
                    "footnote" to cleanFootnote,
                    "sourcePage" to boundedPage,
                    "status" to targetStatus,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                ).filterValues { it != null }
                docRef.set(payload).await()
                recordAuditLog(
                    uid,
                    authorName,
                    if (submitForReview) "SUBMIT_REVIEW" else "CREATE_DRAFT",
                    docRef.id,
                    "Membuat materi '$cleanTitle' (hlm. $boundedPage)"
                )
            } else {
                val docRef = db.collection("editorialSubmissions").document(existingId)
                val updates = mutableMapOf<String, Any>(
                    "chapterId" to chapterId,
                    "chapterTitle" to chapterTitle.take(120),
                    "lessonTitle" to cleanTitle,
                    "arabicText" to cleanArabic,
                    "translation" to cleanTranslation,
                    "explanation" to cleanExplanation,
                    "sourcePage" to boundedPage,
                    "status" to targetStatus,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                if (cleanFootnote != null) {
                    updates["footnote"] = cleanFootnote
                }
                docRef.update(updates).await()
                recordAuditLog(
                    uid,
                    authorName,
                    if (submitForReview) "SUBMIT_REVIEW" else "UPDATE_DRAFT",
                    existingId,
                    "Memperbarui materi '$cleanTitle' -> $targetStatus"
                )
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    suspend fun reviewSubmission(
        submission: EditorialSubmission,
        reviewerId: String,
        reviewerName: String,
        approve: Boolean,
        reviewNote: String
    ): Result<Unit> {
        val path = "editorialSubmissions/${submission.id}"
        return try {
            val uid = requireUserId()
            check(uid == reviewerId)
            require(submission.authorId != uid) {
                "Pemisahan tugas editorial: penyusun materi tidak boleh memverifikasi materinya sendiri."
            }
            val newStatus = if (approve) EditorialStatus.VERIFIED.name else EditorialStatus.REJECTED.name
            val cleanNote = reviewNote.trim().takeIf { it.isNotEmpty() }?.take(1000)
            val updates = mutableMapOf<String, Any>(
                "status" to newStatus,
                "reviewerId" to uid,
                "reviewerName" to reviewerName.trim().ifBlank { "Ustaz Reviewer" }.take(80),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            if (cleanNote != null) {
                updates["reviewNote"] = cleanNote
            }
            db.collection("editorialSubmissions").document(submission.id).update(updates).await()
            recordAuditLog(
                uid,
                reviewerName,
                if (approve) "APPROVE_CONTENT" else "REJECT_CONTENT",
                submission.id,
                "${if (approve) "Menyetujui" else "Menolak"} materi '${submission.lessonTitle}'"
            )
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, path)
            Result.failure(e)
        }
    }

    suspend fun deleteEditorialDraft(submissionId: String, actorName: String): Result<Unit> {
        val path = "editorialSubmissions/$submissionId"
        return try {
            val uid = requireUserId()
            db.collection("editorialSubmissions").document(submissionId).delete().await()
            recordAuditLog(uid, actorName, "DELETE_DRAFT", submissionId, "Menghapus draft materi $submissionId")
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, path)
            Result.failure(e)
        }
    }

    fun observeAuditLogs(userId: String): Flow<List<AuditLog>> = flow {
        val path = "auditLogs"
        val query = if (isBootstrappedAdmin()) {
            db.collection("auditLogs")
        } else {
            db.collection("auditLogs").whereEqualTo("actorId", userId)
        }
        emitAll(
            query.snapshots()
                .map { snap ->
                    snap.toObjects(AuditLog::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                        .sortedByDescending { it.createdAt?.seconds ?: 0L }
                }
                .catch { handleStreamError<List<AuditLog>>(it, OperationType.LIST, path, userId) }
        )
    }

    private suspend fun recordAuditLog(
        actorId: String,
        actorName: String,
        action: String,
        targetId: String,
        summary: String
    ) {
        try {
            val docRef = db.collection("auditLogs").document()
            docRef.set(
                mapOf(
                    "actorId" to actorId,
                    "actorName" to actorName.trim().ifBlank { "Pengguna" }.take(80),
                    "action" to action.take(64),
                    "targetId" to targetId.take(128),
                    "summary" to summary.take(300),
                    "createdAt" to FieldValue.serverTimestamp()
                )
            ).await()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            handleFirestoreError(e, OperationType.CREATE, "auditLogs")
        }
    }
}
