package client.petmooby.com.br.petmooby.actvity

import android.support.v7.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Created by idoctor on 06/08/2019.
 */
open class BaseActivity: AppCompatActivity() {
    protected var fbReference = FirebaseFirestore.getInstance()
}