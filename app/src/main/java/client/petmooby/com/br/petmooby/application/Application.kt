package client.petmooby.com.br.petmooby.application

import android.support.annotation.StringRes
import android.support.multidex.MultiDexApplication
import java.util.*

/**
 * Created by Rafael Rocha on 11/07/2019.
 */
class Application : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        app = this
    }

    companion object {
        private var app : Application?=null
        var DEVICE_LANGUAGE = Locale.getDefault().language
        val LANG_PT = "pt"
        val LANG_EN = "en"
        val LANG_ES = "es"
        fun getInstance(): Application{
            if(app == null){
                throw IllegalStateException("Configure the Application class on Manifest xml.")
            }
            return app!!
        }

        fun getString(@StringRes resId:Int ) : String{
            return app?.getString(resId)!!
        }

    }
}