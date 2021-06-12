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
import client.petmooby.com.br.petmooby.databinding.FragmentHomeBinding
import client.petmooby.com.br.petmooby.extensions.*
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.model.enums.StatusAnimal
import client.petmooby.com.br.petmooby.ui.viewmodel.HomeViewModel
import client.petmooby.com.br.petmooby.util.*
import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton


/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
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
        _binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
//        return inflater.inflate(R.layout.fragment_home, container, false)
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

//        fabAddNewPet.setOnClickListener { startNewAnimalActivity() }
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
        resultForAddPet.launch(Intent(activity, AddNewPetActivity::class.java))
    }

    private fun getMyAnimals(){
        dialog = showLoadingDialog(getString(R.string.gettingMyPets))
        homeViewModel.getAnimalList(requireContext())
    }

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
    }

    private fun animalDetail(animal: Animal){
        VariablesUtil.gbSelectedAnimal = animal
        val intent = Intent(activity,AddNewPetActivity::class.java)
        intent.putExtra(Parameters.IS_FOR_UPDATE,true)
        resultForAddPet.launch(intent)
    }

    private fun initObservers(){
        homeViewModel.animalListData.observe(viewLifecycleOwner, Observer { resource ->
            dialog?.dismiss()
            when(resource.status()){
                StatusAnimal.SUCCESS ->{
                    updateAdapter(resource.data()!!)
                }else ->{
                    binding.llHomeNoPetYet.visibility = VISIBLE
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
