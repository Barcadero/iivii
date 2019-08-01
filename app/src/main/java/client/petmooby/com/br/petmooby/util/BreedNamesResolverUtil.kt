package client.petmooby.com.br.petmooby.util

import android.content.Context
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.model.enums.EnumBreedBase
import client.petmooby.com.br.petmooby.model.enums.EnumBreedsForDogs

/**
 * Created by Rafael Rocha on 22/07/2019.
 */
class BreedNamesResolverUtil {

    companion object {

        fun getByValueForDogs(value:String): EnumBreedsForDogs?{
            var breed:EnumBreedsForDogs?=null
            EnumBreedsForDogs.values().forEach {
               if(it.value == value){
                    breed = it
               }
            }
            if(breed == null)return EnumBreedsForDogs.OTHER
            return breed
        }
    }
}