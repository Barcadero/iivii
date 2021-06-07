package client.petmooby.com.br.petmooby.model.repository

import client.petmooby.com.br.petmooby.extensions.onFailedQueryReturn
import client.petmooby.com.br.petmooby.fragment.HomeFragment
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.model.Resource
import client.petmooby.com.br.petmooby.model.User
import client.petmooby.com.br.petmooby.model.enums.StatusUser
import client.petmooby.com.br.petmooby.util.FireStoreReference
import com.google.firebase.firestore.DocumentReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor() : BaseRepo() {
//    private var docRefUser = fbReference.collection(CollectionsName.USER)

//    suspend fun getUserById(userId : String) : Resource<DocumentReference,StatusUser>{
//        docRefUser.document(userId)
//                //.whereEqualTo(User.USER_ID_FACEBOOK,userId)
//                .get()
//                .addOnCompleteListener {
//                    task ->
//                    run {
//                        if (task.result?.exists()!!) {
////                            dialog.dismiss()
////                            FireStoreReference.docRefUser = task.result?.reference
////                            switchFragment(HomeFragment())
//                        }else{
////                            dialog.dismiss()
////                            switchFragment(HomeFragment())
//                        }
//                    }
//                }
//
//                .addOnFailureListener {
//                    exception -> onFailedQueryReturn(dialog,exception.message!!)
//                }
//    }

}