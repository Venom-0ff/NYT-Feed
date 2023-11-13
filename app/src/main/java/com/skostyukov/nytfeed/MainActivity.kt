package com.skostyukov.nytfeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.skostyukov.nytfeed.databinding.ActivityMainBinding
import com.skostyukov.nytfeed.model.APIFormat
import com.skostyukov.nytfeed.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerViewManager: RecyclerView.LayoutManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setup recyclerView
        recyclerViewManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.layoutManager = recyclerViewManager
        binding.recyclerView.setHasFixedSize(true)

        startCoroutine()
    }

    private fun startCoroutine() {
        CoroutineScope(Dispatchers.Main).launch {
            val request: APIFormat? = getBookDataFromCoroutine()

            if (request != null && request.results.isNotEmpty()) {
                updateUI(request)
            }
        }
    }

    private fun updateUI(request: APIFormat) {
//        binding.textViewCopyright.text = request.copyright
        binding.recyclerView.adapter =RecyclerAdapter(request.results)
    }

    private suspend fun getBookDataFromCoroutine(): APIFormat? {
        val defer = CoroutineScope(Dispatchers.IO).async {
            val url = URL("https://api.nytimes.com/svc/news/v3/content/all/world.json?api-key=435dnRm9ZmRAYv7gnsTuC8tgbFCfe90n")
            val connection = url.openConnection() as HttpsURLConnection

            if (connection.responseCode == 200) {
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val request = Gson().fromJson(inputStreamReader, APIFormat::class.java)

                inputStreamReader.close()
                inputSystem.close()

                return@async request
            }
            else {
                return@async null
            }
        }

        return defer.await()
    }
}