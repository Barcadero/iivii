package client.petmooby.com.br.petmooby.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.adapter.DayEventsAdapter
import client.petmooby.com.br.petmooby.extensions.defaultRecycleView
import client.petmooby.com.br.petmooby.extensions.getDefaultLayoutManager
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.util.DateTimeUtil
import client.petmooby.com.br.petmooby.util.DrawableUtils
import client.petmooby.com.br.petmooby.util.VariablesUtil
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.utils.DateUtils
import com.facebook.FacebookSdk.getApplicationContext
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class CalendarFragment : Fragment() {

    val TAG = "CALENDAR"
    val MAX_DAYS = 370
    val MIN_DAYS = -35


    var rcView: RecyclerView? =null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    var listForAdapter = mutableListOf<Detail>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(R.id.toolbar, getString(R.string.Calendar))

        val min = Calendar.getInstance()
        min.add(Calendar.DAY_OF_MONTH, MIN_DAYS)

        val max = Calendar.getInstance()
        max.add(Calendar.DAY_OF_MONTH, MAX_DAYS)

        calendarView.setMinimumDate(min)
        calendarView.setMaximumDate(max)

        llCalender.visibility = View.VISIBLE
        rcListOfEvents.layoutManager = getDefaultLayoutManager()
        rcListOfEvents.adapter = DayEventsAdapter(listForAdapter, activity!!)

        ivCalender.setImageDrawable(DrawableUtils.getThreeDots(activity!!))
        calendarView.setOnDayClickListener { eventDay ->
            listForAdapter.clear()
            if(VariablesUtil.gbAnimals != null){
                for(animal in VariablesUtil.gbAnimals!!){
                    if(animal.vaccineCards != null){
                        //Here we need to compare the dates get only ones related with the selected day
                        val vaccines = animal.vaccineCards!!.filter {
                            DateTimeUtil.getOnlyDate(it.nextRemember)
                                    .compareTo(DateTimeUtil.getOnlyDate(eventDay.calendar.time)) == 0
                        }
                        for(vaccine in vaccines) {
                            val detail = Detail(animal.photo!!, vaccine.vaccine_type!!, animal.name!!)
                            listForAdapter.add(detail)
                        }

                    }
                }
                if(listForAdapter.size > 0) {
                    llCalender.visibility = View.GONE
                    rcListOfEvents.adapter?.notifyDataSetChanged()
                }else{
                    llCalender.visibility = View.VISIBLE
                }
            }

        }
        calendarView.setEvents(getVaccinesEvents())

    }

    private fun getVaccinesEvents() : List<EventDay>{
        var list = mutableListOf<EventDay>()
        if(VariablesUtil.gbAnimals != null){
            for(animal in VariablesUtil.gbAnimals!!){
                for(vaccine in animal.vaccineCards!!){
                    if(vaccine.nextRemember != null && vaccine.nextRemember!!.after(DateTimeUtil.addDaysAsDate(MIN_DAYS))){
                        list.add(EventDay(DateTimeUtil.dateAsCalendar(vaccine.nextRemember),DrawableUtils.getThreeDots(activity!!)))
                    }
                }
            }
        }
        return list
    }

    data class Detail(val urlPhoto: String, val description:String, val name: String)

}
