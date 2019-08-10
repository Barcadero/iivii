package client.petmooby.com.br.petmooby.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import client.petmooby.com.br.petmooby.model.Animal

/**
 * Created by Rafael Rocha on 06/08/2019.
 */
class VaccineAdapter (val vaccines: List<Animal.VaccineCards>,
                     val onClick : (Animal.VaccineCards) -> Unit) : RecyclerView.Adapter<VaccineAdapter.VaccineHolder>(){
    override fun getItemCount(): Int = vaccines.size

    override fun onBindViewHolder(p0: VaccineHolder, p1: Int) {

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VaccineHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    class VaccineHolder(view : View) : RecyclerView.ViewHolder(view){

    }
}