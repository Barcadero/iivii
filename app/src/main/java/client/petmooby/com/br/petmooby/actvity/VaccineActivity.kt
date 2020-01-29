package client.petmooby.com.br.petmooby.actvity


import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.UiThread
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.adapter.HistoricVaccineAdapter
import client.petmooby.com.br.petmooby.extensions.*
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.util.*
import kotlinx.android.synthetic.main.activity_vaccine.*
import kotlinx.android.synthetic.main.empty_view_list_layout.*
import org.jetbrains.anko.*
import java.util.*

class VaccineActivity : BaseActivity() {

    var vaccine:Animal.VaccineCards?=null
    var date                = Date()
    var dateVaccineApp      = Date()
    var action              = 0
    var historyLastIndex    = 0
    var isForUpdate         = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccine)
        setupToolbar(R.id.toolbarVaccine, R.string.vaccines)
        edtVaccineDate.setOnClickListener {
            DateTimePickerDialog.showDatePicker(this,edtVaccineDate,date)
        }
        edtVaccineDate.setOnFocusChangeListener { view, hasFocus -> if(hasFocus)
            DateTimePickerDialog.showDatePicker(this,view,date)  }

        edtVaccineApplication.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus)
                DateTimePickerDialog.showDatePicker(this,view,dateVaccineApp)
        }
        edtVaccineApplication.setOnClickListener {
            DateTimePickerDialog.showDatePicker(this,it,dateVaccineApp)
        }
        edtVaccinePrice.addTextChangedListener(CurrencyMaskTextWatch(edtVaccinePrice,this))
        btnAddVaccine.setOnClickListener {
            addHistory()
        }
        action = intent.getIntExtra(Parameters.ACTION,0)
        when(action){
            ResultCodes.REQUEST_ADD_VACCINE -> {
                isForUpdate = false
                initHistoricAdapter()
            }
            ResultCodes.REQUEST_UPDATE_VACCINE ->{
                isForUpdate = true
                vaccine     = intent.getSerializableExtra(Parameters.VACCINE_CARD) as Animal.VaccineCards
                edtVaccineDescription.setText(vaccine?.vaccine_type)
                edtVaccineDate.setText(DateTimeUtil.formatDateTime(vaccine?.nextRemember) )
                initHistoricAdapter()
//                if(vaccine?.historic != null) {
//                    initHistoricAdapter(vaccine)
//                }else{
//                    initHistoricAdapter(vaccine)
//                    layoutEmptyList.visibility = VISIBLE
//                }

            }
        }
    }

    private fun initHistoricAdapter() {
        var hasVaccine = false
        var hasHistoric = false
       VariablesUtil.gbSelectedAnimal?.vaccineCards?.filter {
            vaccine?.identity == it.identity
        }!!.forEach {
            hasVaccine = true
            if (it.historic == null) {
                it.historic = mutableListOf()
            }else{
                if(it.historic?.isNotEmpty()!!) {
                    hasHistoric = true
                }
            }
            setHistoryAdapterLayout(it)
        }

        if(hasVaccine){
            if(hasHistoric){
                llVaccineHistoricList.visibility = VISIBLE
            }else{
                llVaccineHistoricList.visibility = GONE
            }
        }else{
            llVaccineHistoricList.visibility = GONE
        }
    }
    @UiThread
    private fun setHistoryAdapterLayout(vaccineCard: Animal.VaccineCards) {
        if(vaccineCard.historic == null){
            vaccineCard.historic = mutableListOf()
        }
        rcViewHistoricVaccine.adapter = HistoricVaccineAdapter(vaccineCard.historic!!, {historic -> onClickDeleteHistory(historic)}, { historic -> showNotes(historic) })
        rcViewHistoricVaccine.layoutManager = getDefaultLayoutManager()
        if(vaccineCard.historic!!.size > 0){
//            rcViewHistoricVaccine.visibility = VISIBLE
//            layoutEmptyList.visibility = GONE
            llVaccineHistoricList.visibility = VISIBLE
        }else{
//            layoutEmptyList.visibility = VISIBLE
//            rcViewHistoricVaccine.visibility = GONE
            llVaccineHistoricList.visibility = GONE
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
                        saveVaccine()
                    }
                    ResultCodes.REQUEST_UPDATE_VACCINE ->{
                        saveVaccine()
                    }
                }
            }
            R.id.menuDelete ->{
                alert(R.string.areYouSureToDeleteThisVaccine,R.string.delete) {
                    yesButton {  deleteVaccine() }
                    noButton {it.dismiss()}
                }.show()

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveVaccine(){
        val dialog = showLoadingDialog()
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

    private fun saveAnimal(dialog: ProgressDialog, onSuccess: () -> Unit) {
        animalRef.document(VariablesUtil.gbSelectedAnimal?.id!!)
                .set(VariablesUtil.gbSelectedAnimal!!)
                .addOnSuccessListener {
                    dialog.dismiss()
                   onSuccess
                }.addOnFailureListener {
                    dialog.dismiss()
                    showAlert(R.string.wasNotPossibleSaveVaccine)
                }
    }

    private fun getCurrentVaccineInfo() {
        validateFields()
        if(!isForUpdate) {
            vaccine = Animal.VaccineCards()
            with(vaccine!!) {
                identity = Random().nextInt(1000000000)
                nextRemember = date
                vaccine_type = edtVaccineDescription.text.toString()
            }
        }else{
            with(VariablesUtil
                    .gbSelectedAnimal?.vaccineCards
                    ?.filter { vaccine?.identity == it.identity }!!
                    [0]){
                nextRemember = date
                vaccine_type = edtVaccineDescription.text.toString()
            }
        }

        if (VariablesUtil.gbSelectedAnimal?.vaccineCards == null) {
            VariablesUtil.gbSelectedAnimal?.vaccineCards = mutableListOf()
        }
        if(!isForUpdate) {
            VariablesUtil.gbSelectedAnimal?.vaccineCards?.add(vaccine!!)
        }
    }

    private fun saveAndSetResult(dialog: ProgressDialog) {
        VaccineUtil().scheduleEvent(this,vaccine!!,VariablesUtil.gbSelectedAnimal?.name!!,isForUpdate)
        showAlert(R.string.savedSuccess)
        dialog.dismiss()
    }

    private fun deleteVaccine(){
        val dialog = showLoadingDialog()
        if(vaccine != null){
            with(VariablesUtil.gbSelectedAnimal?.vaccineCards?.filter { it.identity == vaccine?.identity }!![0]){
                VariablesUtil.gbSelectedAnimal?.vaccineCards?.remove(this)
            }
            VaccineUtil().cancelEvent(this,vaccine!!)
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
            val dialog = showLoadingDialog()
            try {
                val historic = Animal.Historic()
                with(historic) {
                    date = dateVaccineApp
                    observation = edtVaccineNotes.text.toString()
                    value = NumberFormatUtil.currencyToDouble(edtVaccinePrice.text.toString(),this@VaccineActivity)
                    veterinary = edtVaccineClinic.text.toString()
                }
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
                            it.historic?.add(historic)
                            historyLastIndex = it.historic!!.size - 1
                        }

                animalRef.document(VariablesUtil.gbSelectedAnimal?.id!!)
                        .set(VariablesUtil.gbSelectedAnimal!!)
                        .addOnSuccessListener {
                            //vaccine?.historic?.add(historic)
                            notifyDataChange(false)//notifyItemInserted(vaccine?.historic?.lastIndex!!)
                            clearVaccineApply()
                            dialog.dismiss()
                            llVaccineHistoricList.visibility = VISIBLE
                            hideKeyboard()
                        }.addOnFailureListener {
                    dialog.dismiss()
                    showAlert(R.string.wasNotPossibleSaveVaccine)
                }
            }catch (e:Exception){
                dialog.dismiss()
                e.printStackTrace()
                LogUtil.logDebug(e.message!!)
            }
        }
    }

    @UiThread
    fun notifyDataChange(isDelete: Boolean) {
        if(rcViewHistoricVaccine.adapter == null){
            initHistoricAdapter()
        }
        if(isDelete) {
            rcViewHistoricVaccine.adapter?.notifyDataSetChanged()
        }else{
            rcViewHistoricVaccine.adapter?.notifyItemInserted(historyLastIndex)
        }
    }


    private fun clearVaccineApply(){
        edtVaccineNotes.text.clear()
        edtVaccinePrice.setText("0.00")
        edtVaccineClinic.text.clear()
        edtVaccineApplication.text.clear()
    }

    private fun showNotes(historic: Animal.Historic){
        showAlert(R.string.notes,historic.observation!!)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun onClickDeleteHistory(historic: Animal.Historic){
        alert(R.string.areYouSureDeleteHistoric,R.string.Advice){
            yesButton { deleteHistoric(historic) }
            noButton { it.dismiss() }
            onCancelled { it.dismiss() }
        }.show()
    }

    private fun deleteHistoric(historic: Animal.Historic){
        val dialog = showLoadingDialog()
        if(vaccine != null){
            with(VariablesUtil.gbSelectedAnimal?.vaccineCards?.filter { it.identity == vaccine?.identity }!![0]){
//                if(this.historic?.remove(historic)!!){
//                    LogUtil.logDebug("Deleted history")
//                }
                this.historic?.forEach {
                    if(it.date?.equals(historic.date)!! && it.value == historic.value
                            && it.veterinary == historic.veterinary){
                        this.historic?.remove(it)
                    }
                }
//                if(vaccine != null) {
//                    vaccine!!.historic?.remove(historic)
//                }
            }
            saveAnimal(dialog) {notifyHistoricDataDelete()}
        }else{
            dialog.dismiss()
        }
    }

    private fun notifyHistoricDataDelete(){
        notifyDataChange(true)
        toast(R.string.hitoricWasRemoved)
    }

}
