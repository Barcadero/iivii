package client.petmooby.com.br.petmooby.util

import android.content.Context
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.model.enums.EnumTypeInterval
import java.util.concurrent.TimeUnit

object TreatmentUtil {

    fun generateTreatmentAlarm(context: Context,animal:Animal,identity: Long){
        var treatment = animal.treatmentCard?.filter { it.identity == identity }!![0]
        if(treatment != null){
            scheduleEvent(context,treatment,animal.name!!)
//            when(treatment.typeInterval){
//                EnumTypeInterval.HOUR ->{
//                    scheduleEvent(context,treatment,animal.name!!)
//                }
//            }
        }
    }

    fun scheduleEvent(context: Context, treatment:Animal.TreatmentCard, name : String){
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
        NotificationWorkerUtil().scheduleEventPeriodic(context,param,timeUnit,NotificationWorkerTreatment::class.java)
    }

    fun cancelEvent(context: Context, treatmentCard: Animal.TreatmentCard){
        val identity = treatmentCard.identity
        NotificationWorkerUtil().cancel(context,identity.toString())
    }
}