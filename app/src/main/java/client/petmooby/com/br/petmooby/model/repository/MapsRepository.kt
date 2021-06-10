package client.petmooby.com.br.petmooby.model.repository


import android.util.Log
import client.petmooby.com.br.petmooby.BuildConfig
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.application.Application
import client.petmooby.com.br.petmooby.model.Resource
import client.petmooby.com.br.petmooby.model.api.MapsAPI
import client.petmooby.com.br.petmooby.model.api.response.MapsVetResponse
import client.petmooby.com.br.petmooby.model.enums.StatusVetsMaps
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapsRepository @Inject constructor(
        private val mapsAPI: MapsAPI
) {
    private val TAG: String = "MAPS-API"
    private val apiKey = Application.getString(R.string.google_api_key)
    suspend fun getNearVetClinics(location : String, language:String) : Resource<MapsVetResponse, StatusVetsMaps>{
        return try {
            val response = mapsAPI.getNearVetClinic(location = location,key = apiKey, language = language)
            if(BuildConfig.DEBUG){
                Log.d(TAG,"Total result ofr near vet >>> ${response.results.size}")
            }
            Resource(response,StatusVetsMaps.SUCCESS)
        }catch (e : Exception){
            return Resource(null,StatusVetsMaps.FAIL)
        }
    }
}