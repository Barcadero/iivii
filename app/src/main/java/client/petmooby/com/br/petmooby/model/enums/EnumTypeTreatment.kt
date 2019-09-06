package client.petmooby.com.br.petmooby.model.enums

import androidx.annotation.StringRes
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.application.Application

/**
 * Created by idoctor on 18/05/2019.
 */
enum class EnumTypeTreatment(@StringRes resId:Int, value:String) {
    VERMIFUGE(R.string.vermifuge,"VERMIFUGE"),
    MEDICINE(R.string.medicine,"MEDICINE");

    val resId = resId
    val value = value

    override fun toString(): String {
        return Application.getString(resId)
    }


}