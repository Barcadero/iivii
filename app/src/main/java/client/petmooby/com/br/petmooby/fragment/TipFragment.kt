package client.petmooby.com.br.petmooby.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.extensions.defaultRecycleView
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.java.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.fragment_tip.*


/**
 * A simple [Fragment] subclass.
 */
class TipFragment : Fragment() {

    private var endlessListener:EndlessRecyclerViewScrollListener?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_tip, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(R.id.toolbar,getString(R.string.Tips))
        var linearLayout = LinearLayoutManager(activity)
        rcListOfTips.layoutManager = linearLayout
        endlessListener = object : EndlessRecyclerViewScrollListener(linearLayout){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                //TODO Do here the implementation
            }
        }
        rcListOfTips!!.addOnScrollListener(endlessListener!!)
    }

}// Required empty public constructor
