package client.petmooby.com.br.petmooby.model.repository

import android.util.Log
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.model.Resource
import client.petmooby.com.br.petmooby.model.dao.AnimalDAO
import client.petmooby.com.br.petmooby.model.entities.AnimalEntity
import client.petmooby.com.br.petmooby.model.enums.StatusAnimal
import client.petmooby.com.br.petmooby.util.DateTimeUtil
import client.petmooby.com.br.petmooby.util.JsonUtil
import client.petmooby.com.br.petmooby.util.TAG_READ_FIREBASE
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimalRepository @Inject constructor(
        private val animalDAO: AnimalDAO
) : BaseRepo() {
    private var docRefVet = FirebaseFirestore.getInstance()
    suspend fun getAnimalListFromFirebase(userPath : String) : Resource<List<Animal>, StatusAnimal> {
        val userRef = docRefVet.document(userPath)
        return try {
            Log.d(TAG_READ_FIREBASE, "Get Animals")
            val query = docRefVet.collection(CollectionsName.ANIMAL)
                    .whereEqualTo("user", userRef)
                    .get()
                    .await()
            if(query.isEmpty){
                Resource(null, StatusAnimal.EMPTY)
            }else{
                Resource(getAnimalListFromQuerySnapshot(query),StatusAnimal.SUCCESS)
            }
        }catch (e : Exception){
            return Resource(null, StatusAnimal.FAIL)
        }

    }

    private fun getAnimalListFromQuerySnapshot(querySnapshot : QuerySnapshot) : List<Animal>{
        val animalList = mutableListOf<Animal>()
        querySnapshot.documents.forEach{
            val animal = it.toObject(Animal::class.java)
            if(animal?.id == null || animal.id!!.isEmpty()){
                animal?.id = it.id
            }
            animalList.add(animal!!)
        }
        return animalList
    }

    suspend fun insertAnimalLocally(animal: Animal){
        val json = JsonUtil.toJson(animal)
        val animalEntity = AnimalEntity().apply {
            this.idFirebase     = animal.id!!
            this.animalAsJSON   = json
            this.time           = DateTimeUtil.addHourdToADate(Date(),12).time
        }
        animalDAO.insert(animalEntity)
    }

    suspend fun getLocalAnimals() : List<Animal>{
        val animals     = animalDAO.getLocalAnimals()
        val animalList  = mutableListOf<Animal>()
        if(animals.isEmpty()){
            return animalList
        }else {
            if(animals[0].time < Date().time){
                animalList.clear()
            }else{
                animals.forEach {
                    animalList.add(
                            JsonUtil.fromJson(it.animalAsJSON, Animal::class.java)
                    )
                }
            }
        }
        return animalList
    }

    suspend fun clearLocalDataAnimals(){
        animalDAO.clear()
    }
}