package client.petmooby.com.br.petmooby.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.util.DateTimeUtil

/**
 * Created by Rafael Rocha on 06/08/2019.
 */
class HistoricVaccineAdapter(val historic:List<Animal.Historic>, val onDeleteClick: (historic: Animal.Historic) -> Unit) : RecyclerView.Adapter<HistoricVaccineAdapter.HistoricViewHolder>(){
    override fun getItemCount(): Int = historic.size

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): HistoricViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.historic_vaccines_adapter,parent,false)
        return HistoricViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoricViewHolder, position: Int) {
        val historic = historic[position]
        holder.notes.text   = historic.observation
        holder.doctor.text  = historic.veterinary
        holder.date.text    = DateTimeUtil.formatDateTime(historic.date)
        holder.price.text   = historic.value.toString()
        holder.btnDelete.setOnClickListener {
            onDeleteClick
        }
    }

    class HistoricViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var notes = view.findViewById<TextView>(R.id.edtHistoricNotes)
        var doctor = view.findViewById<TextView>(R.id.edtDoctorName)
        var date = view.findViewById<TextView>(R.id.edtDateHistoric)
        var price = view.findViewById<TextView>(R.id.edtHistoricPrice)
        var btnDelete = view.findViewById<ImageButton>(R.id.btnExcludeHistoric)
    }

}