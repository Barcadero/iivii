package client.petmooby.com.br.petmooby.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.LayerDrawable
import androidx.core.content.ContextCompat
import client.petmooby.com.br.petmooby.R
import com.applandeo.materialcalendarview.CalendarUtils

object DrawableUtils {

    fun getCircleDrawableWithText(context: Context, string: String): Drawable {
        val background = ContextCompat.getDrawable(context, R.drawable.sample_circle)
        val text = CalendarUtils.getDrawableText(context, string, null, android.R.color.white, 12)

        val layers = arrayOf<Drawable>(background!!, text)
        return LayerDrawable(layers)
    }

    fun getThreeDots(context: Context): Drawable {
        val drawable = ContextCompat.getDrawable(context, R.drawable.sample_three_icons)

        //Add padding to too large icon
        return InsetDrawable(drawable, 100, 0, 100, 0)
    }

}