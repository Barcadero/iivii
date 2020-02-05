package client.petmooby.com.br.petmooby.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.util.DateTimeUtil
import kotlinx.android.synthetic.main.adapter_attendance_list_layout.view.*
import kotlinx.android.synthetic.main.vaccine_adapter_layout.view.*
import kotlinx.android.synthetic.main.vaccine_adapter_layout.view.viewVaccineList

/**
 * Created by Rafael Rocha on 06/08/2019.
 */
class AttendanceAdapter (val attendances: List<Animal.VetConsultation>,
                         val onClick : (Animal.VetConsultation) -> Unit) : RecyclerView.Adapter<AttendanceAdapter.VaccineHolder>(){
    override fun getItemCount(): Int = attendances.size

    override fun onBindViewHolder(holder: VaccineHolder, position: Int) {
        val attendance = attendances[position]
        holder.vetName.text = attendance.nameVet
        holder.attDate.text = DateTimeUtil.formatDateTime(attendance.date)
        if(position == itemCount - 1){
            holder.viewAttendanceList.visibility = GONE
        }else{
            holder.viewAttendanceList.visibility = VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): VaccineHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.adapter_attendance_list_layout,parent,false)
        val holder = VaccineHolder(layout)
        holder.itemView.setOnClickListener {
            val vaccine = attendances[holder.adapterPosition]
            onClick(vaccine)
        }
        return holder
    }


    class VaccineHolder(view : View) : RecyclerView.ViewHolder(view){
        var vetName             = view.txtAttendanceListDescription
        var clinicName          = view.txtAttendanceListClinic
        val attDate             = view.txtAttendanceListDate
        val viewAttendanceList  = view.viewAttendanceList
    }
}