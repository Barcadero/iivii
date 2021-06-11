package client.petmooby.com.br.petmooby.model.repository


import android.util.Log
import client.petmooby.com.br.petmooby.BuildConfig
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.application.Application
import client.petmooby.com.br.petmooby.model.Resource
import client.petmooby.com.br.petmooby.model.api.MapsAPI
import client.petmooby.com.br.petmooby.model.api.response.MapsVetResponse
import client.petmooby.com.br.petmooby.model.dao.MapsDAO
import client.petmooby.com.br.petmooby.model.entities.NearbyVetsEntity
import client.petmooby.com.br.petmooby.model.enums.StatusVetsMaps
import client.petmooby.com.br.petmooby.util.DateTimeUtil
import client.petmooby.com.br.petmooby.util.JsonUtil
import java.lang.Exception
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapsRepository @Inject constructor(
        private val mapsAPI: MapsAPI,
        private val mapsDAO: MapsDAO
) {
    private val TAG: String = "MAPS-API"
    private val apiKey = Application.getString(R.string.google_api_key)
    suspend fun getNearVetClinics(location : String, language:String) : Resource<MapsVetResponse, StatusVetsMaps>{
        return try {
            val nearbyVets = mapsDAO.getNearbyVets()
            if(nearbyVets != null && isAValidCache(nearbyVets.time)) {
                log("Cache is valid")
                return Resource(JsonUtil.fromJson(nearbyVets.json, MapsVetResponse::class.java), StatusVetsMaps.SUCCESS)
            }
            log("That will call the maps API")
            val response = mapsAPI.getNearVetClinic(location = location,key = apiKey, language = language)
            Log.d(TAG,"Total result ofr near vet >>> ${response.results.size}")
            mapsDAO.insertNearbyVets(NearbyVetsEntity().apply {
                json = JsonUtil.toJson(response)
                time = DateTimeUtil.addHourdToADate(Date(),12).time
            })
            Resource(response,StatusVetsMaps.SUCCESS)
        }catch (e : Exception){
            return Resource(null,StatusVetsMaps.FAIL)
        }
    }

    private fun log(message : String){
        Log.d(TAG,message)
    }

    private fun isAValidCache(time : Long)  = time > Date().time

}