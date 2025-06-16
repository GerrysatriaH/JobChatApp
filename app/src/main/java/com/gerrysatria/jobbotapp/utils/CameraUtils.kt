package com.gerrysatria.jobbotapp.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import com.gerrysatria.jobbotapp.BuildConfig
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CameraUtils {
    private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
    private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

    fun getImageUri(context: Context): Uri {
        var uri: Uri? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
            }
            uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
        }
        return uri ?: getImageUriForPreQ(context)
    }

    private fun getImageUriForPreQ(context: Context): Uri {
        val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(filesDir, "/MyCamera/$timeStamp.jpg")
        if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()
        return FileProvider.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            imageFile
        )
    }

    fun getFileName(context: Context, uri: Uri): String {
        var name = "unknown_file"
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst()) {
                name = it.getString(nameIndex)
            }
        }
        return name
    }
}