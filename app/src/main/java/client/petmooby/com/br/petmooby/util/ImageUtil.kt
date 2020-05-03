package client.petmooby.com.br.petmooby.util

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import client.petmooby.com.br.petmooby.application.Application
import id.zelory.compressor.Compressor
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.math.BigDecimal
import java.util.*
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import client.petmooby.com.br.petmooby.R
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import id.zelory.compressor.constraint.destination
import java.lang.Exception


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

     fun bitmapToFile(context: Context, bitmap:Bitmap): File {
        // Get the context wrapper
        val wrapper = ContextWrapper(context)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file,"${UUID.randomUUID()}.jpg")

         var stream: OutputStream?=null
        try{
            // Compress the bitmap and save in jpg format
            stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
        }catch (e: IOException){
            e.printStackTrace()
        }finally {
            stream?.flush()
            stream?.close()
        }

        // Return the saved bitmap uri
        return file
    }

   /* fun compress(context: Context, f: File, bitmapOrigin: Bitmap): Bitmap {

        val width   = bitmapOrigin.width.toDouble()
        val height  = bitmapOrigin.height.toDouble()
        val base = 200
        val margin = 10

        val scale = if (width > height) {
            ((base * 100) / width)
        } else
            ((base * 100) / height)

        val maxWidth: Int
        val maxHeight: Int
        if (width > height) {
            val led = getRelativeProportion(height,scale)
            maxWidth = base
            maxHeight = led + margin
        } else {
            val led = getRelativeProportion(width, scale)
            maxWidth = led + margin
            maxHeight = base
        }

        return Compressor(context)
//                .setMaxHeight(maxHeight)
//                .setMaxWidth(maxWidth)
                .setQuality(100)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .compressToBitmap(f)
    }
*/
    private fun getRelativeProportion(width: Double, scale: Double): Int {
        var set = (width * (scale / 100))
        var led = BigDecimal(set).setScale(1, BigDecimal.ROUND_HALF_UP).toInt()
        return led
    }

    fun rotate(original: Bitmap, filePath:String?): Bitmap {

//            Bitmap.createBitmap(original, 0, 0, original.width, original.height, matrix, true)
//            val exif: ExifInterface
            try {
//                exif = ExifInterface(filePath)
//
//                val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
//
//                val matrix = Matrix()
//                if (orientation == 6) {
//                    matrix.postRotate(90f)
//
//                } else if (orientation == 3) {
//                    matrix.postRotate(180f)
//
//                } else if (orientation == 8) {
//                    matrix.postRotate(270f)
//
//                }
                var matrix: Matrix?     = null
                try {
                    val exif = ExifInterface(filePath)
                    val rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                    val rotationInDegrees = exifToDegrees(rotation)
                    matrix = Matrix()
                    if (rotation.toFloat() != 0f) {
                        matrix.preRotate(rotationInDegrees.toFloat())
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (original != null) {
                    return Bitmap.createBitmap(original, 0, 0, original.width, original.height, matrix, true)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return original

    }

    fun resizeImage(bitmapOrigin: Bitmap, newWidth: Float) : Bitmap{
        val scale = newWidth / bitmapOrigin.width
        val newHeight = bitmapOrigin.height * scale
        return Bitmap.createScaledBitmap(bitmapOrigin, newWidth.toInt(), newHeight.toInt(), false)

    }

    fun loadImageFromUrl(context: Context, url: String, view: ImageView, progress: ProgressBar?) {
        Picasso.with(context)
                .load(url)
                .fit()
                .centerCrop()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .into(view, object : Callback {
                    override fun onSuccess() {
                        if (progress != null) {
                            progress.visibility = View.GONE
                        }
                        view.visibility = View.VISIBLE
                    }

                    override fun onError() {
                        if (progress != null) {
                            progress.visibility = View.GONE
                        }
                        view.visibility = View.VISIBLE
                        view.setImageResource(R.drawable.no_image)
                    }
                })
    }

    fun loadImageFromFile(context: Context, file: File, view: ImageView, progress: ProgressBar?) {
        Picasso.with(context)
                .load(file)
                //.rotate(getCameraPhotoOrientation(file.absolutePath).toFloat())
                .fit()
                .centerCrop()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .into(view, object : Callback {
                    override fun onSuccess() {
                        if (progress != null) {
                            progress.visibility = View.GONE
                        }
                        view.visibility = View.VISIBLE
                    }

                    override fun onError() {
                        if (progress != null) {
                            progress.visibility = View.GONE
                        }
                        view.visibility = View.VISIBLE
                        view.setImageResource(R.drawable.no_image)
                    }
                })

    }

    fun getCameraPhotoOrientation( imageFilePath:String):Int {
        var rotate = 0
        try {

            val exif = ExifInterface(imageFilePath)
            val exifOrientation:String  = exif
                    .getAttribute(ExifInterface.TAG_ORIENTATION)
            Log.d("exifOrientation", exifOrientation)
            val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL)
            Log.d("ROTATE", "orientation : $orientation")
            rotate = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270->  270
                ExifInterface.ORIENTATION_ROTATE_180->  180
                ExifInterface.ORIENTATION_ROTATE_90->   90
                else -> 0
            }
        } catch (e:IOException) {
            e.printStackTrace()
            rotate = 0
        }

        return rotate
    }


}