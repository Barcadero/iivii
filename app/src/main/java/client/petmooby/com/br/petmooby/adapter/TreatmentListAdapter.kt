package client.petmooby.com.br.petmooby.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.model.Animal
import kotlinx.android.synthetic.main.adapter_treatment_list.view.*

/**
 * Created by Rafael Rocha on 07/08/2019.
 */
class TreatmentListAdapter (
        private val treatments:List<Animal.TreatmentCard>,
        private val callback: (Animal.TreatmentCard) -> Unit) : RecyclerView.Adapter<TreatmentListAdapter.TreatmentViewHolder>() {
    override fun getItemCount(): Int = treatments.size


    override fun onBindViewHolder(holder: TreatmentViewHolder, position: Int) {
        val treatment = treatments[position]
        holder.name.text = treatment.name

    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): TreatmentViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.adapter_treatment_list,parent,false)
        val holder = TreatmentViewHolder(layout)
        holder.itemView.setOnClickListener {
            val treatment = treatments[holder.adapterPosition]
            callback(treatment)
        }
        return holder
    }


    class TreatmentViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val name = view.txtTreatmentName
    }
}