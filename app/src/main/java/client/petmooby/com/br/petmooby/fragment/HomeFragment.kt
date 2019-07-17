package client.petmooby.com.br.petmooby.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup

import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.actvity.AddNewPetActivity
import client.petmooby.com.br.petmooby.adapter.AnimalAdapter
import client.petmooby.com.br.petmooby.extensions.*
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.util.FireStoreReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A simple [Fragment] subclass.
 */
const val CODE_RESULT_FOR_ADD_PET = 122
class HomeFragment : Fragment() {


    var rcMyAnimalsList:RecyclerView?=null


    private var docRefVet = FirebaseFirestore.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(R.id.toolbar,getString(R.string.Home))
        rcMyAnimalsList = defaultRecycleView(activity!!,R.id.rcMyAnimalsList)
        getMyAnimals()

    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menuAdd -> {
            startActivityForResult(Intent(activity,AddNewPetActivity::class.java), CODE_RESULT_FOR_ADD_PET)
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun getMyAnimals(){
        var dialog = showLoadingDialog(getString(R.string.gettingMyPets))
        docRefVet.collection(CollectionsName.ANIMAL)
                .whereEqualTo("user",FireStoreReference.docRefUser)
                .get()
                .addOnSuccessListener {
                    querySnapshot -> loadAnimalRCView(querySnapshot)
                    dialog.dismiss()
                }.addOnFailureListener {
                    exception -> onFailedQueryReturn(dialog,exception.message!!)
                }
    }

    private fun loadAnimalRCView(querySnapshot: QuerySnapshot){
        if(querySnapshot.isEmpty){
            llHomeNoPetYet.visibility = VISIBLE
        }else{
            var myList = querySnapshot.toObjects(Animal::class.java)

            rcMyAnimalsList?.adapter = AnimalAdapter(myList,{animal -> animalDetail(animal) })
        }
    }

    private fun animalDetail(animal: Animal){
        toast("Animal ${animal.name}")
    }

}
