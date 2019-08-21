package client.petmooby.com.br.petmooby

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import client.petmooby.com.br.petmooby.extensions.onFailedQueryReturn
import client.petmooby.com.br.petmooby.extensions.showLoadingDialog
import client.petmooby.com.br.petmooby.fragment.*
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.model.User
import client.petmooby.com.br.petmooby.util.FireStoreReference
import client.petmooby.com.br.petmooby.util.PermissionUtil
import client.petmooby.com.br.petmooby.util.Preference
import com.facebook.AccessToken
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private var docRefUser = FirebaseFirestore.getInstance().collection(CollectionsName.USER)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        //For facebook
        checkIfUserExistsAndSave()
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

    fun saveCurrenteUser(){
        var dialog = showLoadingDialog(getString(R.string.savingUser))
        var accessToken = AccessToken.getCurrentAccessToken()
        var isLoggedIn = accessToken != null && !accessToken.isExpired
        if(isLoggedIn){
            var user = User()
            user.name           = Preference.get<String>(this,Preference.USER_NAME)
            user.tokenFacebook  = Preference.get(this,Preference.USER_TOKEN)
            user.userIdFB       = Preference.get(this,Preference.USER_ID)
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
        var userId = Preference.get<String>(this,Preference.USER_ID)
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
                FireStoreReference.docRefUser = task?.result?.documents!![0].reference
                //NOTE: Set home fragment as the main content
                switchFragment(HomeFragment())
            }
        }

    }
}





