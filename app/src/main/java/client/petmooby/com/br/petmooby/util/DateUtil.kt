package client.petmooby.com.br.petmooby.util

import java.util.*

class DateUtil {

    companion object {
        fun getAge(year: Int, month: Int, day: Int): Int? {
            val dob: Calendar = Calendar.getInstance()
            val today: Calendar = Calendar.getInstance()
            dob.set(year, month, day)
            var age: Int = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--
            }
            return age
        }

        fun getAge(birthDate:Date): Int? {
            val calendar = dateToCalendar(birthDate)
            return getAge(calendar?.get(Calendar.YEAR)!!, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        }

        fun dateToCalendar(date: Date): Calendar? {
            val calendar = Calendar.getInstance()
            calendar.time = date
            return calendar
        }
    }
}