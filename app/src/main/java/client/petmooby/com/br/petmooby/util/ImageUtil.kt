package client.petmooby.com.br.petmooby.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import client.petmooby.com.br.petmooby.application.Application
import java.io.File

/**
 * Created by Rafael Rocha on 26/07/2019.
 */
object ImageUtil {
    fun resize(file: File, width:Int, height:Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.absolutePath,options)
        options.inSampleSize = calculateInSampleSize(options,width,height)
        //Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return  BitmapFactory.decodeFile(file.absolutePath,options)
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, width: Int, height: Int): Int{
        val heightOut = options.outHeight
        val widthOut  = options.outWidth
        var inSampleSize = 1
        if(heightOut > height || widthOut > width){
            val halfHeigh = heightOut / 2
            val halfWidth = widthOut / 2
            while(halfHeigh / inSampleSize >= height &&
                    halfWidth / inSampleSize >= width){
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun resizeMapIcons(iconName: String, width: Int, height: Int): Bitmap {
        val imageBitmap = BitmapFactory.decodeResource(Application.getInstance().resources,  Application.getInstance().resources.getIdentifier(iconName, "drawable",Application.getInstance().packageName))
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }

    fun exifToDegrees(exifOrientation: Int): Int {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270
        }
        return 0
    }

    fun rotate(original: Bitmap, matrix: Matrix?): Bitmap {
        return if (matrix == null) {
            original
        } else Bitmap.createBitmap(original, 0, 0, original.width, original.height, matrix, true)
    }
}