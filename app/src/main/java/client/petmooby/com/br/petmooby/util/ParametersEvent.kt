package client.petmooby.com.br.petmooby.util

import client.petmooby.com.br.petmooby.model.enums.EnumTypeEvent
import java.util.*

class ParametersEvent {

    var animalName =""
    var id = 0L
    var dateTime: Date?=null
    var tag = ""
    var type = EnumTypeEvent.OTHER
    var vaccineType=""
    var dateString=""
    var repeatInterval = 0L
    var treatmentName = ""
}