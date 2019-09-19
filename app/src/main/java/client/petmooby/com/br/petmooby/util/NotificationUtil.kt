package client.petmooby.com.br.petmooby.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import client.petmooby.com.br.petmooby.MainActivity
import client.petmooby.com.br.petmooby.R
import kotlin.random.Random

object NotificationUtil {
    private fun create(context: Context, id:Int, intent: Intent, @StringRes contentTitle:Int,@StringRes contentText:Int){
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val p = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context,"id")
                .setContentIntent(p)
                .setContentTitle(context.getString(contentTitle))
                .setContentText(context.getString(contentText))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)

        val n = builder.build()
        manager.notify(id,n)
    }

    fun notifyVaccine(context: Context){
        create(context, Random(5000).nextInt(),Intent(context, MainActivity::class.java),R.string.notificationVaccine,R.string.notificationVaccineBody)
    }
}