package client.petmooby.com.br.petmooby

import android.content.Intent
import android.os.Bundle
import android.util.Log
import client.petmooby.com.br.petmooby.actvity.BaseActivity
import client.petmooby.com.br.petmooby.actvity.RegisterUserActivity
import client.petmooby.com.br.petmooby.extensions.onFailedQueryReturn
import client.petmooby.com.br.petmooby.extensions.showAlert
import client.petmooby.com.br.petmooby.extensions.showLoadingDialog
import client.petmooby.com.br.petmooby.model.User
import client.petmooby.com.br.petmooby.model.enums.TypeUserEnum
import client.petmooby.com.br.petmooby.util.EncryptUtil
import client.petmooby.com.br.petmooby.util.FireStoreReference
import client.petmooby.com.br.petmooby.util.Preference
import com.facebook.*
import com.facebook.Profile.setCurrentProfile
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_login.*


/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity() {

    private var callBackManager:CallbackManager?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        callBackManager = CallbackManager.Factory.create()
//        KeyFileGen.genSH1Base64(this)
        checkLoginin()
        with(btnLoginFace) {
            setPermissions("email","public_profile")
//            setReadPermissions("public_profile, email")
            LoginManager.getInstance().registerCallback(callBackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult?) {
                    val request = GraphRequest.newMeRequest(
                            loginResult?.accessToken) { me, response ->
                        if (response.error != null) {
                            // handle error
                        } else {
                            // get email and id of the user
                            val email = me.optString("email")
                            Preference.setUserEmail(baseContext,email)

                        }
                    }
                    val parameters = Bundle()
                    parameters.putString("fields", "id,name,email,birthday")
                    request.parameters = parameters
                    request.executeAsync()
                    val profile = Profile.getCurrentProfile()
                    if(profile == null){
                        val profileTracker = object : ProfileTracker() {
                            override fun onCurrentProfileChanged(oldProfile: Profile?, currentProfile: Profile?) {
                                this.stopTracking()
                                setCurrentProfile(currentProfile)
                                setProfileAndCallMainActivity(currentProfile, loginResult!!)

                            }
                        }
                        profileTracker.startTracking()
                    }else {
                        setProfileAndCallMainActivity(profile, loginResult!!)
                    }
                }

                override fun onCancel() {
                    Log.d("FACE","cancel")
                }

                override fun onError(exception: FacebookException) {
                    // App code
                    Log.d("FACE",exception.message)
                    exception.printStackTrace()
                }

            })
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this,RegisterUserActivity::class.java))
        }

        btnLogin.setOnClickListener {
            doLoginSystem()
        }


    }

    private fun setProfileAndCallMainActivity(profile: Profile?, loginResult: LoginResult?) {
        val name = "${profile?.firstName} ${profile?.lastName}"
        val accessToken = loginResult?.accessToken
        val userIdFB = profile?.id
        Preference.set(this@LoginActivity, Preference.USER_NAME, name)
        Preference.set(this@LoginActivity, Preference.USER_TOKEN, accessToken?.token)
        Preference.set(this@LoginActivity, Preference.USER_ID, userIdFB)
        Preference.setUserType(this@LoginActivity, TypeUserEnum.FACEBOOK.ordinal)
        startMainActivity()
        finish()
    }

    private fun checkLoginin() {
        var type =  Preference.getUserType(this)
        when(type){
            TypeUserEnum.FACEBOOK.ordinal ->{
                var accessToken = AccessToken.getCurrentAccessToken()
                var isLoggedIn = accessToken != null && !accessToken.isExpired
                if (isLoggedIn) {
                    startMainActivity()
                    finish()
                }

            }
            TypeUserEnum.USER_SYSTEM.ordinal ->{
                val isLoggedIn = Preference.getUserDatabaseId(this)
                if (isLoggedIn!!.isNotEmpty()) {
                    startMainActivity()
                    finish()
                }
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callBackManager!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun doLoginSystem(){
        if(edtLoginEmail.text.toString().trim().isEmpty()){
            showAlert(R.string.pleaseGiveAEmail)
            return
        }
        if(edtLoginPwd.text.toString().trim().isEmpty()){
            showAlert(R.string.pleaseEnterAPassWord)
            return
        }
        var dialog = showLoadingDialog()
        docRefUser
                .whereEqualTo(User.USER_EMAIL,edtLoginEmail.text.toString().trim())
                .whereEqualTo(User.USER_PASSWORD,EncryptUtil.encryptPWD(edtLoginPwd.text.toString().trim()))
                .get()
                .addOnSuccessListener {
                    if(it.documents.isNotEmpty()){
                        Preference.setUserEmail(baseContext,edtLoginEmail.text.toString())
                        FireStoreReference.docRefUser = it.documents[0].reference
                        Preference.setUserDatabaseId(this, FireStoreReference.docRefUser!!.id!!)
                        Preference.setUserId(this,FireStoreReference.docRefUser!!.id!!)
                        Preference.setUserType(this,TypeUserEnum.USER_SYSTEM.ordinal)
                        dialog.dismiss()
                        startMainActivity()
                        finish()
                    }else{
                        dialog.dismiss()
                        showAlert(R.string.invalidCredentials)
                    }
                }
                .addOnFailureListener {
                    exception -> onFailedQueryReturn(dialog,exception.message!!)
                }

    }


}
