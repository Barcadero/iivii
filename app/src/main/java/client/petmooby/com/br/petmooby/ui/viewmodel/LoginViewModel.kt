package client.petmooby.com.br.petmooby.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    init {
        Log.i("viewmodel","creating login view model.")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("viewmodel","Destroying view model.")
    }
}