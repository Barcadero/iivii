package client.petmooby.com.br.petmooby.util

import android.support.annotation.LayoutRes

/**
 * Created by idoctor on 25/07/2019.
 */
object LayoutResourceUtil {
    @LayoutRes
    fun getSpinnerDropDown(): Int{
        return android.R.layout.simple_spinner_item
    }
}