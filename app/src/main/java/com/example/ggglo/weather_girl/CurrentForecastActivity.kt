package com.example.ggglo.weather_girl

import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_current_forecast.*

//current forecast

class CurrentForecastActivity : AppCompatActivity() {

    private lateinit var btn_five_forecast: Button
    //private lateinit var btn_test: Button
    private lateinit var weather_image: ImageView
    private lateinit var txt_description: TextView
    private lateinit var txt_temp: TextView

    private lateinit var key: String

    private lateinit var firebaseAnalytics: FirebaseAnalytics


    companion object {
        val INTENT_LAT = "LAT"
        val INTENT_LANG = "LANG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_forecast)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val lang = intent.getStringExtra(INTENT_LANG).toDouble()
        val lat = intent.getStringExtra(INTENT_LAT).toDouble()

        btn_five_forecast = findViewById(R.id.btn_five_forecast)
        //btn_test = findViewById(R.id.btn_test)
        weather_image = findViewById(R.id.weather_image)
        txt_description = findViewById(R.id.txt_description)
        txt_temp = findViewById(R.id.txt_temp)

        key = getString(com.example.ggglo.weather_girl.R.string.WeatherbitKey)


        //retrieve current location and send that info to Weatherbit
        //retrieve current forecast from Weatherbit API
        //use info to select proper UI
        //ex if sunny put a sun picture etc

        //add button to go to five-day forecast
        //pass location in intent

        //I just have to do the networking part and then I am pretty much finished

        //btn_test.setOnClickListener(){
            //Toast.makeText(this,"$key", Toast.LENGTH_SHORT).show()
            WeatherManager(key, lat, lang).recieveinfoCurrent(
                errorCallback = {
                    firebaseAnalytics.logEvent("Complete_API_Faluire", null)
                    runOnUiThread{
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    }
                } ,
                    successCallback = {  arrayweather ->
                    runOnUiThread{
                        firebaseAnalytics.logEvent("Complete_API_Success", null)
                        val temp = arrayweather.component1()
                        val weatherIcon = arrayweather.component2()
                        val weatherDescription = arrayweather.component3()

                        val resID = resources.getIdentifier(weatherIcon, "drawable", packageName)

                        val degreeSymbol = '\u00B0'
                        weather_image.setImageResource(resID)
                        txt_description.setText(weatherDescription)
                        txt_temp.setText(temp + degreeSymbol+ "F")

                        //Toast.makeText(this, "${lang.toString()}", Toast.LENGTH_SHORT).show()

                        //Toast.makeText(this, "$weatherIcon", android.widget.Toast.LENGTH_SHORT).show()
                        //Toast.makeText(this, "$temp", android.widget.Toast.LENGTH_SHORT).show()
                    }

                }
            )
            /*
            weather_image.setImageResource(R.drawable.rain_cloud)
            */
        //}


        btn_five_forecast.setOnClickListener(){
            firebaseAnalytics.logEvent("went_to_next_few_hours", null)
            val intent_five = Intent(this,FiveDayForecastActivity::class.java)
            //Toast.makeText(this, "${lang.toString()}", Toast.LENGTH_SHORT).show()
            val langString = lang.toString()
            val latString = lat.toString()
            intent_five.putExtra(FiveDayForecastActivity.INTENT_LANG_HOUR, langString)
            intent_five.putExtra(FiveDayForecastActivity.INTENT_LAT_HOUR, latString)
            startActivity(intent_five)
        }


    }
}