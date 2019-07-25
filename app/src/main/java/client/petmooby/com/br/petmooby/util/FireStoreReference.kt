package client.petmooby.com.br.petmooby.util

import com.google.firebase.firestore.DocumentReference

/**
 * Created by Rafael Rocha on 15/07/2019.
 */
class FireStoreReference {
    companion object {
        var docRefUser  : DocumentReference?=null
        var docRefMyPets: MutableList<DocumentReference>?=null

        fun saveAnimalReference(documentReference: DocumentReference){
            if(docRefMyPets == null){
                docRefMyPets = mutableListOf()
            }
            docRefMyPets!!.add(documentReference)
        }
    }

}