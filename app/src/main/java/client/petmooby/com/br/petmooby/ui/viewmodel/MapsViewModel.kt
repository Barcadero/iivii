package client.petmooby.com.br.petmooby.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import client.petmooby.com.br.petmooby.model.Resource
import client.petmooby.com.br.petmooby.model.api.response.MapsVetResponse
import client.petmooby.com.br.petmooby.model.enums.StatusVetsMaps
import client.petmooby.com.br.petmooby.model.repository.MapsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
        private val mapsRepository: MapsRepository
) : ViewModel() {
    val vetsNearLiveData : MutableLiveData<Resource<MapsVetResponse, StatusVetsMaps>> by lazy {
        MutableLiveData<Resource<MapsVetResponse,StatusVetsMaps>>()
    }
    fun getVetsNear(location: String, language:String){
        viewModelScope.launch {
            vetsNearLiveData.value = mapsRepository.getNearVetClinics(location,language)
        }
    }
}