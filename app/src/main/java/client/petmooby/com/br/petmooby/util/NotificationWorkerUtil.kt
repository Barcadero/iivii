package client.petmooby.com.br.petmooby.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.*
import client.petmooby.com.br.petmooby.android.receiver.NotificationAlarmReceiver
import java.util.*
import java.util.concurrent.TimeUnit


class NotificationWorkerUtil {

    companion object {
        val workTag = "notificationWork"
        val PARAMETER = "parameters"
    }

    /*fun scheduleEvent(dateEvent:Date, context: Context,param:ParametersEvent ,clazz: Class<out ListenableWorker>){
        val inputData = getInputData(param)

        val future = DateTimeUtil.getDateDiff(Date(),dateEvent,TimeUnit.HOURS)
        Log.d(workTag,"Date now : ${DateTimeUtil.formatDateTime(Date(),"dd/MM/yyyy 'at' HH:mm:ss")}")
        Log.d(workTag,"Date dateEvent : ${DateTimeUtil.formatDateTime(dateEvent,"dd/MM/yyyy 'at' HH:mm:ss")}")
        Log.d(workTag,"Differ in hours $future")
        val notificationWork = OneTimeWorkRequest.Builder(clazz)
                .setInitialDelay(future,TimeUnit.HOURS)
                .setInputData(inputData)
                .addTag(param.tag)
                .build()
        WorkManager.getInstance(context).enqueueUniqueWork(param.tag,ExistingWorkPolicy.REPLACE,notificationWork)
    }*/

    private fun getInputData(param: ParametersEvent): Data {
        val inputDataBuilder = Data.Builder()
        inputDataBuilder.putLong("id", param.id)
        inputDataBuilder.putString("name", param.animalName)
        if(param.dateTime != null) {
            inputDataBuilder.putString("date", DateTimeUtil.formatDateTime(param.dateTime, DateTimeUtil.APPLICATION_FORMAT_OUTPUT))
        }
        if(param.vaccineType != null){
            inputDataBuilder.putString("vaccineType",param.vaccineType)
        }
        inputDataBuilder.putString("treatmentName",param.treatmentName)
        inputDataBuilder.putString("tag", param.tag)

        return inputDataBuilder.putInt(param.tag, param.id.toInt())
                .build()
    }

    fun cancel(context: Context, tag: String){
        WorkManager.getInstance(context).cancelAllWorkByTag(tag)
    }


    fun cancel(context: Context){
        WorkManager.getInstance(context).cancelAllWork()
    }

    fun scheduleEventPeriodic(dateEvent:Date, context: Context, param:ParametersEvent , isForUpdate:Boolean, clazz: Class<out ListenableWorker>){
        val inputData = getInputData(param)
        val future = DateTimeUtil.getDateDiff(Date(),dateEvent,TimeUnit.HOURS)
        Log.d("NOTIFICATION","NEXT NOTIFICATION IN HOURS: $future")
        val notificationWork = PeriodicWorkRequest.Builder(clazz,370,TimeUnit.DAYS)//(NotificationWorker::class.java!!)
                .setInitialDelay(future,TimeUnit.HOURS)
                .setInputData(inputData)
                .addTag(param.tag)
                .build()
        enqueueUniqueWork(isForUpdate, context, param, notificationWork)
    }

    private fun enqueueUniqueWork(isForUpdate: Boolean, context: Context, param: ParametersEvent, notificationWork: PeriodicWorkRequest) {
        if (isForUpdate) {
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(param.tag, ExistingPeriodicWorkPolicy.REPLACE, notificationWork)
        } else {
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(param.tag, ExistingPeriodicWorkPolicy.KEEP, notificationWork)
        }
    }

    fun scheduleEventPeriodic(context: Context, param:ParametersEvent ,timeUnit: TimeUnit,isForUpdate: Boolean,clazz: Class<out ListenableWorker>){
        val inputData = getInputData(param)
        //val future = DateTimeUtil.getDateDiff(Date(),dateEvent,TimeUnit.HOURS)
        //Log.d("NOTIFICATION","NEXT NOTIFICATION IN HOURS: $future")
        val notificationWork = PeriodicWorkRequest.Builder(clazz,param.repeatInterval,timeUnit)//(NotificationWorker::class.java!!)
                .setInitialDelay(param.repeatInterval,timeUnit)
                .setInputData(inputData)
                .addTag(param.tag)
                .build()
//        WorkManager.getInstance(context).enqueueUniquePeriodicWork(param.tag, ExistingPeriodicWorkPolicy.REPLACE,notificationWork)
        enqueueUniqueWork(isForUpdate,context,param,notificationWork)
    }
    //Save that code to use later
    fun scheduleEventPeriodicWithAlarm(context: Context, param:ParametersEvent ,timeUnit: TimeUnit){
        val intent = createIntentForAlarmManager(context, param)

        // Create a PendingIntent to be triggered when the alarm goes off
        val pIntent = createPendingIntent(context, param, intent)
        // Setup periodic alarm every every half hour from this point onwards
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.HOUR, param.repeatInterval.toInt())
            set(Calendar.MINUTE, 0)
        }
        val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        if(timeUnit == TimeUnit.HOURS) {
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                    AlarmManager.INTERVAL_HOUR, pIntent)
        }
    }

    private fun createIntentForAlarmManager(context: Context, param: ParametersEvent): Intent {
        // Construct an intent that will execute the AlarmReceiver
        val intent = Intent(context, NotificationAlarmReceiver::class.java)
        intent.putExtra(PARAMETER, param)
        return intent
    }

    private fun createPendingIntent(context: Context, param: ParametersEvent, intent: Intent): PendingIntent? {
        return PendingIntent.getBroadcast(context, param.tag.toInt(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    //Save that code to use later
    fun cancelAlarmManager(context: Context, param: ParametersEvent){
        val intent = createIntentForAlarmManager(context, param)
        // Create a PendingIntent to be triggered when the alarm goes off
        val pIntent = createPendingIntent(context, param, intent)
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmMgr?.cancel(pIntent)
    }

}