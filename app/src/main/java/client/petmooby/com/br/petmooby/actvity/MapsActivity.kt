package client.petmooby.com.br.petmooby.actvity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.application.Application
import client.petmooby.com.br.petmooby.model.api.response.MapsVetResponse
import client.petmooby.com.br.petmooby.model.enums.StatusVetsMaps
import client.petmooby.com.br.petmooby.ui.viewmodel.MapsViewModel
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

    private val ZOOM_LEVEL = 13f
    private lateinit var map: GoogleMap
    private val mapsViewModel : MapsViewModel by viewModels()
    private var defaultLocation : LatLng? = LatLng(-33.8523341, 151.2106085)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment : SupportMapFragment? = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        initObservers()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        with(googleMap){
            map = this
        }
        enableMyLocation()
        setupMarkClick()
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
            }else{
                val location = "${defaultLocation?.latitude?:0},${defaultLocation?.longitude?:0}"
                val language = getDeviceLanguage()
                mapsViewModel.getVetsNear(location,language)
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

    private fun getDeviceLanguage() : String{
        return if(Application.DEVICE_LANGUAGE == Application.LANG_PT){
            "pt-BR"
        }else{
            "en"
        }
    }

    private fun initObservers(){
        mapsViewModel.vetsNearLiveData.observe(this, Observer {resource->
            when(resource.status()){
                StatusVetsMaps.SUCCESS -> {
                    setVetClinicsOnTheMap(resource.data()!!)
                }else ->{
                    Toast.makeText(this,getString(R.string.errorUpdateMapWithVets),Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setVetClinicsOnTheMap(response : MapsVetResponse){
        response.results.forEach {place ->
            addMarkOnTheMap(LatLng(place.geometry.location.lat, place.geometry.location.lng), place.name)
        }
    }

    private fun addMarkOnTheMap(latLng: LatLng, vetName : String){
        map.addMarker(MarkerOptions().apply {
            this.position(latLng)
            this.title(vetName)
            this.snippet("See where this appears")
        })
    }

    private fun setupMarkClick(){
        map.setOnMarkerClickListener {
            callMapGPSApp(LatLng(it.position.latitude,it.position.longitude))
            false
        }
    }

    private fun callMapGPSApp(latLng: LatLng){
        val geo = "geo:${latLng.latitude},${latLng.longitude}"
        val uri = Uri.parse(geo)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = uri
        }
        startActivity(intent)
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