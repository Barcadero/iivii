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
import kotlinx.android.synthetic.main.historic_vaccines_adapter.*
import org.jetbrains.anko.toast
//import org.jetbrains.anko.alert
//import org.jetbrains.anko.noButton
//import org.jetbrains.anko.okButton
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
        edtVaccinePrice.addTextChangedListener(CurrencyMaskTextWatch(edtVaccinePrice,this))
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
                    rcViewHistoricVaccine.adapter = HistoricVaccineAdapter(vaccine?.historic!!,{},{historic -> showNotes(historic)  }) //{ /*historic -> deleteVaccineHistory(historic)*/}
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
                        saveVaccine()
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
            getCurrentVaccineInfo()
            saveAnimal(dialog)
        }catch (e:Exception){
            showAlert(R.string.errorOnSaveVaccine)
            dialog.dismiss()
        }
    }

    private fun saveAnimal(dialog: ProgressDialog, isDelete : Boolean = false) {
        animalRef.document(VariablesUtil.gbSelectedAnimal?.id!!)
                .set(VariablesUtil.gbSelectedAnimal!!)
                .addOnSuccessListener {
                    if(isDelete){
                        toast(R.string.vaccineDeleted)
                        finish()
                    }else {
                        saveAndSetResult(dialog)
                    }
                }.addOnFailureListener {
                    showAlert(R.string.wasNotPossibleSaveVaccine)
                }
    }

    private fun getCurrentVaccineInfo() {
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
    }

    private fun saveAndSetResult(dialog: ProgressDialog) {
        showAlert(R.string.savedSuccess)
//        val intent = Intent()
//        intent.putExtra(Parameters.ANIMAL_PARAMETER, animal)
//        intent.putExtra(Parameters.ANIMAL_PARAMETER,Parcels.wrap(animal))
//        setResult(Activity.RESULT_OK, intent)
        dialog.dismiss()
    }

    private fun deleteVaccine(){
        var dialog = showLoadingDialog()
        if(vaccine != null){
            VariablesUtil.gbSelectedAnimal?.vaccineCards?.remove(vaccine!!)
            saveAnimal(dialog, true)
        }else{
            dialog.dismiss()
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
                    value = NumberFormatUtil.currencyToDouble(edtVaccinePrice.text.toString(),this@VaccineActivity)
                    veterinary = edtVaccineClinic.text.toString()
                }
//                for(vaccine in VariablesUtil.gbSelectedAnimal?.vaccineCards!!){
//                    if(vaccine.identity == this.vaccine?.identity){
//                        vaccine.historic?.add(historic)
//                    }
//                }

                if(vaccine == null){
                    getCurrentVaccineInfo()
                }

                VariablesUtil.gbSelectedAnimal?.vaccineCards!!
                        .filter {
                            it.identity == this.vaccine?.identity
                        }
                        .forEach {
                            if(it.historic == null){
                                it.historic = mutableListOf()
                            }
                            it.historic?.add(historic) }

                animalRef.document(VariablesUtil.gbSelectedAnimal?.id!!)
                        .set(VariablesUtil.gbSelectedAnimal!!)
                        .addOnSuccessListener {
                            vaccine?.historic?.add(historic)
                            rcViewHistoricVaccine.adapter?.notifyItemInserted(vaccine?.historic?.lastIndex!!)
                            clearVaccineApply()
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

//    fun deleteVaccineHistory(historic: Animal.Historic) {
//        alert(getString(R.string.areYouSure), getString(R.string.Advice)){
//            okButton{  it.dismiss();deleteVaccineHistoryOnDatabase(historic) }
//            noButton { it.dismiss() }
//        }.show()
//
//    }

//    private fun deleteVaccineHistoryOnDatabase(historic: Animal.Historic){
//        var dialog = showLoadingDialog()
//        VariablesUtil.gbSelectedAnimal.vaccineCards.
//        animalRef.document(VariablesUtil.gbSelectedAnimal?.id!!)
//                .set(VariablesUtil.gbSelectedAnimal!!)
//                .addOnSuccessListener {
//                    vaccine?.historic?.add(historic)
//                    rcViewHistoricVaccine.adapter?.notifyItemInserted(vaccine?.historic?.lastIndex!!)
//                    dialog.dismiss()
//                }.addOnFailureListener {
//                    dialog.dismiss()
//                    showAlert(R.string.wasNotPossibleSaveVaccine)
//                }
//    }

    private fun clearVaccineApply(){
        edtVaccineNotes.text.clear()
        edtVaccinePrice.setText("0.00")
        edtVaccineClinic.text.clear()
        edtVaccineApplication.text.clear()
    }

    private fun showNotes(historic: Animal.Historic){
        showAlert(R.string.notes,historic.observation!!)
    }

}
