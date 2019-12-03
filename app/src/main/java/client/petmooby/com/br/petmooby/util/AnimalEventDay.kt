package client.petmooby.com.br.petmooby.util

import android.graphics.drawable.Drawable
import client.petmooby.com.br.petmooby.model.metadata.Detail
import com.applandeo.materialcalendarview.EventDay
import java.util.*

class AnimalEventDay(day: Calendar?, drawable: Drawable?) : EventDay(day, drawable) {

    private var detail:Detail?=null
    var details:MutableList<Detail> = mutableListOf()
    constructor(day: Calendar?, drawable: Drawable?, detail: Detail) : this(day,drawable) {
        this.detail = detail
        this.details.add(detail)
    }
}