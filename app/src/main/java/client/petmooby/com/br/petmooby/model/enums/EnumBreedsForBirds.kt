package client.petmooby.com.br.petmooby.model.enums

import android.support.annotation.StringRes
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.application.Application

/**
 * Created by Rafael Rocha on 25/07/2019.
 */
enum class EnumBreedsForBirds(@StringRes resId:Int, value:String) : EnumBreedBase {

    COCKATIEL    (R.string.Cockatiel     ,"Cockatiel"),
    BUDGERIGAR   (R.string.Budgerigar    ,"Budgerigar"),
    GREY_PARROT  (R.string.Grey_Parrot   ,"Grey Parrot"),
    CAIQUE       (R.string.Caique        ,"Caique"),
    CONURE       (R.string.Conure        ,"Conure"),
    FINCHE       (R.string.Finche        ,"Finche"),
    CANARIE      (R.string.Canarie       ,"Canarie"),
    AGAPORNIS    (R.string.Agapornis     ,"Agapornis"),
    AMAZON_PARROT(R.string.Amazon_parrot ,"Amazon parrot"),
    CACATUA      (R.string.Cacatua       ,"Cacatua"),
    OTHER         (R.string.Other        ,"Other" );

    val label = Application.getString(resId)
    val value = value

    fun getByValue(value:String): EnumBreedsForBirds{
        return EnumBreedsForBirds.values()
                .firstOrNull()
                ?.let { if(it.value == value) it else EnumBreedsForBirds.OTHER }
                ?: EnumBreedsForBirds.OTHER
    }

    override fun toString(): String {
        return label
    }

    override fun getValue(position: Int): String {
        return EnumBreedsForBirds.values()[position].value
    }

    override fun getIndex(enumBreedBase: EnumBreedBase): Int {
        return (enumBreedBase as EnumBreedsForBirds).ordinal
    }
    
}