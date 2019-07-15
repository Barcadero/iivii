package client.petmooby.com.br.petmooby.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.extensions.toast
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.model.Tip
import client.petmooby.com.br.petmooby.model.metadata.Description
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.jetbrains.anko.toast
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class CalendarFragment : Fragment() {

    val TAG = "CALENDAR"



    var rcView:RecyclerView? =null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(R.id.toolbar, getString(R.string.Calendar))


    }

    /*

    var i:Int = 0
        btnSeeDate.setOnClickListener {
            var docRefUser = FirebaseFirestore.getInstance().collection(CollectionsName.TIP)
            ++i
            var tip = Tip()
            with(tip){
                date = Date()
                photo = "https://www.ictq.com.br/images/varejo_farmaceutico/FARMACEUTICO-VETERINARIO-ICTQ.jpg"
                name = "Helio Gaspar de Melo"
                desc = Description()
                title = Description()
                title?.PT_BR = "SPIRULINA PARA OS PETS? _ num $i"
                title?.EN_US = "SPIRULINA FOR PETS? _ num $i"
                desc?.EN_US = "Do you know the alga Spirulina? Considered the \"richest food in the world\", nutritionally speaking, this alga has much to add to your health and the health of your pet!\n" +
                        "\n" +
                        "The earliest reports of the use of Spirulina in food date from prehistory, from the information that hunter tribes consumed filamentous algae to enrich their diets, which were collected from alkaline lakes.\n" +
                        "\n" +
                        "Spirulina has high protein value, high digestibility and has significant amounts of polyunsaturated fatty acids, vitamins, minerals, essential amino acids, among other nutrients, and can be used in food and feed. It is considered to be one of the richest sources of provitamin A (beta-carotene) and absorbable iron. Because it contains a large amount of B-complex vitamins, iron, copper, proteins and amino acids, Spirulina is a great ally in the fight against anemia."
                desc?.PT_BR = "Você conhece a alga Spirulina? Considerado o \"alimento mais rico do mundo\", nutricionalmente falando, essa alga tem muito o que agregar à sua saúde e à saúde do seu pet! \n" +
                        "\n" +
                        "Os primeiros relatos do uso da Spirulina na alimentação datam da pré-história, a partir da informação de que tribos de caçadores consumiam algas filamentosas para enriquecer suas dietas, as quais eram coletadas de lagos alcalinos. \n" +
                        "\n" +
                        "A Spirulina possui alto valor proteico, alta digestibilidade e apresenta quantidades significativas de ácidos graxos poliinsaturados, vitaminas, minerais, aminoácidos essenciais, entre outros nutrientes, podendo ser utilizada na alimentação humana e animal. É considerada uma das fontes mais ricas em provitamina A (beta-caroteno) e de ferro absorvível. Por possuir grande quantidade de vitaminas do complexo B, ferro, cobre, proteínas e aminoácidos, a alga Spirulina é uma grande aliada no combate à anemia. "
            }
            docRefUser.add(tip).addOnSuccessListener {
                toast("User saved")
            }.addOnFailureListener {
                exception -> toast("Erro ${exception.message}")
            }
        }
     */

}// Required empty public constructor
