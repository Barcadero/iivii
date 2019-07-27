package client.petmooby.com.br.petmooby.util

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

/**
 * Created by idoctor on 26/07/2019.
 */
object PermissionUtil {

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_GALLERY = 2
    val PERMISSION_REQUEST_CODE: Int = 101
    fun checkPersmission(context: Context): Boolean {
        return (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    fun requestPermission(context: Activity) {
        ActivityCompat.requestPermissions(context, arrayOf(READ_EXTERNAL_STORAGE, CAMERA),
                PERMISSION_REQUEST_CODE)
    }
}