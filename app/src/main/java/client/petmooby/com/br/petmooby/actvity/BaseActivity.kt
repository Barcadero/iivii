package client.petmooby.com.br.petmooby.actvity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import client.petmooby.com.br.petmooby.MainActivity
import client.petmooby.com.br.petmooby.model.CollectionsName
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Created by idoctor on 06/08/2019.
 */
open class BaseActivity: AppCompatActivity() {
    protected var fbReference = FirebaseFirestore.getInstance()
    protected var animalRef = fbReference.collection(CollectionsName.ANIMAL)
    protected var docRefUser = fbReference.collection(CollectionsName.USER)

    protected fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}