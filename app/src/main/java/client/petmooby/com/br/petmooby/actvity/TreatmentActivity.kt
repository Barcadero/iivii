package client.petmooby.com.br.petmooby.actvity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.extensions.showAlert
import client.petmooby.com.br.petmooby.model.enums.EnumTypeInterval
import client.petmooby.com.br.petmooby.model.enums.EnumTypePeriod
import client.petmooby.com.br.petmooby.model.enums.EnumTypeTreatment
import client.petmooby.com.br.petmooby.util.DateTimePickerDialog
import client.petmooby.com.br.petmooby.util.LayoutResourceUtil
import kotlinx.android.synthetic.main.activity_treatment.*
import java.util.*


class TreatmentActivity : BaseActivity() {

    var dateInformed         = Date()
    var dateInitial          = Date()
    var dateFinal            = Date()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment)
        initSpinners()

//        edtTreatmentDate.setOnClickListener {
//            DateTimePickerDialog.showDatePicker(this,edtTreatmentDate,dateInformed)
//        }

        edtTreatmentDateInitial.setOnClickListener {
            DateTimePickerDialog.showDatePicker(this,edtTreatmentDateInitial,dateInitial)
        }

        edtTreatmentDateFinal.setOnClickListener {
            DateTimePickerDialog.showDatePicker(this,edtTreatmentDateFinal,dateFinal)
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
        spTreatmentInterval.adapter = ArrayAdapter<Int>(this,LayoutResourceUtil.getSpinnerDropDown(), listOf(1,2,3,4,5,6,7,8,9,10))
        spTreatmentPeriod.adapter = ArrayAdapter<EnumTypePeriod>(this,LayoutResourceUtil.getSpinnerDropDown(),EnumTypePeriod.values())
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
}
