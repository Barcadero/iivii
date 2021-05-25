package client.petmooby.com.br.petmooby.fragment


import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.actvity.AddNewPetActivity
import client.petmooby.com.br.petmooby.adapter.AnimalAdapter
import client.petmooby.com.br.petmooby.extensions.defaultRecycleView
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.extensions.showAlert
import client.petmooby.com.br.petmooby.extensions.showLoadingDialog
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.ui.viewmodel.AnimalViewModel
import client.petmooby.com.br.petmooby.util.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton


/**
 * A simple [Fragment] subclass.
 */
const val CODE_RESULT_FOR_ADD_PET = 122
class HomeFragment : Fragment() {


    var rcMyAnimalsList: RecyclerView?=null
    private val viewModel: AnimalViewModel by viewModels()
    var currentProgress: ProgressDialog?=null


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

        viewModel.animalLiveData.observe(viewLifecycleOwner, Observer {animals ->
            loadAnimalRCView(animals)
        })
        viewModel.progress.observe(viewLifecycleOwner, Observer {showProgress ->
            if(showProgress){
                currentProgress = showLoadingDialog()
            }else{
                if(currentProgress != null){
                    currentProgress!!.dismiss()
                }
            }
        })
        if(showMessage) {
            requireActivity().alert(R.string.loginMessage, R.string.Advice) {
                okButton { Preference.setShowMessageLogin(requireActivity(),false); getMyAnimals(savedInstanceState) }
                onCancelled { Preference.setShowMessageLogin(requireActivity(),false);getMyAnimals(savedInstanceState) }
            }.show()
        }else{
            getMyAnimals(savedInstanceState)
        }

    }

    private fun getMyAnimals(savedInstanceState: Bundle?) {
        if(savedInstanceState == null) {
            currentProgress = showLoadingDialog()
            viewModel.getAnimals()
        }
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
        startActivityForResult(Intent(activity, AddNewPetActivity::class.java), CODE_RESULT_FOR_ADD_PET)
    }

    private fun loadAnimalRCView(animals: List<Animal>){
        if(animals.isEmpty()){
            llHomeNoPetYet.visibility = VISIBLE
        }else{
            if(animals.size != VariablesUtil.gbAnimals?.size) {
                VariablesUtil.gbAnimals?.clear()
                animals.forEach { animal ->
                    VariablesUtil.addAnimal(animal)
                    scheduleAllEvents(animal)
                }
            }
            updateAdapter()
        }
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

    private fun updateAdapter() {
        rcMyAnimalsList?.adapter = AnimalAdapter(VariablesUtil.gbAnimals!!) { animal -> animalDetail(animal) }
        if(VariablesUtil.gbAnimals != null && VariablesUtil.gbAnimals?.isNotEmpty()!!){
            llHomeNoPetYet.visibility = GONE
        }else{
            llHomeNoPetYet.visibility = VISIBLE
        }
    }

    private fun animalDetail(animal: Animal){
        //animal.userPath = animal.user?.path
        VariablesUtil.gbSelectedAnimal = animal
        var intent = Intent(activity,AddNewPetActivity::class.java)
        intent.putExtra(Parameters.IS_FOR_UPDATE,true)
        startActivityForResult(intent,CODE_RESULT_FOR_ADD_PET)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CODE_RESULT_FOR_ADD_PET) {
            when (resultCode) {
                ResultCodes.RESULT_FOR_DELETE -> {
                    VariablesUtil.gbAnimals?.remove(VariablesUtil.gbSelectedAnimal)
                    updateAdapter()
                }
                RESULT_OK -> {
                    VariablesUtil.addAnimal(VariablesUtil.gbSelectedAnimal!!)
                    updateAdapter()
                }
                else -> updateAdapter()
            }
        }

    }

}
