package client.petmooby.com.br.petmooby.model

class Resource <T, StatusT : Enum<*>> constructor(private val data : T?, private val enumStatus: StatusT) {

    fun data() = data
    fun status() = enumStatus

}