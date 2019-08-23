package client.petmooby.com.br.petmooby.extensions

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import client.petmooby.com.br.petmooby.R
import org.jetbrains.anko.alert
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.okButton


/**
 * Created by Rafael Rocha on 07/08/2018.
 */
inline fun Fragment.defaultRecycleView(view: FragmentActivity, resId:Int): RecyclerView {
    var recycleView = view.findViewById<RecyclerView>(resId)
    recycleView?.layoutManager = LinearLayoutManager(activity)
    recycleView?.itemAnimator = DefaultItemAnimator()
    recycleView?.setHasFixedSize(true)
    return recycleView
}

inline fun Fragment.defaultRecycleView(view: View, resId:Int): RecyclerView{
    //recycleView = view.findViewById(R.id.rcFindProf)
    var recycleView = view.findViewById<RecyclerView>(resId)
    recycleView?.layoutManager = LinearLayoutManager(activity)
    recycleView?.itemAnimator = DefaultItemAnimator()
    recycleView?.setHasFixedSize(true)
    return recycleView
}

inline fun Fragment.setupToolbar(@IdRes id: Int, title:String?= null, upNavigation: Boolean = false) : ActionBar {

    val activityCompat = activity as AppCompatActivity
    val toolbar = activity?.findViewById<Toolbar>(id)
    activityCompat.setSupportActionBar(toolbar)
    if(title != null){
        activityCompat.supportActionBar?.title = title
    }
    activityCompat.supportActionBar?.setHomeButtonEnabled(true)
    return activityCompat.supportActionBar!!
}

fun Fragment.switchFragmentToMainContent(fragment: Fragment){
    fragmentManager?.beginTransaction()
            ?.replace(R.id.mainFragment,fragment)
            ?.addToBackStack(fragment.javaClass.name)
            ?.commit()

}

fun Fragment.toast(message: CharSequence, length: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(context,message,length).show()

fun Fragment.showValidation(message: CharSequence) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showError(throwable: Throwable?) {
    var message = "Erro desconecido"
    if(throwable?.message != null){
        message = throwable.message!!
    }
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.backToFragment(fragment: Fragment){
    fragmentManager?.popBackStackImmediate(fragment.javaClass.name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

 @SuppressLint("NewApi")
 fun Fragment.showLoadingDialog(message: String = getString(R.string.loading), title: String=getString(R.string.wait)): ProgressDialog {


     val dialog = activity?.indeterminateProgressDialog(message, title) {
        setProgressStyle(ProgressDialog.STYLE_SPINNER)
        setCancelable(false)
        //ProgressLoadDialog(activity)
        setOnDismissListener({
             Handler().postDelayed({
                 dismiss()
             },3000)
        })
        show()
    }
    return dialog!!

     //return showLoadingDialogWithDelay(message,title)
}



fun Fragment.showAlertValidation(message:String){
    activity?.alert(message,activity?.getString(R.string.msgValidationTitle)!!){
        okButton{ it.dismiss() }
    }?.show()
}

fun Fragment.showAlertValidation(@StringRes idResource: Int){
    showAlertValidation(activity?.getString(idResource)!!)
}

fun Fragment.showAlert(message:String){
    activity?.alert(message,activity?.getString(R.string.Advice)!!){
        okButton{ it.dismiss() }
    }?.show()
}

fun Fragment.showAlert(@StringRes idResource: Int){
    showAlert(activity?.getString(idResource)!!)
}

fun Fragment.showAlertError(message:String){
    activity?.alert(message,activity?.getString(R.string.Advice)!!){
        okButton{ it.dismiss() }
    }?.show()
}

fun Fragment.showAlertError(@StringRes idResource: Int){
    showAlertError(activity?.getString(idResource)!!)
}

fun Fragment.showAlertFailure(message:String){
    activity?.alert(message,activity?.getString(R.string.Advice)!!){
        okButton{ it.dismiss() }
    }?.show()
}

fun Fragment.showAlertFailure(@StringRes idResource: Int){
    showAlertFailure(activity?.getString(idResource)!!)
}

fun Fragment.showAlert(@StringRes idResource: Int,onOkClick : ()->Unit){
    activity?.alert(activity?.getString(idResource)!!,activity?.getString(R.string.Advice)!!){
        okButton{ onOkClick }
    }?.show()
}

fun Fragment.callEmailHost(emailTo:String, subject:String, mailBody:String, title: String){
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.type = "plain/text"
    intent.data = Uri.parse("mailto:")
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailTo))
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, mailBody)
    activity?.startActivity(Intent.createChooser(intent, title))
}

fun Fragment.onFailedQueryReturn(dialog: ProgressDialog, message:String){
    dialog.dismiss()
    Log.d("FACE",message)
}