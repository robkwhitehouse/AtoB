package com.example.android.fromatob

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY


class MainActivity : AppCompatActivity() {
    private val LOCATION_PERMISSION_REQ_CODE = 1000;
    private val PERMISSION_ID = 42
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var distanceText: String
    private lateinit var bearingText: String
    private val pointAlocation: Location = Location("gps")
    private val pointBlocation = Location("gps")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListeners()

        distanceText = getString(R.string.distance_text)
        bearingText = getString(R.string.bearing_text)
        // initialize fused location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
     }


    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                } else {
                    // permission denied
                    point_A_latitude.text ="You need to grant permission to access location"

                }
            }
        }
    }

    private fun setListeners() {
        val clickableViews: List<View> = listOf(
            button_A, button_B
        )
        for (item in clickableViews) item.setOnClickListener { buttonClickHandler(it) }
    }

    fun buttonClickHandler(view: View) {
//Check and get permission if necessary
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request permission
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE)
        }
//Now get the location and update the View
         when (view.id) {
                R.id.button_A -> {
                    point_A_latitude.text = "Latitude:"
                    point_A_longitude.text = "Longitude:"
                    point_A_accuracy.text = "Accuracy:"
                    fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null)
                            .addOnSuccessListener(this) { location: Location ->
                                // getting the last known or current location
                                pointAlocation.set(location)
                                point_A_latitude.text = "Latitude: ${location.latitude}"
                                point_A_longitude.text = "Longitude: ${location.longitude}"
                                point_A_accuracy.text = "Accuracy: ${location.accuracy} meters"
                            }
                            .addOnFailureListener {
                                point_A_latitude.text = "Latitude: Error: Could not get Location"
                                point_A_longitude.text = "Longitude: Check app permissions"
                            }
                }
                R.id.button_B -> {
                    point_B_latitude.text = "Latitude:"
                    point_B_longitude.text = "Longitude:"
                    point_B_accuracy.text = "Accuracy:"
                    fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null)
                            .addOnSuccessListener(this) { location: Location ->
                                pointBlocation.set(location)
                                point_B_latitude.text = "Latitude: ${location.latitude}"
                                point_B_longitude.text = "Longitude: ${location.longitude}"
                                point_B_accuracy.text = "Accuracy: ${location.accuracy} meters"
                            }
                            .addOnFailureListener {
                                point_B_latitude.text = "Latitude: Error:Could not get Location"
                                point_A_longitude.text = "Longitude: Check app permissions"
                            }
                }
            }

        if (pointAlocation.hasAccuracy() && pointBlocation.hasAccuracy()) {
            val distance = pointAlocation.distanceTo(pointBlocation).toInt()
            var bearing = pointAlocation.bearingTo(pointBlocation).toInt()
            if (bearing < 0) bearing += 360
            distance_textview.text = getString(R.string.distance_text, distance)
            bearing_textview.text = getString(R.string.bearing_text, bearing)
        }
    }//End of the button click handler
}//end of class MainActivity







