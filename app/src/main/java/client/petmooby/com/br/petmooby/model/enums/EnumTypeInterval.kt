package client.petmooby.com.br.petmooby.model.enums

import androidx.annotation.StringRes
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.application.Application

/**
 * Created by idoctor on 18/05/2019.
 */
enum class EnumTypeInterval(@StringRes resId:Int, value:String) {
    DAY(R.string.day,"DAY"),
    MONTH(R.string.month,"MONTH"),
    YEAR(  R.string.year,"YEAR"),
    HOUR(  R.string.hour,"HOUR");

    val label = Application.getString(resId)
    val value = value

    override fun toString(): String {
        return label
    }
}