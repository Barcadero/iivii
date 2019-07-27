package client.petmooby.com.br.petmooby.model

import android.os.Parcel
import android.os.Parcelable
import client.petmooby.com.br.petmooby.model.enums.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import java.io.Serializable
import java.util.*

/**
 * Created by Rafael Rocha on 18/05/2019.
 */
class Animal() : Parcelable{

    @Exclude
    var id            :String?=null
    var breed         :String?=null
    var dateOfBirthday:Date?=null
    var gender        :String?=null
    var name          :String?=null
    var photo         :String?=null
    var treatmentCard:MutableList<TreatmentCard>?=null
    var type:EnumTypeAnimal?=null
    var user:DocumentReference?=null
    @Exclude
    var userPath:String?=null
    var vaccineCards:List<VaccineCards>?=null

    constructor(parcel: Parcel) : this() {
        id      = parcel.readString()
        breed   = parcel.readString()
        gender  = parcel.readString()
        name    = parcel.readString()
        photo   = parcel.readString()
        type    = EnumTypeAnimal.valueOf(parcel.readString())
        dateOfBirthday = parcel.readSerializable() as Date
        userPath =   parcel.readString()
    }

    class VaccineCards() : Parcelable {
        var historic:List<Historic>?=null
        var nextRemember:Date?=null
        var vaccine_type:String?=null

        constructor(parcel: Parcel) : this() {
            vaccine_type = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(vaccine_type)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<VaccineCards> {
            override fun createFromParcel(parcel: Parcel): VaccineCards {
                return VaccineCards(parcel)
            }

            override fun newArray(size: Int): Array<VaccineCards?> {
                return arrayOfNulls(size)
            }
        }
    }

    class Historic() :Parcelable{
        var date:Date?=null
        var observation:String?=null
        var value:Double?=null
        var veterinary:String?=null

        constructor(parcel: Parcel) : this() {
            observation = parcel.readString()
            value = parcel.readValue(Double::class.java.classLoader) as? Double
            veterinary = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(observation)
            parcel.writeValue(value)
            parcel.writeString(veterinary)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Historic> {
            override fun createFromParcel(parcel: Parcel): Historic {
                return Historic(parcel)
            }

            override fun newArray(size: Int): Array<Historic?> {
                return arrayOfNulls(size)
            }
        }
    }

    class TreatmentCard() :Parcelable{
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

        constructor(parcel: Parcel) : this() {
            identity = parcel.readValue(Long::class.java.classLoader) as? Long
            isActive = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
            name = parcel.readString()
            notes = parcel.readString()
            timeInterval = parcel.readValue(Long::class.java.classLoader) as? Long
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(identity)
            parcel.writeValue(isActive)
            parcel.writeString(name)
            parcel.writeString(notes)
            parcel.writeValue(timeInterval)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<TreatmentCard> {
            override fun createFromParcel(parcel: Parcel): TreatmentCard {
                return TreatmentCard(parcel)
            }

            override fun newArray(size: Int): Array<TreatmentCard?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(breed)
        parcel.writeString(gender)
        parcel.writeString(name)
        parcel.writeString(photo)
        parcel.writeString(type?.name)
        parcel.writeSerializable(dateOfBirthday)
        //parcel.writeValue(user)
        parcel.writeString(userPath)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Animal> {
        override fun createFromParcel(parcel: Parcel): Animal {
            return Animal(parcel)
        }

        override fun newArray(size: Int): Array<Animal?> {
            return arrayOfNulls(size)
        }
    }

}