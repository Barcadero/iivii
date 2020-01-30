package client.petmooby.com.br.petmooby.util

import android.content.Context
import android.preference.PreferenceManager
import android.content.SharedPreferences
import android.text.TextUtils
import client.petmooby.com.br.petmooby.model.enums.EnumTypePreferences


/**
 * Created by Rafael Rocha on 16/05/2019.
 */
object Preference {
    val USER_NAME   = "user.name"
    val USER_TOKEN  = "user.token"
    val USER_ID     = "user.id"
    val USER_TYPE   = "user.type"
    val LOGIN_MSN   = "login.message"
    val USER_EMAIL  = "user.email"

    fun getUserName(context: Context): String?{
        return get(context, USER_NAME)
    }

    fun setUserName(context: Context,key: String, value:String): Boolean{
        return set(context,key,value)
    }

    fun getUserEmail(context: Context): String?{
        return get(context, USER_EMAIL)
    }

    fun setUserEmail(context: Context, value:String): Boolean{
        return set(context, USER_EMAIL,value)
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
                Boolean::class ->{
                    editor.putBoolean(key, value as Boolean)
                }
            }
            return editor.commit()
        }
        return false
    }

    fun getFacebookDebugToken() : String{
        return "EAAdWrDWSnZBoBAAc8EZCkQlh2lhIR5J9W5eeoikUfNT3rclWWbHySgn4HMb00QbrDGcRi4lMWrF8OMgFzemtIu1GSXEfediHv1ZBcHae6td80DladxtO3ZAhuSuFBFbNvhRHWXzjulpJTSt1UpY5DEL9JH34gQJsG2mr5DjnbjN5Biv0GeYrSouMIVn7ZBf9IFW7dltlwGeG8PYg34JFkFFCnrIOU5UUZD"
    }

    fun getFacebookDebugUserId():String{
        return "1825747277526491"
    }

    fun getUserType(context: Context): Int{
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getInt(USER_TYPE,-1)
    }

    fun setUserType(context: Context, value:Int): Boolean{
        return set(context, USER_TYPE,value)
    }

    fun getUserDatabaseId(context: Context): String?{
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(USER_ID,"")
    }

    fun setUserDatabaseId(context: Context, value:String): Boolean{
        return set(context, USER_ID,value)
    }

    fun setUserId(context: Context, value:String ) : Boolean{
        return set(context, USER_ID ,value)
    }

    fun getUserId(context: Context): String?{
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(USER_ID,"")
    }

    fun clear(context: Context){
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.edit().clear().apply()
    }

    fun setShowMessageLogin(context: Context, value:Boolean) : Boolean{
        return set(context, LOGIN_MSN ,value)
    }

    fun getShowMessageLogin(context: Context): Boolean{
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getBoolean(LOGIN_MSN,true)
    }


}