package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.AppSettings
import com.example.data.AppThemeMode
import com.example.data.ReaderDesignTheme
import com.example.model.ReaderMode
import com.example.model.UserProfile
import com.example.model.UserRole
import com.example.ui.theme.AmiriFontFamily
import java.io.File
import kotlin.math.roundToInt

@Composable
fun ProfileAvatar(
    displayName: String,
    customPhotoPath: String?,
    fallbackPhotoUrl: String?,
    size: Dp = 56.dp,
    showCameraBadge: Boolean = false,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val initials = remember(displayName) {
        displayName.trim().split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercaseChar().toString() }
            .ifBlank { "SN" }
    }
    val photoModel: Any? = remember(customPhotoPath, fallbackPhotoUrl) {
        when {
            !customPhotoPath.isNullOrBlank() && File(customPhotoPath).exists() -> File(customPhotoPath)
            !fallbackPhotoUrl.isNullOrBlank() -> fallbackPhotoUrl
            else -> null
        }
    }

    Box(
        modifier = modifier
            .size(size)
            .let { base ->
                if (onClick != null) {
                    base.clickable(onClick = onClick)
                } else base
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .border(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                style = if (size >= 72.dp) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            if (photoModel != null) {
                AsyncImage(
                    model = photoModel,
                    contentDescription = "Foto profil $displayName",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        if (showCameraBadge) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(1.5.dp, MaterialTheme.colorScheme.surface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Ubah foto profil",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileMenuCard(
    profile: UserProfile?,
    appSettings: AppSettings,
    dailyTargetMinutes: Int,
    onOpenEditProfile: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenCmsPortal: () -> Unit,
    onRequestLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val emailText = profile?.email?.takeIf { it.isNotBlank() } ?: "Akun Google aktif"
    val fontPercent = (appSettings.arabicFontScale * 100).roundToInt()

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = stringResource(R.string.profile_menu_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )

            ProfileActionRow(
                icon = Icons.Default.Edit,
                iconContainerColor = MaterialTheme.colorScheme.primaryContainer,
                iconTint = MaterialTheme.colorScheme.primary,
                title = stringResource(R.string.profile_edit_title),
                subtitle = "Ubah nama tampilan & target ngaji (${dailyTargetMinutes} mnt/hari)",
                onClick = onOpenEditProfile,
                modifier = Modifier.testTag("menu_edit_profile")
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            ProfileActionRow(
                icon = Icons.Default.Settings,
                iconContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                iconTint = MaterialTheme.colorScheme.secondary,
                title = stringResource(R.string.profile_settings_title),
                subtitle = "Tema ${appSettings.themeMode.label} • Desain ${appSettings.readerDesignTheme.shortLabel} • Teks $fontPercent%",
                onClick = onOpenSettings,
                modifier = Modifier.testTag("menu_settings")
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            ProfileActionRow(
                icon = Icons.Default.AdminPanelSettings,
                iconContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                iconTint = MaterialTheme.colorScheme.onTertiaryContainer,
                title = stringResource(R.string.profile_cms_portal),
                subtitle = "Kelola materi fiqih, draft kitab & review ustaz",
                onClick = onOpenCmsPortal,
                modifier = Modifier.testTag("open_cms_portal_button")
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            ProfileActionRow(
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                iconContainerColor = MaterialTheme.colorScheme.errorContainer,
                iconTint = MaterialTheme.colorScheme.error,
                title = stringResource(R.string.profile_sign_out),
                subtitle = "Akhiri sesi ($emailText)",
                titleColor = MaterialTheme.colorScheme.error,
                onClick = onRequestLogout,
                modifier = Modifier.testTag("sign_out_button")
            )
        }
    }
}

@Composable
private fun ProfileActionRow(
    icon: ImageVector,
    iconContainerColor: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    titleColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconContainerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = titleColor,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditProfileModalSheet(
    profile: UserProfile?,
    role: UserRole,
    dailyTargetMinutes: Int,
    customPhotoPath: String?,
    fallbackPhotoUrl: String?,
    isBusy: Boolean,
    onPickPhoto: (Uri) -> Unit,
    onRemovePhoto: () -> Unit,
    onDismiss: () -> Unit,
    onSave: (targetMinutes: Long, displayName: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val initialName = profile?.displayName?.ifBlank { "Santri NgajiFiqih" } ?: "Santri NgajiFiqih"
    var nameInput by remember(initialName) { mutableStateOf(initialName) }
    var targetSlider by remember(dailyTargetMinutes) { mutableFloatStateOf(dailyTargetMinutes.toFloat()) }
    val trimmedName = nameInput.trim()
    val nameError = trimmedName.isEmpty()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let { onPickPhoto(it) }
        }
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.testTag("edit_profile_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.profile_edit_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Photo Picker + Account Summary Banner
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        ProfileAvatar(
                            displayName = trimmedName.ifBlank { initialName },
                            customPhotoPath = customPhotoPath,
                            fallbackPhotoUrl = fallbackPhotoUrl,
                            size = 72.dp,
                            showCameraBadge = true,
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            modifier = Modifier.testTag("edit_profile_avatar_tap")
                        )
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = profile?.email?.takeIf { it.isNotBlank() } ?: "Akun Google Terhubung",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Peran: ${role.labelId} • Streak ${profile?.streakCount ?: 1} hari",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 48.dp)
                                .testTag("change_profile_photo_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(stringResource(R.string.profile_change_photo))
                        }

                        if (!customPhotoPath.isNullOrBlank()) {
                            TextButton(
                                onClick = onRemovePhoto,
                                modifier = Modifier
                                    .heightIn(min = 48.dp)
                                    .testTag("remove_profile_photo_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(R.string.profile_remove_photo))
                            }
                        }
                    }
                }
            }

            OutlinedTextField(
                value = nameInput,
                onValueChange = { if (it.length <= 80) nameInput = it },
                label = { Text("Nama Tampilan") },
                placeholder = { Text("Masukkan nama lengkap atau panggilan") },
                isError = nameError,
                supportingText = {
                    if (nameError) {
                        Text("Nama tampilan tidak boleh kosong")
                    } else {
                        Text("${trimmedName.length}/80 karakter • Tersimpan di Firestore")
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("edit_profile_name_input")
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Target Ngaji Harian",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "${targetSlider.roundToInt()} menit / hari",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Slider(
                    value = targetSlider,
                    onValueChange = { targetSlider = it },
                    valueRange = 5f..240f,
                    steps = 46,
                    modifier = Modifier.testTag("edit_profile_target_slider")
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(10, 20, 30, 60).forEach { preset ->
                        FilterChip(
                            selected = targetSlider.roundToInt() == preset,
                            onClick = { targetSlider = preset.toFloat() },
                            label = { Text("$preset mnt") },
                            modifier = Modifier.heightIn(min = 48.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("cancel_edit_profile_button")
                ) {
                    Text("Batal")
                }
                Button(
                    onClick = {
                        onSave(targetSlider.roundToInt().toLong(), trimmedName)
                        onDismiss()
                    },
                    enabled = !nameError && !isBusy,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("save_edit_profile_button")
                ) {
                    if (isBusy) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Simpan Profil")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingsModalSheet(
    appSettings: AppSettings,
    onChangeThemeMode: (AppThemeMode) -> Unit,
    onChangeDefaultReaderMode: (ReaderMode) -> Unit,
    onChangeReaderDesignTheme: (ReaderDesignTheme) -> Unit,
    onChangeFontScale: (Float) -> Unit,
    onToggleBookmarkBanner: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val fontPct = (appSettings.arabicFontScale * 100).roundToInt()
    val previewPalette = rememberReaderPalette(appSettings.readerDesignTheme)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.testTag("settings_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Pengaturan Aplikasi & Bacaan",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // 1. Theme Mode
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Tema Tampilan Aplikasi",
                    style = MaterialTheme.typography.titleSmall
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AppThemeMode.entries.forEach { mode ->
                        val icon = when (mode) {
                            AppThemeMode.SYSTEM -> Icons.Default.SettingsBrightness
                            AppThemeMode.LIGHT -> Icons.Default.LightMode
                            AppThemeMode.DARK -> Icons.Default.DarkMode
                        }
                        FilterChip(
                            selected = appSettings.themeMode == mode,
                            onClick = { onChangeThemeMode(mode) },
                            leadingIcon = {
                                Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
                            },
                            label = { Text(mode.label) },
                            modifier = Modifier
                                .heightIn(min = 48.dp)
                                .testTag("setting_theme_${mode.name}")
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // 2. Reader Design Theme
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.reader_design_title),
                    style = MaterialTheme.typography.titleSmall
                )
                ReaderDesignSwatchSelector(
                    selectedTheme = appSettings.readerDesignTheme,
                    onSelectTheme = onChangeReaderDesignTheme
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // 3. Default Reader Mode
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Mode Bacaan Kitab Default",
                    style = MaterialTheme.typography.titleSmall
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ReaderMode.entries.forEach { mode ->
                        FilterChip(
                            selected = appSettings.defaultReaderMode == mode,
                            onClick = { onChangeDefaultReaderMode(mode) },
                            label = { Text(mode.label) },
                            modifier = Modifier
                                .heightIn(min = 48.dp)
                                .testTag("setting_reader_${mode.name}")
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // 4. Arabic & Translation Text Scale + Live Reader Design Preview
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Ukuran Teks & Pratinjau Baca",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    Text(
                        text = "$fontPct%",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Slider(
                    value = appSettings.arabicFontScale,
                    onValueChange = { onChangeFontScale(it) },
                    valueRange = 0.85f..2.0f,
                    modifier = Modifier.testTag("setting_font_scale_slider")
                )

                Surface(
                    color = previewPalette.cardBackground,
                    border = BorderStroke(1.dp, previewPalette.borderColor),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                            Text(
                                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                fontFamily = AmiriFontFamily,
                                fontSize = (22f * appSettings.arabicFontScale).sp,
                                lineHeight = (34f * appSettings.arabicFontScale).sp,
                                textAlign = TextAlign.Right,
                                color = previewPalette.arabicText,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Text(
                            text = "Dengan menyebut nama Allah Yang Maha Pengasih lagi Maha Penyayang.",
                            fontSize = (13.5f * appSettings.arabicFontScale).sp,
                            lineHeight = (20f * appSettings.arabicFontScale).sp,
                            color = previewPalette.textPrimary
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // 4. Home Bookmark Resume Banner Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp)
                    .clickable { onToggleBookmarkBanner(!appSettings.showBookmarkBannerOnHome) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "Kartu Lanjut Bookmark di Beranda",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "Tampilkan pintasan halaman terakhir yang ditandai di Beranda",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = appSettings.showBookmarkBannerOnHome,
                    onCheckedChange = onToggleBookmarkBanner,
                    modifier = Modifier.testTag("setting_bookmark_banner_switch")
                )
            }

            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("close_settings_button")
            ) {
                Text("Selesai")
            }
        }
    }
}

@Composable
fun LogoutConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirmLogout: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(text = stringResource(R.string.profile_logout_confirm_title))
        },
        text = {
            Text(text = stringResource(R.string.profile_logout_confirm_body))
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .testTag("cancel_logout_button")
            ) {
                Text("Batal")
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmLogout,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .testTag("confirm_logout_button")
            ) {
                Text("Keluar")
            }
        }
    )
}
