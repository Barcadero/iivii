package client.petmooby.com.br.petmooby.util

import android.util.Log

object LogUtil {
    val DEBUB_TAG = "DEBUG"
    fun logDebug(event:String){
        Log.d(DEBUB_TAG,event)
    }
}