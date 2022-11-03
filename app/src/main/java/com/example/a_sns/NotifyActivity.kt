package com.example.a_sns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a_sns.databinding.ActivityNotifyBinding
import com.example.a_sns.databinding.ActivityPostingBinding

class NotifyActivity : AppCompatActivity() {

    lateinit var binding: ActivityNotifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}