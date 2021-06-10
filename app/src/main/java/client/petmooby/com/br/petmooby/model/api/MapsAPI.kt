package client.petmooby.com.br.petmooby.model.api

import client.petmooby.com.br.petmooby.model.api.response.MapsVetResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MapsAPI {

    @GET("nearbysearch/json")
    suspend fun getNearVetClinic(
            @Query("location") location: String,
            @Query("type") type: String = "veterinary_care",
            @Query("key") key: String = "key",
            @Query("language") language: String,
            @Query("rankby") rankBy : String = "distance"
    ): MapsVetResponse

    companion object {
        private const val BASE_URL = "https://maps.googleapis.com/maps/api/place/"

        fun create(): MapsAPI {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()

            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MapsAPI::class.java)
        }
    }
}