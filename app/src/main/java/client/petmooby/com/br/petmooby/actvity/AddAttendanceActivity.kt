package client.petmooby.com.br.petmooby.actvity

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
    private val adapterValues = ArrayAdapter<Int>(this, LayoutResourceUtil.getSpinnerDropDown(), listOf(1,2,3,4,5,6,7,8,9,10))
    private var mCurrentPhotoBitmap: Bitmap?=null

    var action              = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_attendance)
        setupToolbar(R.id.toolbarAttendanceAdd, R.string.addNewAttendance)
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
        btnTakeAPictureAttendance.setOnClickListener {
            showImagePicker()
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
                dialog.dismiss()
                this.finish()
            }
        }else{
            dialog.dismiss()
        }
    }

    private  fun initFields(identity:Long){
        with(VariablesUtil.gbSelectedAnimal?.vetConsultation?.filter { it.identity == identity }!![0]){
            edtVetAttendanceDescription.setText(this.nameClinic)
            edtClinicVeterinary.setText(this.nameVet)
            edtAttendanceNotes.setText(this.notes)
            edtAttendancePrice.setText(this.price)
            spAttendanceRate.setSelection(adapterValues.getPosition(rank))
            dateAttendance = this.date!!
            dateOfReturn = this.dateReturn!!
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == PICK_IMAGE) {
            mCurrentPhotoBitmap = onResultActivityForGallery(requestCode, resultCode, data,null)
        }else if(requestCode == TAKE_PICTURE){
            mCurrentPhotoBitmap = onResultActivityForCamera(requestCode,resultCode,data,null)
        }
    }

    private fun uploadImageAndSaveOrUpdatePet(){
        //if(mCurrentPhotoPath == null) return
        if(mCurrentPhotoBitmap == null) return
        val dialog      = showLoadingDialog(message = getString(R.string.savingImage))
        //var file        = Uri.fromFile(File(mCurrentPhotoPath))
        val ref   = storage.child("animal/${VariablesUtil.gbSelectedAnimal?.id}.jpg")
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
                VariablesUtil.gbSelectedAnimal?.photo = downloadUri?.toString()//riversRef.downloadUrl.toString()
                callFireStoreServiceToUpdateTheAnimal(dialog,false)
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
