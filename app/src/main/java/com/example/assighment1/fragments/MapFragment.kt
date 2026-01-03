package com.example.assighment1.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.assighment1.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_map, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        return v
    }


    override fun onMapReady(map: GoogleMap) {
        this.googleMap = map


        val defaultLocation = LatLng(32.0853, 34.7818)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
    }


    fun zoom(lat: Double, lon: Double) {
        val location = LatLng(lat, lon)

        googleMap?.let { map ->
            map.clear()
            map.addMarker(MarkerOptions().position(location).title("Score Location"))

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }
    }
}