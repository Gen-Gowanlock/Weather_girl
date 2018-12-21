package com.example.ggglo.weather_girl

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.*
import com.crashlytics.android.Crashlytics
//import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_login.*
import io.fabric.sdk.android.Fabric

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

/*
Notes:

the remember switches are bugged - they remember something and put stuff in the textbox
but it is not the email and password inserted for some reason

the login button in not logging in users even if they are on firebase -
get a notification that the email is poorly formatted, not sure how to fix and if
you have an idea that would be great
*/

class LoginActivity : AppCompatActivity() {

    private lateinit var txt_email: EditText

    private lateinit var txt_password: EditText

    private lateinit var btn_signup: Button

    private lateinit var  btn_login: Button

    private lateinit var  sw_remember_email: Switch

    private lateinit var  sw_remember_password: Switch

    private val PREF_FILENAME = "weather_girl"
    private val PREF_EMAIL = "email"
    private val PREF_PASSWORD = "password"

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_login)

        val preferences = getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE)

        txt_email = findViewById(R.id.txt_email)
        txt_password = findViewById(R.id.txt_password)
        btn_signup = findViewById(R.id.btn_signup)
        btn_login = findViewById(R.id.btn_login)
        sw_remember_email = findViewById(R.id.sw_remember_email)
        sw_remember_password = findViewById(R.id.sw_remember_password)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAuth = FirebaseAuth.getInstance()

        val savedEmail: String = preferences.getString(PREF_EMAIL,"")
        val savedPassword: String = preferences.getString(PREF_PASSWORD,"")

        //firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        if(savedEmail != ""){
            txt_email.setText(savedEmail)
            //change switch to checked position
        }
        if(savedPassword != ""){
            txt_password.setText(savedPassword)
            //change switch to checked position
        }

        btn_signup.setOnClickListener{
            val intent1 = Intent(this, SignupActivity::class.java)
            startActivity(intent1)

        }

        btn_signup.setOnClickListener{
            val intent_signup = Intent(this,SignupActivity::class.java)

            startActivity(intent_signup)
        }

        btn_login.setOnClickListener{

            //Crashlytics.getInstance().crash()

            val email = txt_email.text.toString()
            val password = txt_password.text.toString()

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    firebaseAnalytics.logEvent("login_success", null)

                    val user = firebaseAuth.currentUser

                    if (user != null) {
                        Toast.makeText(this, "Logged in as ${user.email}!", Toast.LENGTH_SHORT).show()
                        /*
                        val intent_current_forecast = Intent(this,CurrentForecastActivity::class.java)
                        startActivity(intent_current_forecast)
                        */
                        val intent_geocoding = Intent(this, Geocoding::class.java)
                        startActivity(intent_geocoding)
                    }

                    //preferences.edit().putString(PREF_SAVED_USERNAME, username).apply()

                    // Start the ChooseLocationActivity, sending it the inputted username
                    //val intent = Intent(this, ChooseLocationActivity::class.java)
                    //intent.putExtra(ChooseLocationActivity.INTENT_KEY_USERNAME, username)
                    //startActivity(intent)
                } else {
                    val exception = task.exception
                    val errorType = if (exception is FirebaseAuthInvalidCredentialsException)
                        "invalid credentials" else "network connection"

                    // Acts similar to an Intent
                    val bundle = Bundle()
                    bundle.putString("error_type", errorType)

                    firebaseAnalytics.logEvent("login_failed", bundle)



                    Toast.makeText(this, "Login failed: $exception", Toast.LENGTH_SHORT).show()
                }
            }


            if(sw_remember_email.isChecked){
                if (txt_email.text.toString() != ""){
                    preferences.edit().putString(PREF_EMAIL,txt_email.text.toString()).apply()
                }
                else{
                    preferences.edit().putString(PREF_EMAIL,"").apply()
                }
            }
            if(sw_remember_password.isChecked){
                if(txt_password.text.toString() != ""){
                    preferences.edit().putString(PREF_PASSWORD,txt_password.text.toString()).apply()
                }
                else{
                    preferences.edit().putString(PREF_PASSWORD,"").apply()
                }
            }
        }

    }
}
