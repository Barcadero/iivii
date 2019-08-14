package client.petmooby.com.br.petmooby.actvity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.adapter.TreatmentListAdapter
import client.petmooby.com.br.petmooby.extensions.defaultRecycleView
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.util.Parameters
import client.petmooby.com.br.petmooby.util.ResultCodes
import kotlinx.android.synthetic.main.activity_treatment_list.*
import org.parceler.Parcels

class TreatmentListActivity : BaseActivity() {

    private var treatments = mutableListOf<Animal.TreatmentCard>()
    private var adapter    = TreatmentListAdapter(treatments,this::onTreatmentClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment_list)
        setupToolbar(R.id.toolbarTreatmentList, R.string.treatments)
        initRcViewList()
//        var animal = intent.getParcelableExtra<Animal>(Parameters.ANIMAL_PARAMETER)
        var animal = Parcels.unwrap<Animal>(intent.getParcelableExtra(Parameters.ANIMAL_PARAMETER))
        for(treatment in animal.treatmentCard!!){
            addTreatment(treatment)
        }
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
        rcViewTreatmentList.adapter = adapter
        val layoutManager = GridLayoutManager(this,2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return if (position == 0) 2 else 1
            }
        }
    }

    private fun addTreatment(treatmentCard: Animal.TreatmentCard){
        treatments.add(treatmentCard)
        adapter.notifyItemInserted(treatments.lastIndex)
    }

    private fun onTreatmentClick(treatmentCard: Animal.TreatmentCard){
        val intent = Intent(this,TreatmentActivity::class.java)
        intent.putExtra(Parameters.ACTION,ResultCodes.REQUEST_UPDATE)
//        intent.putExtra(Parameters.TREATMENT,treatmentCard)
        intent.putExtra(Parameters.TREATMENT,Parcels.wrap(treatmentCard))
        startActivityForResult(intent,ResultCodes.REQUEST_UPDATE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == ResultCodes.REQUEST_UPDATE){
//                val treatment = data?.getParcelableExtra<Animal.TreatmentCard>(Parameters.TREATMENT)
                val treatment = Parcels.unwrap<Animal.TreatmentCard>(data?.getParcelableExtra(Parameters.TREATMENT))
                addTreatment(treatment!!)
            }
        }
    }


}
