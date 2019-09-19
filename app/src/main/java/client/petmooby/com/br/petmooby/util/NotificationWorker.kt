package client.petmooby.com.br.petmooby.util

import android.content.Context
import androidx.annotation.NonNull
import androidx.work.Worker
import androidx.work.WorkerParameters


class NotificationWorker(@NonNull context: Context, @NonNull  params: WorkerParameters): Worker(context,params) {

    override fun doWork(): Result {
        NotificationUtil.notifyVaccine(super.getApplicationContext())
        return Result.success()
    }


}