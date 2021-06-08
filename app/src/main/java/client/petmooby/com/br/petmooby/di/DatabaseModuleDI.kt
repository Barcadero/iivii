package client.petmooby.com.br.petmooby.di

import android.content.Context
import client.petmooby.com.br.petmooby.model.dao.AnimalDAO
import client.petmooby.com.br.petmooby.model.database.PetDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModuleDI  {
    @Singleton
    @Provides
    fun providePetDatabase(@ApplicationContext context: Context) : PetDatabase{
        return PetDatabase.getInstance(context)
    }

    @Provides
    fun provideAnimalDAO( petDatabase: PetDatabase) : AnimalDAO{
        return petDatabase.animalDAO()
    }
}