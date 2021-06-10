package client.petmooby.com.br.petmooby.actvity

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.extensions.onFailedQueryReturn
import client.petmooby.com.br.petmooby.extensions.showAlert
import client.petmooby.com.br.petmooby.extensions.showLoadingDialog
import client.petmooby.com.br.petmooby.fragment.HomeFragment
import client.petmooby.com.br.petmooby.model.User
import client.petmooby.com.br.petmooby.model.enums.TypeUserEnum
import client.petmooby.com.br.petmooby.util.EncryptUtil
import client.petmooby.com.br.petmooby.util.FireStoreReference
import client.petmooby.com.br.petmooby.util.Preference
import kotlinx.android.synthetic.main.activity_register_user.*
import org.jetbrains.anko.toast
import java.util.*

class RegisterUserActivity : BaseActivity() {

    private var currentUser: User?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
        btnSaveNewUserRegister.setOnClickListener {
            validateAndSaveUser()
        }
    }


    private fun validate():Boolean{
        if(edtNewUserName.text.toString().trim().isEmpty()){
            showAlert(R.string.pleaseGiveYourName)
            return false
        }
        if(edtNewUserEmail.text.toString().trim().isEmpty()){
            showAlert(R.string.pleaseGiveYourEmail)
            return false
        }
        if(edtNewUserPwd.text.toString().trim().isEmpty()){
            showAlert(R.string.pwdShoulBeNotEmpty)
            return false
        }

        if(edtNewUserConfirmPwd.text.toString().trim().isEmpty()){
            showAlert(R.string.confirmPwdShoulBeNotEmpty)
            return false
        }
        return true
    }

    private fun saveUser(dialog: ProgressDialog){
        docRefUser.add(currentUser!!)
                .addOnSuccessListener {
                    documentReference ->
                    //TODO check if docRefUser is necessary
                        FireStoreReference.docRefUser = documentReference
                        Preference.setUserType(this,currentUser?.type?.ordinal!!)
                        Preference.setUserId(this,documentReference.id)
                        dialog.dismiss()
                        startMainActivity()
                        finish()

                }
                .addOnFailureListener {
                    exception ->  dialog.dismiss();toast("Erro ${exception.message}")
                }
    }

    private fun validateAndSaveUser(){
        var dialog = showLoadingDialog()
        if(validate()){
            bindUserFromFields()
            //saveUser(dialog)
            checkIfAEmailExist(dialog)
        }else{
            dialog.dismiss()
        }

    }

    private fun bindUserFromFields(){
        if(currentUser == null){
            currentUser = User()
        }
        with(currentUser!!){
            name            = edtNewUserName.text.toString().trim()
            email           = edtNewUserEmail.text.toString().trim()
            password        = EncryptUtil.encryptPWD(edtNewUserPwd.text.toString().trim())
            type            = TypeUserEnum.USER_SYSTEM
            registerDate    = Date()
        }
    }

    private fun checkIfAEmailExist(dialog: ProgressDialog){
        docRefUser
                .whereEqualTo(User.USER_EMAIL,currentUser?.email)
                .get()
                .addOnSuccessListener {
                    if(it.documents.isEmpty()){
                        saveUser(dialog)
                    }else{
                        dialog.dismiss()
                        showAlert(R.string.emailAlreadyExist)
                    }
                }
                .addOnFailureListener {
                    exception -> onFailedQueryReturn(dialog,exception.message!!)
                }
    }
}
