package com.example.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.ReaderMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

enum class AppThemeMode(val label: String) {
    SYSTEM("Ikuti Sistem"),
    LIGHT("Terang"),
    DARK("Gelap");

    companion object {
        fun fromRaw(raw: String?): AppThemeMode =
            entries.firstOrNull { it.name == raw } ?: SYSTEM
    }
}

enum class ReaderDesignTheme(
    val label: String,
    val shortLabel: String,
    val description: String
) {
    DEFAULT("Standar Modern", "Standar", "Tampilan bersih khas NgajiFiqih"),
    KITAB_KUNING("Kitab Kuning", "Kuning", "Kertas kuning klasik pesantren"),
    SEPIA("Sepia Hangat", "Sepia", "Nyaman di mata untuk baca lama"),
    NIGHT_FOCUS("Malam Fokus", "Malam", "Latar gelap tenang untuk muroja'ah");

    companion object {
        fun fromRaw(raw: String?): ReaderDesignTheme =
            entries.firstOrNull { it.name == raw } ?: DEFAULT
    }
}

data class AppSettings(
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    val defaultReaderMode: ReaderMode = ReaderMode.DUAL,
    val readerDesignTheme: ReaderDesignTheme = ReaderDesignTheme.DEFAULT,
    val arabicFontScale: Float = 1.0f,
    val showBookmarkBannerOnHome: Boolean = true
)

private val Context.ngajiSettingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "ngajifiqih_app_settings"
)

class AppSettingsDataStore(context: Context) {
    private val appContext = context.applicationContext
    private val dataStore = appContext.ngajiSettingsDataStore

    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val DEFAULT_READER_MODE = stringPreferencesKey("default_reader_mode")
        val READER_DESIGN_THEME = stringPreferencesKey("reader_design_theme")
        val ARABIC_FONT_SCALE = floatPreferencesKey("arabic_font_scale")
        val SHOW_BOOKMARK_BANNER = booleanPreferencesKey("show_bookmark_banner_on_home")

        fun profilePhotoKey(userId: String) = stringPreferencesKey("profile_photo_path_$userId")
    }

    val settingsFlow: Flow<AppSettings> = dataStore.data.map { prefs ->
        AppSettings(
            themeMode = AppThemeMode.fromRaw(prefs[Keys.THEME_MODE]),
            defaultReaderMode = ReaderMode.fromRaw(prefs[Keys.DEFAULT_READER_MODE]),
            readerDesignTheme = ReaderDesignTheme.fromRaw(prefs[Keys.READER_DESIGN_THEME]),
            arabicFontScale = (prefs[Keys.ARABIC_FONT_SCALE] ?: 1.0f).coerceIn(0.85f, 2.0f),
            showBookmarkBannerOnHome = prefs[Keys.SHOW_BOOKMARK_BANNER] ?: true
        )
    }

    fun profilePhotoPathFlow(userId: String): Flow<String?> = dataStore.data.map { prefs ->
        val path = prefs[Keys.profilePhotoKey(userId)]
        if (!path.isNullOrBlank() && File(path).exists()) path else null
    }

    suspend fun setThemeMode(mode: AppThemeMode) {
        dataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    suspend fun setDefaultReaderMode(mode: ReaderMode) {
        dataStore.edit { it[Keys.DEFAULT_READER_MODE] = mode.name }
    }

    suspend fun setReaderDesignTheme(theme: ReaderDesignTheme) {
        dataStore.edit { it[Keys.READER_DESIGN_THEME] = theme.name }
    }

    suspend fun setArabicFontScale(scale: Float) {
        dataStore.edit { it[Keys.ARABIC_FONT_SCALE] = scale.coerceIn(0.85f, 2.0f) }
    }

    suspend fun setShowBookmarkBannerOnHome(show: Boolean) {
        dataStore.edit { it[Keys.SHOW_BOOKMARK_BANNER] = show }
    }

    suspend fun saveProfilePhotoFromUri(userId: String, sourceUri: Uri): Result<String> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val dir = File(appContext.filesDir, "profile_photos").apply { mkdirs() }
                val safeUid = userId.replace(Regex("[^a-zA-Z0-9_-]"), "_")
                // Clean old files for this user
                dir.listFiles()?.filter { it.name.startsWith("avatar_${safeUid}_") }?.forEach { it.delete() }

                val outFile = File(dir, "avatar_${safeUid}_${System.currentTimeMillis()}.jpg")
                val inputStream = appContext.contentResolver.openInputStream(sourceUri)
                    ?: throw IllegalStateException("Tidak dapat membaca file gambar yang dipilih.")

                val decoded = inputStream.use { BitmapFactory.decodeStream(it) }
                    ?: throw IllegalStateException("Format gambar tidak didukung.")

                val maxDim = 512
                val scaled = if (decoded.width > maxDim || decoded.height > maxDim) {
                    val ratio = minOf(maxDim.toFloat() / decoded.width, maxDim.toFloat() / decoded.height)
                    val targetW = (decoded.width * ratio).toInt().coerceAtLeast(1)
                    val targetH = (decoded.height * ratio).toInt().coerceAtLeast(1)
                    Bitmap.createScaledBitmap(decoded, targetW, targetH, true).also {
                        if (it !== decoded) decoded.recycle()
                    }
                } else {
                    decoded
                }

                FileOutputStream(outFile).use { fos ->
                    scaled.compress(Bitmap.CompressFormat.JPEG, 88, fos)
                }
                if (!scaled.isRecycled) {
                    scaled.recycle()
                }

                val savedPath = outFile.absolutePath
                dataStore.edit { it[Keys.profilePhotoKey(userId)] = savedPath }
                savedPath
            }
        }
    }

    suspend fun clearProfilePhoto(userId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val dir = File(appContext.filesDir, "profile_photos")
                val safeUid = userId.replace(Regex("[^a-zA-Z0-9_-]"), "_")
                if (dir.exists()) {
                    dir.listFiles()?.filter { it.name.startsWith("avatar_${safeUid}_") }?.forEach { it.delete() }
                }
                dataStore.edit { it.remove(Keys.profilePhotoKey(userId)) }
                Unit
            }
        }
    }
}
