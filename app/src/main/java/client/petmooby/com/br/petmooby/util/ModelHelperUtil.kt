package client.petmooby.com.br.petmooby.util

import java.util.*
import kotlin.math.absoluteValue

object ModelHelperUtil {
    fun getNewIdentity() = Random().nextLong().absoluteValue

}