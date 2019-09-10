package client.petmooby.com.br.petmooby.actvity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.adapter.TreatmentListAdapter
import client.petmooby.com.br.petmooby.extensions.getDefaultLayoutManager
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.util.Parameters
import client.petmooby.com.br.petmooby.util.ResultCodes
import client.petmooby.com.br.petmooby.util.VariablesUtil
import kotlinx.android.synthetic.main.activity_treatment_list.*

class TreatmentListActivity : BaseActivity() {
    private var adapter:TreatmentListAdapter?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment_list)
        setupToolbar(R.id.toolbarTreatmentList, R.string.treatments)
        initRcViewList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            R.id.menuAdd ->{
                val intent = Intent(this,TreatmentActivity::class.java)
                startActivityForResult(intent,ResultCodes.REQUEST_INSERT)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    private fun initRcViewList(){
        if(VariablesUtil.gbSelectedAnimal?.treatmentCard == null){
            VariablesUtil.gbSelectedAnimal?.treatmentCard = mutableListOf()
        }else {
            adapter = TreatmentListAdapter(VariablesUtil.gbSelectedAnimal?.treatmentCard!!, this::onTreatmentClick)
            rcViewTreatmentList.adapter = adapter
            rcViewTreatmentList.layoutManager = getDefaultLayoutManager()
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
        rcViewTreatmentList.adapter?.notifyDataSetChanged()
    }


}
