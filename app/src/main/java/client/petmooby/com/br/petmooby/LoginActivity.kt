package client.petmooby.com.br.petmooby

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import client.petmooby.com.br.petmooby.actvity.BaseActivity
import client.petmooby.com.br.petmooby.actvity.RegisterUserActivity
import client.petmooby.com.br.petmooby.databinding.ActivityLoginBinding
import client.petmooby.com.br.petmooby.extensions.onFailedQueryReturn
import client.petmooby.com.br.petmooby.extensions.showAlert
import client.petmooby.com.br.petmooby.extensions.showLoadingDialog
import client.petmooby.com.br.petmooby.model.enums.StatusLogin
import client.petmooby.com.br.petmooby.model.enums.TypeUserEnum
import client.petmooby.com.br.petmooby.ui.viewmodel.LoginViewModel
import client.petmooby.com.br.petmooby.util.Preference
import com.facebook.*
import com.facebook.Profile.setCurrentProfile
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import dagger.hilt.android.AndroidEntryPoint


/**
 * A login screen that offers login via email/password.
 */
@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private var dialog : ProgressDialog? = null
    private var callBackManager:CallbackManager?=null
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        callBackManager = CallbackManager.Factory.create()
//        KeyFileGen.genSH1Base64(this)
        checkLoginin()
        with(binding.btnLoginFace) {
            setPermissions("email","public_profile")
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
                    Log.d("FACE",exception.message!!)
                    exception.printStackTrace()
                }

            })
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this,RegisterUserActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            doLoginSystem()
        }

        initObservers()
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
        val type =  Preference.getUserType(this)
        when(type){
            TypeUserEnum.FACEBOOK.ordinal ->{
                val accessToken = AccessToken.getCurrentAccessToken()
                val isLoggedIn = accessToken != null && !accessToken.isExpired
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
        if(binding.edtLoginEmail.text.toString().trim().isEmpty()){
            showAlert(R.string.pleaseGiveAEmail)
            return
        }
        if(binding.edtLoginPwd.text.toString().trim().isEmpty()){
            showAlert(R.string.pleaseEnterAPassWord)
            return
        }
        dialog = showLoadingDialog()
        viewModel.doLoginSystem(
                email =  binding.edtLoginEmail.text.toString().trim(),
                pwd   =  binding.edtLoginPwd.text.toString().trim()
        )
    }

    private fun initObservers(){
        viewModel.loginLiveData.observe(this, Observer {
            dialog?.dismiss()
            when(it.status()){
                StatusLogin.SUCCESS -> {
                    viewModel.setPreference(this,it.data()!!)
                    startMainActivity()
                    finish()
                }
                StatusLogin.EMPTY ->{
                    showAlert(R.string.invalidCredentials)
                }else -> {
                    onFailedQueryReturn(dialog!!,getString(R.string.login_fail))
                }
            }
        })
    }
}
