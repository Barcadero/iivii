package br.com.transferr.passenger.extensions

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.squareup.picasso.Picasso

/**
 * Created by Rafael Rocha on 23/02/2018.
 */
fun ImageView.loadUrl(url: String?, progress: ProgressBar? = null){
    if(url == null || url.trim().isEmpty()){
        setImageBitmap(null)
        return
    }
    if(progress == null){
        Picasso.with(context).load(url).fit().into(this)
    }else{
        progress.visibility = View.VISIBLE
        Picasso.with(context).load(url).fit().into(this,
                object : com.squareup.picasso.Callback{
                    override fun onSuccess() {
                        progress?.visibility = View.GONE
                    }

                    override fun onError() {
                        progress?.visibility = View.GONE
                    }
                }
        )
    }
}