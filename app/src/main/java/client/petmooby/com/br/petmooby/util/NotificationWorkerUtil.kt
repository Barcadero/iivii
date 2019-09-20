package client.petmooby.com.br.petmooby.util

import android.content.Context
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
        val future = DateTimeUtil.getDateDiff(Date(),dateEvent,TimeUnit.MILLISECONDS)
        val notificationWork = OneTimeWorkRequest.Builder(clazz)
                .setInitialDelay(future,TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .addTag(tag)
                .build()
//NotificationWorker::class.java!!
        WorkManager.getInstance(context).enqueueUniqueWork(tag,ExistingWorkPolicy.REPLACE,notificationWork)
    }

    fun cancel(context: Context, tag: String){
        WorkManager.getInstance(context).cancelAllWorkByTag(tag)
    }

    fun scheduleEventPeriodic(dateEvent:Date, context: Context, tag: String,id: Int,clazz: Class<out ListenableWorker>){
        val inputData = Data.Builder().putInt(tag, id).build()
        val notificationWork = PeriodicWorkRequest.Builder(clazz,2,TimeUnit.MINUTES)//(NotificationWorker::class.java!!)
                //.setInitialDelay(60000,TimeUnit.MILLISECONDS)
                //.setInitialDelay(future,TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .addTag(workTag)
                .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(tag, ExistingPeriodicWorkPolicy.REPLACE,notificationWork)
    }

}