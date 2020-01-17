package client.petmooby.com.br.petmooby.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.application.Application
import client.petmooby.com.br.petmooby.model.Tip
import com.squareup.picasso.Picasso
import android.text.method.ScrollingMovementMethod



/**
 * Created by Rafael Rocha on 11/07/2019.
 */
class TipAdapter(val tips: List<Tip>,
                 val onClick: (tip:Tip) -> Unit) : RecyclerView.Adapter<TipAdapter.VetViewHolder>() {
    override fun onBindViewHolder(holder: VetViewHolder, position: Int) {
        val context = holder.itemView.context
        val tip     = tips[position]
        holder.txtText.movementMethod = ScrollingMovementMethod()
        if(Application.DEVICE_LANGUAGE == Application.LANG_EN) {
            holder.txtTitle.text = tip.title?.EN_US
            holder.txtText.text  = tip.desc?.EN_US
        }else{
            holder.txtTitle.text = tip.title?.PT_BR
            holder.txtText.text  = tip.desc?.PT_BR
        }
        holder.txtVetName.text = tip.name
//        PicassoUtil.build(tip.photo!!,holder.imgPhoto,context = context)
        holder.progressTipList.visibility = VISIBLE
        Picasso.with(context).load(tip.photo!!).error(R.drawable.ic_paw_24).fit().into(holder.imgPhoto,
                object : com.squareup.picasso.Callback{
                    override fun onSuccess() {
                        holder.progressTipList.visibility = View.GONE
                        holder.imgPhoto.visibility = VISIBLE
                    }

                    override fun onError() {
                        holder.progressTipList.visibility = View.GONE
                        holder.imgPhoto.visibility = VISIBLE
                    }

                })
        if(onClick != null) {
            holder.itemView.setOnClickListener { onClick(tip) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): VetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_tip_list,parent,false)
        return VetViewHolder(view)
    }

    override fun getItemCount(): Int  = tips.size

    class VetViewHolder(view: View): RecyclerView.ViewHolder(view){
        var imgPhoto    = view.findViewById<ImageView>(R.id.imgAdapterTip)
        var txtVetName  = view.findViewById<TextView>(R.id.txtVetNameAdapterTip)
        var txtTitle    = view.findViewById<TextView>(R.id.txtTitleAdapterTip)
        var txtText     = view.findViewById<TextView>(R.id.txtTextAdapterTip)
        var progressTipList     = view.findViewById<ProgressBar>(R.id.progressTipList)

    }
}