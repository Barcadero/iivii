package client.petmooby.com.br.petmooby.model.repository

import client.petmooby.com.br.petmooby.model.Resource
import client.petmooby.com.br.petmooby.model.enums.StatusLogin
import com.google.firebase.firestore.FirebaseFirestore


abstract class BaseRepo {
    protected var fbReference = FirebaseFirestore.getInstance()
}