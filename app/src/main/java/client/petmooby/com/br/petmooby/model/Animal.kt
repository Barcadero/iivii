package client.petmooby.com.br.petmooby.model


import android.os.Parcelable
import client.petmooby.com.br.petmooby.model.enums.*
import client.petmooby.com.br.petmooby.util.FireStoreReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.model.DocumentKey
import com.google.firebase.firestore.model.ResourcePath
import org.parceler.*
import java.util.*

/**
 * Created by Rafael Rocha on 18/05/2019.
 */
@Parcel
class Animal(@Exclude var id:String? = ""
             , var breed         :String?=""
             , var dateOfBirthday:Date?=null
             , var gender        :String?=""
             , var name          :String?=""
             , var photo         :String?=""
             , var treatmentCard:MutableList<TreatmentCard>?=null
             , var type:EnumTypeAnimal?=null

    ,@Exclude var userPath:String?=""
    ,var vaccineCards:MutableList<VaccineCards>?=null) {

    @ParcelPropertyConverter(DocumentReferenceConverter::class)
    var user:DocumentReference?=null


    @Parcel
    class VaccineCards(
        var identity:Int?=0
        ,var historic:MutableList<Historic>?=null
        ,var nextRemember:Date?=null
        ,var vaccine_type:String?="")

    @Parcel
    class Historic (
        var date:Date?=null
        ,var observation:String?=""
        ,var value:Double?=null
        ,var veterinary:String?="")

    @Parcel
    class TreatmentCard(
        var dateFinal    :Date?=null
        ,var dateInitial  :Date?=null
        ,var identity     :Long?=0
        ,var isActive     :Boolean?=false
        ,var name         :String?=""
        ,var notes        :String?=""
        ,var timeInterval :Long?=0
        ,var typeInterval :EnumTypeInterval?=null
        ,var typePeriod   :EnumTypePeriod?=null
        ,var typeTreatment:EnumTypeTreatment?=null){

//        constructor(parcel: Parcel) : this() {
//            identity = parcel.readValue(Long::class.java.classLoader) as? Long
//            isActive = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
//            name = parcel.readString()
//            notes = parcel.readString()
//            timeInterval = parcel.readValue(Long::class.java.classLoader) as? Long
//        }
//
//        override fun writeToParcel(parcel: Parcel, flags: Int) {
//            parcel.writeValue(identity)
//            parcel.writeValue(isActive)
//            parcel.writeString(name)
//            parcel.writeString(notes)
//            parcel.writeValue(timeInterval)
//        }

//        override fun describeContents(): Int {
//            return 0
//        }

//        companion object CREATOR : Parcelable.Creator<TreatmentCard> {
//            override fun createFromParcel(parcel: Parcel): TreatmentCard {
//                return TreatmentCard(parcel)
//            }
//
//            override fun newArray(size: Int): Array<TreatmentCard?> {
//                return arrayOfNulls(size)
//            }
//        }
    }

//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(id)
//        parcel.writeString(breed)
//        parcel.writeString(gender)
//        parcel.writeString(name)
//        parcel.writeString(photo)
//        parcel.writeString(type?.name)
//        parcel.writeSerializable(dateOfBirthday)
//        parcel.writeValue(user)
//        parcel.writeValue(vaccineCards)
//        parcel.writeString(userPath)
//
//
//    }

//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<Animal> {
//        override fun createFromParcel(parcel: Parcel): Animal {
//            return Animal(parcel)
//        }
//
//        override fun newArray(size: Int): Array<Animal?> {
//            return arrayOfNulls(size)
//        }
//    }

    class DocumentReferenceConverter : ParcelConverter<DocumentReference>{
        override fun toParcel(input: DocumentReference?, parcel: android.os.Parcel?) {
            //parcel?.writeParcelable(Parcels.wrap(DocumentReference::class.java, input), 0)
        }

        override fun fromParcel(parcel: android.os.Parcel?): DocumentReference {
            //return Parcels.unwrap(parcel?.readParcelable<Parcelable>(DocumentReference::class.java.classLoader))
            return FireStoreReference.docRefUser!!
        }

    }

}