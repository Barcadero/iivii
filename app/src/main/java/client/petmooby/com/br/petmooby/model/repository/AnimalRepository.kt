package client.petmooby.com.br.petmooby.model.repository

import android.util.Log
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.model.Resource
import client.petmooby.com.br.petmooby.model.dao.AnimalDAO
import client.petmooby.com.br.petmooby.model.entities.AnimalEntity
import client.petmooby.com.br.petmooby.model.enums.StatusAnimal
import client.petmooby.com.br.petmooby.model.enums.StatusAnimalDelete
import client.petmooby.com.br.petmooby.model.enums.StatusAnimalUpdate
import client.petmooby.com.br.petmooby.util.*
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
            logFB("Get Animals")
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

    suspend fun insertAnimalInFirebase(animal: Animal) : Resource<Animal, StatusAnimal>{
        return try {
            logFB("Insert Animal on FireStore")
            val query = docRefVet.collection(CollectionsName.ANIMAL)
                    .add(VariablesUtil.gbSelectedAnimal!!)
                    .await()
            if(query.id.isNotEmpty()){
                animal.id = query.id
                logFB("OK - Success added with id --> ${animal.id}")
                Resource(animal, StatusAnimal.SUCCESS)
            }else{
                logFB("Insert Error: id is empty")
                Resource(null, StatusAnimal.FAIL)
            }
        }catch (e : Exception){
            logFB("Exception ${e.message}")
            e.printStackTrace()
            Resource(null, StatusAnimal.FAIL)
        }
    }

    suspend fun updateAnimalOnFireStorage(animal: Animal, isToUpdateLocal:Boolean): Resource<Animal, StatusAnimalUpdate>{
        return try {
            docRefVet.collection(CollectionsName.ANIMAL)
                    .document(animal.id!!)
                    .set(animal)
                    .await()
            if(isToUpdateLocal){
                //TODO update animal on local database
            }
            Resource(animal, StatusAnimalUpdate.SUCCESS)
        }catch (e : Exception){
            Resource(null, StatusAnimalUpdate.FAIL)
        }
    }

    suspend fun removeAnimal(animalId : String, isToRemoveLocal: Boolean) : StatusAnimalDelete{
        return try {
            docRefVet.collection(CollectionsName.ANIMAL)
                 .document(animalId)
                 .delete()
                 .await()
            if(isToRemoveLocal){
                animalDAO.delete(animalId)
            }
            StatusAnimalDelete.SUCCESS
        }catch (e : Exception){
            StatusAnimalDelete.FAIL
        }
    }

    private fun logFB(message : String){
        Log.d(TAG_READ_FIREBASE, message)
    }
}