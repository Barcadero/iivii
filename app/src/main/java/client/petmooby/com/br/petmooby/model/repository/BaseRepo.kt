package client.petmooby.com.br.petmooby.model.repository

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Singleton


abstract class BaseRepo {
    protected var fbReference = FirebaseFirestore.getInstance()
}