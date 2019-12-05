package client.petmooby.com.br.petmooby.android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import client.petmooby.com.br.petmooby.android.service.NotificationAlarmService
import client.petmooby.com.br.petmooby.util.NotificationWorkerUtil
import client.petmooby.com.br.petmooby.util.ParametersEvent

class NotificationAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val i = Intent(context,NotificationAlarmService::class.java)
        val parameter = intent?.extras?.getSerializable(NotificationWorkerUtil.PARAMETER) as ParametersEvent
        i.putExtra(NotificationWorkerUtil.PARAMETER,parameter)
        context?.startService(i)
    }
}