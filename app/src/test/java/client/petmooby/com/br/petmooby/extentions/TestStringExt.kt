package client.petmooby.com.br.petmooby.extentions

import client.petmooby.com.br.petmooby.extensions.toDate
import org.junit.Test
import org.junit.Assert.*

class TestStringExt {

    @Test
    fun check_if_date_is_after(){
        val dateVaccine = "19/01/2020".toDate("dd/MM/yyyy")
        val dateNow     = "23/01/2020".toDate("dd/MM/yyyy")
        val isAfter = dateNow?.after(dateVaccine)
        assertTrue(isAfter!!)
    }
}