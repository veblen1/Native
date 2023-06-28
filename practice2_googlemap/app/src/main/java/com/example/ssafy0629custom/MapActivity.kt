package com.example.ssafy0629custom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val multicampus = LatLng(37.5013068,127.0385654)
        googleMap?.addMarker(
            MarkerOptions()
                .position(multicampus)
                .title("역삼 멀티캠퍼스")
        )

        val multicampus2 = LatLng(36.3552,127.298)
        googleMap?.addMarker(
            MarkerOptions()
                .position(multicampus2)
                .title("대전 캠퍼스")
        )

        val multicampus3 = LatLng(35.2045,126.8075)
        googleMap?.addMarker(
            MarkerOptions()
                .position(multicampus3)
                .title("광주 캠퍼스")
        )

        val multicampus4 = LatLng(36.1073,128.4153)
        googleMap?.addMarker(
            MarkerOptions()
                .position(multicampus4)
                .title("구미 캠퍼스")
        )

        val multicampus5 = LatLng(35.0958,128.8566)
        googleMap?.addMarker(
            MarkerOptions()
                .position(multicampus5)
                .title("부울경 캠퍼스")
        )

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(multicampus2,16F))

    }

}
