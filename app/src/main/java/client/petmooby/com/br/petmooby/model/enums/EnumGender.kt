package client.petmooby.com.br.petmooby.model.enums


import androidx.annotation.StringRes
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.application.Application

/**
 * Created by idoctor on 25/07/2019.
 */
enum class EnumGender(@StringRes resId:Int, value:String) {
    FEMALE(R.string.female,"FEMALE"),
    MALE  (R.string.male,"MALE");
    val res     = resId
    val value   = value
    override fun toString(): String {
        return Application.getString(res)
    }

}