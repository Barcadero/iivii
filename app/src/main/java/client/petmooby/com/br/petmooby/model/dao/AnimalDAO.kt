package client.petmooby.com.br.petmooby.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import client.petmooby.com.br.petmooby.model.entities.AnimalEntity

@Dao
interface AnimalDAO {

    @Query("SELECT * FROM animal WHERE id_firebase = :id ")
    suspend fun getAnimalByIdFirebase(id : String) : AnimalEntity
    @Query("SELECT * FROM animal")
    suspend fun getLocalAnimals() : List<AnimalEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(animalEntity: AnimalEntity)
    @Query("DELETE FROM animal")
    suspend fun clear()
    @Query("DELETE FROM animal WHERE id_firebase = :idFireStore")
    suspend fun delete(idFireStore: String)
}