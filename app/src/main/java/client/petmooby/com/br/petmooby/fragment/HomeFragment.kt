package client.petmooby.com.br.petmooby.fragment


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.actvity.AddNewPetActivity
import client.petmooby.com.br.petmooby.adapter.AnimalAdapter
import client.petmooby.com.br.petmooby.extensions.*
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.util.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A simple [Fragment] subclass.
 */
const val CODE_RESULT_FOR_ADD_PET = 122
class HomeFragment : Fragment() {


    var rcMyAnimalsList: RecyclerView?=null


    private var docRefVet = FirebaseFirestore.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(R.id.toolbar,getString(R.string.Home))

        rcMyAnimalsList = defaultRecycleView(activity!!,R.id.rcMyAnimalsList)
        getMyAnimals()

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_add,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menuAdd -> {
            if(VariablesUtil.gbAnimals != null){
                if(VariablesUtil.gbAnimals?.size!! > 7) {
                    showAlert(R.string.youCanOnlyHaveSomeAnimals)
                }else startNewAnimalActivity()
            }else startNewAnimalActivity()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun startNewAnimalActivity() {
        startActivityForResult(Intent(activity, AddNewPetActivity::class.java), CODE_RESULT_FOR_ADD_PET)
    }

    private fun getMyAnimals(){
        if(VariablesUtil.gbAnimals  != null && VariablesUtil.gbAnimals?.size!! > 0){
            updateAdapter()
        }else {
            var dialog = showLoadingDialog(getString(R.string.gettingMyPets))
            docRefVet.collection(CollectionsName.ANIMAL)
                    .whereEqualTo("user", FireStoreReference.docRefUser)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        loadAnimalRCView(querySnapshot)
                        dialog.dismiss()
                    }.addOnFailureListener { exception ->
                onFailedQueryReturn(dialog, exception.message!!)
            }
        }
    }

    private fun loadAnimalRCView(querySnapshot: QuerySnapshot){
        if(querySnapshot.isEmpty){
            llHomeNoPetYet.visibility = VISIBLE
        }else{
            //VariablesUtil.gbAnimals = mutableListOf<Animal>()
            querySnapshot.documents.forEach{
                var animal = it.toObject(Animal::class.java)
                if(animal?.id == null){
                    animal?.id = it.id

                }
                VariablesUtil.addAnimal(animal!!)
                scheduleAllEvents(animal)
            }
            updateAdapter()
        }
    }

    private fun scheduleAllEvents(animal: Animal?) {
        //Schedule all alarms for each animal
        val vaccineUtil = VaccineUtil()
        if(animal?.vaccineCards != null) {
            for (vaccine in animal.vaccineCards!!) {
                vaccineUtil.scheduleEvent(activity!!, vaccine, animal.name!!,false)
            }
        }
        if(animal?.treatmentCard != null) {
            for (treatment in animal.treatmentCard!!) {
                TreatmentUtil.generateTreatmentAlarm(activity!!, animal.name!!, treatment,false)
            }
        }
    }

    private fun updateAdapter() {
        rcMyAnimalsList?.adapter = AnimalAdapter(VariablesUtil.gbAnimals!!) { animal -> animalDetail(animal) }
    }

    private fun animalDetail(animal: Animal){
        //animal.userPath = animal.user?.path
        VariablesUtil.gbSelectedAnimal = animal
        var intent = Intent(activity,AddNewPetActivity::class.java)
        intent.putExtra(Parameters.IS_FOR_UPDATE,true)

//        intent.putExtra(Parameters.ANIMAL_PARAMETER,animal)
//        intent.putExtra(Parameters.ANIMAL_PARAMETER,Parcels.wrap(animal))
        startActivityForResult(intent,CODE_RESULT_FOR_ADD_PET)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CODE_RESULT_FOR_ADD_PET) {
            if (resultCode == ResultCodes.RESULT_FOR_DELETE) {
//                val animal = data?.getSerializableExtra(Parameters.ANIMAL_PARAMETER)
//                val animal = Parcels.unwrap<Animal>(data?.getParcelableExtra(Parameters.ANIMAL_PARAMETER))
                VariablesUtil.gbAnimals?.remove(VariablesUtil.gbSelectedAnimal)
                updateAdapter()
            }else if(resultCode == Activity.RESULT_OK){
//                val animal = data?.getSerializableExtra(Parameters.ANIMAL_PARAMETER) as Animal
//                val animal = Parcels.unwrap<Animal>(data?.getParcelableExtra(Parameters.ANIMAL_PARAMETER))
//                VariablesUtil.gbAnimals?.add(VariablesUtil.gbSelectedAnimal!!)
                VariablesUtil.addAnimal(VariablesUtil.gbSelectedAnimal!!)
                updateAdapter()
            }else{
                updateAdapter()
            }
        }

    }

}
