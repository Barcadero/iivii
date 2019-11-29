package client.petmooby.com.br.petmooby.util

import android.content.Context
import android.util.Log
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit


class NotificationWorkerUtil {

    companion object {
        val workTag = "notificationWork"
    }

    fun scheduleEvent(dateEvent:Date, context: Context,param:ParametersEvent ,clazz: Class<out ListenableWorker>){
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
    }

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

    fun scheduleEventPeriodic(dateEvent:Date, context: Context, param:ParametersEvent ,clazz: Class<out ListenableWorker>){
        val inputData = getInputData(param)
        val future = DateTimeUtil.getDateDiff(Date(),dateEvent,TimeUnit.HOURS)
        Log.d("NOTIFICATION","NEXT NOTIFICATION IN HOURS: $future")
        val notificationWork = PeriodicWorkRequest.Builder(clazz,370,TimeUnit.DAYS)//(NotificationWorker::class.java!!)
                .setInitialDelay(future,TimeUnit.HOURS)
                .setInputData(inputData)
                .addTag(param.tag)
                .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(param.tag, ExistingPeriodicWorkPolicy.REPLACE,notificationWork)
    }

    fun scheduleEventPeriodic(context: Context, param:ParametersEvent ,timeUnit: TimeUnit,clazz: Class<out ListenableWorker>){
        val inputData = getInputData(param)
        //val future = DateTimeUtil.getDateDiff(Date(),dateEvent,TimeUnit.HOURS)
        //Log.d("NOTIFICATION","NEXT NOTIFICATION IN HOURS: $future")
        val notificationWork = PeriodicWorkRequest.Builder(clazz,param.repeatInterval,timeUnit)//(NotificationWorker::class.java!!)
                //.setInitialDelay(future,TimeUnit.HOURS)
                .setInputData(inputData)
                .addTag(param.tag)
                .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(param.tag, ExistingPeriodicWorkPolicy.REPLACE,notificationWork)
    }

}