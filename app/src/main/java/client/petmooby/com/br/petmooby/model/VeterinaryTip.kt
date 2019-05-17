package client.petmooby.com.br.petmooby.model

import client.petmooby.com.br.petmooby.model.metadata.About
import java.io.Serializable

/**
 * Created by Rafael Rocha on 16/05/2019.
 */
class VeterinaryTip : Serializable {

    var address:String?=null
    var about: About?=null
    var clinic:String?=null
    var contact:String?=null
    var email:String?=null
    var latitude:Double?=null
    var longitude:Double?=null
    var name:String?=null
    var photo:String?=null
}