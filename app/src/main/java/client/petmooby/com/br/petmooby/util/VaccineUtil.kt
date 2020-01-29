package client.petmooby.com.br.petmooby.util

import android.content.Context
import client.petmooby.com.br.petmooby.model.Animal

class VaccineUtil {

    fun scheduleEvent(context: Context,vaccine:Animal.VaccineCards, name : String, isForUpdate:Boolean){
        val param = ParametersEvent()
        val identity = vaccine.identity
        val date     = DateTimeUtil.addHourdToADate(DateTimeUtil.getOnlyDate(vaccine.nextRemember!!) ,-12)
        with(param){
            id = identity?.toLong()!!
            tag = identity.toString()
            dateTime = date
            animalName = name
            vaccineType = vaccine.vaccine_type!!
        }
//        NotificationWorkerUtil().scheduleEvent(date, context,param,NotificationWorkerVaccine::class.java)
        NotificationWorkerUtil().scheduleEventPeriodic(date, context,param,isForUpdate, NotificationWorkerVaccine::class.java)
    }

    fun cancelEvent(context: Context,vaccine:Animal.VaccineCards){
        val identity = vaccine.identity
        NotificationWorkerUtil().cancel(context,identity.toString())
    }

    fun cancelEventVaccinesForAAnimal(context: Context,animal:Animal){
        if(animal.vaccineCards != null && animal.vaccineCards?.isNotEmpty()!!){
            animal.vaccineCards?.forEach {
                NotificationWorkerUtil().cancel(context,it.identity.toString())
            }
        }
    }
}