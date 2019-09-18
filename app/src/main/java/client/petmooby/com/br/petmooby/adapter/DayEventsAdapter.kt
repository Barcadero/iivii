package client.petmooby.com.br.petmooby.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.transferr.passenger.extensions.loadUrl
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.fragment.CalendarFragment
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class DayEventsAdapter(val events : List<CalendarFragment.Detail>, val context: Context) : RecyclerView.Adapter<DayEventsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.adapter_calender_detail,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = events.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        with(holder){
            description.text = event.description
            name.text        = event.name
            progress.visibility = View.VISIBLE
            photo.visibility = View.INVISIBLE
            Picasso.with(context).load(event.urlPhoto).fit()
                    .placeholder(R.drawable.ic_camera)
                    .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                    .into(photo!!,
                    object : com.squareup.picasso.Callback{
                        override fun onSuccess() {
                            progress.visibility = View.GONE
                            photo.visibility = View.VISIBLE

                        }

                        override fun onError() {
                            progress.visibility = View.GONE
                            photo.visibility = View.VISIBLE
                        }
                    }
            )
        }
    }


    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var description = view.findViewById<TextView>(R.id.txtDetailDescription)
        var name        = view.findViewById<TextView>(R.id.txtDetailName)
        var photo       = view.findViewById<ImageView>(R.id.ivDetailPhoto)
        var progress   = view.findViewById<ProgressBar>(R.id.detailProgress)
    }
}