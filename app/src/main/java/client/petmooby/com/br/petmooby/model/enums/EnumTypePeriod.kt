package client.petmooby.com.br.petmooby.model.enums

import androidx.annotation.StringRes
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.application.Application

/**
 * Created by Rafael Rocha on 18/05/2019.
 */
enum class EnumTypePeriod(@StringRes resId:Int, value:String) {
    ALWAYS(R.string.always,"ALWAYS"),
    INFORMED(R.string.informed,"INFORMED");

    val resId = resId
    val value = value

    override fun toString(): String {
        return Application.getString(resId)
    }
}