package client.petmooby.com.br.petmooby

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import client.petmooby.com.br.petmooby.extensions.onFailedQueryReturn
import client.petmooby.com.br.petmooby.extensions.showLoadingDialog
import client.petmooby.com.br.petmooby.fragment.HomeFragment
import client.petmooby.com.br.petmooby.fragment.MenuFragment
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.model.User
import client.petmooby.com.br.petmooby.util.Preference
import com.facebook.AccessToken
import com.google.android.gms.tasks.Task
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
        //NOTE: Set home fragment as the main content
        switchFragment(HomeFragment())
        //For facebook
        checkIfUserExistsAndSave()

    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.mainFragment, fragment)
                .commit()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                switchFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                //message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                //message.setText(R.string.title_notifications)
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
        var accessToken = AccessToken.getCurrentAccessToken()
        var isLoggedIn = accessToken != null && !accessToken.isExpired
        if(isLoggedIn){
            var user = User()
            user.name           = Preference.get<String>(this,Preference.USER_NAME)
            user.tokenFacebook  = Preference.get(this,Preference.USER_TOKEN)
            user.userIdFB       = Preference.get(this,Preference.USER_ID)
            docRefUser.add(user).addOnSuccessListener {
                toast("User saved")
            }.addOnFailureListener {
                exception -> toast("Erro ${exception.message}")
            }
        }
    }

    fun checkIfUserExistsAndSave(){
        var dialog = showLoadingDialog()
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
            if(task.result.isEmpty){
                saveCurrenteUser()
            }else {
                task.result
                        .map { it.toObject(User::class.java) }
                        .forEach { Log.d("FACE","Olá ${it.name}") }
            }
        }

    }
}





