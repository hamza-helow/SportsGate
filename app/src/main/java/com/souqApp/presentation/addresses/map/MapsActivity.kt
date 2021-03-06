package com.souqApp.presentation.addresses.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.souqApp.R
import com.souqApp.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import android.location.LocationManager
import com.google.android.gms.location.LocationServices
import com.souqApp.infra.utils.LOCATION_USER


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        binding.btnSubmit.setOnClickListener {
            val intent = Intent()
            intent.putExtra(LOCATION_USER, mMap.cameraPosition.target)
            setResult(RESULT_OK , intent)
            finish()
        }

        handlerPermissions()

    }

    private fun handlerPermissions() {
        val requestMultiplePermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

                var granted = 0
                permissions.entries.forEach {
                    Log.e("DEBUG", "${it.key} = ${it.value}")
                    if (it.value)
                        granted++
                }
                if (granted == 2) {
                    //  whenPermissionsGranted()

                    getDeviceLocation()
                }
            }

        requestMultiplePermissions.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    override fun onLocationChanged(location: Location) {
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
            LatLng(
                location.latitude,
                location.longitude
            ), 16f
        )
        mMap.animateCamera(cameraUpdate)
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        mMap.isMyLocationEnabled = true

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {

            val location = it.result
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    location.latitude,
                    location.longitude
                ), 16f
            )

            mMap.animateCamera(cameraUpdate)

        }


    }

}