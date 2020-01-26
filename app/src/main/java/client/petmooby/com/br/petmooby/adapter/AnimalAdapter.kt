package client.petmooby.com.br.petmooby.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.model.Animal
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_my_animal_list.view.*

/**
 * Created by Rafael Rocha on 15/07/2019.
 */
class AnimalAdapter (
        val animals: List<Animal>,
        val onClick: (Animal) -> Unit) : RecyclerView.Adapter<AnimalAdapter.AnimalHolder>(){

    override fun onBindViewHolder(holder: AnimalHolder, position:  Int) {
        val animal = animals[position]
        val context = holder.itemView.context
        holder.txtPetName.text              = animal.name
        holder.txtKind.text                 = animal.breed
        holder.ivProfile.visibility         = INVISIBLE
        if(animal.photo == null)animal.photo = ""
        if(animal.photo!!.isEmpty()){
            holder.ivProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icons8_identidade_de_cachorro_90))
            holder.ivProfile.visibility         = VISIBLE
        }else {
            holder.progressMyAnimal.visibility  = VISIBLE
            Picasso.with(context)
                    .load(if (animal.photo?.isEmpty()!!) null else animal.photo)
                    .error(R.drawable.no_image)
                    .placeholder(R.drawable.icons8_identidade_de_cachorro_90)
                    .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                    .memoryPolicy(MemoryPolicy.NO_STORE)
                    .fit()
                    .into(holder.ivProfile, object : Callback {
                        override fun onSuccess() {
                            holder.progressMyAnimal.visibility = GONE
                            holder.ivProfile.visibility = VISIBLE
                        }

                        override fun onError() {
                            holder.progressMyAnimal.visibility = GONE
                            holder.ivProfile.visibility = VISIBLE
                        }

                    })
        }

        //}
        holder.itemView.setOnClickListener {onClick(animal)}
        if(position == itemCount - 1){
            holder.viewSeparator.visibility = GONE
        }else{
            holder.viewSeparator.visibility = VISIBLE
        }
    }

    override fun getItemCount() = animals.size

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): AnimalHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_my_animal_list,parent,false)
        return AnimalHolder(view)
    }


    class AnimalHolder(view: View) : RecyclerView.ViewHolder(view){
        var txtPetName = view.findViewById<TextView>(R.id.txtNamePet)
        var txtKind     = view.findViewById<TextView>(R.id.txtMyPetKind)
        var ivProfile = view.findViewById<ImageView>(R.id.ivProfileMyPet)
        var progressMyAnimal = view.findViewById<ProgressBar>(R.id.progressMyAnimal)
        val viewSeparator = view.viewAnimalList
    }
}