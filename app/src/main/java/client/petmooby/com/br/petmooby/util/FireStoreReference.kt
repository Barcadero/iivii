package client.petmooby.com.br.petmooby.util

import com.google.firebase.firestore.DocumentReference

/**
 * Created by Rafael Rocha on 15/07/2019.
 */
class FireStoreReference {
    companion object {
        var docRefUser  : DocumentReference?=null
        var docRefMyPets: List<DocumentReference>?=null
    }

}