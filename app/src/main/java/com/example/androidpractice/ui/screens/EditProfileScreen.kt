package com.example.androidpractice.ui.screens

import android.Manifest
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.widget.TimePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.androidpractice.domain.model.Profile
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    initialProfile: Profile,
    onSave: (Profile) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf(initialProfile.name) }
    var avatarUri by remember { mutableStateOf(initialProfile.avatarUri) }
    var resumeUrl by remember { mutableStateOf(initialProfile.resumeUrl) }
    var reminderTime by remember { mutableStateOf(initialProfile.reminderTime) }
    var timeError by remember { mutableStateOf<String?>(null) }

    fun validateTime(time: String): Boolean {
        return Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]$").matches(time)
    }

    val context = LocalContext.current
    val showTimePicker = remember { mutableStateOf(false) }

    if (showTimePicker.value) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            context,
            { _: TimePicker, hour: Int, minute: Int ->
                reminderTime = String.format("%02d:%02d", hour, minute)
                timeError = null
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).also {
            it.show()
            showTimePicker.value = false
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { avatarUri = it.toString() }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            val savedUri = saveBitmapToCache(context, it)
            avatarUri = savedUri.toString()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
        if (cameraGranted) {
            cameraLauncher.launch(null)
        }
    }

    fun pickFromGallery() = galleryLauncher.launch("image/*")
    fun takePhoto() {
        when {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                cameraLauncher.launch(null)
            }
            else -> {
                val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                }
                permissionLauncher.launch(permissions)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактирование профиля") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable { }
            ) {
                if (avatarUri.isNotBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(avatarUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = "Default",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = { pickFromGallery() }) { Text("Галерея") }
                Button(onClick = { takePhoto() }) { Text("Камера") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("ФИО") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = resumeUrl,
                onValueChange = { resumeUrl = it },
                label = { Text("Ссылка на резюме (PDF)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = reminderTime,
                onValueChange = {
                    reminderTime = it
                    timeError = if (validateTime(it)) null else "Неверный формат. Используйте HH:mm"
                },
                label = { Text("Время любимой пары") },
                isError = timeError != null,
                supportingText = { timeError?.let { Text(it) } },
                trailingIcon = {
                    IconButton(onClick = { showTimePicker.value = true }) {
                        Icon(Icons.Default.AccessTime, contentDescription = "Выбрать время")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Проверка на пустое поле
                    if (reminderTime.isBlank()) {
                        timeError = "Поле обязательно для заполнения"
                        return@Button
                    }
                    // Проверка на корректный формат
                    if (!validateTime(reminderTime)) {
                        timeError = "Неверный формат. Используйте HH:mm"
                        return@Button
                    }
                    val updatedProfile = Profile(
                        name = name,
                        avatarUri = avatarUri,
                        resumeUrl = resumeUrl,
                        reminderTime = reminderTime
                    )
                    onSave(updatedProfile)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Готово")
            }
        }
    }
}

fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
    val cacheDir = context.cacheDir
    val file = File(cacheDir, "avatar_${System.currentTimeMillis()}.jpg")
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
    }
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}