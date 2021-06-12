package client.petmooby.com.br.petmooby.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.model.Resource
import client.petmooby.com.br.petmooby.model.enums.StatusAnimal
import client.petmooby.com.br.petmooby.model.repository.AnimalRepository
import client.petmooby.com.br.petmooby.util.Preference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
        private val animalRepository: AnimalRepository
) : ViewModel() {
    var animalListData : MutableLiveData<Resource<List<Animal>, StatusAnimal>> =
            MutableLiveData()

    fun getAnimalList(context: Context){
        viewModelScope.launch {
            val animals = animalRepository.getLocalAnimals()
            if(animals.isEmpty()) {
                val resource = animalRepository
                        .getAnimalListFromFirebase(
                                Preference.getUserPath(context)!!
                        )
                animalListData.value = resource
                animalRepository.clearLocalDataAnimals()
                resource.data()?.forEach {
                    animalRepository.insertAnimalLocally(it)
                }
            }else{
                animalListData.value = Resource(animals,StatusAnimal.SUCCESS)
            }
        }
    }
}