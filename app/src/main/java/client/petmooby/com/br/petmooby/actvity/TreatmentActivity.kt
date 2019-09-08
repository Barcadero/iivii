package client.petmooby.com.br.petmooby.actvity

import android.app.ProgressDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.extensions.showAlert
import client.petmooby.com.br.petmooby.extensions.showLoadingDialog
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.model.enums.EnumTypeInterval
import client.petmooby.com.br.petmooby.model.enums.EnumTypePeriod
import client.petmooby.com.br.petmooby.model.enums.EnumTypeTreatment
import client.petmooby.com.br.petmooby.util.*
import kotlinx.android.synthetic.main.activity_treatment.*
import org.jetbrains.anko.toast
import java.util.*


class TreatmentActivity : BaseActivity() {

//    var dateInformed         = Date()
    var mDateInitial          = Date()
    var mDateFinal            = Date()
    var currentTreatment: Animal.TreatmentCard?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment)
        setupToolbar(R.id.toolbarTreatment,R.string.treatment)
        initSpinners()

        edtTreatmentDateInitial.setOnClickListener {
            DateTimePickerDialog.showDatePicker(this,it,mDateInitial)
        }

        edtTreatmentDateInitial.setOnFocusChangeListener { view, hasFocus -> if(hasFocus)
            DateTimePickerDialog.showDatePicker(this,view,mDateInitial)
        }
        edtTreatmentDateFinal.setOnClickListener {
            DateTimePickerDialog.showDatePicker(this,it,mDateFinal)
        }

        edtTreatmentDateFinal.setOnFocusChangeListener { view, hasFocus -> if(hasFocus)
            DateTimePickerDialog.showDatePicker(this,view,mDateFinal)
        }

        var treatmentID = intent.getLongExtra(Parameters.TREATMENT,0L)
        if(treatmentID != 0L){
            currentTreatment = try {
                VariablesUtil.gbSelectedAnimal?.treatmentCard!!.filter { it.identity == treatmentID }[0]
            }catch (e:Exception){
                null
            }
            loadFields()
        }
    }

    private fun loadFields() {
        if (currentTreatment != null) {
            if(currentTreatment?.dateFinal != null) {
                mDateFinal = currentTreatment?.dateFinal!!
                edtTreatmentDateFinal.setText(DateTimeUtil.formatDateTime(currentTreatment?.dateFinal,"dd/MM/yyyy"))
            }
            if(currentTreatment?.dateInitial != null){
                mDateInitial = currentTreatment?.dateInitial!!
                edtTreatmentDateInitial.setText(DateTimeUtil.formatDateTime(currentTreatment?.dateInitial,"dd/MM/yyyy"))
            }
            edtTreatmentDesc.setText(currentTreatment?.name)
            swtTreatmentAlarm.isChecked = currentTreatment?.isActive!!
            edtTreatmentNote.setText(currentTreatment?.notes)
            spTreatmentInterval.setSelection((currentTreatment?.timeInterval!! - 1).toInt())
            spTreatmentTypeInterval.setSelection(currentTreatment?.typeInterval?.ordinal!!)
            spTreatmentPeriod.setSelection(currentTreatment?.typePeriod?.ordinal!!)
            spTreatmentType.setSelection(currentTreatment?.typeTreatment?.ordinal!!)


        }
    }

    private fun initSpinners(){
        spTreatmentType.adapter = ArrayAdapter<EnumTypeTreatment>(this, LayoutResourceUtil.getSpinnerDropDown(),EnumTypeTreatment.values())
        spTreatmentType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

        }
        spTreatmentInterval.adapter     = ArrayAdapter<Int>(this,LayoutResourceUtil.getSpinnerDropDown(), listOf(1,2,3,4,5,6,7,8,9,10))
        spTreatmentPeriod.adapter       = ArrayAdapter<EnumTypePeriod>(this,LayoutResourceUtil.getSpinnerDropDown(),EnumTypePeriod.values())
        spTreatmentTypeInterval.adapter = ArrayAdapter<EnumTypeInterval>(this,LayoutResourceUtil.getSpinnerDropDown(), EnumTypeInterval.values())
    }

    private fun validate() : Boolean{
//        if(dateFinal == null){
//            edtTreatmentDateFinal.error = getString(R.string.pleaseGiveADate)
//            edtTreatmentDateFinal.requestFocus()
//            return false
//        }
//        if(dateInformed == null){
//            edtTreatmentDate.error = getString(R.string.pleaseGiveADate)
//            edtTreatmentDate.requestFocus()
//            return false
//        }

        if(spTreatmentType.selectedItem == null){
            showAlert(R.string.pleaseSelectATreatmentType)
            return false
        }
        if(spTreatmentInterval.selectedItem == null){
            showAlert(R.string.pleaseSelectATreatmentInterval)
            return false
        }

        if(spTreatmentPeriod.selectedItem == null){
            showAlert(R.string.pleaseSelectATreatmentPeriod)
        }

        if(edtTreatmentDesc.text.toString().isEmpty()){
            edtTreatmentDesc.error = getString(R.string.pleaseGiveATreatmentName)
            edtTreatmentDesc.requestFocus()
            return false
        }

        return true
    }

    private fun save(){
        var dialog = showLoadingDialog()
        if(!validate())return
        if(currentTreatment == null){
            //Save a treatment
            currentTreatment = Animal.TreatmentCard()
            with(currentTreatment!!){
                dateFinal      = mDateFinal
                dateInitial    = mDateInitial
                name                = edtTreatmentDesc.text.toString()
                identity            = Random().nextInt(1000000000).toLong()
                isActive            = swtTreatmentAlarm.isChecked
                notes               = edtTreatmentNote.text.toString()
                timeInterval        = (spTreatmentInterval.selectedItem as Int).toLong()
                typeInterval        = spTreatmentTypeInterval.selectedItem as EnumTypeInterval
                typePeriod          = spTreatmentPeriod.selectedItem as EnumTypePeriod
                typeTreatment       = spTreatmentType.selectedItem as EnumTypeTreatment
            }
            VariablesUtil.gbSelectedAnimal?.treatmentCard?.add(currentTreatment!!)
            saveAnimal(dialog)
        }else{
            //Update a treatment
            with(VariablesUtil.gbSelectedAnimal?.treatmentCard?.filter { it.identity == currentTreatment?.identity }!![0]){
                dateFinal     = mDateFinal
                dateInitial   = mDateInitial
                name               = edtTreatmentDesc.text.toString()
                isActive           = swtTreatmentAlarm.isChecked
                notes           = edtTreatmentNote.text.toString()
                timeInterval    = (spTreatmentInterval.selectedItem as Int).toLong()
                typeInterval    = spTreatmentTypeInterval.selectedItem as EnumTypeInterval
                typePeriod      = spTreatmentPeriod.selectedItem as EnumTypePeriod
                typeTreatment   = spTreatmentType.selectedItem as EnumTypeTreatment
                saveAnimal(dialog)
            }
        }
    }

    private fun saveAnimal(dialog: ProgressDialog, isDelete : Boolean = false) {
        animalRef.document(VariablesUtil.gbSelectedAnimal?.id!!)
                .set(VariablesUtil.gbSelectedAnimal!!)
                .addOnSuccessListener {
                    if(isDelete){
                        toast(R.string.treatmentDeleted)
                        finish()
                    }else {
                        saveAndSetResult(dialog)
                    }
                }.addOnFailureListener {
                    showAlert(R.string.wasNotPossibleSaveVaccine)
                }
    }

    private fun saveAndSetResult(dialog: ProgressDialog) {
        showAlert(R.string.savedSuccess)
        dialog.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_vaccine,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menuSave ->{
//               if(currentTreatment == null){
//                   save()
//               }else{
//                   save()
//               }
                save()
            }
            R.id.menuDelete ->{

            }
        }
        return super.onOptionsItemSelected(item)
    }
}
