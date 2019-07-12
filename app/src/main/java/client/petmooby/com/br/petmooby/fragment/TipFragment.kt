package client.petmooby.com.br.petmooby.fragment


import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.adapter.TipAdapter
import client.petmooby.com.br.petmooby.extensions.onFailedQueryReturn
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.extensions.showLoadingDialog
import client.petmooby.com.br.petmooby.extensions.toast
import client.petmooby.com.br.petmooby.java.EndlessRecyclerViewScrollListener
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.model.LastTip
import client.petmooby.com.br.petmooby.model.Tip
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.fragment_tip.*
import android.widget.Toast
import com.google.firebase.firestore.Query


/**
 * A simple [Fragment] subclass.
 */
class TipFragment : Fragment() {

    private var endlessListener:EndlessRecyclerViewScrollListener?=null
    private var docRefVet = FirebaseFirestore.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tip, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(R.id.toolbar,getString(R.string.Tips))
        initRCListOfTips()
        getTipList()
    }

    private fun initRCListOfTips() {
        var linearLayout = LinearLayoutManager(activity)
        rcListOfTips.layoutManager = linearLayout

        rcListOfTips!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    Toast.makeText(activity, getString(R.string.end), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun loadRCTipView(tips : LastTip){
        rcListOfTips.adapter = TipAdapter(tips.lastTips!!,{toast("clicou em ")})
    }

    fun getTipList(){
        var dialog = showLoadingDialog()
        docRefVet.collection(CollectionsName.LAST_TIP)
                .orderBy("lastTips.date")
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
            if(task.result.isEmpty){
                toast("No result found!")
            }else {
                var list = task.result.toObjects(LastTip::class.java)
                loadRCTipView(list[0])
            }
        }
    }
}
