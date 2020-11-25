package client.petmooby.com.br.petmooby.application


import androidx.annotation.StringRes
import androidx.multidex.MultiDexApplication
import client.petmooby.com.br.petmooby.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level
import java.util.*

/**
 * Created by Rafael Rocha on 11/07/2019.
 */
class Application : MultiDexApplication() {

    var koinApplication: KoinApplication? = null
    override fun onCreate() {
        super.onCreate()
        app = this
        initKoin()
    }

    companion object {
        private var app : Application?=null
        val IS_DEBUG =  BuildConfig.DEBUG
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

    fun initKoin(){
        stopKoin()
        koinApplication?.close()
        koinApplication = null

        koinApplication = startKoin {
            androidContext(this@Application)
            androidLogger(Level.DEBUG)
            modules(
                    listOf(
                            repositories,
                            services,
                            viewModels
                    )
            )
        }

    }
}