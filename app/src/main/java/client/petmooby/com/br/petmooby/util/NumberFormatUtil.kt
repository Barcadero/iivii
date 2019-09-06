package client.petmooby.com.br.petmooby.util

import android.content.Context
import client.petmooby.com.br.petmooby.R

object NumberFormatUtil {

    fun getCurrencyRengex(context: Context) : Regex{
        val regex = "[${context.getString(R.string.currencyRegex)}]"
        return Regex(regex)
    }

    fun currencyToDouble(value:String,context: Context):Double{
        if(value == null)return 0.0
        val cleanString = value.replace(getCurrencyRengex(context), "")
        return cleanString.toDouble() / 100
    }
}