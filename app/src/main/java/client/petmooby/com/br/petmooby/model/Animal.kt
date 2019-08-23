package client.petmooby.com.br.petmooby.model


//import org.parceler.*
import android.os.Parcel
import client.petmooby.com.br.petmooby.model.enums.EnumTypeAnimal
import client.petmooby.com.br.petmooby.model.enums.EnumTypeInterval
import client.petmooby.com.br.petmooby.model.enums.EnumTypePeriod
import client.petmooby.com.br.petmooby.model.enums.EnumTypeTreatment
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parceler
import java.util.*

/**
 * Created by Rafael Rocha on 18/05/2019.
 */
//@Parcelize
class Animal(@Exclude var id:String? = ""
             , var breed         :String?=""
             , var dateOfBirthday:Date?=null
             , var gender        :String?=""
             , var name          :String?=""
             , var photo         :String?=""
             , var treatmentCard:MutableList<TreatmentCard>?=null
             , var type:EnumTypeAnimal?=null

    ,@Exclude var userPath:String?=""
    ,var vaccineCards:MutableList<VaccineCards>?=null)  {

//    @ParcelPropertyConverter(DocumentReferenceConverter::class)
    var user:DocumentReference?=null


    //@Parcelize
    class VaccineCards(
        var identity:Int?=0
        ,var historic:MutableList<Historic>?=null
        ,var nextRemember:Date?=null
        ,var vaccine_type:String?="")  {

    }

//    @Parcelize
    class Historic (
        var date:Date?=null
        ,var observation:String?=""
        ,var value:Double?=null
        ,var veterinary:String?="")

//    @Parcelize
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
        ,var typeTreatment:EnumTypeTreatment?=null)  {

//        override fun describeContents(): Int {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }

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
        companion object : Parceler<TreatmentCard> {
            override fun TreatmentCard.write(dest: Parcel, flags: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun create(parcel: Parcel): TreatmentCard = TODO()
        }
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

//    class DocumentReferenceConverter : ParcelConverter<DocumentReference>{
//        override fun toParcel(input: DocumentReference?, parcel: android.os.Parcel?) {
//            //parcel?.writeParcelable(Parcels.wrap(DocumentReference::class.java, input), 0)
//        }
//
//        override fun fromParcel(parcel: android.os.Parcel?): DocumentReference {
//            //return Parcels.unwrap(parcel?.readParcelable<Parcelable>(DocumentReference::class.java.classLoader))
//            return FireStoreReference.docRefUser!!
//        }
//
//    }

}