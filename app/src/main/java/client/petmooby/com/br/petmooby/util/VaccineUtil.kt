package client.petmooby.com.br.petmooby.util

import android.content.Context
import client.petmooby.com.br.petmooby.model.Animal

class VaccineUtil {

    fun scheduleEvent(context: Context,vaccine:Animal.VaccineCards){
        val identity = vaccine.identity
        val date     = DateTimeUtil.addHourdToADate(vaccine.nextRemember!!,-12)
        NotificationWorkerUtil().scheduleEvent(date, context,identity.toString(),identity!!,NotificationWorker::class.java)
    }

    fun cancelEvent(context: Context,vaccine:Animal.VaccineCards){
        val identity = vaccine.identity
        NotificationWorkerUtil().cancel(context,identity.toString())
    }
}