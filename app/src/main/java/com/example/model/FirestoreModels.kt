package com.example.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@Keep
@IgnoreExtraProperties
data class UserProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val role: String = UserRole.STUDENT.name,
    val dailyTargetMinutes: Long = 20L,
    val streakCount: Long = 1L,
    val studyMinutesToday: Long = 0L,
    val lastStudyDate: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class LessonProgress(
    @DocumentId val id: String = "",
    val userId: String = "",
    val lessonId: String = "",
    val chapterId: String = "",
    val status: String = "IN_PROGRESS",
    val lastReadMode: String = ReaderMode.DUAL.name,
    val studySeconds: Long = 0L,
    val completedAt: Timestamp? = null,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class Bookmark(
    @DocumentId val id: String = "",
    val userId: String = "",
    val lessonId: String = "",
    val chapterId: String = "",
    val lessonTitle: String = "",
    val chapterTitle: String = "",
    val sourcePage: Long = 1L,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class StudyNote(
    @DocumentId val id: String = "",
    val userId: String = "",
    val lessonId: String = "",
    val lessonTitle: String = "",
    val highlightText: String? = null,
    val content: String = "",
    val sourcePage: Long = 1L,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class QuizAttempt(
    @DocumentId val id: String = "",
    val userId: String = "",
    val quizId: String = "",
    val quizTitle: String = "",
    val chapterId: String = "",
    val score: Long = 0L,
    val correctCount: Long = 0L,
    val totalQuestions: Long = 1L,
    val passed: Boolean = false,
    val durationSeconds: Long = 0L,
    @ServerTimestamp val createdAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class EditorialSubmission(
    @DocumentId val id: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val chapterId: String = "",
    val chapterTitle: String = "",
    val lessonTitle: String = "",
    val arabicText: String = "",
    val translation: String = "",
    val explanation: String = "",
    val footnote: String? = null,
    val sourcePage: Long = 1L,
    val status: String = EditorialStatus.DRAFT.name,
    val reviewerId: String? = null,
    val reviewerName: String? = null,
    val reviewNote: String? = null,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class AuditLog(
    @DocumentId val id: String = "",
    val actorId: String = "",
    val actorName: String = "",
    val action: String = "",
    val targetId: String = "",
    val summary: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
)
