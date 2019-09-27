package client.petmooby.com.br.petmooby.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import client.petmooby.com.br.petmooby.MainActivity
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.model.enums.EnumTypeEvent
import kotlin.random.Random

object NotificationUtil {
    private fun create(context: Context, id:Int, intent: Intent, @StringRes contentTitle:Int,@StringRes contentText:Int){
        val title = context.getString(contentTitle)
        val text  = context.getString(contentText)
        buildMessage(context, intent, title, text, id)
    }

    private fun create(context: Context, id:Int, intent: Intent, contentTitle:String,contentText:String){
        buildMessage(context, intent, contentTitle, contentText, id)
    }

    private fun buildMessage(context: Context, intent: Intent, title: String, text: String, id: Int) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val p = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context, "id")
                .setContentIntent(p)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)

        val n = builder.build()
        manager.notify(id, n)
    }

    fun notifyVaccine(context: Context, param:ParametersEvent){
        var message = ""
        var title = ""
        when(param.type){
            EnumTypeEvent.VACCINE ->{
                title = context.getString(R.string.vaccine)
                val builder = StringBuilder()
                //A vacina do Rex está próxima: dia 25/09/2019
                //builder.append(conntext.getString(R.string.))
            }
        }
        create(context, Random(5000).nextInt(),Intent(context, MainActivity::class.java),title,message)
    }
}