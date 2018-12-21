package com.example.ggglo.weather_girl

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.analytics.FirebaseAnalytics

class SignupActivity : AppCompatActivity() {
    private lateinit var txt_signup_email: EditText

    private lateinit var txt_signup_password: EditText

    private lateinit var btn_signup_signup: Button

    private lateinit var txt_confirm_password: EditText

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var firebaseAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        txt_signup_email = findViewById(R.id.txt_signup_email)
        txt_signup_password = findViewById(R.id.txt_signup_password)
        btn_signup_signup = findViewById(R.id.btn_signup_signup)
        txt_confirm_password = findViewById(R.id.txt_confirm_password)



        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAuth = FirebaseAuth.getInstance()

        btn_signup_signup.setOnClickListener {
            if(txt_signup_password.text.toString() == txt_confirm_password.text.toString()){
                val usernameemail = txt_signup_email.text.toString()
                val password = txt_signup_password.text.toString()
                firebaseAuth.createUserWithEmailAndPassword(usernameemail, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        if (user != null) {
                            Toast.makeText(this, "Account created for ${user.email}!", Toast.LENGTH_SHORT).show()
                        }
                        val intent_login = Intent(this,LoginActivity::class.java)
                        startActivity(intent_login)
                    } else {
                        val exception = task.exception
                        Toast.makeText(this, "account creation failed! $exception", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            else{
                Toast.makeText(this,"Passwords do not match", Toast.LENGTH_SHORT).show()
            }

        }
    }

}