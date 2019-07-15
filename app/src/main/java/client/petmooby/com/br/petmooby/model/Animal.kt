package client.petmooby.com.br.petmooby.model

import client.petmooby.com.br.petmooby.model.enums.*
import com.google.firebase.firestore.DocumentReference
import java.io.Serializable
import java.util.*

/**
 * Created by Rafael Rocha on 18/05/2019.
 */
class Animal : Serializable{

    var breed         :String?=null
    var dateOfBirthday:Date?=null
    var gender        :String?=null
    var name          :String?=null
    var photo         :String?=null
    var treatmentCard:MutableList<TreatmentCard>?=null
    var type:EnumTypeAnimal?=null
    var user:DocumentReference?=null
    var vaccineCards:List<VaccineCards>?=null

    class VaccineCards : Serializable {
        var historic:List<Historic>?=null
        var nextRemember:Date?=null
        var vaccine_type:String?=null
    }

    class Historic {
        var date:Date?=null
        var observation:String?=null
        var value:Double?=null
        var veterinary:String?=null
    }

    class TreatmentCard :Serializable{
        var dateFinal    :Date?=null
        var dateInitial  :Date?=null
        var identity     :Long?=null
        var isActive     :Boolean?=null
        var name         :String?=null
        var notes        :String?=null
        var timeInterval :Long?=null
        var typeInterval :EnumTypeInterval?=null
        var typePeriod   :EnumTypePeriod?=null
        var typeTreatment:EnumTypeTreatment?=null
    }

}