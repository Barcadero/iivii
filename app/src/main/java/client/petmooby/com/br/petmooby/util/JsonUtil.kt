package client.petmooby.com.br.petmooby.util

import com.google.gson.Gson

class JsonUtil {
    companion object{
        fun toJson(any: Any) : String{
            return Gson().toJson(any)
        }

        fun <T> fromJson(json: String , type : Class<T>) : T{
            return Gson().fromJson(json,type)
        }
    }
}