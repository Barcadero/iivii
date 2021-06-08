package client.petmooby.com.br.petmooby.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import client.petmooby.com.br.petmooby.consts.DATABASE_NAME
import client.petmooby.com.br.petmooby.model.dao.AnimalDAO
import client.petmooby.com.br.petmooby.model.entities.AnimalEntity


@Database(version = 1, entities = [AnimalEntity::class])
abstract class PetDatabase : RoomDatabase() {

    abstract fun animalDAO(): AnimalDAO
    companion object {

        // For Singleton instantiation
        @Volatile private var instance: PetDatabase? = null

        fun getInstance(context: Context): PetDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): PetDatabase {
            return Room.databaseBuilder(context, PetDatabase::class.java, DATABASE_NAME)
                    .build()
        }
    }
}