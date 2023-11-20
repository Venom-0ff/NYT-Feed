package com.skostyukov.nytfeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skostyukov.nytfeed.databinding.ActivityMainBinding
import com.skostyukov.nytfeed.model.APIFormat
import com.skostyukov.nytfeed.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerViewManager: RecyclerView.LayoutManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val apiObserver = Observer<APIFormat> {
                newAPI -> updateUI(newAPI)//binding.api = newAPI
        }

        viewModel.apiFormat.observe(this, apiObserver)

        // setup recyclerView
        recyclerViewManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.layoutManager = recyclerViewManager
        binding.recyclerView.setHasFixedSize(true)

        viewModel.startCoroutine()
    }

    private fun updateUI(request: APIFormat) {
//        binding.textViewCopyright.text = request.copyright
        binding.recyclerView.adapter =RecyclerAdapter(request.results)
    }
}