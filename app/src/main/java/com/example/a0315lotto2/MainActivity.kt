package com.example.a0315lotto2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        generateButton.setOnClickListener {
            val lottoNumbers = generateLottoNumbers()
            lottoNumbersTextView.text = lottoNumbers.joinToString(", ")
        }
    }

    private fun generateLottoNumbers(): List<Int> {
        val lottoNumbers = mutableListOf<Int>()

        while (lottoNumbers.size < 6) {
            val randomNumber = Random.nextInt(1, 46)

            if (!lottoNumbers.contains(randomNumber)) {
                lottoNumbers.add(randomNumber)
            }
        }

        return lottoNumbers.sorted()
    }
}