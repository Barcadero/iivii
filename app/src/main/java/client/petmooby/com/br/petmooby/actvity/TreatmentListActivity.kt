package client.petmooby.com.br.petmooby.actvity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.UiThread
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.adapter.TreatmentListAdapter
import client.petmooby.com.br.petmooby.extensions.getDefaultLayoutManager
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.extensions.showAlert
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.util.Parameters
import client.petmooby.com.br.petmooby.util.ResultCodes
import client.petmooby.com.br.petmooby.util.VariablesUtil
import kotlinx.android.synthetic.main.activity_treatment_list.*
import kotlinx.android.synthetic.main.activity_vaccine_lits.*
import kotlinx.android.synthetic.main.empty_view_list_layout.*

class TreatmentListActivity : BaseActivity() {
    private var adapter:TreatmentListAdapter?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment_list)
        ivEmptyListIcon.setImageResource(R.drawable.icons8_pills_60)
        setupToolbar(R.id.toolbarTreatmentList, R.string.treatments)
        initRcViewList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menuAdd ->{
                if(VariablesUtil.gbSelectedAnimal?.treatmentCard?.size!! > 7){
                    showAlert(R.string.youCanOnlyHaveSomeTreatments)
                }else {
                    val intent = Intent(this, TreatmentActivity::class.java)
                    startActivityForResult(intent, ResultCodes.REQUEST_INSERT)
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    private fun initRcViewList(){
        if(VariablesUtil.gbSelectedAnimal?.treatmentCard == null || VariablesUtil.gbSelectedAnimal?.treatmentCard?.isEmpty()!!){
            VariablesUtil.gbSelectedAnimal?.treatmentCard = mutableListOf()
            showAndHideControls(false)
        }else {
            adapter = TreatmentListAdapter(VariablesUtil.gbSelectedAnimal?.treatmentCard!!, this::onTreatmentClick)
            rcViewTreatmentList.adapter = adapter
            rcViewTreatmentList.layoutManager = getDefaultLayoutManager()
            showAndHideControls(true)
        }
    }

    private fun onTreatmentClick(treatmentCard: Animal.TreatmentCard){
        val intent = Intent(this,TreatmentActivity::class.java)
        intent.putExtra(Parameters.ACTION,ResultCodes.REQUEST_UPDATE)
        intent.putExtra(Parameters.TREATMENT,treatmentCard.identity)
        startActivityForResult(intent,ResultCodes.REQUEST_UPDATE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(rcViewTreatmentList.adapter == null) {
            initRcViewList()
        }else{
            rcViewTreatmentList.adapter?.notifyDataSetChanged()
        }
    }

    @UiThread
    fun showAndHideControls(show:Boolean) {
        if(show){
            layoutEmptyList.visibility      = View.GONE
            rcViewTreatmentList.visibility    = View.VISIBLE
        }else {
            layoutEmptyList.visibility = View.VISIBLE
            rcViewTreatmentList.visibility = View.GONE
        }
    }


}
