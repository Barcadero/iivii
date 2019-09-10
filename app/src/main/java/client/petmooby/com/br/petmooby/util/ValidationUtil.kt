package client.petmooby.com.br.petmooby.util

import android.widget.EditText
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.application.Application


object ValidationUtil {

    fun isValidEmail(editText: EditText): Boolean {
        val target = editText.text
        return if (!android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
            editText.requestFocus()
            //editText.setText("")
            editText.error = Application.getString(R.string.emailInvalid)
            false
        } else {
            true
        }
    }
}