package client.petmooby.com.br.petmooby.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import client.petmooby.com.br.petmooby.MainActivity
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.application.Application
import client.petmooby.com.br.petmooby.model.enums.EnumTypeEvent
import kotlin.random.Random
import android.app.NotificationChannel
import android.graphics.BitmapFactory
import android.os.Build.VERSION_CODES.O
import android.os.Build
import org.jetbrains.anko.Android


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
        val builder = NotificationCompat.Builder(context,"${id + 1}")
                .setContentIntent(p)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.mipmap.logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources,
                        R.mipmap.logo))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)


        if (Build.VERSION.SDK_INT >= O) {
            val channelId = "${id + 1}"
            val channel = NotificationChannel(
                    channelId,
                    "Vaccine note",
                    NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
            builder.setChannelId(channelId)
        }
        val n = builder.build()
        manager.notify(id, n)
    }

    fun notify(context: Context, param:ParametersEvent){
        var message:String
        var title:String
        when(param.type){
            EnumTypeEvent.VACCINE ->{
                title = context.getString(R.string.vaccine)
                val builder = StringBuilder()
                //A vacina do Rex está próxima: dia 25/09/2019
                if(Application.DEVICE_LANGUAGE == Application.LANG_PT){
                    builder.append("${param.animalName} precisa ser vacinado " )
                            .append("\n por ${param.vaccineType} no dia: ${param.dateString}.")
                }else{
                    //English version
                    builder.append("${param.animalName} needs to be vaccinated " )
                            .append("\n for ${param.vaccineType} in day: ${param.dateString}.")
                }
                message = builder.toString()
            }
            else ->{
                title = "Treatment"
                val builder = StringBuilder()
                if(Application.DEVICE_LANGUAGE == Application.LANG_PT){
                    builder.append("${param.treatmentName} para ${param.animalName}.")
                }else{
                    //English version
                    builder.append("${param.treatmentName} for ${param.animalName}.")
                }
                message = builder.toString()
            }
        }
        create(context, Random(5000).nextInt(),Intent(context, MainActivity::class.java),title,message)
    }

}