package client.petmooby.com.br.petmooby.extensions

import android.app.Activity
import android.app.ProgressDialog
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.util.Log
import client.petmooby.com.br.petmooby.R
import org.jetbrains.anko.alert
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.okButton

/**
 * Created by Rafael Rocha on 25/07/18.
 */

inline fun Activity.defaultRecycleView(view: Activity, resId:Int): RecyclerView {
    var recycleView = view.findViewById<RecyclerView>(resId)
    recycleView?.layoutManager = LinearLayoutManager(this)
    recycleView?.itemAnimator = DefaultItemAnimator()
    recycleView?.setHasFixedSize(true)
    return recycleView
}
//@Deprecated("Please replace with setupToolbar(@IdRes id: Int, @IdRes idString: Int, upNavigation: Boolean = false) : ActionBar")
inline fun AppCompatActivity.setupToolbar(@IdRes id: Int,title:String?= null,upNavigation: Boolean = false) : ActionBar{
    val toolbar = findViewById<Toolbar>(id)
    setSupportActionBar(toolbar)
    if(title != null){
        supportActionBar?.title = title
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(upNavigation)
    return supportActionBar!!
}

fun AppCompatActivity.setupToolbar(@IdRes id: Int,  idString: Int, upNavigation: Boolean = false) : ActionBar{
    val toolbar = findViewById<Toolbar>(id)
    setSupportActionBar(toolbar)
    if(title != null){
        supportActionBar?.title = resources.getString(idString)
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(upNavigation)
    return supportActionBar!!
}

fun AppCompatActivity.switchFragmentToMainContent(fragment: Fragment){
    supportFragmentManager.beginTransaction()
            ?.replace(R.id.mainFragment,fragment)
            ?.commit()
}

fun AppCompatActivity.switchFragment(@IdRes id: Int, fragment: Fragment){
    supportFragmentManager.beginTransaction()
            ?.replace(id,fragment)
            ?.commit()
}


fun AppCompatActivity.showLoadingDialog(message: String = getString(R.string.loading), title: String=getString(R.string.wait)): ProgressDialog {
    val dialog = indeterminateProgressDialog(message) {
        setProgressStyle(ProgressDialog.STYLE_SPINNER)
        setCancelable(false)
        show()
    }
    return dialog!!
}

fun Activity.showAlert(message:String){
    alert(message,getString(R.string.Advice)!!){
        okButton{ it.dismiss() }
    }?.show()
}

fun Activity.showAlert(@StringRes idResource: Int){
    showAlert(getString(idResource)!!)
}

fun Activity.showAlertError(message:String){
    alert(message,getString(R.string.Advice)!!){
        okButton{ it.dismiss() }
    }?.show()
}

fun Activity.showAlertError(@StringRes idResource: Int){
    showAlertError(getString(idResource)!!)
}

fun Activity.onFailedQueryReturn(dialog: ProgressDialog,message:String){
    dialog.dismiss()
    Log.d("FACE",message)
}

fun Activity.getDefaulLayoutManager(): GridLayoutManager{
    val layoutManager = GridLayoutManager(this,2)
    layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
        override fun getSpanSize(position: Int): Int {
            return if (position == 0) 2 else 1
        }

    }
    return layoutManager

}
