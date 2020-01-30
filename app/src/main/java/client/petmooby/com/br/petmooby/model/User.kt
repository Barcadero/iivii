package client.petmooby.com.br.petmooby.model

import client.petmooby.com.br.petmooby.model.enums.TypeUserEnum
import java.util.*

/**
 * Created by Rafael Rocha on 16/05/2019.
 */
class User {
    companion object {
        val TOKEN_FACEBOOK      = "tokenFacebook"
        val USER_ID_FACEBOOK    = "userIdFB"
        var USER_EMAIL          = "email"
        var USER_PASSWORD       = "password"
        //var USER_ID             = "password"
    }


    var name:String?=null
    var tokenFacebook:String?=null
    var userIdFB:String?=null
    //New fields
    var notifications:MutableList<NotificationPet> = mutableListOf()
    var password:String?=null
    var email:String?=null
    var type: TypeUserEnum?=null
    var registerDate: Date?= null

}