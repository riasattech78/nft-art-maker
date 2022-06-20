@file:Suppress("DEPRECATION")

package com.bnx.ntart.creator.utility

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.view.drawToBitmap
import com.bnx.ntart.R
import com.bnx.ntart.creator.activities.canvas.outerCanvasInstance
import com.bnx.ntart.creator.activities.canvas.projectTitle
import com.bnx.ntart.creator.enums.OutputCode
import com.bnx.ntart.utils.Tools
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class FileHelperUtilities(private val context: Context) {
    companion object {
        fun createInstance(context: Context) = FileHelperUtilities(context)
    }

    fun saveBitmapAsImage(compressionOutputQuality: Int,
                          compressionFormat: Bitmap.CompressFormat,
                          onTaskFinished: (OutputCode, Uri?, String?) -> Unit) {
        // Thanks to https://stackoverflow.com/users/3571603/javatar on StackOverflow - quite a bit of the code is based off of their solution

        var exceptionMessage: String? = null
        var outputCode = OutputCode.SUCCESS
        val pathData = "image/jpeg"
        val outputName = if (compressionFormat == Bitmap.CompressFormat.PNG) "$projectTitle.png" else "$projectTitle.jpg"

        val bitmapToCompress = outerCanvasInstance.fragmentHost.drawToBitmap()
        val imageOutStream: OutputStream?

        var uri: Uri? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, outputName)
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + File.separator + context.getString(R.string.app_name))
                uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                imageOutStream = resolver.openOutputStream(uri!!)
            } else {
                val imageFile = File(getDirectory(), outputName)
                imageOutStream = FileOutputStream(imageFile)
                uri = Uri.fromFile(imageFile)
            }

            bitmapToCompress.compress(compressionFormat, compressionOutputQuality, imageOutStream)
            imageOutStream!!.flush()
            imageOutStream.close()


        } catch (exception: Exception) {
            exceptionMessage = exception.message
            outputCode = OutputCode.FAILURE
        } finally {
            onTaskFinished(outputCode, uri, exceptionMessage)
        }
        if (uri != null) {
            galleryAddPic(uri)
        }
    }

    fun openImageFromUri(uri: Uri, onTaskFinished: (OutputCode, String?) -> Unit) {
        var exceptionMessage: String? = null
        var outputCode = OutputCode.SUCCESS
        try {
            Tools.openImage(context, uri.toString())
        } catch (exception: Exception) {
            exceptionMessage = exception.message
            outputCode = OutputCode.FAILURE
        } finally {
            onTaskFinished(outputCode, exceptionMessage)
        }
    }

    private fun galleryAddPic(uri: Uri) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        mediaScanIntent.data = uri
        context.sendBroadcast(mediaScanIntent)
    }

    fun getDirectory(): File? {
        val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + context.getString(R.string.app_name)
        var storageDir: File? = File(imagesDir)
        if (!storageDir!!.exists()) storageDir.mkdirs()
        return storageDir
    }

    fun deleteFile(file: File) {
        if(file.exists()){
            file.delete()
        }
    }
}
