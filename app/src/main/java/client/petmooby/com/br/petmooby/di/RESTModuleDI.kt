package client.petmooby.com.br.petmooby.di

import client.petmooby.com.br.petmooby.model.api.MapsAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RESTModuleDI {

    @Singleton
    @Provides
    fun provideMapsAPI() : MapsAPI{
        return MapsAPI.create()
    }
}