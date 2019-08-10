package client.petmooby.com.br.petmooby.actvity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.adapter.HistoricVaccineAdapter
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.util.DateTimePickerDialog
import client.petmooby.com.br.petmooby.util.Parameters
import client.petmooby.com.br.petmooby.util.ResultCodes
import com.google.firebase.firestore.FieldValue
import kotlinx.android.synthetic.main.activity_vaccine.*
import java.util.*

class VaccineActivity : BaseActivity() {

    var animal:Animal?=null
    var vaccine:Animal.VaccineCards?=null
    var date = Date()
    var action = 0
    var historicTemp = mutableListOf<Animal.Historic>()
    var animalRef = fbReference.collection(CollectionsName.ANIMAL)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccine)
        setupToolbar(R.id.toolbarVaccine, R.string.vaccines)
        edtVaccineDate.setOnClickListener {
            DateTimePickerDialog.showDatePicker(this,edtVaccineDate,date)
        }
        action = intent.getIntExtra(Parameters.ACTION,0)
        when(action){
            ResultCodes.REQUEST_ADD_VACCINE -> {
                //To add a vaccine
                animal = intent.getParcelableExtra(Parameters.ANIMAL_PARAMETER)
            }
            ResultCodes.REQUEST_UPDATE_VACCINE ->{
                //To alter a vaccine
                vaccine = intent.getParcelableExtra(Parameters.VACCINE_CARD)
                //TODO: put on delete event here
                rcViewHistoricVaccine.adapter = HistoricVaccineAdapter(vaccine?.historic!!,{"on delete method"})

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_vaccine,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menuSave ->{
                when(action){
                    ResultCodes.REQUEST_ADD_VACCINE -> {
                        //To add a vaccine
                        saveVaccine()
                    }
                    ResultCodes.REQUEST_UPDATE_VACCINE ->{
                        //To alter a vaccine
                    }
                }
            }
            R.id.menuDelete ->{
                deleteVaccine()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveVaccine(){
        vaccine = Animal.VaccineCards()
        with(vaccine!!){
            identity        = Random().nextInt(1000000000)
            nextRemember    = date
            vaccine_type    = edtVaccineDescription.text.toString()
            historic        = historicTemp
        }
    }

    private fun deleteVaccine(){
        if(vaccine != null){
            val updates = hashMapOf<String,Any>(
                "vaccineCards" to FieldValue.arrayRemove("identity:${vaccine?.identity}")
            )
               animalRef
                       .document(animal?.id!!)
                       .update(updates)

        }
    }



}
