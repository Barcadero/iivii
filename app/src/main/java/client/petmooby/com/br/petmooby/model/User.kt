package client.petmooby.com.br.petmooby.model

/**
 * Created by idoctor on 16/05/2019.
 */
class User {
    companion object {
        val TOKEN_FACEBOOK      = "tokenFacebook"
        val USER_ID_FACEBOOK    = "userIdFB"
    }


    var name:String?=null
    var tokenFacebook:String?=null
    var userIdFB:String?=null

}