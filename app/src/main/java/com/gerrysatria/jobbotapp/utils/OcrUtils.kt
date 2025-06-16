package com.gerrysatria.jobbotapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object OcrUtils {
    private val ocrModel = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun renderPdfToBitmapList(context: Context, filePath: String): List<Bitmap> {
        val bitmaps = mutableListOf<Bitmap>()
        val file = File(filePath)
        val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val renderer = PdfRenderer(fileDescriptor)

        if (renderer.pageCount > 2) {
            renderer.close()
            throw IllegalArgumentException("PDF memiliki lebih dari 2 halaman, tidak dapat diproses.")
        }

        for (i in 0 until renderer.pageCount) {
            val page = renderer.openPage(i)
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            bitmaps.add(bitmap)
            page.close()
        }
        renderer.close()
        return bitmaps
    }

    suspend fun recognizeTextFromBitmap(bitmap: Bitmap): String {
        return suspendCoroutine { cont ->
            val image = InputImage.fromBitmap(bitmap, 0)
            ocrModel.process(image)
                .addOnSuccessListener { visionText ->
                    cont.resume(visionText.text)
                }
                .addOnFailureListener { e ->
                    cont.resumeWithException(e)
                }
        }
    }

    suspend fun processImageFile(filePath: String): String {
        val bitmap = BitmapFactory.decodeFile(filePath)
        return recognizeTextFromBitmap(bitmap)
    }

    suspend fun processPdfFile(context: Context, filePath: String): String {
        val bitmaps = renderPdfToBitmapList(context, filePath)
        val allText = StringBuilder()
        for (bitmap in bitmaps) {
            val text = recognizeTextFromBitmap(bitmap)
            allText.append(text).append("\n")
        }
        return allText.toString()
    }
}