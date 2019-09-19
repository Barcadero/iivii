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

    fun scheduleEvent(dateEvent:Date, context: Context){
        //store DBEventID to pass it to the PendingIntent and open the appropriate event page on notification click
        val inputData = Data.Builder().putInt("vaccine", 5).build()
// we then retrieve it inside the NotifyWorker with:
// final int DBEventID = getInputData().getInt(DBEventIDTag, ERROR_VALUE);

        val future = DateTimeUtil.getDateDiff(Date(),dateEvent,TimeUnit.MILLISECONDS)
        val date = Date(future)
        val notificationWork = OneTimeWorkRequest.Builder(NotificationWorker::class.java!!)
//                .setInitialDelay(300000,TimeUnit.MILLISECONDS)
                .setInitialDelay(future,TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .addTag(workTag)
                .build()

        WorkManager.getInstance(context).enqueue(notificationWork)
    }

    fun cancel(context: Context){
//        Finally, we have used addTag to set a tag for this work type in order to be able to call
        WorkManager.getInstance(context).cancelAllWorkByTag(workTag)
    }

    fun scheduleEventPeriodic(dateEvent:Date, context: Context){
        //store DBEventID to pass it to the PendingIntent and open the appropriate event page on notification click
        val inputData = Data.Builder().putInt("vaccine", 5).build()
// we then retrieve it inside the NotifyWorker with:
// final int DBEventID = getInputData().getInt(DBEventIDTag, ERROR_VALUE);

        //val future = DateTimeUtil.getDateDiff(Date(),dateEvent,TimeUnit.MILLISECONDS)
        //val date = Date(future)
        val notificationWork = PeriodicWorkRequest.Builder(NotificationWorker::class.java,2,TimeUnit.MINUTES)//(NotificationWorker::class.java!!)
                .setInitialDelay(60000,TimeUnit.MILLISECONDS)
                //.setInitialDelay(future,TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .addTag(workTag)
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(workTag, ExistingPeriodicWorkPolicy.KEEP,notificationWork)
    }

}