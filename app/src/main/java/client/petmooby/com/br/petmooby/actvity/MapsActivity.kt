package client.petmooby.com.br.petmooby.actvity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.content.ContextCompat
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.util.AppLocationUtil
import client.petmooby.com.br.petmooby.util.PermissionUtils.requestPermission
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, OnRequestPermissionsResultCallback {

    private val SYDNEY = LatLng(-33.862, 151.21)
    private val ZOOM_LEVEL = 13f
    private lateinit var map: GoogleMap
    private var defaultLocation : LatLng? = LatLng(-33.8523341, 151.2106085)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment : SupportMapFragment? = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        with(googleMap){
//            moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY,ZOOM_LEVEL))
//            addMarker(MarkerOptions().position(SYDNEY))
            map = this
        }
        enableMyLocation()
    }

    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        // [START maps_check_location_permission]
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
            defaultLocation = AppLocationUtil(this).getLocation()
            if(defaultLocation == null){
                Toast.makeText(this,getString(R.string.yourLocationIsNotEnable),Toast.LENGTH_SHORT)
                        .show()
            }
            updateCamera(defaultLocation)
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        }
        // [END maps_check_location_permission]
    }

    private fun updateCamera(latLng: LatLng?) {
        if (latLng != null) map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL))

    }

    companion object {
        /**
         * Request code for location permission request.
         *
         * @see .onRequestPermissionsResult
         */
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}