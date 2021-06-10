package client.petmooby.com.br.petmooby.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.model.Resource
import client.petmooby.com.br.petmooby.model.enums.StatusAnimal
import client.petmooby.com.br.petmooby.model.enums.StatusAnimalDelete
import client.petmooby.com.br.petmooby.model.enums.StatusAnimalUpdate
import client.petmooby.com.br.petmooby.model.repository.AnimalRepository
import client.petmooby.com.br.petmooby.util.TreatmentUtil
import client.petmooby.com.br.petmooby.util.VaccineUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewPetViewModel @Inject constructor(
        private val animalRepository: AnimalRepository
) : ViewModel(){

    val insertAnimalData : MutableLiveData<Resource<Animal,StatusAnimal>> by lazy {
        MutableLiveData<Resource<Animal,StatusAnimal>>()
    }

    val updateAnimalData : MutableLiveData<Resource<Animal,StatusAnimalUpdate>> by lazy {
        MutableLiveData<Resource<Animal,StatusAnimalUpdate>>()
    }

    val deleteAnimalData : MutableLiveData<StatusAnimalDelete> by lazy {
        MutableLiveData<StatusAnimalDelete>()
    }

    fun addAnimalToFireStorageAndLocally(animal: Animal){
        viewModelScope.launch {
            val resource = animalRepository.insertAnimalInFirebase(animal)
            if(resource.status() == StatusAnimal.SUCCESS){
                animalRepository.insertAnimalLocally(animal)
            }
            insertAnimalData.value = resource
        }
    }

    fun updateAnimal(animal: Animal){
        viewModelScope.launch {
            val resource = animalRepository.updateAnimalOnFireStorage(animal,true)
            updateAnimalData.value = resource
        }
    }

    fun deleteAnimal(animalId : String){
        viewModelScope.launch {
            deleteAnimalData.value = animalRepository.removeAnimal(animalId,true)
        }
    }

    fun removeEvents(context: Context, animal: Animal){
        //TODO put this method on viewmodel
        VaccineUtil().cancelEventVaccinesForAAnimal(context,animal)
        TreatmentUtil.cancelEventTreatmentForAAnimal(context,animal)
    }
}