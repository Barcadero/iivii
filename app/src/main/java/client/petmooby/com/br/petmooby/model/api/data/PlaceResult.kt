package client.petmooby.com.br.petmooby.model.api.data

import client.petmooby.com.br.petmooby.util.JsonUtil
import com.google.gson.annotations.SerializedName

data class PlaceResult (
        @SerializedName("business_status")
        val businessStatus: String,

        val geometry: Geometry,
        val icon: String,
        val name: String,

        @SerializedName("place_id")
        val placeID: String,

        @SerializedName("plus_code")
        val plusCode: PlusCode,

        val reference: String,
        val scope: String,
        val types: List<String>,
        val vicinity: String
){
    override fun toString(): String {
        return JsonUtil.toJson(this)
    }
}

data class Geometry (
        val location: Location,
        val viewport: Viewport
)

data class Location (
        val lat: Double,
        val lng: Double
)

data class Viewport (
        val northeast: Location,
        val southwest: Location
)

data class PlusCode (
        @SerializedName("compound_code")
        val compoundCode: String,
        @SerializedName("global_code")
        val globalCode: String
)