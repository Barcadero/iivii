package client.petmooby.com.br.petmooby.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.util.DateTimeUtil
import client.petmooby.com.br.petmooby.util.DrawableUtils
import client.petmooby.com.br.petmooby.util.VariablesUtil
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.utils.DateUtils
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class CalendarFragment : Fragment() {

    val TAG = "CALENDAR"



    var rcView: RecyclerView? =null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(R.id.toolbar, getString(R.string.Calendar))
        val events = mutableListOf<EventDay>()

        val calendar = Calendar.getInstance()
        events.add(EventDay(calendar, R.drawable.icons8_pegada_de_urso_30))
        events.add(EventDay(calendar, R.drawable.icons8_calendar_30))

        val calendar4 = Calendar.getInstance()
        calendar4.add(Calendar.DAY_OF_MONTH, 13)
        events.add(EventDay(calendar4, DrawableUtils.getThreeDots(activity!!)))

        calendarView.setMinimumDate(DateTimeUtil.addDaysAsCalendar(-35))
        calendarView.setMaximumDate(DateTimeUtil.addDaysAsCalendar(370))
        //calendarView.selectedDates = getSelectedDays()
        calendarView.setEvents(events)

    }

    private fun getVaccinesEvents(){
//        if(VariablesUtil.gbSelectedAnimal != null){
//            if(VariablesUtil.gbSelectedAnimal){
//
//            }
//        }
    }
//    private fun getSelectedDays(): List<Calendar> {
//        val calendars = mutableListOf<Calendar>()
//
//        for (i in 0..9) {
//            val calendar = DateUtils.getCalendar()
//            calendar.add(Calendar.DAY_OF_MONTH, i)
//            calendars.add(calendar)
//        }
//
//        return calendars
//    }


}
