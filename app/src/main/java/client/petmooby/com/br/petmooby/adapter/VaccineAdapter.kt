package client.petmooby.com.br.petmooby.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.util.DateTimeUtil
import kotlinx.android.synthetic.main.vaccine_adapter_layout.view.*

/**
 * Created by Rafael Rocha on 06/08/2019.
 */
class VaccineAdapter (val vaccines: List<Animal.VaccineCards>,
                     val onClick : (Animal.VaccineCards) -> Unit) : RecyclerView.Adapter<VaccineAdapter.VaccineHolder>(){
    override fun getItemCount(): Int = vaccines.size

    override fun onBindViewHolder(holder: VaccineHolder, position: Int) {
        val vaccine = vaccines[position]
        holder.vaccineName.text = vaccine.vaccine_type
        holder.dateNext.text = DateTimeUtil.formatDateTime(vaccine.nextRemember)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): VaccineHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.vaccine_adapter_layout,parent,false)
        val holder = VaccineHolder(layout)
        holder.itemView.setOnClickListener {
            val vaccine = vaccines[holder.adapterPosition]
            onClick(vaccine)
        }
        return holder
    }


    class VaccineHolder(view : View) : RecyclerView.ViewHolder(view){
        var vaccineName = view.txtVaccineName
        var dateNext    = view.txtDateNext

    }
}