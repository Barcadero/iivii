package client.petmooby.com.br.petmooby

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import client.petmooby.com.br.petmooby.actvity.BaseActivity
import client.petmooby.com.br.petmooby.extensions.onFailedQueryReturn
import client.petmooby.com.br.petmooby.extensions.showLoadingDialog
import client.petmooby.com.br.petmooby.fragment.CalendarFragment
import client.petmooby.com.br.petmooby.fragment.HomeFragment
import client.petmooby.com.br.petmooby.fragment.MenuFragment
import client.petmooby.com.br.petmooby.fragment.TipFragment
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.model.User
import client.petmooby.com.br.petmooby.model.enums.TypeUserEnum
import client.petmooby.com.br.petmooby.util.*
import com.facebook.AccessToken
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.util.*
import android.app.AlarmManager
import android.content.Context.ALARM_SERVICE
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import client.petmooby.com.br.petmooby.android.receiver.NotificationAlarmReceiver
import client.petmooby.com.br.petmooby.android.service.NotificationAlarmService
import com.annimon.stream.operator.IntArray
import org.jetbrains.anko.doAsync
import java.lang.Exception


class MainActivity : BaseActivity() {

//    private var docRefUser = FirebaseFirestore.getInstance().collection(CollectionsName.USER)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
//        scheduleAlarm()
//    startServiceNotification()

        val userType = Preference.getUserType(this)
        if(userType!! > -1){
//            checkIfUserExistsAndSave()
            when(TypeUserEnum.values()[userType]){
                TypeUserEnum.FACEBOOK ->{
                    checkIfUserExistsAndSave()
                }
                TypeUserEnum.USER_SYSTEM->{
                    if(FireStoreReference.docRefUser == null) {
                        getUserFromDataBase()
                    }else{
                        switchFragment(HomeFragment())
                    }
                }
            }
        }
    //For test
//    val calendar = Calendar.getInstance()
//    calendar.set(2019,8,19,0,58,15)
//    NotificationWorkerUtil().scheduleEvent(calendar.time,this)
//    NotificationWorkerUtil().scheduleEventPeriodic(Date(),this,"vaccine",225544,NotificationWorkerVaccine::class.java)
        if(!PermissionUtil.checkPersmission(this))PermissionUtil.requestPermission(this)

    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.mainFragment, fragment)
                .commitAllowingStateLoss()//it solve the problem related at the below link
                //--------------------------------------------------------------------------------
                // https://medium.com/@elye.project/handling-illegalstateexception-can-not-perform-this-action-after-onsaveinstancestate-d4ee8b630066
    }           //--------------------------------------------------------------------------------

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                switchFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_tip -> {
                switchFragment(TipFragment())
                return@OnNavigationItemSelectedListener true
            }
            //R.id.navigation_notifications -> {
            //    switchFragment(NotificationFragment())
            //    return@OnNavigationItemSelectedListener true
            //}
            R.id.navigation_calendar->{
                switchFragment(CalendarFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_menu ->{
                switchFragment(MenuFragment())
                return@OnNavigationItemSelectedListener true
            }else -> true
        }
        false
    }

    private fun saveCurrenteUser(){
        var dialog = showLoadingDialog(getString(R.string.savingUser))
        var accessToken = AccessToken.getCurrentAccessToken()
        var isLoggedIn = accessToken != null && !accessToken.isExpired
        if(isLoggedIn){
            var user = User()
            user.name           = Preference.get<String>(this,Preference.USER_NAME)
            user.tokenFacebook  = Preference.get(this,Preference.USER_TOKEN)
            user.userIdFB       = Preference.getUserId(this)//Preference.get(this,Preference.USER_ID)
            user.type           = TypeUserEnum.values()[Preference.getUserType(this)!!]//TypeUserEnum.FACEBOOK
            user.email          = Preference.getUserEmail(this)
            user.registerDate   = Date()
            LogUtil.logDebug("FACE user name: ${user.name}")
            LogUtil.logDebug("FACE user email: ${user.email}")
            docRefUser.add(user)
                    .addOnSuccessListener {
                        documentReference -> FireStoreReference.docRefUser = documentReference;switchFragment(HomeFragment());dialog.dismiss()
                    }
                    .addOnFailureListener {
                        exception ->  dialog.dismiss();toast("Erro ${exception.message}")
                    }
        }
    }

    fun checkIfUserExistsAndSave(){
        var dialog = showLoadingDialog(getString(R.string.checkingUser))
        var userId = Preference.getUserId(this)//Preference.get<String>(this,Preference.USER_ID)
        docRefUser
                .whereEqualTo(User.USER_ID_FACEBOOK,userId)
                .get()
                .addOnCompleteListener {
                    task -> successQueryReturn(dialog,task)
                }
                .addOnFailureListener {
                    exception -> onFailedQueryReturn(dialog,exception.message!!)
                }
    }


    private fun successQueryReturn(dialog: ProgressDialog, task:Task<QuerySnapshot> ) {
        dialog.dismiss()
        //var users = document.toObjects(User::class.java)
        if(task.isSuccessful){
            if(task.result?.isEmpty!!){
                saveCurrenteUser()
            }else {
                FireStoreReference.docRefUser = task.result?.documents!![0].reference
                doAsync {saveUnknowUserInformation(task) }
                //NOTE: Set home fragment as the main content
                switchFragment(HomeFragment())
            }
        }

    }

    private fun saveUnknowUserInformation(task: Task<QuerySnapshot>) {
        try {
           var userNeedsUpdate = false
           val documents = task.result?.documents
           //val user = User()
           if (documents?.size == 1) {
               val id = documents[0].id
               val currentUser = documents[0].toObject(User::class.java)
               if (currentUser?.name == null) {
                   userNeedsUpdate = true
                   currentUser?.name = Preference.getUserName(baseContext)
               }
               if (currentUser?.email == null || currentUser.email?.isEmpty()!!) {
                   userNeedsUpdate = true
                   currentUser?.email = Preference.getUserEmail(baseContext)
               }
               if (currentUser?.type == null) {
                   userNeedsUpdate = true
                   currentUser?.type = TypeUserEnum.values()[Preference.getUserType(baseContext)]
               }

               if (userNeedsUpdate) {
                   docRefUser.document(id)
                           .set(currentUser!!)
                           .addOnSuccessListener {
                               LogUtil.logDebug("FACE: user updated!!")
                           }
                           .addOnFailureListener {
                               it.printStackTrace()
                           }
               }
           }
       }catch (e:Exception){
           LogUtil.logDebug("Fail to save unknown user information")
           e.printStackTrace()
       }
    }

    fun getUserFromDataBase(){
        var dialog = showLoadingDialog(getString(R.string.checkingUser))
//        var userId = Preference.getUserDatabaseId(this)//Preference.get<String>(this,Preference.USER_ID)
        var userId = Preference.getUserId(this)
        docRefUser.document(userId!!)
                //.whereEqualTo(User.USER_ID_FACEBOOK,userId)
                .get()
                .addOnCompleteListener {
                    task ->
                    run {
                        if (task.result?.exists()!!) {
                            dialog.dismiss()
                            FireStoreReference.docRefUser = task.result?.reference
                            switchFragment(HomeFragment())
                        }else{
                            dialog.dismiss()
                            switchFragment(HomeFragment())
                        }
                    }
                }

                .addOnFailureListener {
                    exception -> onFailedQueryReturn(dialog,exception.message!!)
                }
    }


    fun startServiceNotification(){
        val i = Intent(this, NotificationAlarmService::class.java)
        i.putExtra("animal","text")
        startService(i)
    }

    /*fun scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        val intent = Intent(applicationContext, NotificationAlarmReceiver::class.java)
        intent.putExtra("name","first intent")
        // Create a PendingIntent to be triggered when the alarm goes off
        val pIntent = PendingIntent.getBroadcast(this, 100,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
        // Setup periodic alarm every every half hour from this point onwards
        val firstMillis = System.currentTimeMillis() // alarm is set right away
        val alarm = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pIntent)
    }*/
}





