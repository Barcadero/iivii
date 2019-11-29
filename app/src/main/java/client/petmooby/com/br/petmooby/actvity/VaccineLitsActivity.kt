package client.petmooby.com.br.petmooby.actvity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.UiThread
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.adapter.VaccineAdapter
import client.petmooby.com.br.petmooby.extensions.getDefaulLayoutManager
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.extensions.showAlert
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.util.Parameters
import client.petmooby.com.br.petmooby.util.ResultCodes
import client.petmooby.com.br.petmooby.util.VariablesUtil
import kotlinx.android.synthetic.main.activity_vaccine_lits.*
import kotlinx.android.synthetic.main.empty_view_list_layout.*

class VaccineLitsActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccine_lits)
        setupToolbar(R.id.toolbarVaccineList, R.string.vaccines)
        if(VariablesUtil.gbSelectedAnimal?.vaccineCards == null){
            VariablesUtil.gbSelectedAnimal?.vaccineCards = mutableListOf()
        }
        getPetVaccines()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menuAdd -> {
            if(VariablesUtil.gbSelectedAnimal?.vaccineCards != null && VariablesUtil.gbSelectedAnimal?.vaccineCards!!.size!! > 7){
                showAlert(R.string.youCanOnlyHaveSomeVaccines)
            }else {
                val intent = Intent(this, VaccineActivity::class.java)
                intent.putExtra(Parameters.ACTION, ResultCodes.REQUEST_ADD_VACCINE)
                startActivityForResult(intent, ResultCodes.REQUEST_ADD_VACCINE)
            }
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun getPetVaccines(){
         if(VariablesUtil.gbSelectedAnimal?.vaccineCards != null){
             if(!VariablesUtil.gbSelectedAnimal?.vaccineCards?.isEmpty()!!) {
                 rcViewVaccineList.adapter       = VaccineAdapter(VariablesUtil.gbSelectedAnimal?.vaccineCards!!) { vaccineCards -> onClick(vaccineCards) }
                 rcViewVaccineList.layoutManager = getDefaulLayoutManager()
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
            layoutEmptyList.visibility      = GONE
            rcViewVaccineList.visibility    = VISIBLE
        }else {
            layoutEmptyList.visibility = VISIBLE
            rcViewVaccineList.visibility = GONE
        }
    }

    private fun onClick(vaccineCards: Animal.VaccineCards){
        var intent = Intent(this,VaccineActivity::class.java)
        intent.putExtra(Parameters.ACTION,ResultCodes.REQUEST_UPDATE_VACCINE)
        intent.putExtra(Parameters.VACCINE_CARD,vaccineCards)
//        intent.putExtra(Parameters.VACCINE_CARD, Parcels.wrap(vaccineCards))
        startActivityForResult(intent, ResultCodes.REQUEST_UPDATE_VACCINE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(rcViewVaccineList.adapter == null){
            getPetVaccines()
        }else {
            rcViewVaccineList.adapter?.notifyDataSetChanged()
        }
    }
}
