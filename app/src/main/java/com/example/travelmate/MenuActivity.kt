package com.example.travelmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val mapButton = findViewById<ImageButton>(R.id.mapButton)
        val tourListButton = findViewById<Button>(R.id.tourList)
        val signOut = findViewById<Button>(R.id.signOut)
        val email = findViewById<TextView>(R.id.email)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if(user == null){
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
        else{
            email.text = user.email
        }

        signOut.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        mapButton.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        tourListButton.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        val seekBar = findViewById<SeekBar>(R.id.seekBar2)
        val textViewPrice = findViewById<TextView>(R.id.textViewPrice)
        val searchButton = findViewById<Button>(R.id.button3)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val price = progress.toString()
                textViewPrice.text = "Приблизний бюджет: $price грн"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        searchButton.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("budget", seekBar.progress)
            startActivity(intent)
        }
    }
}