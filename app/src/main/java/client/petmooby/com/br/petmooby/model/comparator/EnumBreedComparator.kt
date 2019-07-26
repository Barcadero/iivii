package client.petmooby.com.br.petmooby.model.comparator

import client.petmooby.com.br.petmooby.model.enums.EnumBreedBase

/**
 * Created by idoctor on 25/07/2019.
 */
class EnumBreedComparator : Comparator<EnumBreedBase> {


    override fun compare(value1: EnumBreedBase?, value2: EnumBreedBase?): Int {
        return value1.toString().compareTo(value2.toString())
    }


}