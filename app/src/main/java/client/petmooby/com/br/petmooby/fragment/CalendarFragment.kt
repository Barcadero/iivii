package client.petmooby.com.br.petmooby.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.adapter.DayEventsAdapter
import client.petmooby.com.br.petmooby.extensions.getDefaultLayoutManager
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.model.enums.EnumTypeInterval
import client.petmooby.com.br.petmooby.model.enums.EnumTypePeriod
import client.petmooby.com.br.petmooby.model.metadata.Detail
import client.petmooby.com.br.petmooby.util.AnimalEventDay
import client.petmooby.com.br.petmooby.util.DateTimeUtil
import client.petmooby.com.br.petmooby.util.DrawableUtils
import client.petmooby.com.br.petmooby.util.VariablesUtil
import com.applandeo.materialcalendarview.EventDay
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
    var dateFinalCalendar:Date?=null
    var dateInitialCalendar:Date?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(R.id.toolbar, getString(R.string.Calendar))

        val min = Calendar.getInstance()
        min.add(Calendar.DAY_OF_MONTH, MIN_DAYS)
        dateInitialCalendar = min.time

        val max = Calendar.getInstance()
        max.add(Calendar.DAY_OF_MONTH, MAX_DAYS)
        dateFinalCalendar = max.time

        calendarView.setMinimumDate(min)
        calendarView.setMaximumDate(max)

        llCalender.visibility = View.VISIBLE
        rcListOfEvents.layoutManager = getDefaultLayoutManager()
        rcListOfEvents.adapter = DayEventsAdapter(listForAdapter, activity!!)

        ivCalender.setImageDrawable(DrawableUtils.getThreeDots(activity!!))
        calendarView.setOnDayClickListener { eventDay ->
            showEventCards(eventDay)

        }
        calendarView.setEvents(getEvents())

    }

    private fun showEventCards(eventDay: EventDay) {
        listForAdapter.clear()
        if(eventDay is AnimalEventDay) {
            for (animal in eventDay.details) {
                listForAdapter.add(animal)
            }
            if (listForAdapter.size > 0) {
                llCalender.visibility = View.GONE
                rcListOfEvents.adapter?.notifyDataSetChanged()
            } else {
                llCalender.visibility = View.VISIBLE
            }
        }else{
            llCalender.visibility = View.VISIBLE
        }

    }

    private fun getEvents(): List<AnimalEventDay>{
        var list = mutableListOf<AnimalEventDay>()
        if(VariablesUtil.gbAnimals != null){
            for(animal in VariablesUtil.gbAnimals!!){
                if(animal.vaccineCards != null && animal.vaccineCards!!.size > 0) {
                    for (vaccine in animal.vaccineCards!!) {
                        if (vaccine.nextRemember != null && vaccine.nextRemember!!.after(DateTimeUtil.addDaysAsDate(MIN_DAYS))) {
                            val detail = Detail(animal.photo!!, vaccine.vaccine_type!!, animal.name!!)
                            addEventIfExist(list, vaccine.nextRemember!!, detail)
                        }
                    }
                }

                if(animal.treatmentCard != null && animal.treatmentCard!!.size > 0){
                    for(treatment in animal.treatmentCard!!){
                        // if treatment has final date so show until that date
                        var treatmentIsActive = true
                        if(treatment.dateInitial?.before(dateInitialCalendar)!!){
                            treatmentIsActive = false
                        }
                        if(treatment.dateFinal != null && treatment.dateFinal?.after(dateFinalCalendar)!!){
                            treatmentIsActive = false
                        }
                        if (treatmentIsActive) {
                            var desc = "${treatment.typeTreatment?.toString()} : ${treatment.name}"
                            var detail = Detail(animal.photo!!, desc, animal.name!!)
                            when(treatment.typeInterval){
                                EnumTypeInterval.YEAR -> {
                                    var dateEvent = treatment.dateInitial
                                    for (year in 0..3){
                                        dateEvent = DateTimeUtil.addYearsToADate(dateEvent, treatment.timeInterval?.toInt()!!)
                                        if(dateEvent.after(dateFinalCalendar)) break
                                        if(treatment.typePeriod == EnumTypePeriod.INFORMED){
                                            if(dateEvent.after(treatment.dateFinal)){
                                                break
                                            }
                                        }
                                        addEventIfExist(list, dateEvent, detail)
                                    }
                                }
                                EnumTypeInterval.MONTH ->{
                                    var dateEvent = treatment.dateInitial
                                    for (month in 0..15){
                                        dateEvent = DateTimeUtil.addMonthsToADate(dateEvent, treatment.timeInterval?.toInt()!!)
                                        if(dateEvent.after(dateFinalCalendar)) break
                                        if(treatment.typePeriod == EnumTypePeriod.INFORMED){
                                            if(dateEvent.after(treatment.dateFinal)){
                                                break
                                            }
                                        }
                                        addEventIfExist(list, dateEvent, detail)
                                    }
                                }

                                EnumTypeInterval.DAY, EnumTypeInterval.HOUR ->{
                                    var dateEvent = treatment.dateInitial
                                    if(treatment.typeInterval == EnumTypeInterval.HOUR){
                                        desc = if(treatment.timeInterval!! > 0){
                                            "${getString(R.string.every)} ${treatment.timeInterval} ${getString(R.string.hours)} }"
                                        }else{
                                            "${getString(R.string.every)} ${getString(R.string.oneHour)} ${getString(R.string.hour)} }"
                                        }
                                        detail = Detail(animal.photo!!, desc, animal.name!!)
                                    }
                                    for (day in 0..500){
                                        dateEvent = DateTimeUtil.addDaysToADate(dateEvent, treatment.timeInterval?.toInt()!!)
                                        if(dateEvent.after(dateFinalCalendar)) break
                                        if(treatment.typePeriod == EnumTypePeriod.INFORMED){
                                            if(dateEvent.after(treatment.dateFinal)){
                                                break
                                            }
                                        }
                                        addEventIfExist(list, dateEvent, detail)
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
        return list

    }

    private fun addEventIfExist(list: MutableList<AnimalEventDay>, nextRemember: Date, detail: Detail) {
        var tempList = list.filter { DateTimeUtil.getOnlyDate(it.calendar.time) == DateTimeUtil.getOnlyDate(nextRemember) }
        if (tempList.isNotEmpty()) {
            var tempEvent = tempList[0]
            if (tempEvent.details.isNotEmpty()) {
                tempEvent.details.add(detail)
            } else {
                list.add(AnimalEventDay(DateTimeUtil.dateAsCalendar(nextRemember), DrawableUtils.getThreeDots(activity!!), detail))
            }
        } else {
            list.add(AnimalEventDay(DateTimeUtil.dateAsCalendar(nextRemember), DrawableUtils.getThreeDots(activity!!), detail))
        }
    }


}
