package client.petmooby.com.br.petmooby.fragment


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.actvity.VeterinaryPartnerDetailActivity
import client.petmooby.com.br.petmooby.adapter.VeterinaryTipAdapter
import client.petmooby.com.br.petmooby.extensions.defaultRecycleView
import client.petmooby.com.br.petmooby.extensions.onFailedQueryReturn
import client.petmooby.com.br.petmooby.extensions.showLoadingDialog
import client.petmooby.com.br.petmooby.extensions.toast
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.model.VeterinaryTip
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


/**
 * A simple [Fragment] subclass.
 */
class VeterinaryPartnersListFragment : Fragment() {

    var recycleView: RecyclerView?=null
    var vetList:MutableList<VeterinaryTip>?=null
    private var docRefVet = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_veterinary_partners_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycleView = defaultRecycleView(view,R.id.rcVeterinaryList)
        getVetList()
    }

    fun getVetList(){
        var dialog = showLoadingDialog()
        docRefVet.collection(CollectionsName.VET_TIP)
                .get()
                .addOnCompleteListener {
                    task -> successQueryReturn(dialog,task)
                }
                .addOnFailureListener {
                    exception -> onFailedQueryReturn(dialog,exception.message!!)
                }
    }

    private fun successQueryReturn(dialog: ProgressDialog, task: Task<QuerySnapshot>) {
        dialog.dismiss()
        if(task.isSuccessful){
            if(task.result?.isEmpty!!){
                toast("No result found!")
            }else {
                vetList = task.result?.toObjects(VeterinaryTip::class.java)
                recycleView!!.adapter = VeterinaryTipAdapter(vetList!!,{veterinaryTip: VeterinaryTip -> onClick(veterinaryTip) })

            }
        }
    }

    private fun onClick(vet:VeterinaryTip){
        var intent = Intent(activity,VeterinaryPartnerDetailActivity::class.java)
        intent.putExtra(CollectionsName.VET_TIP,vet)
        startActivity(intent)
    }

}// Required empty public constructor
