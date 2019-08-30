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
      var treatmentCard:MutableList<TreatmentCard>?=null
      var type:EnumTypeAnimal?=null
     var userPath:String ?=null
     var vaccineCards:MutableList<VaccineCards>?=null



    //    @ParcelPropertyConverter(DocumentReferenceConverter::class)
    var user: DocumentReference?=null


    //    @Parcel
    class VaccineCards(
            var identity: Int? = 0
            , var historic: MutableList<Historic>? = null
            , var nextRemember: Date? = null
            , var vaccine_type: String? = "") : Serializable

    //    @Parcel
    class Historic(
            var date: Date? = null
            , var observation: String? = ""
            , var value: Double? = null
            , var veterinary: String? = "") : Serializable

    //    @Parcel
    class TreatmentCard(
            var dateFinal: Date? = null
            , var dateInitial: Date? = null
            , var identity: Long? = 0
            , var isActive: Boolean? = false
            , var name: String? = ""
            , var notes: String? = ""
            , var timeInterval: Long? = 0
            , var typeInterval: EnumTypeInterval? = null
            , var typePeriod: EnumTypePeriod? = null
            , var typeTreatment: EnumTypeTreatment? = null) : Serializable


}
