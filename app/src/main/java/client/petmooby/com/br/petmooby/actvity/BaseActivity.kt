package client.petmooby.com.br.petmooby.actvity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import client.petmooby.com.br.petmooby.MainActivity
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.extensions.showAlert
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.util.CameraUtil
import client.petmooby.com.br.petmooby.util.ImageUtil
import client.petmooby.com.br.petmooby.util.LogUtil
import client.petmooby.com.br.petmooby.util.VariablesUtil
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mvc.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.toast
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File


/**
 * Created by idoctor on 06/08/2019.
 */
open class BaseActivity: AppCompatActivity() {
    protected var fbReference = FirebaseFirestore.getInstance()
    protected var animalRef = fbReference.collection(CollectionsName.ANIMAL)
    protected var docRefUser = fbReference.collection(CollectionsName.USER)
    protected var isForUpdate:Boolean = false
    private val camera      = CameraUtil()
    protected val TAKE_PICTURE        = 130
    protected val PICK_IMAGE                    = 125
    protected val photoName   = "photoProfile.jpg"
    protected var currentImageFilePath:String?=null
    var storage             = FirebaseStorage.getInstance().reference

    protected fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    protected fun saveAnimal(onSuccess : () -> Unit) {
        animalRef.document(VariablesUtil.gbSelectedAnimal?.id!!)
                .set(VariablesUtil.gbSelectedAnimal!!)
                .addOnSuccessListener {
                    runOnUiThread(onSuccess)
                }.addOnFailureListener {
                    it.printStackTrace()
                    showAlert(R.string.wasNotPossibleSave)
                }
    }

    protected fun startsCameraActivityForResult() {
        startActivityForResult(camera.open(this, photoName), TAKE_PICTURE)
    }

    protected fun onResultActivityForGallery(requestCode: Int, resultCode: Int, data: Intent?, imageView:ImageView?, withQuality:Boolean = false): Bitmap?{
        if(resultCode == Activity.RESULT_OK){
            val bitmapOriginal      = ImagePicker.getImageFromResult(this, requestCode, resultCode, data)
            val imagePathFromResult = ImagePicker.getImagePathFromResult(this, requestCode, resultCode, data)
            currentImageFilePath = imagePathFromResult
            LogUtil.logDebug("Width: ${bitmapOriginal?.width}")
            LogUtil.logDebug("Height: ${bitmapOriginal?.height}")
            return if(withQuality){
                val out = ByteArrayOutputStream()
                bitmapOriginal?.compress(Bitmap.CompressFormat.PNG, 100, out)
                val decoded = BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()))
                decoded
            }else {
                val bitmap = ImageUtil.resizeImage(bitmapOriginal!!, 400f)
                imageView?.setImageBitmap(bitmapOriginal)
                LogUtil.logDebug("Width: ${bitmap?.width}")
                LogUtil.logDebug("Height: ${bitmap?.height}")
                bitmap
            }

        }
        return null
    }

    protected fun onResultActivityForCamera(requestCode: Int, resultCode: Int, data: Intent?,imageView:ImageView?):Bitmap?{
        if(resultCode != Activity.RESULT_OK){
            return null
        }
        var f = File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString())
        for (temp : File in f.listFiles()) {
            if (temp.name == photoName) {
                f = temp
                break
            }
        }
        if (!f.exists()) {
            Toast.makeText(this,
                    getString(R.string.errorWhileCapturingImage), Toast.LENGTH_LONG)
                    .show()
            return null

        }
        currentImageFilePath = f.absolutePath
        var bitmapOrigin = BitmapFactory.decodeFile(f.absolutePath)
        if(bitmapOrigin == null){
            toast(getString(R.string.cantGetFileImage))
        }
        return try{
            val bitmap = ImageUtil.resizeImage(bitmapOrigin,350f)
            imageView?.setImageBitmap(bitmap)
            bitmap

        } catch (e:Exception ) {
            e.printStackTrace()
            null
        }
    }

    protected fun showImagePicker() {
        ImagePicker.pickImage(this, getString(R.string.selectAPicture), PICK_IMAGE, true)
    }

}