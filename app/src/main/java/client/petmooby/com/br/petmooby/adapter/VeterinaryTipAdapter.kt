package client.petmooby.com.br.petmooby.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.model.VeterinaryTip
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_vet_partners_list.view.*

/**
 * Created by idoctor on 16/05/2019.
 */
class VeterinaryTipAdapter(
        val vets: List<VeterinaryTip>,
        val onClick: (VeterinaryTip) -> Unit) : RecyclerView.Adapter<VeterinaryTipAdapter.VetViewHolder>(){

    override fun onBindViewHolder(holder: VetViewHolder, position: Int) {
        val context = holder.itemView.context
        val vet = vets[position]
        holder.tvNameVet.text       = vet.name
        holder.tvEmailVet.text      = vet.email
        holder.progess.visibility   = View.VISIBLE
        //holder.ivProfileVet.visibility = View.GONE
        Picasso.with(context).load(vet.photo).error(R.drawable.ic_paw_24).fit().into(holder.ivProfileVet,
                object : com.squareup.picasso.Callback{
                    override fun onSuccess() {
                        holder.progess.visibility = View.GONE
                        holder.ivProfileVet.visibility = View.VISIBLE
                    }

                    override fun onError() {
                        holder.progess.visibility = View.GONE
                        holder.ivProfileVet.visibility = View.VISIBLE
                    }

                })

        holder.itemView.setOnClickListener { onClick(vet) }
        if(position == itemCount - 1){
            holder.viewVeterinaryList.visibility = GONE
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VetViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_vet_partners_list,parent,false)

        return VetViewHolder(view)
    }

    override fun getItemCount() = this.vets.size

    class VetViewHolder(view: View): RecyclerView.ViewHolder(view){
        var cardView        = view.findViewById<CardView>(R.id.cvVetPartnersList)
        var ivProfileVet    = view.findViewById<ImageView>(R.id.ivProfileVet)
        var tvNameVet       = view.findViewById<TextView>(R.id.tvNameVet)
        var tvEmailVet      = view.findViewById<TextView>(R.id.tvEmailVet)
        var progess         = view.findViewById<ProgressBar>(R.id.progressVetList)
        var viewVeterinaryList   = view.viewVeterinaryList

    }

}