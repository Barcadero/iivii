package client.petmooby.com.br.petmooby.extensions

import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(dateFormat : String) : Date?{
    return SimpleDateFormat(dateFormat, Locale.US).parse(this)
}