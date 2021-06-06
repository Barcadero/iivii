package client.petmooby.com.br.petmooby.ui.viewmodel

import android.content.Context
import androidx.lifecycle.*
import client.petmooby.com.br.petmooby.model.Resource
import client.petmooby.com.br.petmooby.model.UserLogin
import client.petmooby.com.br.petmooby.model.enums.StatusLogin
import client.petmooby.com.br.petmooby.model.repository.LoginRepository
import client.petmooby.com.br.petmooby.util.Preference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel  @Inject constructor(
        private val loginRepository: LoginRepository
) : ViewModel() {
    var  loginLiveData : MutableLiveData<Resource<UserLogin, StatusLogin>> =
        MutableLiveData()

    fun doLoginSystem(email : String, pwd : String) {
        viewModelScope.launch {
            val resource = loginRepository.doLoginSystem(email,pwd)
            loginLiveData.value = resource
        }
    }

    fun setPreference(context: Context, userLogin: UserLogin){
        Preference.setUserEmail(context,userLogin.email)
        Preference.setUserDatabaseId(context, userLogin.docId)
        Preference.setUserId(context,userLogin.docId)
        Preference.setUserType(context,userLogin.type.ordinal)
    }

}