package client.petmooby.com.br.petmooby.data.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import client.petmooby.com.br.petmooby.data.dao.UserDAO
import client.petmooby.com.br.petmooby.data.enums.EnumUserStatus
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.model.Resource
import client.petmooby.com.br.petmooby.model.User
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataSource @Inject constructor(
        private val userDAO: UserDAO
) {
    private var fbReference = FirebaseFirestore.getInstance()
    private val userLiveData by lazy {
        MutableLiveData<Resource<User?,EnumUserStatus>>()
    }
    private val docRefUser = fbReference.collection(CollectionsName.USER)
    fun getFacebookUser(userId : String) : LiveData<Resource<User?,EnumUserStatus>>{
        docRefUser
                .whereEqualTo(User.USER_ID_FACEBOOK,userId)
                .get()
                .addOnCompleteListener { task ->
                    task.isSuccessful.let { success ->
                        if(success){
                            task.result?.isEmpty.let { isEmpty ->
                                if(isEmpty == true){
                                    userLiveData.value = Resource(User(),EnumUserStatus.EMPTY)
                                }else{
                                    val user = task.result?.documents?.get(0)!!.toObject(User::class.java)
                                    userLiveData.value = Resource(user,EnumUserStatus.SUCCESS)
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    userLiveData.value = Resource(EnumUserStatus.FAIL,it.message)
                }
        return userLiveData
    }

    fun updateUser(documentId: String, user: User) : LiveData<Resource<User?,EnumUserStatus>>{
        docRefUser.document(documentId)
                .set(user)
                .addOnSuccessListener {
                    userLiveData.value = Resource(user, EnumUserStatus.UPDATED)
                }
                .addOnFailureListener {
                    userLiveData.value = Resource(user,EnumUserStatus.FAIL_UPDATE)
                }
        return userLiveData
    }

    private fun getUserFromRemoteDatabase(userId: String) : LiveData<Resource<User?,EnumUserStatus>> {
        docRefUser.document(userId)
                .get()
                .addOnCompleteListener {task ->
                    task.isSuccessful.let {
                        if(task.result?.exists()!!){
                            val user = task.result?.toObject(User::class.java)
                            userLiveData.value = Resource(user,EnumUserStatus.SUCCESS)
                        }else{
                            userLiveData.value = Resource(EnumUserStatus.SUCCESS)
                        }
                    }
                }
                .addOnFailureListener {
                    userLiveData.value = Resource(EnumUserStatus.FAIL,it.message)
                }
        return userLiveData
    }

    fun getUser(userId: String) : LiveData<Resource<User?,EnumUserStatus>> {
        //TODO check on cache first
        var hasValidCache = false
        return if(!hasValidCache){
            getUserFromRemoteDatabase(userId)
        }else{
            //TODO returns from cache
            userLiveData
        }
    }

    fun saveNewUser(user: User) : LiveData<Resource<User?,EnumUserStatus>> {
        docRefUser.add(user)
                .addOnSuccessListener { documentReference ->
                    run {
                        user.document = documentReference.id
                        userLiveData.value = Resource(user, EnumUserStatus.SUCCESS)
                    }
                }
                .addOnFailureListener {
                    userLiveData.value = Resource(EnumUserStatus.FAIL,it.message)
                }
        return userLiveData
    }
}