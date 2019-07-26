package client.petmooby.com.br.petmooby.actvity

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
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
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.model.comparator.EnumBreedComparator
import client.petmooby.com.br.petmooby.model.enums.*
import client.petmooby.com.br.petmooby.util.DateMaskTextWatcher
import client.petmooby.com.br.petmooby.util.DateTimePickerDialog
import client.petmooby.com.br.petmooby.util.FireStoreReference
import client.petmooby.com.br.petmooby.util.LayoutResourceUtil
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_new_pet.*
import java.util.*

class AddNewPetActivity : AppCompatActivity() {

    var bithDate = Date()
    var animalRef = FirebaseFirestore.getInstance().collection(CollectionsName.ANIMAL)
    var enumSelectedBreed: EnumBreedBase?=null
    //var currentAnimalRef: DocumentReference?=null
    var animal :Animal?=null
    var isForUpdate = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_pet)
        btnNewPetCalender.setOnClickListener {
            DateTimePickerDialog.showDatePicker(this,edtNewPetBirthday,bithDate)
        }
        edtNewPetBirthday.addTextChangedListener(DateMaskTextWatcher(edtNewPetBirthday))
        setupToolbar(R.id.toolbarNewPet, R.string.addPet)
        initSpinners()

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
                        var adapterValue = ArrayAdapter<EnumBreedsForDogs>(view?.context, LayoutResourceUtil.getSpinnerDropDown(), EnumBreedsForDogs.values())
                        adapterValue.sort(EnumBreedComparator())
                        spNewPetBreed.adapter = adapterValue

                    }
                    2 -> {
                        //CATS
                        spNewPetBreed.isEnabled = true
                        spNewPetBreed.adapter = ArrayAdapter<EnumBreedsForCats>(view?.context, LayoutResourceUtil.getSpinnerDropDown(), EnumBreedsForCats.values())
                    }
                    3 -> {
                        //BIRDS
                        spNewPetBreed.isEnabled = true
                        spNewPetBreed.adapter = ArrayAdapter<EnumBreedsForBirds>(view?.context, android.R.layout.simple_spinner_item, EnumBreedsForBirds.values())

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
                        enumSelectedBreed = EnumBreedsForDogs.values()[position]

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
            animal = Animal()
        }
        enumSelectedBreed = spNewPetBreed.selectedItem as EnumBreedBase
        with(animal!!){
            name            = edtNewPetName.text.toString()
            dateOfBirthday  = bithDate
            breed           = enumSelectedBreed?.getValue(enumSelectedBreed!!.getIndex(enumSelectedBreed!!))
            type            = EnumTypeAnimal.values()[spNewPetKindAnimal.selectedItemPosition]
            gender          = EnumGender.values()[spNewPetGender.selectedItemPosition].value
            user            = FireStoreReference.docRefUser
        }
        if(validateFields(animal!!)) {
            if(!isForUpdate) {
                callFireStoreServiceToSaveTheAnimal( dialog)
            }else{
                callFireStoreServiceToUpdateTheAnimal(dialog)
            }
        }
    }

    private fun callFireStoreServiceToSaveTheAnimal(dialog: ProgressDialog) {
        animalRef
                .add(animal!!)
                .addOnSuccessListener { documentReference ->
                    dialog.dismiss()
                    successSaved(documentReference)
                }.addOnFailureListener {
                    exception -> dialog.dismiss()
                }
    }

    private fun callFireStoreServiceToUpdateTheAnimal(dialog: ProgressDialog) {
        animalRef.document(animal?.id!!)
                .set(animal!!)
                .addOnSuccessListener { documentReference ->
                    dialog.dismiss()
                }.addOnFailureListener {
                    exception -> dialog.dismiss()
                }
    }

    private fun successSaved(documentReference: DocumentReference){
        animal?.id = documentReference.id
        isForUpdate = true
        FireStoreReference.saveAnimalReference(documentReference)
    }

    fun validateFields(animal: Animal): Boolean{
        with(animal) {
            return if (breed.isNullOrEmpty()) {
                showAlert(R.id.animalBreedIsMissing)
                false
            } else if (dateOfBirthday == null) {
                showAlert(R.id.bithDayIsMissing)
                false
            } else if (type == null) {
                showAlert(R.id.animalTypeIsMissing)
                false
            }else if(gender.isNullOrEmpty()) {
                showAlert(R.id.animalGenderIsMissing)
                false
            }else if(user == null){
                showAlert(R.id.animalGenderIsMissing)
                false
            } else true
        }
    }
}
