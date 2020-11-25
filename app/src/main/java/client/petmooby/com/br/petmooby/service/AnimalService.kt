package client.petmooby.com.br.petmooby.service

import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.ui.repository.AnimalRepository

class AnimalService (
        private val animalRepository: AnimalRepository
){
    fun getAnimals(callback: (animals : List<Animal>) -> Unit){
        animalRepository.getAnimals(callback)
    }
}