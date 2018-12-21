package com.example.ggglo.weather_girl

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.google.firebase.analytics.FirebaseAnalytics

//This now shows the next 12 hour forecast for weather

class FiveDayForecastActivity : AppCompatActivity() {

    private lateinit var btn_current: Button
    private lateinit var key: String

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var pic_1: ImageView
    private lateinit var txt_description1: TextView
    private lateinit var txt_time1: TextView
    private lateinit var txt_temp1: TextView

    private lateinit var pic_2: ImageView
    private lateinit var txt_description2: TextView
    private lateinit var txt_time2: TextView
    private lateinit var txt_temp2: TextView

    private lateinit var pic_3: ImageView
    private lateinit var txt_description3: TextView
    private lateinit var txt_time3: TextView
    private lateinit var txt_temp3: TextView

    private lateinit var pic_4: ImageView
    private lateinit var txt_description4: TextView
    private lateinit var txt_time4: TextView
    private lateinit var txt_temp4: TextView

    companion object {
        val INTENT_LAT_HOUR = "LAT"
        val INTENT_LANG_HOUR = "LANG"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_five_forecast)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val lang = intent.getStringExtra(INTENT_LANG_HOUR).toDouble()
        val lat = intent.getStringExtra(INTENT_LAT_HOUR).toDouble()


        btn_current = findViewById(R.id.btn_current)
        pic_1 = findViewById(R.id.pic_1)
        txt_description1 = findViewById(R.id.txt_description1)
        txt_temp1 = findViewById(R.id.txt_temp1)
        txt_time1 = findViewById(R.id.txt_time1)

        pic_2 = findViewById(R.id.pic_2)
        txt_description2 = findViewById(R.id.txt_description2)
        txt_temp2 = findViewById(R.id.txt_temp2)
        txt_time2 = findViewById(R.id.txt_time2)

        pic_3 = findViewById(R.id.pic_3)
        txt_description3 = findViewById(R.id.txt_description3)
        txt_temp3 = findViewById(R.id.txt_temp3)
        txt_time3 = findViewById(R.id.txt_time3)

        pic_4 = findViewById(R.id.pic_4)
        txt_description4 = findViewById(R.id.txt_description4)
        txt_temp4 = findViewById(R.id.txt_temp4)
        txt_time4 = findViewById(R.id.txt_time4)


        key = getString(com.example.ggglo.weather_girl.R.string.WeatherbitKey)

        //retrieve current location from intent from currentforecast activity
        //retrieve five-day forecast from Weatherbit API
        //use info to select proper UI
        //ex if sunny put a sun picture etc

        //add button to go back to current forecast
        //pass location in intent

        //I just have to do the networking part and then I am pretty much finished
        WeatherManager(key, lat, lang).recieveinfo3Days(
                errorCallback = {
                    runOnUiThread{
                        firebaseAnalytics.logEvent("Five_API_Faluire", null)
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    }
                } ,
                successCallback = {  arrayweather ->
                    runOnUiThread{
                        firebaseAnalytics.logEvent("Five_API_Success", null)
                        val degreeSymbol = '\u00B0'

                        //1
                        val temp = arrayweather.component1().component1()
                        val weatherIcon = arrayweather.component1().component2()
                        val weatherDescription = arrayweather.component1().component3()
                        val timeish = arrayweather.component1().component4()

                        val resID1 = resources.getIdentifier(weatherIcon, "drawable", packageName)


                        pic_1.setImageResource(resID1)
                        txt_description1.text = weatherDescription
                        txt_temp1.text = (temp + degreeSymbol+ "F")

                        txt_time1.setText(timeish.substring(11,16))

                        //2
                        val temp2 = arrayweather.component2().component1()
                        val weatherIcon2 = arrayweather.component2().component2()
                        val weatherDescription2 = arrayweather.component2().component3()
                        val timeish2 = arrayweather.component2().component4()

                        val resID2 = resources.getIdentifier(weatherIcon2, "drawable", packageName)


                        pic_2.setImageResource(resID2)
                        txt_description2.text = weatherDescription2
                        txt_temp2.text = (temp2 + degreeSymbol+ "F")

                        txt_time2.setText(timeish2.substring(11,16))

                        //3
                        val temp3 = arrayweather.component3().component1()
                        val weatherIcon3 = arrayweather.component3().component2()
                        val weatherDescription3 = arrayweather.component3().component3()
                        val timeish3 = arrayweather.component3().component4()

                        val resID3 = resources.getIdentifier(weatherIcon3, "drawable", packageName)


                        pic_3.setImageResource(resID3)
                        txt_description3.text = (weatherDescription3)
                        txt_temp3.text = (temp3 + degreeSymbol+ "F")

                        txt_time3.setText(timeish3.substring(11,16))

                        //4
                        val temp4 = arrayweather.component4().component1()
                        val weatherIcon4 = arrayweather.component4().component2()
                        val weatherDescription4 = arrayweather.component4().component3()
                        val timeish4 = arrayweather.component4().component4()

                        val resID4 = resources.getIdentifier(weatherIcon4, "drawable", packageName)


                        pic_4.setImageResource(resID4)
                        txt_description4.text = (weatherDescription4)
                        txt_temp4.text = (temp4 + degreeSymbol+ "F")

                        txt_time4.setText(timeish4.substring(11,16))

                        //Toast.makeText(this, "$weatherIcon", android.widget.Toast.LENGTH_SHORT).show()
                        //Toast.makeText(this, "$temp", android.widget.Toast.LENGTH_SHORT).show()
                    }

                }
        )
        btn_current.setOnClickListener(){
            firebaseAnalytics.logEvent("Return_to_Complete", null)
            val intent_current = Intent(this,CurrentForecastActivity::class.java)
            intent_current.putExtra(CurrentForecastActivity.INTENT_LANG, lang.toString())
            intent_current.putExtra(CurrentForecastActivity.INTENT_LAT, lat.toString())

            startActivity(intent_current)
        }
    }
}