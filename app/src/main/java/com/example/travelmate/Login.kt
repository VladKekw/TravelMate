package com.example.travelmate

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginEditText = findViewById<TextInputEditText>(R.id.login)
        val passwordEditText = findViewById<TextInputEditText>(R.id.editTextPassword)
        val enterButton = findViewById<Button>(R.id.button1)
        val regButton = findViewById<Button>(R.id.regButton)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        auth = FirebaseAuth.getInstance()

        regButton.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }

        enterButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val enteredLogin = loginEditText.text.toString()
            val enteredPassword = passwordEditText.text.toString()

            if (TextUtils.isEmpty(enteredLogin)) {
                Toast.makeText(this, "Login cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(enteredPassword)) {
                Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(enteredLogin, enteredPassword)
                    .addOnCompleteListener(this) { task ->
                        progressBar.visibility = View.GONE
                        if (task.isSuccessful) {
                            Toast.makeText(
                                baseContext,
                                "Authentication successful.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            val intent = Intent(this, MenuActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }
        }
    }

}

