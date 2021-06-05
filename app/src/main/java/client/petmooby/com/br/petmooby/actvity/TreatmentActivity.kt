package client.petmooby.com.br.petmooby.actvity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.UiThread
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.databinding.ActivityTreatmentBinding
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.extensions.showAlert
import client.petmooby.com.br.petmooby.extensions.showLoadingDialog
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.model.enums.EnumTypeInterval
import client.petmooby.com.br.petmooby.model.enums.EnumTypePeriod
import client.petmooby.com.br.petmooby.model.enums.EnumTypeTreatment
import client.petmooby.com.br.petmooby.util.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import java.util.*


class TreatmentActivity : BaseActivity() {

    private lateinit var binding: ActivityTreatmentBinding
    var mDateInitial:Date?          = Date()
    var mDateFinal:Date?            = Date()
    var currentTreatment: Animal.TreatmentCard?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTreatmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar(R.id.toolbarTreatment,R.string.treatment)
        initSpinners()

        binding.edtTreatmentDateInitial.setOnClickListener {
            DateTimePickerDialog.showDatePicker(this,it,mDateInitial!!)
        }

        binding.edtTreatmentDateInitial.setOnFocusChangeListener { view, hasFocus -> if(hasFocus)
            DateTimePickerDialog.showDatePicker(this,view,mDateInitial!!)
        }
        binding.edtTreatmentDateFinal.setOnClickListener {
            DateTimePickerDialog.showDatePicker(this,it,mDateFinal!!)
        }

        binding.edtTreatmentDateFinal.setOnFocusChangeListener { view, hasFocus -> if(hasFocus)
            DateTimePickerDialog.showDatePicker(this,view,mDateFinal!!)
        }

        val treatmentID = intent.getLongExtra(Parameters.TREATMENT,0L)
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
                binding.edtTreatmentDateFinal.setText(DateTimeUtil.formatDateTime(currentTreatment?.dateFinal,"dd/MM/yyyy"))
            }
            if(currentTreatment?.dateInitial != null){
                mDateInitial = currentTreatment?.dateInitial!!
                binding.edtTreatmentDateInitial.setText(DateTimeUtil.formatDateTime(currentTreatment?.dateInitial,"dd/MM/yyyy"))
            }
            binding.edtTreatmentDesc.setText(currentTreatment?.name)
            binding.swtTreatmentAlarm.isChecked = currentTreatment?.isIsActive!!
            binding.edtTreatmentNote.setText(currentTreatment?.notes)
            //spTreatmentInterval.setSelection((currentTreatment?.timeInterval!! - 1).toInt())
            binding.spTreatmentTypeInterval.setSelection(currentTreatment?.typeInterval?.ordinal!!)
            binding.spTreatmentPeriod.setSelection(currentTreatment?.typePeriod?.ordinal!!)
            binding.spTreatmentType.setSelection(currentTreatment?.typeTreatment?.ordinal!!)
            binding.spTreatmentType.isEnabled = false
            binding.edtTreatmentInterval.setText(currentTreatment?.timeInterval!!.toString())

        }
    }

    private fun initSpinners(){
        binding.spTreatmentType.adapter = ArrayAdapter<EnumTypeTreatment>(this, LayoutResourceUtil.getSpinnerDropDown(),EnumTypeTreatment.values())
        binding.spTreatmentPeriod.adapter       = ArrayAdapter<EnumTypePeriod>(this,LayoutResourceUtil.getSpinnerDropDown(),EnumTypePeriod.values())
        binding.spTreatmentPeriod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //NOTE do nothing
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    EnumTypePeriod.ALWAYS.ordinal ->{
                        //Only need to inform the initial date
                        setFinalDateVisibility(false)
                    }
                    EnumTypePeriod.INFORMED.ordinal ->{
                        setFinalDateVisibility(true)
                    }
                }
            }

        }
        binding.spTreatmentTypeInterval.adapter = ArrayAdapter<EnumTypeInterval>(this,LayoutResourceUtil.getSpinnerDropDown(), EnumTypeInterval.values())

    }

    private fun validate() : Boolean{
        if(binding.spTreatmentType.selectedItem == null){
            showAlert(R.string.pleaseSelectATreatmentType)
            return false
        }
        if(binding.edtTreatmentInterval.text.toString().isEmpty()){
            showAlert(R.string.pleaseSelectATreatmentInterval)
            return false
        }else{
            val interval = binding.edtTreatmentInterval.text.toString().toLong()
            if(interval <= 0){
                showAlert(R.string.pleaseIntervalShouldGreatThenZero)
            }
        }

        if(binding.spTreatmentPeriod.selectedItem == null){
            showAlert(R.string.pleaseSelectATreatmentPeriod)
        }

        if(binding.edtTreatmentDesc.text.toString().isEmpty()){
            binding.edtTreatmentDesc.error = getString(R.string.pleaseGiveATreatmentName)
            binding.edtTreatmentDesc.requestFocus()
            return false
        }

        when(binding.spTreatmentPeriod.selectedItem as EnumTypePeriod){
            EnumTypePeriod.INFORMED ->{
                if(!mDateFinal!!.after(mDateInitial)){
                    showAlert(R.string.pleaseFinalDateShoulBeGreateThanInitial)
                }
            }
            EnumTypePeriod.ALWAYS ->{
                mDateFinal = null

            }
        }

        return true
    }

    private fun save(){
        val dialog = showLoadingDialog()
        if(!validate())return
        if(currentTreatment == null){
            //Save a treatment
            isForUpdate = false
            currentTreatment = Animal.TreatmentCard()
            with(currentTreatment!!){
                dateFinal      = mDateFinal
                dateInitial    = mDateInitial
                name                = binding.edtTreatmentDesc.text.toString()
                identity            = Random().nextInt(1000000000).toLong()
                isIsActive          = binding.swtTreatmentAlarm.isChecked
                notes               = binding.edtTreatmentNote.text.toString()
                timeInterval        = binding.edtTreatmentInterval.text.toString().toLong()
                typeInterval        = binding.spTreatmentTypeInterval.selectedItem as EnumTypeInterval
                typePeriod          = binding.spTreatmentPeriod.selectedItem as EnumTypePeriod
                typeTreatment       = binding.spTreatmentType.selectedItem as EnumTypeTreatment
            }
            VariablesUtil.gbSelectedAnimal?.treatmentCard?.add(currentTreatment!!)
            saveAnimal(dialog)
        }else{
            //Update a treatment
            isForUpdate = true
            with(VariablesUtil.gbSelectedAnimal?.treatmentCard?.filter { it.identity == currentTreatment?.identity }!![0]){
                dateFinal     = mDateFinal
                dateInitial   = mDateInitial
                name          = binding.edtTreatmentDesc.text.toString()
                isIsActive    = binding.swtTreatmentAlarm.isChecked
                notes         = binding.edtTreatmentNote.text.toString()
                timeInterval  = binding.edtTreatmentInterval.text.toString().toLong()
                typeInterval  = binding.spTreatmentTypeInterval.selectedItem as EnumTypeInterval
                typePeriod    = binding.spTreatmentPeriod.selectedItem as EnumTypePeriod
                typeTreatment = binding.spTreatmentType.selectedItem as EnumTypeTreatment
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
                        val intent = Intent()
                        //intent.putExtra()
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }else {
                        saveAndSetResult(dialog)
                        binding.spTreatmentType.isEnabled = false
                        val animal = VariablesUtil.gbSelectedAnimal
                        if(animal != null){
                            if(isForUpdate){
                                if(currentTreatment?.isIsActive!!){
                                    TreatmentUtil.generateTreatmentAlarm(this, animal.name!!, currentTreatment!!,isForUpdate)
                                }else{
                                    TreatmentUtil.cancelEvent(this,currentTreatment!!)
                                }
                            }else {
                                TreatmentUtil.generateTreatmentAlarm(this, animal.name!!, currentTreatment!!,false)
                            }
                        }

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuSave ->{
                save()
            }
            R.id.menuDelete ->{
                alert(R.string.areYouSureToDeleteThisTreatment,R.string.delete) {
                    yesButton {  delete() }
                    noButton {it.dismiss()}
                }.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun delete(){
        var dialog = showLoadingDialog()
        with(VariablesUtil.gbSelectedAnimal?.treatmentCard?.filter { it.identity == currentTreatment?.identity }!![0]){
            VariablesUtil.gbSelectedAnimal?.treatmentCard?.remove(this)
            TreatmentUtil.cancelEvent(this@TreatmentActivity,this)
        }
        saveAnimal(dialog,true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_OK)
        finish()
    }
    @UiThread
    fun setFinalDateVisibility(isVisible: Boolean){
        if(isVisible){
            binding.edtTreatmentDateFinal.isEnabled     = true
            binding.edtTreatmentDateFinal.visibility    = VISIBLE
            binding.ivTreatment6.visibility             = VISIBLE
        }else {
            binding.edtTreatmentDateFinal.isEnabled     = false
            binding.edtTreatmentDateFinal.visibility    = INVISIBLE
            binding.ivTreatment6.visibility             = INVISIBLE
        }
    }
}
