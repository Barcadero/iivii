package client.petmooby.com.br.petmooby.util

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptUtil {

    private var key     = "h2R45X7x9r1X34W4"
    private var vetor   = "123XTY789HIJ34U4"
    private var keyVar: String? = null
    private var vetVar: String? = null

    fun encrypt(value: String): String? {
        return encrypt(getKey(), getVetor(), value)
    }

    fun decrypt(value: String): String? {
        return decrypt(getKey(), getVetor(), value)
    }

    fun encryptVar(value: String): String? {
        return encrypt(getKeyVar(), getVetVar(), value)
    }

    fun decryptVar(value: String): String? {
        return decrypt(getKeyVar(), getVetVar(), value)
    }

    private fun encrypt(key: String?, initVector: String?, value: String): String? {
        try {
            val iv = IvParameterSpec(initVector!!.toByteArray(charset("UTF-8")))
            val skeySpec = SecretKeySpec(key!!.toByteArray(charset("UTF-8")), "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv)

            val encrypted = cipher.doFinal(value.toByteArray())

            return Base64.encodeToString(encrypted, Base64.DEFAULT)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }

    private fun decrypt(key: String?, initVector: String?, encrypted: String?): String? {
        try {
            val iv = IvParameterSpec(initVector!!.toByteArray(charset("UTF-8")))
            val skeySpec = SecretKeySpec(key!!.toByteArray(charset("UTF-8")), "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)

            val original = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT))

            return String(original)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }


    fun encryptPWD(pwd: String): String {
        return encrypt(pwd)!!.replace("\n", "")
    }

    fun getKey(): String {
        return key
    }

    fun setKey(key: String) {
        EncryptUtil.key = key
    }

    fun getVetor(): String {
        return vetor
    }

    fun setVetor(vetor: String) {
        EncryptUtil.vetor = vetor
    }

    fun getKeyVar(): String? {
        return keyVar
    }

    fun setKeyVar(keyVar: String) {
        this.keyVar = keyVar
    }

    fun getVetVar(): String? {
        return vetVar
    }

    fun setVetVar(vetVar: String) {
        this.vetVar = vetVar
    }


}