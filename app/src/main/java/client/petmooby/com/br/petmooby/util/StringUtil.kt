package client.petmooby.com.br.petmooby.util

import android.R.attr.name



object StringUtil {

    fun capitalizeString(string:String) : String{
        val s1 = string.substring(0, 1).toUpperCase()
        return s1 + string.substring(1).toLowerCase()
    }
}