package com.example.model

enum class UserRole(val labelId: String) {
    STUDENT("Santri / Pelajar"),
    CONTENT_EDITOR("Content Editor"),
    REVIEWER_USTAZ("Reviewer / Ustaz"),
    ADMIN("Admin"),
    SUPER_ADMIN("Super Admin");

    companion object {
        fun fromRaw(raw: String?): UserRole =
            entries.firstOrNull { it.name == raw } ?: STUDENT
    }
}

enum class EditorialStatus(val label: String) {
    DRAFT("Draft"),
    REVIEW("Menunggu Review"),
    VERIFIED("Terverifikasi"),
    REJECTED("Perlu Revisi");

    companion object {
        fun fromRaw(raw: String?): EditorialStatus =
            entries.firstOrNull { it.name == raw } ?: DRAFT
    }
}

enum class ReaderMode(val label: String) {
    DUAL("Arab & Terjemah"),
    ARABIC("Teks Arab"),
    TRANSLATION("Terjemahan");

    companion object {
        fun fromRaw(raw: String?): ReaderMode =
            entries.firstOrNull { it.name == raw } ?: DUAL
    }
}

enum class BlockType {
    HEADING,
    ARABIC,
    TRANSLATION,
    EXPLANATION,
    QUOTE,
    FOOTNOTE
}

enum class QuestionType {
    MULTIPLE_CHOICE,
    TRUE_FALSE
}

enum class KitabCategory(val title: String) {
    ALL("Semua"),
    IBADAH("Ibadah"),
    MUAMALAH("Muamalah"),
    MUNAKAHAT("Munakahat"),
    JINAYAT_QADLA("Jinayat & Qadla'")
}

data class Book(
    val id: String,
    val title: String,
    val arabicTitle: String,
    val author: String,
    val originalMatanAuthor: String,
    val publisher: String,
    val totalPages: Int,
    val description: String,
    val status: EditorialStatus
)

data class KitabSection(
    val id: String,
    val bookId: String,
    val title: String,
    val arabicTitle: String,
    val category: KitabCategory,
    val startPage: Int,
    val endPage: Int,
    val sequence: Int,
    val summary: String,
    val babTitles: List<String>
)

data class Chapter(
    val id: String,
    val kitabId: String,
    val bookId: String,
    val title: String,
    val subtitle: String,
    val category: KitabCategory,
    val startPage: Int,
    val endPage: Int,
    val sequence: Int
)

data class LessonBlock(
    val id: String,
    val blockType: BlockType,
    val content: String,
    val sequence: Int,
    val sourcePage: Int
)

data class GlossaryTerm(
    val id: String,
    val term: String,
    val arabicTerm: String,
    val definition: String,
    val sourcePage: Int
)

data class Lesson(
    val id: String,
    val bookId: String,
    val chapterId: String,
    val kitabTitle: String,
    val title: String,
    val summary: String,
    val sourcePage: Int,
    val estimatedMinutes: Int,
    val sequence: Int,
    val verificationStatus: EditorialStatus,
    val blocks: List<LessonBlock>,
    val glossaryIds: List<String> = emptyList()
)

data class AnswerOption(
    val id: String,
    val text: String,
    val isCorrect: Boolean
)

data class QuizQuestion(
    val id: String,
    val quizId: String,
    val question: String,
    val questionType: QuestionType,
    val options: List<AnswerOption>,
    val explanation: String,
    val sourcePage: Int
)

data class Quiz(
    val id: String,
    val chapterId: String,
    val title: String,
    val subtitle: String,
    val passingScore: Int,
    val estimatedMinutes: Int,
    val questions: List<QuizQuestion>
)
