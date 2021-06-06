package client.petmooby.com.br.petmooby.model

import client.petmooby.com.br.petmooby.model.enums.TypeUserEnum

data class UserLogin(
        val email : String,
        val docRef : String,
        val docId : String,
        val type : TypeUserEnum

)