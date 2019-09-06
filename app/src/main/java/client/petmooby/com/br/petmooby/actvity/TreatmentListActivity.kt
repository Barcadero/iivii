package client.petmooby.com.br.petmooby.actvity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.adapter.TreatmentListAdapter
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.util.Parameters
import client.petmooby.com.br.petmooby.util.ResultCodes
import client.petmooby.com.br.petmooby.util.VariablesUtil
import kotlinx.android.synthetic.main.activity_treatment_list.*
//import org.parceler.Parcels

//import org.parceler.Parcels

class TreatmentListActivity : BaseActivity() {

//    private var treatments = mutableListOf<Animal.TreatmentCard>()
    private var adapter:TreatmentListAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment_list)
        setupToolbar(R.id.toolbarTreatmentList, R.string.treatments)
        initRcViewList()
//        var animal = intent.getParcelableExtra<Animal>(Parameters.ANIMAL_PARAMETER)
//        var animal = intent.getSerializableExtra(Parameters.ANIMAL_PARAMETER) as Animal
//        var animal = Parcels.unwrap<Animal>(intent.getParcelableExtra(Parameters.ANIMAL_PARAMETER))
//        for(treatment in VariablesUtil.gbSelectedAnimal?.treatmentCard!!){
//            addTreatment(treatment)
//        }
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
        if(VariablesUtil.gbSelectedAnimal?.treatmentCard != null){
            VariablesUtil.gbSelectedAnimal?.treatmentCard = mutableListOf()
        }else {
            adapter = TreatmentListAdapter(VariablesUtil.gbSelectedAnimal?.treatmentCard!!, this::onTreatmentClick)
            rcViewTreatmentList.adapter = adapter
            val layoutManager = GridLayoutManager(this, 2)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == 0) 2 else 1
                }
            }
        }
    }

    private fun addTreatment(treatmentCard: Animal.TreatmentCard){
        VariablesUtil.gbSelectedAnimal?.treatmentCard?.add(treatmentCard)
        adapter?.notifyItemInserted(VariablesUtil.gbSelectedAnimal?.treatmentCard?.lastIndex!!)
    }

    private fun onTreatmentClick(treatmentCard: Animal.TreatmentCard){
        val intent = Intent(this,TreatmentActivity::class.java)
        intent.putExtra(Parameters.ACTION,ResultCodes.REQUEST_UPDATE)
        intent.putExtra(Parameters.TREATMENT,treatmentCard)
//        intent.putExtra(Parameters.TREATMENT, Parcels.wrap(treatmentCard))
        startActivityForResult(intent,ResultCodes.REQUEST_UPDATE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == ResultCodes.REQUEST_UPDATE){
                val treatment = data?.getSerializableExtra(Parameters.TREATMENT) as Animal.TreatmentCard
//                val treatment = Parcels.unwrap<Animal.TreatmentCard>(data?.getParcelableExtra(Parameters.TREATMENT))
                addTreatment(treatment)
            }
        }
    }


}
