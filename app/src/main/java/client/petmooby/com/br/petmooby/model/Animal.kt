package client.petmooby.com.br.petmooby.model



import client.petmooby.com.br.petmooby.model.enums.EnumTypeAnimal
import client.petmooby.com.br.petmooby.model.enums.EnumTypeInterval
import client.petmooby.com.br.petmooby.model.enums.EnumTypePeriod
import client.petmooby.com.br.petmooby.model.enums.EnumTypeTreatment
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import java.io.Serializable
import java.util.*

/**
 * Created by Rafael Rocha on 18/05/2019.
 */
//@Parcelize
 class Animal
{
     @Exclude var id:String?=""
      var breed         :String?=""
      var dateOfBirthday:Date?=null
      var gender        :String?=""
      var name          :String?=""
      var photo         :String?=""
      var treatmentCard:MutableList<TreatmentCard>?= mutableListOf()
      var type:EnumTypeAnimal?=null
      var vaccineCards:MutableList<VaccineCards>?=mutableListOf()
      var vetConsultation:MutableList<VetConsultation>?=mutableListOf()
      @Transient
      var user: DocumentReference?=null

    class VaccineCards(
            var identity: Int? = 0
            , var historic: MutableList<Historic>? = mutableListOf()
            , var nextRemember: Date? = null
            , var vaccine_type: String? = "") : Serializable

    //    @Parcel
    class Historic(
            var date: Date? = null
            , var observation: String? = ""
            , var tradeMark:String?=null
            , var value: Double? = null
            , var veterinary: String? = "") : Serializable

    //    @Parcel
    class TreatmentCard(
            var dateFinal: Date? = null
            , var dateInitial: Date? = null
            , var identity: Long? = 0
            , var isIsActive: Boolean? = false
            , var name: String? = ""
            , var notes: String? = ""
            , var timeInterval: Long? = 0
            , var typeInterval: EnumTypeInterval? = null
            , var typePeriod: EnumTypePeriod? = null
            , var typeTreatment: EnumTypeTreatment? = null) : Serializable

    class VetConsultation

     : Serializable{
        var identity: Long?=null
        var nameVet:String?=null
        var nameClinic:String?=null
        var date: Date?         =null
        var dateReturn:Date?    =null
        var notes : String?     =null
        var price : String?     =null
        var rank   : Int?       =null
        var photo1: String?     =null
        var descPhoto1: String?=null
        var photo2: String?          =null
        var descPhoto2: String?      =null
    }


}
