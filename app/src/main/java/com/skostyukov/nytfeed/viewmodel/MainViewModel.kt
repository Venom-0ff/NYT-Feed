package com.skostyukov.nytfeed.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.skostyukov.nytfeed.model.APIFormat
import com.skostyukov.nytfeed.model.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainViewModel: ViewModel() {
    var apiFormat = MutableLiveData<APIFormat>(APIFormat("", listOf<Result>()))

    fun startCoroutine() {
        CoroutineScope(Dispatchers.Main).launch {
            val request: APIFormat? = getDataFromCoroutine()

            if (request != null && request.results.isNotEmpty()) {
                apiFormat.value = request!!
            }
        }
    }

    private suspend fun getDataFromCoroutine(): APIFormat? {
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