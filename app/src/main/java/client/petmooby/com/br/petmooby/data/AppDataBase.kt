package client.petmooby.com.br.petmooby.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import client.petmooby.com.br.petmooby.data.dao.UserDAO
import client.petmooby.com.br.petmooby.util.DATABASE_NAME

@Database(entities = [], version = 1)
abstract class AppDataBase : RoomDatabase() {
   companion object {
       @Volatile private var instance: AppDataBase? = null

       fun getInstance(context: Context): AppDataBase {
           return instance ?: synchronized(this) {
               instance ?: buildDatabase(context).also { instance = it }
           }
       }

       private fun buildDatabase(context: Context): AppDataBase {
           return Room.databaseBuilder(context, AppDataBase::class.java, DATABASE_NAME)
                   .addCallback(
                           object : RoomDatabase.Callback() {
                               override fun onCreate(db: SupportSQLiteDatabase) {
                                   super.onCreate(db)
//                                   val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
//                                   WorkManager.getInstance(context).enqueue(request)
                               }
                           }
                   )
                   .build()
       }
   }
    abstract fun getUserDAO() : UserDAO
}