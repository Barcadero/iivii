package client.petmooby.com.br.petmooby.android.service

import android.app.IntentService
import android.content.Intent
import client.petmooby.com.br.petmooby.model.enums.EnumTypeEvent
import client.petmooby.com.br.petmooby.util.NotificationUtil
import client.petmooby.com.br.petmooby.util.NotificationWorkerUtil
import client.petmooby.com.br.petmooby.util.ParametersEvent

class NotificationAlarmService : IntentService("NotificationAlarmService") {


    override fun onHandleIntent(intent: Intent?) {
        val parameter = intent?.extras?.getSerializable(NotificationWorkerUtil.PARAMETER) as ParametersEvent
        parameter.type = EnumTypeEvent.TREATMENT
        NotificationUtil.notify(super.getApplicationContext(),parameter)
    }

}