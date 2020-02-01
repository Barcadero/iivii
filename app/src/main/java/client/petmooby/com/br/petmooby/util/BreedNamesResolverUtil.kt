package client.petmooby.com.br.petmooby.util
import client.petmooby.com.br.petmooby.model.enums.EnumBreedsForBirds
import client.petmooby.com.br.petmooby.model.enums.EnumBreedsForCats
import client.petmooby.com.br.petmooby.model.enums.EnumBreedsForDogs
import client.petmooby.com.br.petmooby.model.enums.EnumTypeAnimal

/**
 * Created by Rafael Rocha on 22/07/2019.
 */
class BreedNamesResolverUtil {

    companion object {

        fun resolverName(type: EnumTypeAnimal, breedValue: String) : String?{
           return when(type){
                EnumTypeAnimal.DOG ->{
                    getByValueForDogs(breedValue)?.label
                }
                EnumTypeAnimal.CAT ->{
                    getByValueForCats(breedValue)?.label
                }
                EnumTypeAnimal.BIRD ->{
                    getByValueForBirds(breedValue)?.label
                }
                else -> getByValueForDogs("")?.label
            }
        }

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

        fun getByValueForCats(value:String): EnumBreedsForCats?{
            var breed:EnumBreedsForCats?=null
            EnumBreedsForCats.values().forEach {
                if(it.value == value){
                    breed = it
                }
            }
            if(breed == null)return EnumBreedsForCats.OTHER
            return breed
        }

        fun getByValueForBirds(value:String): EnumBreedsForBirds?{
            var breed:EnumBreedsForBirds?=null
            EnumBreedsForBirds.values().forEach {
                if(it.value == value){
                    breed = it
                }
            }
            if(breed == null)return EnumBreedsForBirds.OTHER
            return breed
        }
    }


}