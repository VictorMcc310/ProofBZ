package com.example.proofbz.Helper

import android.app.Activity
import android.widget.Toast
import com.example.proofbz.Intertaces.ConnectInterface
import com.example.proofbz.Main.ClimaActivity
import com.example.proofbz.R
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.HashMap

class HelperConnect {
    private var urlMode: modeURL = modeURL()

    fun forecast(map: HashMap<String, Any>, activity: Activity){
        activity as ClimaActivity
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(urlMode.principal)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api: ConnectInterface = retrofit.create(ConnectInterface::class.java)
        val call: Call<String?>? = api.forecast(map["latitude"].toString(),map["longitude"].toString(),map["current"].toString(),map["hourly"].toString())
        call?.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val jsonObject = JSONObject(response.body().toString())
                        val jsonCurrent_units = JSONObject(jsonObject.getString("current_units"))
                        val jsoncurrent = JSONObject(jsonObject.getString("current"))
                        println("Status separado " +jsoncurrent.get("temperature_2m")+ jsonCurrent_units.get("temperature_2m"))
                        activity.txtTemperatura.text = jsoncurrent.get("temperature_2m").toString()+" "+ jsonCurrent_units.get("temperature_2m").toString()
                    } else
                        println(" response.body() ==> null  hh")
                } else
                    println("response.isSuccessful " + response.isSuccessful)
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                val helperDialogs = HelperDialog(activity)
                helperDialogs.showToast(activity.resources.getString(R.string.error) + " " + t.message)
            }
        })
    }
}