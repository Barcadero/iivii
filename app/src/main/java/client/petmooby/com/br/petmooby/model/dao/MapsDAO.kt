package client.petmooby.com.br.petmooby.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import client.petmooby.com.br.petmooby.model.entities.NearbyVetsEntity

@Dao
interface MapsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNearbyVets(nearbyVetsEntity: NearbyVetsEntity)
    @Query("SELECT * FROM nearby_vets")
    suspend fun getNearbyVets() : NearbyVetsEntity?
    @Query("DELETE FROM nearby_vets" )
    suspend fun clear()
}