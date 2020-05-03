package client.petmooby.com.br.petmooby.actvity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.UiThread
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.adapter.AttendanceAdapter
import client.petmooby.com.br.petmooby.extensions.getDefaulLayoutManager
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.extensions.showAlert
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.util.Parameters
import client.petmooby.com.br.petmooby.util.ResultCodes
import client.petmooby.com.br.petmooby.util.VariablesUtil
import kotlinx.android.synthetic.main.activity_attendance_list.*
import kotlinx.android.synthetic.main.empty_view_list_layout.*

class AttendanceListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_list)
        setupToolbar(R.id.toolbarAttendanceList, R.string.attendances)
        ivEmptyListIcon.setImageResource(R.drawable.vaccine_animal)
        txtEmptyMessage.text = getString(R.string.youDontHaveAttendances)
        if(VariablesUtil.gbSelectedAnimal?.vetConsultation == null){
            VariablesUtil.gbSelectedAnimal?.vetConsultation = mutableListOf()
        }
        getPetVetConsultation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menuAdd -> {
            if(VariablesUtil.gbSelectedAnimal?.vetConsultation != null && VariablesUtil.gbSelectedAnimal?.vetConsultation!!.size > VariablesUtil.maxRegisteredAttendances - 1){
                showAlert(getString(R.string.youCanOnlyHaveSomeAttendances,VariablesUtil.maxRegisteredAttendances))
            }else {
                val intent = Intent(this, AddAttendanceActivity::class.java)
                intent.putExtra(Parameters.ACTION, ResultCodes.REQUEST_INSERT)
                startActivityForResult(intent, ResultCodes.REQUEST_INSERT)
            }
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun getPetVetConsultation(){
        if(VariablesUtil.gbSelectedAnimal?.vetConsultation != null){
            if(!VariablesUtil.gbSelectedAnimal?.vetConsultation?.isEmpty()!!) {
                rcViewAttendanceList.adapter       = AttendanceAdapter(VariablesUtil.gbSelectedAnimal?.vetConsultation!!) {  vetConsultation -> onClick(vetConsultation) }
                rcViewAttendanceList.layoutManager = getDefaulLayoutManager()
                showAndHideControls(true)
            }else{
                showAndHideControls(false)
            }
        }else{
            showAndHideControls(false)
        }
    }

    @UiThread
    fun showAndHideControls(show:Boolean) {
        if(show){
            layoutEmptyList.visibility      = View.GONE
            rcViewAttendanceList.visibility    = View.VISIBLE
        }else {
            layoutEmptyList.visibility = View.VISIBLE
            rcViewAttendanceList.visibility = View.GONE
        }
    }

    private fun onClick(vetConsultation: Animal.VetConsultation){
        var intent = Intent(this,AddAttendanceActivity::class.java)
        intent.putExtra(Parameters.ACTION,ResultCodes.REQUEST_UPDATE)
        intent.putExtra(Parameters.IDENTITY,vetConsultation.identity)
        startActivityForResult(intent, ResultCodes.REQUEST_UPDATE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(rcViewAttendanceList.adapter == null){
            getPetVetConsultation()
        }else {
            rcViewAttendanceList.adapter?.notifyDataSetChanged()
        }
    }
}
