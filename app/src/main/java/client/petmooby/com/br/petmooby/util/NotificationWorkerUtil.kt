package client.petmooby.com.br.petmooby.util

import android.content.Context
import android.util.Log
import androidx.work.*
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit


class NotificationWorkerUtil {

    companion object {
        val workTag = "notificationWork"
    }

    fun scheduleEvent(dateEvent:Date, context: Context, tag:String, id:Int,clazz: Class<out ListenableWorker>){
        val inputData = Data.Builder().putInt(tag, id).build()
        val future = DateTimeUtil.getDateDiff(Date(),dateEvent,TimeUnit.HOURS)
        Log.d(workTag,"Date now : ${DateTimeUtil.formatDateTime(Date(),"dd/MM/yyyy 'at' HH:mm:ss")}")
        Log.d(workTag,"Date dateEvent : ${DateTimeUtil.formatDateTime(dateEvent,"dd/MM/yyyy 'at' HH:mm:ss")}")
        Log.d(workTag,"Differ in hours $future")
        val notificationWork = OneTimeWorkRequest.Builder(clazz)
                .setInitialDelay(future,TimeUnit.HOURS)
                .setInputData(inputData)
                .addTag(tag)
                .build()
        WorkManager.getInstance(context).enqueueUniqueWork(tag,ExistingWorkPolicy.REPLACE,notificationWork)
    }

    fun cancel(context: Context, tag: String){
        WorkManager.getInstance(context).cancelAllWorkByTag(tag)
    }

    fun scheduleEventPeriodic(dateEvent:Date, context: Context, tag: String,id: Int,clazz: Class<out ListenableWorker>){
        val inputData = Data.Builder().putInt(tag, id).build()
        val future = DateTimeUtil.getDateDiff(Date(),dateEvent,TimeUnit.HOURS)
        val notificationWork = PeriodicWorkRequest.Builder(clazz,future,TimeUnit.HOURS)//(NotificationWorker::class.java!!)
                .setInitialDelay(future,TimeUnit.HOURS)
                .setInputData(inputData)
                .addTag(workTag)
                .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(tag, ExistingPeriodicWorkPolicy.REPLACE,notificationWork)
    }

}