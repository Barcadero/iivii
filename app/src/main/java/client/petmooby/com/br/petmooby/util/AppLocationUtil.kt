package client.petmooby.com.br.petmooby.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.maps.model.LatLng
import javax.inject.Singleton

@Singleton
class AppLocationUtil(private val context: Context) {
    private var bestLocation: Location? = null

    @SuppressLint("MissingPermission")
    fun getLocation(): LatLng? {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var providers: List<String?>? = null
        providers = locationManager.getProviders(true)
        bestLocation = null
        for (provider in providers) {
            val location: Location = locationManager.getLastKnownLocation(provider!!)
                    ?: continue
            if (bestLocation == null || location.accuracy < bestLocation?.accuracy!!) {
                bestLocation = location
            }
        }
        var bestLatLngLocation: LatLng? = null
        if (bestLocation != null) {
            bestLatLngLocation = LatLng(bestLocation?.latitude!!, bestLocation?.longitude!!)
        }
        return bestLatLngLocation
    }

}