package com.example.voyaassessment.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream

class Tools {
    companion object{

        fun showToast(context: Context?, message: String?) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        // Save Bitmap to Internal Storage when using Camera
        fun saveImageToInternalStorage(context: Context, bitmap: Bitmap): Uri {
            val filename = "${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, filename)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()
            return Uri.fromFile(file)
        }

        fun uriToFile(context: Context, uri: Uri): File? {
            return try {
                val inputStream = context.contentResolver.openInputStream(uri) ?: return null
                val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
                tempFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                tempFile
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

         fun handleErrorResponse(errorBody: String?): ApiResponse.ErrorResponse? =
            Gson().fromJson(errorBody, ApiResponse.ErrorResponse::class.java)

    }
}