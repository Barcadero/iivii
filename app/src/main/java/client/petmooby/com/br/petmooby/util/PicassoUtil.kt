package client.petmooby.com.br.petmooby.util

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import client.petmooby.com.br.petmooby.R
import com.squareup.picasso.*

/**
 * Created by idoctor on 10/07/2019.
 */
object PicassoUtil {
    var picassoCache: LruCache?=null
    fun build(imageURI:String, view: ImageView, callback: Callback?=null, isFit:Boolean = false, isCenterCrop:Boolean = false, context: Context){
        if(picassoCache == null){
            Log.d("DESTROY","Picasso cache is null")
            buildGlobalCache(context)
        }
        var pBuilder = Picasso.Builder(context)
                .memoryCache(picassoCache!!)
                //.downloader(OkHttp3Downloader(SuperWebService().httpClient))
                .build()
                .load(imageURI)
                .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                .memoryPolicy(MemoryPolicy.NO_STORE)
                .error(R.drawable.no_image)
        if(isFit){
            pBuilder.fit()
        }
        if(isCenterCrop){
            pBuilder.centerCrop()
        }
        if(callback == null) {
            pBuilder.placeholder(R.drawable.picasso_load_animation).into(view)
        }else{
            pBuilder.placeholder(R.drawable.picasso_load_animation).into(view, callback)
        }
    }

    fun buildGlobalCache(context: Context){
        picassoCache = LruCache(context)
    }

    fun clearGlobalCache(){
        if(picassoCache != null){
            picassoCache?.clear()
        }
    }
}