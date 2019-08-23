package client.petmooby.com.br.petmooby.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import android.util.Log
import java.io.File
import java.io.FileOutputStream

/**
 * Created by Rafael Rocha on 26/07/2019.
 */
class CameraUtil {

    val FILE_KEY = "file"
    companion object {
        private val TAG = "CAMERA"
    }
    var file: File? = null
        private set
    //Se girou a tela recupera o status
    fun init(icicle: Bundle?) {
        if(icicle != null){
            file = icicle.getSerializable(FILE_KEY) as File
        }
    }
    //Salvar o estado
    fun onSaveInstanceState(outState: Bundle){
        if(file != null){
            outState.putSerializable(FILE_KEY,file)
        }
    }
    //Intent para abrir a camera
    fun open(context: Context, fileName:String) : Intent {
        file = getSdCardFile(context,fileName)
        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val uri = FileProvider.getUriForFile(context,context.applicationContext.packageName+".provider", file!!)
        i.putExtra(MediaStore.EXTRA_OUTPUT,uri)
        return i
    }
    //Cria o arquivo de foto no diretorio privado do app
    private fun getSdCardFile(context: Context, fileName:String): File {
        val sdCardDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if(!sdCardDir.exists()){
            sdCardDir.mkdir()
        }
        return File(sdCardDir,fileName)
    }
    //Read the bitmap at desired size
    fun getBitmap(width: Int,height: Int): Bitmap?{
        file?.apply {
            if(exists()){
                Log.d(TAG,absolutePath)
                return ImageUtil.resize(this,width,height)
            }
        }
        return null
    }

    //Save the bitmap
    fun save(bitmap: Bitmap){
        file?.apply {
            val out = FileOutputStream(this)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out)
            out.close()
            Log.d(TAG,"Photo saved in ${this.absolutePath}")
        }
    }
}