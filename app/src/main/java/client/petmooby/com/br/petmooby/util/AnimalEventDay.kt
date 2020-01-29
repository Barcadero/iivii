package client.petmooby.com.br.petmooby.util

import android.graphics.drawable.Drawable
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.model.metadata.Detail
import com.applandeo.materialcalendarview.EventDay
import java.util.*

class AnimalEventDay(day: Calendar?) : EventDay(day!!,R.drawable.sample_three_icons) {

    private var detail:Detail?=null
    var details:MutableList<Detail> = mutableListOf()
    constructor(day: Calendar?, detail: Detail) : this(day) {
        this.detail = detail
        this.details.add(detail)
    }
}