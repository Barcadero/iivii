package client.petmooby.com.br.petmooby.actvity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.extensions.showAlert
import client.petmooby.com.br.petmooby.extensions.showLoadingDialog
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.util.*
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_attendance.*
import kotlinx.android.synthetic.main.activity_vaccine.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import java.io.ByteArrayOutputStream
import java.util.*

class AddAttendanceActivity : BaseActivity() {

    private var dateAttendance = Date()
    private var dateOfReturn = Date()
    private var identity     = 0L
    private var adapterValues:ArrayAdapter<Int>? = null
    private var mCurrentPhotoBitmap: Bitmap?=null
    private var selectedVerConsultation:Animal.VetConsultation?=null
    private var imagePath:String?=null

    var action              = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_attendance)
        setupToolbar(R.id.toolbarAttendanceAdd, R.string.addNewAttendance)
        adapterValues = ArrayAdapter<Int>(this, LayoutResourceUtil.getSpinnerDropDown(), listOf(1,2,3,4,5,6,7,8,9,10))
        edtAttendancePrice.addTextChangedListener(CurrencyMaskTextWatch(edtAttendancePrice,this) )
        edtAttendanceDate.setOnClickListener {
            DateTimePickerDialog.showDatePicker(this,it,dateAttendance)
        }
        edtReturnDate.setOnClickListener {
            DateTimePickerDialog.showDatePicker(this,it,dateOfReturn)
        }
        spAttendanceRate.adapter =  adapterValues
        action = intent.getIntExtra(Parameters.ACTION,0)
        when(action){
            ResultCodes.REQUEST_INSERT -> {
                isForUpdate = false
            }
            ResultCodes.REQUEST_UPDATE ->{
                isForUpdate = true
                identity     = intent.getSerializableExtra(Parameters.IDENTITY) as Long
                initFields(identity)
            }
        }
        btnTakeAPictureAttendance.setOnClickListener {
            startsCameraActivityForResult()
        }
        btnUploadAttendance.setOnClickListener {
            showImagePicker()
        }
        btnSeeAttendance.setOnClickListener {
            val intent = Intent(this,PhotoViewActivity::class.java)
            if(mCurrentPhotoBitmap != null){
                intent.putExtra(Parameters.PATH_IMAGE,currentImageFilePath)
            }else {
                if(selectedVerConsultation != null){
                    intent.putExtra(Parameters.URL_IMAGE, selectedVerConsultation?.photo1)
                }
            }
            startActivityForResult(intent,ResultCodes.PHOTO_VIEW_REQUEST)
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
                    ResultCodes.REQUEST_INSERT -> {
                        isForUpdate = false
                        save()
                    }
                    ResultCodes.REQUEST_UPDATE ->{
                        isForUpdate = true
                        save()
                    }
                }
            }
            R.id.menuDelete ->{
                alert(R.string.areYouSureToDeleteThisVaccine,R.string.delete) {
                    yesButton {  deleteVetConsultation() }
                    noButton {it.dismiss()}
                }.show()

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun save(){
        val dialog = showLoadingDialog()
        try {
            getCurrentAttendanceInfo()
            saveAnimal{
                uploadImageAndSaveOrUpdatePet()
                dialog.dismiss()
                toast(getString(R.string.attendanceSaved))
            }
        }catch (e:Exception){
            dialog.dismiss()
            e.printStackTrace()
            showAlert(R.string.errorOnSaveAttendance)

        }
    }

    private fun getCurrentAttendanceInfo(){
        if(!isForUpdate){
            //Create a new attendance
            val attendance  = Animal.VetConsultation()
            val localIdentity = ModelHelperUtil.getNewIdentity()
            this.identity     = localIdentity
            with(attendance){
                this.identity   = localIdentity
            }

            VariablesUtil.gbSelectedAnimal?.vetConsultation?.add(attendance)
            selectedVerConsultation = attendance

        }
        with(VariablesUtil.gbSelectedAnimal?.vetConsultation?.filter { it.identity == identity }!![0]){
            this.date       = dateAttendance
            this.dateReturn = dateOfReturn
            this.descPhoto1 = edtPhotoDescriptionAttendance.text.toString()
            this.descPhoto2 = ""
            this.nameClinic = edtVetAttendanceDescription.text.toString()
            this.nameVet    = edtClinicVeterinary.text.toString()
            this.notes      = edtAttendanceNotes.text.toString()
            price           = edtAttendancePrice.text.toString()
            rank            = spAttendanceRate.selectedItem as Int
        }


    }

    private fun deleteVetConsultation(){
        val dialog = showLoadingDialog()
        if(identity > 0){
            with(VariablesUtil.gbSelectedAnimal?.vetConsultation?.filter { it.identity == identity }!![0]){
                VariablesUtil.gbSelectedAnimal?.vetConsultation?.remove(this)
            }
            //VaccineUtil().cancelEvent(this,vaccine!!)
            saveAnimal{
                uploadImageAndSaveOrUpdatePet()
                dialog.dismiss()
                this.finish()
            }
        }else{
            dialog.dismiss()
        }
    }

    private  fun initFields(identity:Long){
        with(VariablesUtil.gbSelectedAnimal?.vetConsultation?.filter { it.identity == identity }!![0]){
            setFieldsOfTheActivity(this)
        }
    }

    private fun Animal.VetConsultation.setFieldsOfTheActivity(vetCon:Animal.VetConsultation) {
        selectedVerConsultation = vetCon
        edtVetAttendanceDescription.setText(vetCon.nameClinic)
        edtClinicVeterinary.setText(vetCon.nameVet)
        edtAttendanceNotes.setText(vetCon.notes)
        edtAttendancePrice.setText(vetCon.price)
        spAttendanceRate.setSelection(adapterValues?.getPosition(rank)!!)
        dateAttendance = vetCon.date!!
        dateOfReturn = vetCon.dateReturn!!
        edtReturnDate.setText(DateTimeUtil.formatDateTime(vetCon.dateReturn))
        edtAttendanceDate.setText(DateTimeUtil.formatDateTime(vetCon.date))
        if (vetCon.photo1 != null && vetCon.photo1?.isNotBlank()!!) {
            enableBtnSeePhoto(true)
        }
        edtPhotoDescriptionAttendance.setText(vetCon.descPhoto1)
    }

    private fun enableBtnSeePhoto(enable: Boolean) {
        if(enable) {
            btnSeeAttendance.visibility = VISIBLE
        }else{
            btnSeeAttendance.visibility = GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_CANCELED){
            return
        }
        if(requestCode == PICK_IMAGE) {
            mCurrentPhotoBitmap = onResultActivityForGallery(requestCode, resultCode, data,null)
        }else if(requestCode == TAKE_PICTURE){
            mCurrentPhotoBitmap = onResultActivityForCamera(requestCode,resultCode,data,null)
        }else if(requestCode == ResultCodes.PHOTO_VIEW_REQUEST){
            val isDelete = data?.getBooleanExtra(Parameters.DELETE,false)!!
            if(isDelete){
                if(selectedVerConsultation != null) {
                    val ref = storage.child("document/vet_care/${VariablesUtil.gbSelectedAnimal?.id}.jpg")
                    ref.delete()
                            .addOnSuccessListener {
                                with(VariablesUtil.gbSelectedAnimal?.vetConsultation?.filter { it.identity == identity }!![0]){
                                    this.photo1 = ""
                                }

                                saveAnimal {
                                    showAlert(getString(R.string.fileDeleted))
                                    enableBtnSeePhoto(false)
                                }
//                                animalRef.document(VariablesUtil.gbSelectedAnimal?.id!!)
//                                        .update("photo1","")
//                                        .addOnSuccessListener {
//
//                                        }.addOnFailureListener {
//                                            showAlert(getString(R.string.wasNotPossibleToSaveAnimal))
//                                        }

                            }
                            .addOnFailureListener {
                                showAlert(getString(R.string.wasNotPossibleToDeleteFile))
                            }
                }else{
                    if(mCurrentPhotoBitmap != null){
                        mCurrentPhotoBitmap = null
                        enableBtnSeePhoto(false)
                    }
                }
            }
        }
        if(mCurrentPhotoBitmap != null){
            enableBtnSeePhoto(true)
        }
    }

    private fun uploadImageAndSaveOrUpdatePet(){
        //if(mCurrentPhotoPath == null) return
        if(mCurrentPhotoBitmap == null) return
        val dialog      = showLoadingDialog(message = getString(R.string.savingImage))
        //var file        = Uri.fromFile(File(mCurrentPhotoPath))
        val ref   = storage.child("document/vet_care/${VariablesUtil.gbSelectedAnimal?.id}.jpg")
        val baos = ByteArrayOutputStream()
        mCurrentPhotoBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val data = baos.toByteArray()
//        var uploadTask  = ref.putFile(file)
        var uploadTask  = ref.putBytes(data)
        val urlTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation ref.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                //VariablesUtil.gbSelectedAnimal?.photo = downloadUri?.toString()//riversRef.downloadUrl.toString()
                selectedVerConsultation?.photo1 = downloadUri?.toString()
                callFireStoreServiceToUpdateTheAnimal(dialog,false)
                mCurrentPhotoBitmap = null
            } else {
                // Handle failures
                // ...
            }
        }
    }

    private fun callFireStoreServiceToUpdateTheAnimal(dialog: ProgressDialog, uploadImage:Boolean) {
        animalRef.document(VariablesUtil.gbSelectedAnimal?.id!!)
                .set(VariablesUtil.gbSelectedAnimal!!)
                .addOnSuccessListener { documentReference ->
                    dialog.dismiss()
                    if(uploadImage) {
                        uploadImageAndSaveOrUpdatePet()
                    }
                    //returnThePetForShowOnList()
                }.addOnFailureListener {
                    dialog.dismiss()
                }
    }





}
