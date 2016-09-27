package com.uaemex.rr.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.net.Uri
import android.os.Environment
import android.util.Log
import groovy.transform.CompileStatic

@CompileStatic
class CameraUtil {

    File createPhoto(String name) {
        File storagePhotos = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "RR_Bitacora")
        if (!storagePhotos.exists()) {
            if (!storagePhotos.mkdirs()) {
                Log.d("CameraUtil", "Error al crear directorio")
            }
        }
        new File(storagePhotos.getPath() + File.separator + "${name + new Date().format("ddMMyyyy_HHmmss")}.jpg")
    }

    void addPictureToGallery(Context context, String picturePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        File f = new File(picturePath)
        Uri contentUri = Uri.fromFile(f)
        mediaScanIntent.setData(contentUri)
        context.sendBroadcast(mediaScanIntent)
    }

    File saveBitmapToFile(Bitmap bitmap, String photoName) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + File.separator + photoName)
        FileOutputStream fileOutputStream
        try {
            file.createNewFile()
            fileOutputStream = new FileOutputStream(file)
            fileOutputStream.write(bytes.toByteArray())
            fileOutputStream.close()
        } catch (Exception e) {
            Log.d("CameraUtil", "Error... " + e.message)
        }
        file
    }

    Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        int srcWidth = options.outWidth
        int srcHeight = options.outHeight
        int inSampleSize = 6

        if (srcHeight > destHeight || srcWidth > destWidth) {
            int halfHeight = (srcHeight / 2) as int
            int halfWidth = (srcWidth / 2) as int
            while ((halfHeight / inSampleSize) >= srcHeight && (halfWidth / inSampleSize) >= srcWidth) {
                inSampleSize *= 2
            }
        }
        options = new BitmapFactory.Options()
        options.inSampleSize = inSampleSize
        options.inJustDecodeBounds = false
        BitmapFactory.decodeFile(path, options)

    }

    Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point()
        activity.getWindowManager()
                .getDefaultDisplay()
                .getSize(size)
        getScaledBitmap(path, size.x, size.y)
    }
}