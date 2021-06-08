package client.petmooby.com.br.petmooby.fragment


import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.View.VISIBLE
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.actvity.AddNewPetActivity
import client.petmooby.com.br.petmooby.adapter.AnimalAdapter
import client.petmooby.com.br.petmooby.extensions.*
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.model.enums.StatusAnimal
import client.petmooby.com.br.petmooby.ui.viewmodel.HomeViewModel
import client.petmooby.com.br.petmooby.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton


/**
 * A simple [Fragment] subclass.
 */
const val CODE_RESULT_FOR_ADD_PET = 122
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var dialog : ProgressDialog? = null
    var rcMyAnimalsList: RecyclerView?=null
    private val homeViewModel : HomeViewModel by viewModels()
    //Result call other activities
    private var resultForAddPet = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        when(it.resultCode){
            RESULT_OK -> {
                (rcMyAnimalsList?.adapter as AnimalAdapter).addAnimal(VariablesUtil.gbSelectedAnimal!!)
            }
            ResultCodes.RESULT_FOR_DELETE -> {
                (rcMyAnimalsList?.adapter as AnimalAdapter).removeAnimal(VariablesUtil.gbSelectedAnimal!!)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(R.id.toolbar,getString(R.string.Home))

        rcMyAnimalsList = defaultRecycleView(requireActivity(),R.id.rcMyAnimalsList)
        val showMessage = Preference.getShowMessageLogin(requireActivity())
        if(showMessage) {
            showInitialMessage()
        }else{
            getMyAnimals()
        }

        fabAddNewPet.setOnClickListener { startNewAnimalActivity() }
        initObservers()
    }

    private fun showInitialMessage() {
        requireActivity().alert(R.string.loginMessage, R.string.Advice) {
            okButton { Preference.setShowMessageLogin(requireActivity(), false); getMyAnimals() }
            onCancelled { Preference.setShowMessageLogin(requireActivity(), false);getMyAnimals() }
        }.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add,menu)
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
//        startActivityForResult(Intent(activity, AddNewPetActivity::class.java), CODE_RESULT_FOR_ADD_PET)
        resultForAddPet.launch(Intent(activity, AddNewPetActivity::class.java))
    }

    private fun getMyAnimals(){
        dialog = showLoadingDialog(getString(R.string.gettingMyPets))
        homeViewModel.getAnimalList(requireContext())
//        if(VariablesUtil.gbAnimals  != null && VariablesUtil.gbAnimals?.size!! > 0){
//
//            updateAdapter(VariablesUtil.gbAnimals!!)
//        }else {
//            dialog = showLoadingDialog(getString(R.string.gettingMyPets))
//            homeViewModel.getAnimalList(requireContext())
//            val userPath = Preference.getUserPath(requireContext())
//            val userRef = docRefVet.document(userPath!!)
//            Log.d(TAG_READ_FIREBASE,"Get Animals")
//            docRefVet.collection(CollectionsName.ANIMAL)
//                    .whereEqualTo("user", userRef)
//                    .get()
//                    .addOnSuccessListener { querySnapshot ->
//                        loadAnimalRCView(querySnapshot)
//                        dialog.dismiss()
//                    }.addOnFailureListener { exception ->
//                onFailedQueryReturn(dialog, exception.message!!)
//            }
//        }
    }

//    private fun loadAnimalRCView(animalList: List<Animal>){
//        if(querySnapshot.isEmpty){
//            llHomeNoPetYet.visibility = VISIBLE
//        }else{
//            //VariablesUtil.gbAnimals = mutableListOf<Animal>()
//            querySnapshot.documents.forEach{
//                var animal = it.toObject(Animal::class.java)
//                if(animal?.id == null || animal.id!!.isEmpty()){
//                    animal?.id = it.id
//
//                }
//                VariablesUtil.addAnimal(animal!!)
//                scheduleAllEvents(animal)
//            }
//            updateAdapter()
//        }
//        updateAdapter(animalList)
//    }

    private fun scheduleAllEvents(animal: Animal?) {
        //Schedule all alarms for each animal
        val vaccineUtil = VaccineUtil()
        if(animal?.vaccineCards != null) {
            for (vaccine in animal.vaccineCards!!) {
                vaccineUtil.scheduleEvent(requireActivity(), vaccine, animal.name!!,false)
            }
        }
        if(animal?.treatmentCard != null) {
            for (treatment in animal.treatmentCard!!) {
                TreatmentUtil.generateTreatmentAlarm(requireActivity(), animal.name!!, treatment,false)
            }
        }
    }

    private fun updateAdapter(animalList: List<Animal>) {
        rcMyAnimalsList?.adapter = AnimalAdapter( animalList.toMutableList()) { animal -> animalDetail(animal) }
        animalList.forEach {
            scheduleAllEvents(it)
        }
//        if(VariablesUtil.gbAnimals != null && VariablesUtil.gbAnimals?.isNotEmpty()!!){
//            llHomeNoPetYet.visibility = GONE
//        }else{
//            llHomeNoPetYet.visibility = VISIBLE
//        }
    }

    private fun animalDetail(animal: Animal){
        VariablesUtil.gbSelectedAnimal = animal
        val intent = Intent(activity,AddNewPetActivity::class.java)
        intent.putExtra(Parameters.IS_FOR_UPDATE,true)
        startActivityForResult(intent,CODE_RESULT_FOR_ADD_PET)

    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == CODE_RESULT_FOR_ADD_PET) {
//            when (resultCode) {
//                ResultCodes.RESULT_FOR_DELETE -> {
//                    //TODO Change for the local database
////                    VariablesUtil.gbAnimals?.remove(VariablesUtil.gbSelectedAnimal)
//                    (rcMyAnimalsList?.adapter as AnimalAdapter).removeAnimal(VariablesUtil.gbSelectedAnimal!!)
////                    updateAdapter(VariablesUtil.gbAnimals!!)
//                }
//                RESULT_OK -> {
//                    //TODO Change for the local database
////                    VariablesUtil.addAnimal(VariablesUtil.gbSelectedAnimal!!)
////                    updateAdapter(VariablesUtil.gbAnimals!!)
//                    (rcMyAnimalsList?.adapter as AnimalAdapter).addAnimal(VariablesUtil.gbSelectedAnimal!!)
//                }
//                else -> updateAdapter(VariablesUtil.gbAnimals!!)
//            }
//        }
//    }

    private fun initObservers(){
        homeViewModel.animalListData.observe(viewLifecycleOwner, Observer { resource ->
            dialog?.dismiss()
            when(resource.status()){
                StatusAnimal.SUCCESS ->{
                    updateAdapter(resource.data()!!)
                }else ->{
                    llHomeNoPetYet.visibility = VISIBLE
                }
            }
        })
    }

}
