package client.petmooby.com.br.petmooby.di

import android.content.Context
import client.petmooby.com.br.petmooby.data.AppDataBase
import client.petmooby.com.br.petmooby.data.dao.UserDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataBaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDataBase {
        return AppDataBase.getInstance(context)
    }

    @Provides
    fun provideUserDAO(appDataBase: AppDataBase) : UserDAO {
        return appDataBase.getUserDAO()
    }

}