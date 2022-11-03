package com.example.a_sns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a_sns.databinding.ActivityMainBinding
import com.example.a_sns.databinding.ActivityPostingBinding

class PostingActivity : AppCompatActivity() {

    lateinit var binding: ActivityPostingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}