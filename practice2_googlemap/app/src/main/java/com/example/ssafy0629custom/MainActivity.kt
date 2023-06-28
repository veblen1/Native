package com.example.ssafy0629custom

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.ssafy0629custom.databinding.ActivityMainBinding
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val images = listOf(R.drawable.sunny, R.drawable.snow, R.drawable.rain, R.drawable.cloud)
    private val texts = listOf("Sunny", "Snow", "Rain", "Cloud")
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView: ImageView = findViewById(R.id.image_view)
        val textView: TextView = findViewById(R.id.text_view)
        val changeButton: Button = findViewById(R.id.change_button)
        val btnShowMap: Button = findViewById(R.id.btn_show_map) // 구글맵

        changeButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % images.size
            imageView.setImageResource(images[currentIndex])
            textView.text = texts[currentIndex]
        }

        // 구글맵
        btnShowMap.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
    }
}
