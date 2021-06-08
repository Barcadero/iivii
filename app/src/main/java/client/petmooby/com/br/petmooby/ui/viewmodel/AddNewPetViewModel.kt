package client.petmooby.com.br.petmooby.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.model.repository.AnimalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AddNewPetViewModel @Inject constructor(
        private val animalRepository: AnimalRepository
) : ViewModel(){

    val insertAnimalData : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun insertAnimal(animal : Animal){
        viewModelScope.launch {
            try {
                animalRepository.insertAnimalLocally(animal)
                insertAnimalData.value = true
            }catch (e : Exception){
                insertAnimalData.value = false
            }
        }
    }
}