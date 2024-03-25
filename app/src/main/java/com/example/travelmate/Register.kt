package com.example.travelmate

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val loginEditText = findViewById<TextInputEditText>(R.id.regLogin)
        val passwordEditText = findViewById<TextInputEditText>(R.id.editTextPassword)
        val enterButton = findViewById<Button>(R.id.button1)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        auth = FirebaseAuth.getInstance()

        enterButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val enteredLogin = loginEditText.text.toString()
            val enteredPassword = passwordEditText.text.toString()

            if (TextUtils.isEmpty(enteredLogin)) {
            Toast.makeText(this, "Login cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(enteredPassword)) {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("DEBUG", "Login: $enteredLogin")
                Log.d("DEBUG", "Pass: $enteredPassword")
                auth.createUserWithEmailAndPassword(enteredLogin, enteredPassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d("DEBUG", "createUserWithEmail:success")
                            Toast.makeText(
                                baseContext,
                                "Account created!",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this, Login::class.java)
                            startActivity(intent)
                            progressBar.visibility = View.GONE
                        } else {
                            Log.w("DEBUG", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                            progressBar.visibility = View.GONE
                        }
                    }
            }
        }
    }

}

