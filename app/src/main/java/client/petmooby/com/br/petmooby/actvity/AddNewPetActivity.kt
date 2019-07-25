package client.petmooby.com.br.petmooby.actvity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.model.enums.EnumBreedsForDogs
import client.petmooby.com.br.petmooby.model.enums.EnumTypeAnimal
import client.petmooby.com.br.petmooby.util.DateMaskTextWatcher
import client.petmooby.com.br.petmooby.util.DateTimePickerDialog
import client.petmooby.com.br.petmooby.util.FireStoreReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_new_pet.*
import java.util.*

class AddNewPetActivity : AppCompatActivity() {

    var bithDate: Date?=null
    var animalRef = FirebaseFirestore.getInstance().collection(CollectionsName.ANIMAL)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_pet)
        btnNewPetCalender.setOnClickListener {
            bithDate = DateTimePickerDialog.showDatePicker(this,edtNewPetBirthday)
        }
        edtNewPetBirthday.addTextChangedListener(DateMaskTextWatcher(edtNewPetBirthday))
        setupToolbar(R.id.toolbarNewPet, R.string.addPet)
        spNewPetBreed.adapter = ArrayAdapter<EnumBreedsForDogs>(this,android.R.layout.simple_spinner_item, EnumBreedsForDogs.values())
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
        var animal = Animal()
        with(animal){
            name        = edtNewPetName.text.toString()
            bithDate    = dateOfBirthday
            breed       = EnumBreedsForDogs.values()[spNewPetBreed.selectedItemPosition].value
            type        = EnumTypeAnimal.values()[spNewPetKindAnimal.selectedItemPosition]
            gender      = spNewPetGender.selectedItem.toString()
            user        = FireStoreReference.docRefUser

        }

        //callFireStoreServiceToSaveTheAnimal(animal)
    }

    private fun callFireStoreServiceToSaveTheAnimal(animal: Animal) {
        animalRef
                .add(animal)
                .addOnSuccessListener { documentReference ->
                    successSaved(documentReference)
                }
    }

    private fun successSaved(documentReference: DocumentReference){
        FireStoreReference.saveAnimalReference(documentReference)
    }

    fun validateFields(){

    }
}
