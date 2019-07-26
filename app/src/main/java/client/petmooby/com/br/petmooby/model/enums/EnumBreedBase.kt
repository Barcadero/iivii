package client.petmooby.com.br.petmooby.model.enums

/**
 * Created by Rafael Rocha on 25/07/2019.
 */
interface EnumBreedBase {

    fun getValue(position:Int): String
    fun getIndex(enumBreedBase: EnumBreedBase): Int

}