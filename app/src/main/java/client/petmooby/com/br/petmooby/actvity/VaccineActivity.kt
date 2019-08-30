package client.petmooby.com.br.petmooby.actvity

//import org.parceler.Parcels
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.VISIBLE
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.adapter.HistoricVaccineAdapter
import client.petmooby.com.br.petmooby.extensions.*
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.util.*
import com.google.firebase.firestore.FieldValue
import kotlinx.android.synthetic.main.activity_vaccine.*
import kotlinx.android.synthetic.main.empty_view_list_layout.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.okButton
//import org.parceler.Parcels
import java.util.*

class VaccineActivity : BaseActivity() {

//    var animal:Animal?=null
    var vaccine:Animal.VaccineCards?=null
    var date            = Date()
    var dateVaccineApp  = Date()
    var action          = 0
    var animalRef = fbReference.collection(CollectionsName.ANIMAL)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccine)
        setupToolbar(R.id.toolbarVaccine, R.string.vaccines)
        edtVaccineDate.setOnClickListener {
            DateTimePickerDialog.showDatePicker(this,edtVaccineDate,date)
        }
        edtVaccineDate.setOnFocusChangeListener { view, hasFocus -> if(hasFocus)
            DateTimePickerDialog.showDatePicker(this,edtVaccineDate,date)  }

        edtVaccineApplication.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus)
                DateTimePickerDialog.showDatePicker(this,edtVaccineApplication,dateVaccineApp)
        }
        edtVaccineApplication.setOnClickListener {
            DateTimePickerDialog.showDatePicker(this,edtVaccineApplication,dateVaccineApp)
        }
        edtVaccinePrice.addTextChangedListener(CurrencyMaskTextWatch(edtVaccinePrice))
        btnAddVaccine.setOnClickListener {
            addHistory()
        }
        action = intent.getIntExtra(Parameters.ACTION,0)
        when(action){
            ResultCodes.REQUEST_ADD_VACCINE -> {
                //To add a vaccine
//                animal = intent.getParcelableExtra(Parameters.ANIMAL_PARAMETER)
//                animal = intent.getSerializableExtra(Parameters.ANIMAL_PARAMETER) as Animal
//                animal = Parcels.unwrap(intent.getParcelableExtra(Parameters.ANIMAL_PARAMETER))
            }
            ResultCodes.REQUEST_UPDATE_VACCINE ->{
                //To alter a vaccine
//                vaccine = intent.getParcelableExtra(Parameters.VACCINE_CARD)
                vaccine = intent.getSerializableExtra(Parameters.VACCINE_CARD) as Animal.VaccineCards
//                vaccine = Parcels.unwrap(intent.getParcelableExtra(Parameters.VACCINE_CARD))
                edtVaccineDescription.setText(vaccine?.vaccine_type)
                edtVaccineDate.setText(DateTimeUtil.formatDateTime(vaccine?.nextRemember) )
                if(vaccine?.historic != null) {
                    rcViewHistoricVaccine.adapter = HistoricVaccineAdapter(vaccine?.historic!!) { historic -> deleteVaccineHistory(historic)}
                    rcViewHistoricVaccine.layoutManager = getDefaultLayoutManager()
                }else{
                    layoutEmptyList.visibility = VISIBLE
                }

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
        var dialog = showLoadingDialog()
        try {
            validateFields()
            vaccine = Animal.VaccineCards()
            with(vaccine!!) {
                identity = Random().nextInt(1000000000)
                nextRemember = date
                vaccine_type = edtVaccineDescription.text.toString()
                //historic = historicTemp
            }
            if (VariablesUtil.gbSelectedAnimal?.vaccineCards == null) {
                VariablesUtil.gbSelectedAnimal?.vaccineCards = mutableListOf()
            }
            VariablesUtil.gbSelectedAnimal?.vaccineCards?.add(vaccine!!)
            animalRef.document(VariablesUtil.gbSelectedAnimal?.id!!)
                    .set(VariablesUtil.gbSelectedAnimal!!)
                    .addOnSuccessListener {
                        saveAndSetResult(dialog)
                    }.addOnFailureListener {
                        showAlert(R.string.wasNotPossibleSaveVaccine)
                    }
        }catch (e:Exception){
            showAlert(R.string.errorOnSaveVaccine)
            dialog.dismiss()
        }
    }

    private fun saveAndSetResult(dialog: ProgressDialog) {
        showAlert(R.string.savedSuccess)
        val intent = Intent()
//        intent.putExtra(Parameters.ANIMAL_PARAMETER, animal)
//        intent.putExtra(Parameters.ANIMAL_PARAMETER,Parcels.wrap(animal))
        setResult(Activity.RESULT_OK, intent)
        dialog.dismiss()
    }

    private fun deleteVaccine(){
        if(vaccine != null){
            val updates = hashMapOf<String,Any>(
                "vaccineCards" to FieldValue.arrayRemove("identity:${vaccine?.identity}")
            )
               animalRef
                       .document(VariablesUtil.gbSelectedAnimal?.id!!)
                       .update(updates)

        }
    }

    private fun validateFields() : Boolean{
        if(edtVaccineDescription.text.toString().isEmpty()){
            edtVaccineDescription.error = getString(R.string.tellUsTheVaccineDescriptio)
            return false
        }
        if(date == null){
            edtVaccineDate.error = getString(R.string.pleaseGiveAVaccineDate)
            return false
        }


        return true
    }

    private fun validateForApplyVaccine(): Boolean{
        if(edtVaccineClinic.text.toString().isEmpty()){
            edtVaccineClinic.error = getString(R.string.giveTheVaccineClinic)
            return false
        }
        if(dateVaccineApp == null){
            edtVaccineApplication.error = getString(R.string.giveAnApplicationDate)
            return false
        }
        if(edtVaccinePrice.text.toString().isEmpty()){
            edtVaccinePrice.error = getString(R.string.giveAnApplicationPrice)
        }
        return true
    }

    private fun addHistory(){
        if(validateForApplyVaccine()){
            var dialog = showLoadingDialog()
            try {
                var historic = Animal.Historic()
                with(historic) {
                    date = dateVaccineApp
                    observation = edtVaccineNotes.text.toString()
                    value = edtVaccinePrice.text.toString()
                            .replace("$", "")
                            .replace(",", "")
                            .toDouble()
                    veterinary = edtVaccineClinic.text.toString()
                }
                VariablesUtil.gbSelectedAnimal?.vaccineCards!!
                        .filter { it.identity == this.vaccine?.identity }
                        .forEach { it.historic?.add(historic) }
                animalRef.document(VariablesUtil.gbSelectedAnimal?.id!!)
                        .set(VariablesUtil.gbSelectedAnimal!!)
                        .addOnSuccessListener {
                            vaccine?.historic?.add(historic)
                            rcViewHistoricVaccine.adapter?.notifyItemInserted(vaccine?.historic?.lastIndex!!)
                            dialog.dismiss()
                        }.addOnFailureListener {
                    dialog.dismiss()
                    showAlert(R.string.wasNotPossibleSaveVaccine)
                }
            }catch (e:Exception){
                dialog.dismiss()
            }
        }
    }

    fun deleteVaccineHistory(historic: Animal.Historic) {
        alert(getString(R.string.areYouSure), getString(R.string.Advice)){
            okButton{ it.dismiss() }
            noButton { it.dismiss() }
        }.show()

    }

}
