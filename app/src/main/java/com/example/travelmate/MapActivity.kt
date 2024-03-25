package com.example.travelmate

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    var isPermissionGranted: Boolean = false
    lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map)
        mapView = findViewById(R.id.mapView)
        mapView.getMapAsync(this)
        mapView.onCreate(savedInstanceState)
        checkPermission()

        if (isPermissionGranted) {
            if (checkGooglePlayServices()) {
                // чота будіт
            } else {
                Toast.makeText(this, "Google PlayServices Not Available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkGooglePlayServices(): Boolean {
        val googleApiAvailability: GoogleApiAvailability = GoogleApiAvailability.getInstance()
        val result: Int = googleApiAvailability.isGooglePlayServicesAvailable(this)
        if (result == ConnectionResult.SUCCESS) return true
        else if (googleApiAvailability.isUserResolvableError(result)) {
            val dialog = googleApiAvailability.getErrorDialog(this, result, 201)
            dialog?.setOnCancelListener {
                Toast.makeText(this@MapActivity, "User Canceled Dialog", Toast.LENGTH_SHORT).show()
            }
            dialog?.show()
        }
        return false
    }

    private fun checkPermission() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        isPermissionGranted = true
                        Toast.makeText(this@MapActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
                    } else if (report.isAnyPermissionPermanentlyDenied) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(49.029131, 31.396326)) // Координати центру України
            .zoom(6.0F) // Масштаб
            .build()

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}