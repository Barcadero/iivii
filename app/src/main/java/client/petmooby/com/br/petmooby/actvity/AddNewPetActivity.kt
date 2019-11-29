package client.petmooby.com.br.petmooby.actvity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.extensions.showAlert
import client.petmooby.com.br.petmooby.extensions.showLoadingDialog
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.model.comparator.EnumBreedComparator
import client.petmooby.com.br.petmooby.model.enums.*
import client.petmooby.com.br.petmooby.util.*
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.mvc.imagepicker.ImagePicker
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_add_new_pet.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
//import org.parceler.Parcels
//import org.parceler.Parcel
//import org.parceler.Parcels
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.math.BigDecimal
import java.util.*


class AddNewPetActivity : AppCompatActivity() {

    val PICK_IMAGE          = 125
    val TAKE_PICTURE        = 130
    var bithDate            = Date()
    var mRef                = FirebaseFirestore.getInstance()
    var animalRef           = mRef.collection(CollectionsName.ANIMAL)
    var enumSelectedBreed: EnumBreedBase?=null
    var storage             = FirebaseStorage.getInstance().reference
    private var mCurrentPhotoBitmap: Bitmap?=null
//    var animal :Animal?=null
    var isForUpdate         = false
    var fromOtherScreen     = false
    private val camera      = CameraUtil()
    private val photoName   = "photoProfile.jpg"
    private var enableButtons = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_pet)
        btnNewPetCalender.setOnClickListener {
            DateTimePickerDialog.showDatePicker(this,edtNewPetBirthday,bithDate)
        }
        edtNewPetBirthday.addTextChangedListener(DateMaskTextWatcher(edtNewPetBirthday))
        setupToolbar(R.id.toolbarNewPet, R.string.addPet)
        initSpinners()
        ivProfileMyPet.setOnClickListener {
            showImagePicker()
        }
        btnNewPetGallery.setOnClickListener {
            showImagePicker()
        }
        btnNewPetCapture.setOnClickListener{
            startsCameraActivityForResult()
        }

        btnExcludeNewPet.setOnClickListener {
            remove()
        }
        btnAddVaccine.setOnClickListener {
            val intent = Intent(this,VaccineLitsActivity::class.java)
            startActivity(intent)
        }

        btnAddMedicinalTreatment.setOnClickListener {
            val intent = Intent(this,TreatmentListActivity::class.java)
            startActivity(intent)
        }

        isForUpdate = intent.getBooleanExtra(Parameters.IS_FOR_UPDATE, false)
        enableControlsButtons(isForUpdate)
        getAnimalSentByOtherView()
    }

    @UiThread
    fun enableControlsButtons(enable: Boolean) {
        btnAddVaccine.isEnabled     = enable
        btnAddMedicinalTreatment.isEnabled = enable
    }

    private fun getAnimalSentByOtherView() {
        isForUpdate = intent.getBooleanExtra(Parameters.IS_FOR_UPDATE, false)
        if (isForUpdate) {
//            animal= intent.getParcelableExtra(Parameters.ANIMAL_PARAMETER) ?: return
//            animal= intent.getSerializableExtra(Parameters.ANIMAL_PARAMETER) as Animal
//            animal= Parcels.unwrap(intent.getParcelableExtra(Parameters.ANIMAL_PARAMETER))
//            animal?.user  = mRef.document(animal?.userPath!!)
            edtNewPetName.setText(VariablesUtil.gbSelectedAnimal?.name)
            edtNewPetBirthday.setText(DateTimeUtil.formatDateTime(VariablesUtil.gbSelectedAnimal?.dateOfBirthday))
            spNewPetGender.setSelection(EnumGender.valueOf(VariablesUtil.gbSelectedAnimal?.gender!!).ordinal)
            if(VariablesUtil.gbSelectedAnimal?.type != null) {
                spNewPetKindAnimal.setSelection(EnumTypeAnimal.valueOf(VariablesUtil.gbSelectedAnimal?.type?.name!!).ordinal)
                spNewPetKindAnimal.isEnabled = false
                fromOtherScreen = true
            }
            if(VariablesUtil.gbSelectedAnimal?.photo != null) {
                loadProfilePicture()
            }

        }
    }

    private fun loadProfilePicture() {
        progressAddPet.visibility = VISIBLE
        Picasso.with(this)
                .load(VariablesUtil.gbSelectedAnimal?.photo)
                .error(R.drawable.no_image)
                //.placeholder(R.drawable.icons8_identidade_de_cachorro_90)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .memoryPolicy(MemoryPolicy.NO_STORE)
                .fit()
                .into(ivProfileMyPet, object : Callback {
                    override fun onSuccess() {
                        progressAddPet.visibility = View.GONE
                        ivProfileMyPet.visibility = View.VISIBLE
                    }

                    override fun onError() {
                        progressAddPet.visibility = View.GONE
                        ivProfileMyPet.visibility = View.VISIBLE
                    }

                })
    }

    private fun showImagePicker() {
        ImagePicker.pickImage(this, getString(R.string.selectAPicture), PICK_IMAGE, true)
    }

    private fun initSpinners() {
        spNewPetKindAnimal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        //NOTHING SELECTED
                        spNewPetBreed.isEnabled = false
                        enumSelectedBreed = null
                    }
                    1 -> {
                        //DOGS
                        spNewPetBreed.isEnabled = true
                        val adapterValue = ArrayAdapter<EnumBreedsForDogs>(view?.context, LayoutResourceUtil.getSpinnerDropDown(), EnumBreedsForDogs.values())
                        adapterValue.sort(EnumBreedComparator())
                        spNewPetBreed.adapter = adapterValue
                        if(VariablesUtil.gbSelectedAnimal?.breed != null){
                            val position = adapterValue.getPosition(BreedNamesResolverUtil.getByValueForDogs(VariablesUtil.gbSelectedAnimal?.breed!!))
                            spNewPetBreed.setSelection(position)
                        }

                    }
                    2 -> {
                        //CATS
                        spNewPetBreed.isEnabled = true
                        val adapterValue = ArrayAdapter<EnumBreedsForCats>(view?.context, LayoutResourceUtil.getSpinnerDropDown(), EnumBreedsForCats.values())
                        adapterValue.sort(EnumBreedComparator())
                        spNewPetBreed.adapter = adapterValue
                        if(VariablesUtil.gbSelectedAnimal?.breed != null){
                            val position = adapterValue.getPosition(BreedNamesResolverUtil.getByValueForCats(VariablesUtil.gbSelectedAnimal?.breed!!))
                            spNewPetBreed.setSelection(position)
                        }
                    }
                    3 -> {
                        //Birds
                        spNewPetBreed.isEnabled = true
                        val adapterValue = ArrayAdapter<EnumBreedsForBirds>(view?.context, LayoutResourceUtil.getSpinnerDropDown(), EnumBreedsForBirds.values())
                        adapterValue.sort(EnumBreedComparator())
                        spNewPetBreed.adapter = adapterValue
                        if(VariablesUtil.gbSelectedAnimal?.breed != null){
                            val position = adapterValue.getPosition(BreedNamesResolverUtil.getByValueForBirds(VariablesUtil.gbSelectedAnimal?.breed!!))
                            spNewPetBreed.setSelection(position)
                        }
                    }
                }
            }

        }
        spNewPetBreed.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (spNewPetKindAnimal.selectedItemPosition) {
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
                        enumSelectedBreed =  spNewPetBreed.selectedItem as EnumBreedsForDogs
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
        spNewPetGender.adapter      = ArrayAdapter<EnumGender>(this,LayoutResourceUtil.getSpinnerDropDown(),EnumGender.values())
        spNewPetKindAnimal.adapter  = ArrayAdapter<EnumTypeAnimal>(this,LayoutResourceUtil.getSpinnerDropDown(),EnumTypeAnimal.values())
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
        var dialog = showLoadingDialog()
        if(!isForUpdate) {
            VariablesUtil.gbSelectedAnimal = Animal()
        }
        enumSelectedBreed = spNewPetBreed.selectedItem as EnumBreedBase
        with(VariablesUtil.gbSelectedAnimal!!){
            name            = edtNewPetName.text.toString()
            dateOfBirthday  = bithDate
            breed           = enumSelectedBreed?.getValue(enumSelectedBreed!!.getIndex(enumSelectedBreed!!))
            type            = EnumTypeAnimal.values()[spNewPetKindAnimal.selectedItemPosition]
            gender          = EnumGender.values()[spNewPetGender.selectedItemPosition].value
            user            = FireStoreReference.docRefUser
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
                    //returnThePetForShowOnList()
                }.addOnFailureListener {
                    dialog.dismiss()
                }
    }

    private fun successSaved(documentReference: DocumentReference){
        VariablesUtil.gbSelectedAnimal?.id = documentReference.id
        isForUpdate = true
        enableControlsButtons(true)
        VariablesUtil.gbAnimals?.add(VariablesUtil.gbSelectedAnimal!!)
        FireStoreReference.saveAnimalReference(documentReference)
        if(mCurrentPhotoBitmap != null) {
            uploadImageAndSaveOrUpdatePet()
        }else{
            //returnThePetForShowOnList()
        }
    }

    private fun returnThePetForShowOnList() {
        var intent = Intent()
//        intent.putExtra(Parameters.ANIMAL_PARAMETER, animal)
//        intent.putExtra(Parameters.ANIMAL_PARAMETER, Parcels.wrap(animal))
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
                user == null -> {
//                    showAlert(R.id.animalGenderIsMissing)
                    false
                }
                else -> true
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == PICK_IMAGE) {
            onResultActivityForGallery(requestCode, resultCode, data)
        }else if(requestCode == TAKE_PICTURE){
            onResultActivityForCamera(requestCode,resultCode,data)
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



    private fun startsCameraActivityForResult() {
        startActivityForResult(camera.open(this, photoName), TAKE_PICTURE)
    }

    fun onResultActivityForGallery(requestCode: Int, resultCode: Int, data: Intent?){
        if(resultCode == Activity.RESULT_OK){
            var bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data)
            val imagePathFromResult = ImagePicker.getImagePathFromResult(this, requestCode, resultCode, data)
            var matrix: Matrix? = null
            try {
                val exif = ExifInterface(imagePathFromResult)
                val rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                val rotationInDegrees = ImageUtil.exifToDegrees(rotation)
                matrix = Matrix()
                if (rotation.toFloat() != 0f) {
                    matrix.preRotate(rotationInDegrees.toFloat())
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
            //Se a camera retornou vamos mostar o arquivo da foto
            //val bitmap = camera.getBitmap(600,600)
            if(bitmap != null) {
                bitmap = ImageUtil.rotate(bitmap,matrix)
                ivProfileMyPet.setImageBitmap(bitmap)
                mCurrentPhotoBitmap = bitmap
                //mCurrentPhotoPath = imagePathFromResult
            }
        }
    }

    private fun onResultActivityForCamera(requestCode: Int, resultCode: Int, data: Intent?){
        if(resultCode != Activity.RESULT_OK){
            return
        }
        var f = File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString())
        for (temp : File in f.listFiles()) {
            if (temp.name == photoName) {
                f = temp
                break
            }
        }
        if (!f.exists()) {
            Toast.makeText(this,
                    getString(R.string.errorWhileCapturingImage), Toast.LENGTH_LONG)
                    .show()
            return

        }
        var bitmapOrigin = BitmapFactory.decodeFile(f.absolutePath)
        if(bitmapOrigin == null){
            toast(getString(R.string.cantGetFileImage))
        }
        try{
            var bitmap = compress(f,bitmapOrigin)
            ivProfileMyPet.setImageBitmap(bitmap)
            mCurrentPhotoBitmap = bitmap
            //mCurrentPhotoPath =  f.absolutePath
            //postImageProfile(bitmap!!)

        } catch (e:Exception ) {
            e.printStackTrace()
        }
    }

    private fun compress(f: File, bitmapOrigin: Bitmap): Bitmap? {

        val width   = bitmapOrigin.width.toDouble()
        val height  = bitmapOrigin.height.toDouble()
        val base = 200
        val margin = 10

        val scale = if (width > height) {
            ((base * 100) / width)
        } else
            ((base * 100) / height)

        val maxWidth: Int
        val maxHeight: Int
        if (width > height) {
            val led = getRelativeProportion(height,scale)
            maxWidth = base
            maxHeight = led + margin
        } else {
            val led = getRelativeProportion(width, scale)
            maxWidth = led + margin
            maxHeight = base
        }

        val bitmap = Compressor(this)
                .setMaxHeight(maxHeight)
                .setMaxWidth(maxWidth)
                .setQuality(60)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .compressToBitmap(f)
        return bitmap
    }

    private fun getRelativeProportion(width: Double, scale: Double): Int {
        var set = (width * (scale / 100))
        var led = BigDecimal(set).setScale(1, BigDecimal.ROUND_HALF_UP).toInt()
        return led
    }

    private fun remove(){
        alert(R.string.areYouSure,R.string.removingPet){
            positiveButton(R.string.yes) {
                animalRef.document(VariablesUtil.gbSelectedAnimal?.id!!)
                        .delete()
                        .addOnFailureListener {  }
                        .addOnSuccessListener {
                            var intent = Intent()
                                //intent.putExtra(Parameters.ANIMAL_PARAMETER,animal)
//                            intent.putExtra(Parameters.ANIMAL_PARAMETER,Parcels.wrap(animal) )
                            setResult(ResultCodes.RESULT_FOR_DELETE,intent)
                            finish()
                        }
            }

            negativeButton(R.string.no) {
                it.dismiss()
            }
        }.show()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        returnThePetForShowOnList()
    }


}
