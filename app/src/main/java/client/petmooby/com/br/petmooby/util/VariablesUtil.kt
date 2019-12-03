package client.petmooby.com.br.petmooby.util

import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.model.User

/**
 * Created by Rafael Rocha on 05/08/2019.
 */
object VariablesUtil {

    var gbAnimals: MutableList<Animal>?=null
    var gbSelectedAnimal:Animal?=null
    var gbCurrentUser:User?=null

    fun clear(){
        gbAnimals           = null
        gbCurrentUser       = null
        gbSelectedAnimal    = null
    }
}