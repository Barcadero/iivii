package client.petmooby.com.br.petmooby.model.api.response

import client.petmooby.com.br.petmooby.model.api.data.PlaceResult
import com.google.gson.annotations.SerializedName

data class MapsVetResponse (
        @field:SerializedName("results") val results: List<PlaceResult>

)