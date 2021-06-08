package client.petmooby.com.br.petmooby.actvity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.annotation.UiThread
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.databinding.ActivityAddNewPetBinding
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.extensions.showLoadingDialog
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.model.comparator.EnumBreedComparator
import client.petmooby.com.br.petmooby.model.enums.*
import client.petmooby.com.br.petmooby.ui.viewmodel.AddNewPetViewModel
import client.petmooby.com.br.petmooby.util.*
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.android.synthetic.main.activity_add_new_pet.*
import org.jetbrains.anko.alert
import java.io.ByteArrayOutputStream
import java.util.Date

/**
 * Migrate
 * https://developer.android.com/topic/libraries/view-binding/migration
 * using in Fragments --> https://developer.android.com/topic/libraries/view-binding#fragments
 */
@AndroidEntryPoint
class AddNewPetActivity : BaseActivity() {


    var bithDate            = Date()
//    var mRef                = FirebaseFirestore.getInstance()
    var enumSelectedBreed: EnumBreedBase?   = null
    private var mCurrentPhotoBitmap: Bitmap?= null
    var fromOtherScreen         = false
    private val addPetViewModel : AddNewPetViewModel by viewModels()
    private lateinit var binding: ActivityAddNewPetBinding
//    private var enableButtons   = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewPetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.edtNewPetBirthday.addTextChangedListener(DateMaskTextWatcher(binding.edtNewPetBirthday))
        setupArrowBackAction()
        initSpinners()
        initButtonsActions()
        initObservers()
        enableControlsButtons(isForUpdate)
            getAnimalSentByOtherView()
        }

    private fun setupArrowBackAction() {
        isForUpdate = intent.getBooleanExtra(Parameters.IS_FOR_UPDATE, false)
        if (isForUpdate) {
            setupToolbar(R.id.toolbarNewPet, VariablesUtil.gbSelectedAnimal?.name, true, View.OnClickListener {
                returnThePetForShowOnList()
            })
        } else {
            setupToolbar(R.id.toolbarNewPet, getString(R.string.addPet), true, View.OnClickListener {
                returnThePetForShowOnList()
            })
        }
    }

    private fun initButtonsActions() {
        binding.btnNewPetCalender.setOnClickListener {
            DateTimePickerDialog.showDatePicker(this,binding.edtNewPetBirthday,bithDate)
        }
        binding.ivProfileMyPet.setOnClickListener {
            showImagePicker()
        }
        binding.btnNewPetGallery.setOnClickListener {
            showImagePicker()
        }
        binding.btnNewPetCapture.setOnClickListener {
            startsCameraActivityForResult()
        }

        binding.btnExcludeNewPet.setOnClickListener {
            remove()
        }
        binding.btnAddVaccine.setOnClickListener {
            val intent = Intent(this, VaccineLitsActivity::class.java)
            startActivity(intent)
        }

        binding.btnAddMedicinalTreatment.setOnClickListener {
            val intent = Intent(this, TreatmentListActivity::class.java)
            startActivity(intent)
        }

        binding.btnAddAttendance.setOnClickListener {
            startActivity(Intent(this, AttendanceListActivity::class.java))
        }
    }

    @UiThread
    fun enableControlsButtons(enable: Boolean) {
        binding.btnAddVaccine.isEnabled     = enable
        binding.btnAddMedicinalTreatment.isEnabled = enable
    }

    private fun getAnimalSentByOtherView() {
        isForUpdate = intent.getBooleanExtra(Parameters.IS_FOR_UPDATE, false)
        if (isForUpdate) {
//            animal= intent.getParcelableExtra(Parameters.ANIMAL_PARAMETER) ?: return
//            animal= intent.getSerializableExtra(Parameters.ANIMAL_PARAMETER) as Animal
//            animal= Parcels.unwrap(intent.getParcelableExtra(Parameters.ANIMAL_PARAMETER))
//            animal?.user  = mRef.document(animal?.userPath!!)
            binding.edtNewPetName.setText(VariablesUtil.gbSelectedAnimal?.name)
            binding.edtNewPetBirthday.setText(DateTimeUtil.formatDateTime(VariablesUtil.gbSelectedAnimal?.dateOfBirthday))
            binding.spNewPetGender.setSelection(EnumGender.valueOf(VariablesUtil.gbSelectedAnimal?.gender!!).ordinal)
            if(VariablesUtil.gbSelectedAnimal?.type != null) {
                binding.spNewPetKindAnimal.setSelection(EnumTypeAnimal.valueOf(VariablesUtil.gbSelectedAnimal?.type?.name!!).ordinal)
                binding.spNewPetKindAnimal.isEnabled = false
                fromOtherScreen = true
            }
            if(VariablesUtil.gbSelectedAnimal?.photo != null) {
                loadProfilePicture()
            }
            bithDate = VariablesUtil.gbSelectedAnimal?.dateOfBirthday!!

        }
    }

    private fun loadProfilePicture() {
        val imgPath = VariablesUtil.gbSelectedAnimal?.photo
        if(imgPath?.isEmpty()!!){
            binding.ivProfileMyPet.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.no_image))
            binding.ivProfileMyPet.visibility         = VISIBLE
            return
        }
        binding.progressAddPet.visibility = VISIBLE
        Picasso.with(this)
                .load(VariablesUtil.gbSelectedAnimal?.photo)
                .error(R.drawable.no_image)
                //.placeholder(R.drawable.icons8_identidade_de_cachorro_90)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .memoryPolicy(MemoryPolicy.NO_STORE)
                .fit()
                .into(binding.ivProfileMyPet, object : Callback {
                    override fun onSuccess() {
                        binding.progressAddPet.visibility = GONE
                        binding.ivProfileMyPet.visibility = VISIBLE
                    }

                    override fun onError() {
                        binding.progressAddPet.visibility = View.GONE
                        binding.ivProfileMyPet.visibility = VISIBLE
                    }

                })
    }

    private fun enableSpinnerBreed(enable:Boolean){
        if(enable) {
            binding.spNewPetBreed.visibility = VISIBLE
            binding.btnNewPetBreed.visibility = GONE
        }else{
            binding.spNewPetBreed.visibility = GONE
            binding.btnNewPetBreed.visibility = VISIBLE
        }
    }

    private fun initSpinners() {
        binding.spNewPetKindAnimal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        //NOTHING SELECTED
                        binding.spNewPetBreed.isEnabled = false
                        enumSelectedBreed = null
                        enableSpinnerBreed(false)

                    }
                    1 -> {
                        //DOGS
                        enableSpinnerBreed(true)
                        binding.spNewPetBreed.isEnabled = true
                        val adapterValue = ArrayAdapter<EnumBreedsForDogs>(view?.context!!, LayoutResourceUtil.getSpinnerDropDown(), EnumBreedsForDogs.values())
                        adapterValue.sort(EnumBreedComparator())
                        binding.spNewPetBreed.adapter = adapterValue
                        if(VariablesUtil.gbSelectedAnimal?.breed != null){
                            val position = adapterValue.getPosition(BreedNamesResolverUtil.getByValueForDogs(VariablesUtil.gbSelectedAnimal?.breed!!))
                            binding.spNewPetBreed.setSelection(position)
                        }

                    }
                    2 -> {
                        //CATS
                        enableSpinnerBreed(true)
                        binding.spNewPetBreed.isEnabled = true
                        val adapterValue = ArrayAdapter<EnumBreedsForCats>(view?.context!!, LayoutResourceUtil.getSpinnerDropDown(), EnumBreedsForCats.values())
                        adapterValue.sort(EnumBreedComparator())
                        binding.spNewPetBreed.adapter = adapterValue
                        if(VariablesUtil.gbSelectedAnimal?.breed != null){
                            val position = adapterValue.getPosition(BreedNamesResolverUtil.getByValueForCats(VariablesUtil.gbSelectedAnimal?.breed!!))
                            binding.spNewPetBreed.setSelection(position)
                        }
                    }
                    3 -> {
                        //Birds
                        enableSpinnerBreed(true)
                        binding.spNewPetBreed.isEnabled = true
                        val adapterValue = ArrayAdapter<EnumBreedsForBirds>(view?.context!!, LayoutResourceUtil.getSpinnerDropDown(), EnumBreedsForBirds.values())
                        adapterValue.sort(EnumBreedComparator())
                        binding.spNewPetBreed.adapter = adapterValue
                        if(VariablesUtil.gbSelectedAnimal?.breed != null){
                            val position = adapterValue.getPosition(BreedNamesResolverUtil.getByValueForBirds(VariablesUtil.gbSelectedAnimal?.breed!!))
                            binding.spNewPetBreed.setSelection(position)
                        }
                    }
                }
            }

        }
        binding.spNewPetBreed.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (binding.spNewPetKindAnimal.selectedItemPosition) {
                    0 -> {
                        //NOTHING SELECTED
                        enumSelectedBreed = null
                    }
                    1 -> {
                        //DOGS
                        //enumSelectedBreed = EnumBreedsForDogs.values()[position]
                        //enumSelectedBreed = selectedI
                        Log.d("ADD_NEW_PET",enumSelectedBreed.toString())
                        Log.d("ID",id.toString())
                        Log.d("SELECTED ID",parent?.selectedItemId!!.toString())
                        enumSelectedBreed =  binding.spNewPetBreed.selectedItem as EnumBreedsForDogs
                        Log.d("SELECTED ID",enumSelectedBreed.toString())

                    }
                    2 -> {
                        //CATS
                        enumSelectedBreed = EnumBreedsForCats.values()[position]
                    }
                    3 -> {
                        //BIRDS
                        enumSelectedBreed = EnumBreedsForBirds.values()[position]

                    }
                }
            }

        }
        binding.spNewPetGender.adapter      = ArrayAdapter<EnumGender>(this,LayoutResourceUtil.getSpinnerDropDown(),EnumGender.values())
        binding.spNewPetKindAnimal.adapter  = ArrayAdapter<EnumTypeAnimal>(this,LayoutResourceUtil.getSpinnerDropDown(),EnumTypeAnimal.values())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menuSave -> {
            savePet()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun savePet(){
        val dialog = showLoadingDialog()
        if(!isForUpdate) {
            VariablesUtil.gbSelectedAnimal = Animal()
        }
        enumSelectedBreed = binding.spNewPetBreed.selectedItem as EnumBreedBase
        with(VariablesUtil.gbSelectedAnimal!!){
            name            = binding.edtNewPetName.text.toString()
            dateOfBirthday  = bithDate
            breed           = enumSelectedBreed?.getValue(enumSelectedBreed!!.getIndex(enumSelectedBreed!!))
            type            = EnumTypeAnimal.values()[binding.spNewPetKindAnimal.selectedItemPosition]
            gender          = EnumGender.values()[binding.spNewPetGender.selectedItemPosition].value
//            user            = FireStoreReference.docRefUser
        }
        if(validateFields(VariablesUtil.gbSelectedAnimal!!)) {
            if(!isForUpdate) {
                callFireStoreServiceToSaveTheAnimal( dialog)
            }else{
                callFireStoreServiceToUpdateTheAnimal(dialog,true)
            }
        }
    }

    private fun callFireStoreServiceToSaveTheAnimal(dialog: ProgressDialog) {
        animalRef
                .add(VariablesUtil.gbSelectedAnimal!!)
                .addOnSuccessListener { documentReference ->
                    dialog.dismiss()
                    successSaved(documentReference)
                }.addOnFailureListener {
                    exception -> dialog.dismiss()
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
                }.addOnFailureListener {
                    dialog.dismiss()
                }
    }

    private fun successSaved(documentReference: DocumentReference){
        VariablesUtil.gbSelectedAnimal?.id = documentReference.id
        isForUpdate = true
        enableControlsButtons(true)
        VariablesUtil.addAnimal(VariablesUtil.gbSelectedAnimal!!)
        addPetViewModel.insertAnimal(VariablesUtil.gbSelectedAnimal!!)
        FireStoreReference.saveAnimalReference(documentReference)
        if(mCurrentPhotoBitmap != null) {
            uploadImageAndSaveOrUpdatePet()
        }
    }

    private fun returnThePetForShowOnList() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun validateFields(animal: Animal): Boolean{
        with(animal) {
            return when {
                breed.isNullOrEmpty() -> {
//                    showAlert(R.string.animalBreedIsMissing)
                    false
                }
                dateOfBirthday == null -> {
//                    showAlert(R.id.bithDayIsMissing)
                    false
                }
                type == null -> {
//                    showAlert(R.id.animalTypeIsMissing)
                    false
                }
                gender.isNullOrEmpty() -> {
//                    showAlert(R.id.animalGenderIsMissing)
                    false
                }
//                user == null -> {
////                    showAlert(R.id.animalGenderIsMissing)
//                    false
//                }
                else -> true
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE) {
            mCurrentPhotoBitmap = onResultActivityForGallery(requestCode, resultCode, data,binding.ivProfileMyPet)
        }else if(requestCode == TAKE_PICTURE){
            mCurrentPhotoBitmap = onResultActivityForCamera(requestCode,resultCode,data,binding.ivProfileMyPet)
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

    private fun remove(){
        alert(R.string.areYouSure,R.string.removingPet){
            positiveButton(R.string.yes) {
                animalRef.document(VariablesUtil.gbSelectedAnimal?.id!!)
                        .delete()
                        .addOnFailureListener {  }
                        .addOnSuccessListener {
                            val intent = Intent()
                            setResult(ResultCodes.RESULT_FOR_DELETE,intent)
                            removeEvents(VariablesUtil.gbSelectedAnimal!!)
                            finish()
                        }
            }

            negativeButton(R.string.no) {
                it.dismiss()
            }
        }.show()

    }

    private fun removeEvents(animal: Animal){
        VaccineUtil().cancelEventVaccinesForAAnimal(this,animal)
        TreatmentUtil.cancelEventTreatmentForAAnimal(this,animal)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        returnThePetForShowOnList()
    }

    private fun initObservers(){
        addPetViewModel.insertAnimalData.observe(this, Observer {
            if(it){
                LogUtil.logDebug("OK animal was inserted on local database")
            }else{
                LogUtil.logDebug("ERROR animal was not saved on local database")
            }
        })
    }


}
