package com.example.ggglo.weather_girl

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.google.firebase.analytics.FirebaseAnalytics

class Geocoding: AppCompatActivity(){

    private lateinit var btn_search: Button
    private lateinit var txt_location: EditText
    private lateinit var sw_remember_geo: Switch
    private lateinit var btn_geo: ImageButton

    private val PREF_SAVED_LOCATION = "saved_location"
    private val PREF_FILENAME = "Weather_Girl"

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geocoding)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val preferences = getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE)

        btn_search = findViewById(R.id.btn_search)
        txt_location = findViewById(R.id.txt_location)
        sw_remember_geo = findViewById(R.id.sw_remember_geo)
        btn_geo = findViewById(R.id.btn_geo)

        var choosen_address : Address? = null
        var choosen_lat = 0.0
        var choosen_lang = 0.0

        val savedLocation: String = preferences.getString(PREF_SAVED_LOCATION, "")


        if(savedLocation != ""){
            txt_location.setText(savedLocation)

        }

        btn_geo.setOnClickListener{

            val location = txt_location.text.toString()

            val addressList: List<Address> = Geocoder(this).getFromLocationName(location, 3)

            val arrayAdpter = ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice)

            addressList.forEach{ address ->
                arrayAdpter.add(address.getAddressLine(0))
            }

            AlertDialog.Builder(this)
                    .setTitle("Select Address")
                    .setAdapter(arrayAdpter){dialog, which ->

                        choosen_address = addressList[which]
                        //Toast.makeText(this, "You picked: $choosen_address", Toast.LENGTH_SHORT).show()


                        choosen_lat = choosen_address!!.latitude
                        choosen_lang = choosen_address!!.longitude

                        //Toast.makeText(this, "lat: $choosen_lat", Toast.LENGTH_SHORT).show()
                        //Toast.makeText(this, "lon: $choosen_lang", Toast.LENGTH_SHORT).show()

                        btn_search.isEnabled = true
                        firebaseAnalytics.logEvent("geo_complete", null)

                    }
                    .setNegativeButton("Cancel"){
                        dialog, which ->
                        dialog.dismiss()
                    }
                    .show()


        }
/*
        if(choosen_address != null){
            if (sw_remember_geo.isChecked){
                preferences.edit().putString(PREF_SAVED_LOCATION, choosen_address!!.getAddressLine(0).toString()).apply()
            }
            else{
                preferences.edit().putString(PREF_SAVED_LOCATION, "").apply()
            }
            choosen_lat = choosen_address!!.latitude
            choosen_lang = choosen_address!!.longitude

            Toast.makeText(this, "lat: $choosen_lat", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "lon: $choosen_lang", Toast.LENGTH_SHORT).show()

            btn_search.isEnabled = true
        }
        */

        btn_search.setOnClickListener(){

            if (sw_remember_geo.isChecked){
                preferences.edit().putString(PREF_SAVED_LOCATION, choosen_address!!.getAddressLine(0).toString()).apply()
            }
            else{
                preferences.edit().putString(PREF_SAVED_LOCATION, "").apply()
            }
            val intent = Intent(this, CurrentForecastActivity::class.java)

            intent.putExtra(CurrentForecastActivity.INTENT_LAT, choosen_lat.toString())
            intent.putExtra(CurrentForecastActivity.INTENT_LANG, choosen_lang.toString())

            startActivity(intent)
        }


    }
}