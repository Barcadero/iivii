package client.petmooby.com.br.petmooby.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import client.petmooby.com.br.petmooby.model.repository.AnimalRepository
import client.petmooby.com.br.petmooby.util.NotificationWorkerUtil
import client.petmooby.com.br.petmooby.util.Preference
import client.petmooby.com.br.petmooby.util.VariablesUtil
import com.facebook.login.LoginManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val animalRepository: AnimalRepository
)
    : ViewModel() {

    val clearLiveData : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    fun clear(context: Context){
        LoginManager.getInstance().logOut()
        Preference.clear(context)
        NotificationWorkerUtil().cancel(context)
        VariablesUtil.clear()
        viewModelScope.launch {
            animalRepository.clearLocalDataAnimals()
            clearLiveData.value = true
        }
    }
}