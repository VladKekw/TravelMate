package com.example.travelmate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import com.example.travelmate.R.id.editTextName
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val loginTextInputLayout = findViewById<TextInputLayout>(R.id.textInputLayoutName)
        val passwordTextInputLayout = findViewById<TextInputLayout>(R.id.textInputLayoutPassword)
        val loginEditText = loginTextInputLayout.editText
        val passwordEditText = passwordTextInputLayout.editText
        val enterButton = findViewById<Button>(R.id.button1)


        enterButton.setOnClickListener {

            val enteredLogin = loginEditText?.text.toString()
            val enteredPassword = passwordEditText?.text.toString()


          /*  if (enteredLogin.equals("Vlad")&&enteredPassword=="1234")
            {

            }*/

            Log.e("pass", "$enteredPassword")
            Log.d("login", "$enteredLogin")
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)


        }
    }

    }

