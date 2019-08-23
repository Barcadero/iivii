package client.petmooby.com.br.petmooby.model.enums



import androidx.annotation.StringRes
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.application.Application

/**
 * Created by Rafael Rocha on 18/05/2019.
 */
enum class EnumTypeAnimal(@StringRes resId:Int, value:String) {
    OTHER   (R.string.other,"OTHER"),
    DOG     (R.string.dog  ,"DOG"),
    CAT     (R.string.cat  ,"CAT"),
    BIRD    (R.string.bird ,"BIRD");
    val resId = resId
    val value = value

    override fun toString(): String {
        return Application.getString(resId)
    }

}