package com.example.ggglo.weather_girl


import android.provider.Settings.Global.getInt
import android.support.v4.content.res.TypedArrayUtils.getText
import android.widget.Toast
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException

import java.util.concurrent.TimeUnit


class WeatherManager(var Key: String, var Lat: Double, var Lang: Double) {

    //private var KEY: String = ""
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().let { builder ->
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)

        //Network timeouts
        builder.connectTimeout(20, TimeUnit.SECONDS)
        builder.readTimeout(20, TimeUnit.SECONDS)
        builder.writeTimeout(20, TimeUnit.SECONDS)
        builder.build()
    }


    fun recieveinfoCurrent(
            errorCallback: (Exception) -> Unit,
            successCallback: (List<String>) -> Unit
    ){
        val request = Request.Builder()
                .url("http://api.weatherbit.io/v2.0/current?lat=$Lat&lon=$Lang&units=I&key=$Key")
                .build()

//https://api.weatherbit.io/v2.0/current?lat=40.979898&lon=-79.804688&units=I&key=774a413d89014c949541db9c040f36bd
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                errorCallback(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString: String? = response.body()?.string()

                if (response.isSuccessful && responseString != null) {
                    //val responceobject = JSONObject(responseString)
                    val data = JSONObject(responseString).getJSONArray("data")
                    //val temp = responceobject.getString("temp")
                    //error: no value for temp?
                    //get array of data then temp
                    val curr = data.getJSONObject(0)
                    val weather = curr.getJSONObject("weather")
                    val weatherIcon = weather.getString("icon")
                    val weatherDescription = weather.getString("description")


                    val temp = curr.getInt("temp").toString()
                    //val weather = curr.

                    val arrayWeather = mutableListOf<String>(temp, weatherIcon, weatherDescription)
                    //{ temp, weatherIcon, weatherDescrption}
                    //arrayWeather.add(weatherIcon)
                    //arrayWeather.add(weatherDescription)

                    successCallback(arrayWeather)
                } else {
                    errorCallback(Exception("OAuth returned unsuccessfully"))
                }
    }
})
    }
    fun recieveinfo3Days(
            errorCallback: (Exception) -> Unit,
            successCallback: (List<List<String>>) -> Unit
    ){
        val request = Request.Builder()
                .url("https://api.weatherbit.io/v2.0/forecast/3hourly?lat=$Lat&lon=$Lang&units=I&key=$Key")
                .build()


        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                errorCallback(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString: String? = response.body()?.string()

                if (response.isSuccessful && responseString != null) {
                    //val responceobject = JSONObject(responseString)
                    val data = JSONObject(responseString).getJSONArray("data")
                    //val temp = responceobject.getString("temp")
                    //error: no value for temp?
                    //get array of data then temp

                    val arrayList = mutableListOf<List<String>>()

                    for(i in 0..7){
                        val curr = data.getJSONObject(i)
                        val temp = curr.getInt("temp").toString()
                        val timeish = curr.getString("timestamp_local")
                        val weather = curr.getJSONObject("weather")
                        val weatherIcon = weather.getString("icon")
                        val weatherDescription = weather.getString("description")


                        val arrayWeather = mutableListOf<String>(temp, weatherIcon, weatherDescription, timeish)

                        arrayList.add(arrayWeather)


                    }

                    /*
                    val curr = data.getJSONObject(0)
                    val temp = curr.getInt("temp").toString()
                    val weather = curr.getJSONObject("weather")
                    val weatherIcon = weather.getString("icon")
                    val weatherDescription = weather.getString("description")
                    //val weather = curr.

                    val arrayWeather = mutableListOf<String>(temp, weatherIcon, weatherDescription)
                    //val arrayList = mutableListOf<List<String>>(arrayWeather)
                    //{ temp, weatherIcon, weatherDescrption}
                    //arrayWeather.add(weatherIcon)
                    //arrayWeather.add(weatherDescription)

                    */
                    successCallback(arrayList)
                } else {
                    errorCallback(Exception("OAuth returned unsuccessfully"))
                }
            }
        })
    }


}