package client.petmooby.com.br.petmooby.util

import android.os.Build
import androidx.annotation.RequiresApi
import client.petmooby.com.br.petmooby.model.BirthDate
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit
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

        @RequiresApi(Build.VERSION_CODES.O)
        fun getAgeInYears(year: Int, month: Int, dayOfMonth: Int): Int {
            return Period.between(
                    LocalDate.of(year, month, dayOfMonth),
                    LocalDate.now()
            ).years
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getAgeInMonths(year: Int, month: Int, dayOfMonth: Int): Long {
            return ChronoUnit.MONTHS.between(
                    LocalDate.of(year, month, dayOfMonth),
                    LocalDate.now()
            )
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getAgeWithMonths(birthDate: Date):BirthDate{
            val calendar    = dateToCalendar(birthDate)
            val years       = getAgeInYears(calendar?.get(Calendar.YEAR)!!, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            val totalMonths = getAgeInMonths(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            val month       = totalMonths - (years*12)
            return BirthDate(years,month.toInt())
        }
    }
}