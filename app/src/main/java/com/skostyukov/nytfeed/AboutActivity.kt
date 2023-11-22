package com.skostyukov.nytfeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.skostyukov.nytfeed.databinding.ActivityAboutBinding
import com.skostyukov.nytfeed.databinding.ActivityHistoryBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.aboutToolbar)
    }
}