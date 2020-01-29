package client.petmooby.com.br.petmooby.util

import android.content.Context
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.model.enums.EnumTypeInterval
import java.util.concurrent.TimeUnit

object TreatmentUtil {

    /*fun generateTreatmentAlarm(context: Context,animal:Animal,identity: Long){
        var treatment = animal.treatmentCard?.filter { it.identity == identity }!![0]
        if(treatment != null){
            scheduleEvent(context,treatment,animal.name!!)
        }
    }*/

    fun generateTreatmentAlarm(context: Context,animalName:String,treatment: Animal.TreatmentCard,isForUpdate: Boolean){
        scheduleEvent(context,treatment,animalName,isForUpdate)
    }

    private fun scheduleEvent(context: Context, treatment:Animal.TreatmentCard, name : String, isForUpdate: Boolean){
        if(!treatment.isIsActive!!)return
        val param = ParametersEvent()
        val identity = treatment.identity
        var interval = treatment.timeInterval
        val timeUnit = when(treatment.typeInterval){
            EnumTypeInterval.HOUR -> TimeUnit.HOURS
            EnumTypeInterval.DAY -> TimeUnit.DAYS
            EnumTypeInterval.MONTH -> {
                interval = interval?.times(30)
                TimeUnit.DAYS
            }
            EnumTypeInterval.YEAR -> {
                interval = interval?.times(365)
                TimeUnit.DAYS
            }
            else -> TimeUnit.DAYS
        }
        //val date     = DateTimeUtil.addHourdToADate(DateTimeUtil.getOnlyDate(vaccine.nextRemember!!) ,-12)
        with(param){
            id = identity!!
            tag = identity.toString()
            //dateTime = date
            animalName = name
            repeatInterval = interval!!
            treatmentName = treatment.name!!
        }

//        NotificationWorkerUtil().scheduleEvent(date, context,param,NotificationWorkerVaccine::class.java)
        scheduleNotificationAlarm(treatment, context, param, timeUnit, isForUpdate)
    }

    private fun scheduleNotificationAlarm(treatment: Animal.TreatmentCard, context: Context, param: ParametersEvent, timeUnit: TimeUnit,isForUpdate:Boolean) {
        if (treatment.isIsActive!!) {
//            if (treatment.typeInterval == EnumTypeInterval.HOUR) {
//                NotificationWorkerUtil().scheduleEventPeriodicWithAlarm(context, param, TimeUnit.HOURS)
//            } else {
                NotificationWorkerUtil().scheduleEventPeriodic(context, param, timeUnit,isForUpdate, NotificationWorkerTreatment::class.java)
//            }
        }
    }

    fun cancelEvent(context: Context, treatmentCard: Animal.TreatmentCard){
        val identity = treatmentCard.identity
//        if(treatmentCard.typeInterval == EnumTypeInterval.HOUR){
//            val param = ParametersEvent()
//            param.tag = treatmentCard.identity.toString()
//            NotificationWorkerUtil().cancelAlarmManager(context,param)
//        }else {
            NotificationWorkerUtil().cancel(context, identity.toString())
//        }
    }

    fun cancelEventTreatmentForAAnimal(context: Context,animal:Animal){
        if(animal.treatmentCard != null && animal.treatmentCard?.isNotEmpty()!!){
            animal.treatmentCard?.forEach {
                NotificationWorkerUtil().cancel(context,it.identity.toString())
            }
        }
    }
}