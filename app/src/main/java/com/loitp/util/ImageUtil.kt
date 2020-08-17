package com.loitp.util

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream

class ImageUtil {
    companion object {
        fun saveBitmap(context: Context, bitmap: Bitmap, fileName: String): File? {
            return if (isExternalStorageWritable()) {
                saveImage(context = context, finalBitmap = bitmap, fileName = fileName)
            } else {
                null
            }
        }

        fun getFile(context: Context, fileName: String): File? {
            val root = context.getExternalFilesDir(null)?.absolutePath
            val myDir = File("$root/.mapTracker")
            val path = myDir.absolutePath + "/" + fileName
            return File(path)
        }

        private fun saveImage(context: Context, finalBitmap: Bitmap, fileName: String): File? {
            val root = context.getExternalFilesDir(null)?.absolutePath
            val myDir = File("$root/.mapTracker")
            myDir.mkdirs()
            val file = File(myDir, fileName)
            if (file.exists()) {
                file.delete()
            }
            try {
                val out = FileOutputStream(file)
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 75, out)
                out.flush()
                out.close()
                return file
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        /* Checks if external storage is available for read and write */
        private fun isExternalStorageWritable(): Boolean {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state
        }
    }
}
