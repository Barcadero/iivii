package client.petmooby.com.br.petmooby.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import client.petmooby.com.br.petmooby.R
import java.text.NumberFormat

/**
 * Created by Rafael Rocha on 12/08/2019.
 */
class CurrencyMaskTextWatch(var editText: EditText, val context:Context) : TextWatcher  {

    var current:String = ""
    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if(!s.toString().equals(current)){
           //editText.removeTextChangedListener(this)

            val cleanString = s.toString().replace(NumberFormatUtil.getCurrencyRengex(context), "")
//            val cleanString = s.toString().replace(Regex("[$,.]"), "")
            val parsed      = cleanString.toDouble()
            val formatted   = NumberFormat.getCurrencyInstance().format((parsed/100))
            current         = formatted
            editText.setText(formatted)
            editText.setSelection(formatted.length)
            //editText.addTextChangedListener(this)
        }
    }
}