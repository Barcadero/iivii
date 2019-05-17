package client.petmooby.com.br.petmooby.util

import android.content.Context
import android.preference.PreferenceManager
import android.content.SharedPreferences
import android.text.TextUtils
import client.petmooby.com.br.petmooby.model.enums.EnumTypePreferences


/**
 * Created by idoctor on 16/05/2019.
 */
object Preference {
    val USER_NAME   = "user.name"
    val USER_TOKEN  = "user.token"
    val USER_ID     = "user.id"
    fun getUserName(context: Context): String?{
        return get(context, USER_NAME)
    }

    fun setUserName(context: Context,key: String, value:String): Boolean{
        return set(context,key,value)
    }


    inline fun <reified T> get(context: Context, key: String): T? {
        var value: String? = null
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
           when(T::class){
               String::class ->{
                    return preferences.getString(key,"") as T
               }
               Int::class ->{
                   return preferences.getInt(key,0) as T
               }
               Boolean::class ->{
                   return preferences.getBoolean(key,false) as T
               }
               else -> Any()
           }

        }
        return null
    }

    inline fun <reified T> set(context: Context, key: String, value: T): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null && !TextUtils.isEmpty(key)) {
            val editor = preferences.edit()
            when(T::class){
                String::class->{
                    editor.putString(key, value as String)
                }
                Int::class->{
                    editor.putInt(key, value as Int)
                }
            }
            return editor.commit()
        }
        return false
    }
}