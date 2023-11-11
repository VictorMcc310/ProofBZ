package com.example.proofbz.Main

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.proofbz.Helper.HelperConnect
import com.example.proofbz.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.w3c.dom.Text
import java.util.HashMap

class ClimaActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var permsRequestCode = 100
    var perms = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION)
    lateinit var buttonUpgrade : Button
    lateinit var txtTemperatura: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clima)
        txtTemperatura = findViewById(R.id.textViewTemp)
        buttonUpgrade = findViewById(R.id.buttonUp)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        buttonUpgrade.setOnClickListener { actualizar() }
        if(!permissionsCheck()){
            println("no")
            askForPermits()
        }else{
            println("sí")
          actualizar()
        }

    }

    private fun permissionsCheck(): Boolean {
        var check = false
        if ( checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED )
            check= true
        return check
    }

    private fun askForPermits ()
    {
        requestPermissions(perms, permsRequestCode)
    }

    fun actualizar(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askForPermits()
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->

            try{
                println("Location "+location!!.latitude)
                txtTemperatura.text="--"
                val map: HashMap<String, Any> = HashMap()
                map["latitude"]=location.latitude
                map["longitude"]=location.longitude
                map["current"]="temperature_2m,wind_speed_10m"
                map["hourly"]="temperature_2m,relative_humidity_2m,wind_speed_10m"
                HelperConnect().forecast(map,this)
            }catch (e: Exception){
                askForPermits()
                println("Exception "+e)
            }




        }
    }
}