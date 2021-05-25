package client.petmooby.com.br.petmooby

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import client.petmooby.com.br.petmooby.actvity.BaseActivity
import client.petmooby.com.br.petmooby.data.enums.EnumUserStatus
import client.petmooby.com.br.petmooby.databinding.ActivityMainBinding
import client.petmooby.com.br.petmooby.extensions.onFailedQueryReturn
import client.petmooby.com.br.petmooby.extensions.showLoadingDialog
import client.petmooby.com.br.petmooby.fragment.CalendarFragment
import client.petmooby.com.br.petmooby.fragment.HomeFragment
import client.petmooby.com.br.petmooby.fragment.MenuFragment
import client.petmooby.com.br.petmooby.fragment.TipFragment
import client.petmooby.com.br.petmooby.model.enums.TypeUserEnum
import client.petmooby.com.br.petmooby.ui.viewmodel.UserViewModel
import client.petmooby.com.br.petmooby.util.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.adapter_my_animal_list.*
/**
 * Injetar dependências em classes do Android
Depois que o Hilt é configurado na classe Application e um componente no nível do aplicativo está disponível,
ele pode fornecer dependências para outras classes do Android que tenham a anotação @AndroidEntryPoint:
Atualmente, o Hilt é compatível com as seguintes classes do Android:

Application (ao usar @HiltAndroidApp)
Activity
Fragment
View
Service
BroadcastReceiver
Se você anotar uma classe do Android com @AndroidEntryPoint, também precisará anotar as classes que dependem
dela. Por exemplo, se você anotar um fragmento, também precisará anotar todas as atividades em que ele for usado.
 */
@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userViewModel : UserViewModel by viewModels()
    private var progress = showLoadingDialog()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val userType = Preference.getUserType(this)
        if(userType > -1){
            when(TypeUserEnum.values()[userType]){
                TypeUserEnum.FACEBOOK ->{
                    checkIfUserExistsAndSave()
                }
                TypeUserEnum.USER_SYSTEM->{
//                    if(FireStoreReference.docRefUser == null) {
                    if(Preference.getUserId(this) == null) {
                        getUserFromDataBase()
                    }else{
                        switchFragment(HomeFragment())
                    }
                }
            }
        }
        if(!PermissionUtil.checkPersmission(this))PermissionUtil.requestPermission(this)
        initObserver()
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
        progress = showLoadingDialog(getString(R.string.savingUser))
        userViewModel.saveCurrentUser(this)
//        var accessToken = AccessToken.getCurrentAccessToken()
//        var isLoggedIn = accessToken != null && !accessToken.isExpired
//        if(isLoggedIn){
//            var user = User()
//            user.name           = Preference.get<String>(this,Preference.USER_NAME)
//            user.tokenFacebook  = Preference.get(this,Preference.USER_TOKEN)
//            user.userIdFB       = Preference.getUserId(this)
//            user.type           = TypeUserEnum.values()[Preference.getUserType(this)!!]
//            user.email          = Preference.getUserEmail(this)
//            user.registerDate   = Date()
//            LogUtil.logDebug("FACE user name: ${user.name}")
//            LogUtil.logDebug("FACE user email: ${user.email}")
//            docRefUser.add(user)
//                    .addOnSuccessListener {
//                        documentReference -> FireStoreReference.docRefUser = documentReference;switchFragment(HomeFragment());dialog.dismiss()
//                    }
//                    .addOnFailureListener {
//                        exception ->  dialog.dismiss();toast("Erro ${exception.message}")
//                    }
//        }
    }

    private fun checkIfUserExistsAndSave(){
        val userId = Preference.getUserId(this)
//        val dialog = showLoadingDialog(getString(R.string.checkingUser))
        userViewModel.getFaceBookUser(userId!!)
//        docRefUser
//                .whereEqualTo(User.USER_ID_FACEBOOK,userId)
//                .get()
//                .addOnCompleteListener {
//                    task -> successQueryReturn(dialog,task)
//                }
//                .addOnFailureListener {
//                    exception -> onFailedQueryReturn(dialog,exception.message!!)
//                }
    }


//    private fun successQueryReturn(dialog: ProgressDialog, task:Task<QuerySnapshot> ) {
//        dialog.dismiss()
//        //var users = document.toObjects(User::class.java)
//        if(task.isSuccessful){
//            if(task.result?.isEmpty!!){
//                saveCurrenteUser()
//            }else {
//                FireStoreReference.docRefUser = task.result?.documents!![0].reference
//                doAsync {saveUnknowUserInformation(task) }
//                //NOTE: Set home fragment as the main content
//                switchFragment(HomeFragment())
//            }
//        }
//
//    }

//    private fun saveUnknowUserInformation(task: Task<QuerySnapshot>) {
//        try {
//           var userNeedsUpdate = false
//           val documents = task.result?.documents
//           //val user = User()
//           if (documents?.size == 1) {
//               val id = documents[0].id
//               val currentUser = documents[0].toObject(User::class.java)
//               if (currentUser?.name == null) {
//                   userNeedsUpdate = true
//                   currentUser?.name = Preference.getUserName(baseContext)
//               }
//               if (currentUser?.email == null || currentUser.email?.isEmpty()!!) {
//                   userNeedsUpdate = true
//                   currentUser?.email = Preference.getUserEmail(baseContext)
//               }
//               if (currentUser?.type == null) {
//                   userNeedsUpdate = true
//                   currentUser?.type = TypeUserEnum.values()[Preference.getUserType(baseContext)]
//               }
//
//               if (userNeedsUpdate) {
//                   docRefUser.document(id)
//                           .set(currentUser!!)
//                           .addOnSuccessListener {
//                               LogUtil.logDebug("FACE: user updated!!")
//                           }
//                           .addOnFailureListener {
//                               it.printStackTrace()
//                           }
//               }
//           }
//       }catch (e:Exception){
//           LogUtil.logDebug("Fail to save unknown user information")
//           e.printStackTrace()
//       }
//    }

    private fun getUserFromDataBase(){
        progress = showLoadingDialog(getString(R.string.checkingUser))
        val userId = Preference.getUserId(this)
        userViewModel.getUser(userId!!)
//        var userId = Preference.getUserDatabaseId(this)//Preference.get<String>(this,Preference.USER_ID)
//        var userId = Preference.getUserId(this)
//        docRefUser.document(userId!!)
//                //.whereEqualTo(User.USER_ID_FACEBOOK,userId)
//                .get()
//                .addOnCompleteListener {
//                    task ->
//                    run {
//                        if (task.result?.exists()!!) {
//                            dialog.dismiss()
//                            FireStoreReference.docRefUser = task.result?.reference
//                            switchFragment(HomeFragment())
//                        }else{
//                            dialog.dismiss()
//                            switchFragment(HomeFragment())
//                        }
//                    }
//                }
//
//                .addOnFailureListener {
//                    exception -> onFailedQueryReturn(dialog,exception.message!!)
//                }
    }


//    fun startServiceNotification(){
//        val i = Intent(this, NotificationAlarmService::class.java)
//        i.putExtra("animal","text")
//        startService(i)
//    }

    private fun initObserver(){
        userViewModel.userLiveData?.observe(this, { result ->
            when(result.status){
                EnumUserStatus.SUCCESS -> {
                    userViewModel.saveMissingUserInformation(this@MainActivity,"documento", result.dado!!)
                }
                EnumUserStatus.EMPTY -> {
                    saveCurrenteUser()
                }EnumUserStatus.UPDATED ->{
                    switchFragment(HomeFragment())
                }else -> {
                    onFailedQueryReturn(message =  result.error!!)
                }
            }
        })
        userViewModel.foundUserLiveData?.observe(this,  { result ->
            when(result.status){
                EnumUserStatus.SUCCESS -> {
                    switchFragment(HomeFragment())
                }
                else ->{
                    onFailedQueryReturn(message =  result.error!!)
                }
            }
        })
    }

}





