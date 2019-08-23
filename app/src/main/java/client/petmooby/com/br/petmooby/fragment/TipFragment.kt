package client.petmooby.com.br.petmooby.fragment


import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.adapter.TipAdapter
import client.petmooby.com.br.petmooby.extensions.onFailedQueryReturn
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.extensions.showLoadingDialog
import client.petmooby.com.br.petmooby.extensions.toast
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.model.LastTip
import client.petmooby.com.br.petmooby.model.Tip
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.fragment_tip.*


/**
 * A simple [Fragment] subclass.
 */
class TipFragment : Fragment() {

    var lastQuery:DocumentSnapshot?=null
    private var listOfTips:MutableList<Tip>?=null
    private var isLoading = false
    private var docRefVet = FirebaseFirestore.getInstance()
    private val LIMIT = 5L
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tip, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(R.id.toolbar,getString(R.string.Tips))
        initRCListOfTips()
        getLastTipList()
    }

    private fun initRCListOfTips() {
        var linearLayout = LinearLayoutManager(activity)
        rcListOfTips.layoutManager = linearLayout

        rcListOfTips!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(isLoading) return
                val visibleItemCount    = linearLayout.childCount
                val totalItemCount      = linearLayout.itemCount
                val pastVisibleItems    = linearLayout.findFirstVisibleItemPosition()
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    isLoading = true
                    getTips()
                }

            }
        })


    }

    private fun loadRCTipView(tips : LastTip){
        listOfTips = tips.lastTips!!.toMutableList()
        rcListOfTips.adapter = TipAdapter(listOfTips!!,{toast("clicou em ")})
    }

    fun getLastTipList(){
        var dialog = showLoadingDialog()
        docRefVet.collection(CollectionsName.LAST_TIP)
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


                    var list = task.result?.toObjects(LastTip::class.java)
                    loadRCTipView(list!![0])

            }
        }
    }

    private fun successQueryReturn(dialog: ProgressDialog, query: QuerySnapshot) {
        dialog.dismiss()

        if(query?.isEmpty!!){
            toast(getString(R.string.noModeResults))
        }else {
                Thread.sleep(1000)
                var list    = query!!.toObjects(Tip::class.java)
                lastQuery   =  query.documents[query.size() - 1]
                //lastQuery = query
                changeListOfTips(list)
                isLoading = false
        }

    }


    private fun changeListOfTips(lastTips:List<Tip>) {
        listOfTips?.addAll(lastTips)
        rcListOfTips.adapter?.notifyDataSetChanged()
    }

    fun getTips(){
        var dialog = showLoadingDialog()
        if(lastQuery != null) {
            docRefVet.collection(CollectionsName.TIP)
                    .orderBy("date", Query.Direction.DESCENDING)
                    .limit(LIMIT)
                    .startAfter(lastQuery!!)
                    .get()
                    .addOnSuccessListener {
                        querySnapshot -> successQueryReturn(dialog,querySnapshot)
                    }
                    .addOnFailureListener { exception ->
                        onFailedQueryReturn(dialog, exception.message!!)
                    }
        }else{
            docRefVet.collection(CollectionsName.TIP)
                    .orderBy("date",Query.Direction.DESCENDING)
                    .limit(LIMIT)
                    .get()
                    .addOnSuccessListener {
                        querySnapshot -> successQueryReturn(dialog,querySnapshot)
                    }
                    .addOnFailureListener {
                        exception -> onFailedQueryReturn(dialog,exception.message!!)
                    }
        }
    }


}
