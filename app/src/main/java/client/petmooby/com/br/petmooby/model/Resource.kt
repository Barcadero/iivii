package client.petmooby.com.br.petmooby.model

class Resource<RETURN, ENUM : Enum<*>>(): IDTO {
    constructor(dado: RETURN?, status: ENUM) : this() {
        this.dado = dado
        this.status = status
    }

    constructor(status: ENUM, error: String? = null) : this() {
        this.status = status
        this.error = error
    }

    var dado: RETURN? = null
    var status: ENUM? = null
    var error: String? = null
}