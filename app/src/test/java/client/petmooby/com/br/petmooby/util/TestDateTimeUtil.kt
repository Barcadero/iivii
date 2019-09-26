package client.petmooby.com.br.petmooby.util

import org.junit.Test
import java.util.concurrent.TimeUnit

class TestDateTimeUtil {


    @Test fun differFromDate(){
        var dateNow     = DateTimeUtil.getDate(2019,8,25,19,27,32)
        var dateFuture  = DateTimeUtil.getDate(2019,8,26,19,12,32)
        var hours = DateTimeUtil.getDateDiff(dateNow,dateFuture,TimeUnit.HOURS)
        println("differ in hours: $hours")
    }
}