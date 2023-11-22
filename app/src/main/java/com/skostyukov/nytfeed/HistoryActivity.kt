package com.skostyukov.nytfeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.skostyukov.nytfeed.databinding.ActivityHistoryBinding
import com.skostyukov.nytfeed.model.HistoryItem

class HistoryActivity : AppCompatActivity() {
    private lateinit var recyclerViewManager: RecyclerView.LayoutManager
    private lateinit var binding: ActivityHistoryBinding
    private val db = Firebase.firestore
    private val noHistoryList = listOf(HistoryItem("", "No history available.", ""))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.historyToolbar)


        // setup recyclerView
        recyclerViewManager = LinearLayoutManager(applicationContext)
        binding.recyclerViewHistory.layoutManager = recyclerViewManager
        binding.recyclerViewHistory.setHasFixedSize(true)

        binding.recyclerViewHistory.adapter = HistoryRecyclerAdapter(noHistoryList, this)

        var history: List<HistoryItem>
        db.collection("users")
            .document(MainActivity.user.uid)
            .collection("history")
            .get()
            .addOnSuccessListener { result ->
                history = result.toObjects(HistoryItem::class.java)
                if (history.isNotEmpty()) {
                    binding.recyclerViewHistory.adapter = HistoryRecyclerAdapter(
                        history.asReversed(),
                        this
                    )
                }
            }

        binding.buttonClearHistory.setOnClickListener {
            db.collection("users")
                .document(MainActivity.user.uid)
                .collection("history")
                .get().addOnSuccessListener { result ->
                    val docs = result.documents
                    if (docs.isNotEmpty()) {
                        for (doc in docs) doc.reference.delete()
                        binding.recyclerViewHistory.adapter =
                            HistoryRecyclerAdapter(noHistoryList, this)
                        Toast.makeText(this, R.string.no_history, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}