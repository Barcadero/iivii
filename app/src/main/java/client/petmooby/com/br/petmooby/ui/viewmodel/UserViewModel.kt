package client.petmooby.com.br.petmooby.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import client.petmooby.com.br.petmooby.data.enums.EnumUserStatus
import client.petmooby.com.br.petmooby.data.repository.UserRepository
import client.petmooby.com.br.petmooby.model.Resource
import client.petmooby.com.br.petmooby.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
        private val userRepository: UserRepository
        ): ViewModel() {

    init {
        Log.i("viewmodel","creating login view model.")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("viewmodel","Destroying view model.")
    }
    var userLiveData : LiveData<Resource<User?, EnumUserStatus>>? = null
    var foundUserLiveData : LiveData<Resource<User?, EnumUserStatus>>? = null

    fun getFaceBookUser(userId: String){
        userLiveData = userRepository.getFacebookUser(userId)
    }

    fun saveMissingUserInformation(context: Context, documentId : String, user : User){
        userLiveData = userRepository.saveMissingUserInformation(context, documentId,user)
    }

    fun getUser(userId: String){
        foundUserLiveData = userRepository.getUser(userId)
    }

    fun saveCurrentUser(context: Context){
        userLiveData = userRepository.saveCurrentUser(context)
    }

}