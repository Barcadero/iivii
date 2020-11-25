package client.petmooby.com.br.petmooby.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.service.AnimalService
import client.petmooby.com.br.petmooby.ui.repository.AnimalRepository


class AnimalViewModel(
        private val animalService: AnimalService
) : ViewModel() {

    init {
        Log.i("viewmodel","init view model $this")
    }
    val animalLiveData = MutableLiveData<List<Animal>>()
    fun getAnimals(){
        animalService.getAnimals {animals ->
            animalLiveData.value = animals
        }
    }

//    @Suppress("UNCHECKED_CAST")
//    class AnimalViewModelFactory(private val repository: AnimalRepository) : ViewModelProvider.Factory{
//        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            return AnimalViewModel(repository) as T
//        }
//
//    }

}