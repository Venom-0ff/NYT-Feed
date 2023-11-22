package com.skostyukov.nytfeed

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
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
        setSupportActionBar(binding.mainToolbar)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        Companion.user = Firebase.auth.currentUser!!

        val apiObserver = Observer<APIFormat> { newAPI ->
            updateUI(newAPI)
        }

        viewModel.apiFormat.observe(this, apiObserver)

        // setup recyclerView
        recyclerViewManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.layoutManager = recyclerViewManager
        binding.recyclerView.setHasFixedSize(true)

        viewModel.startCoroutine()
    }

    private fun updateUI(request: APIFormat) {
        binding.recyclerView.adapter = RecyclerAdapter(request.results, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.action_logout -> {
                // logs out of Firebase
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(this, R.string.logged_out, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }

            R.id.action_history -> {
                startActivity(Intent(this, HistoryActivity::class.java))
                true
            }

            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }

            else -> {
                // The user's action isn't recognized.
                // Invoke the superclass to handle it.
                super.onOptionsItemSelected(item)
            }
        }

    companion object {
        lateinit var user: FirebaseUser
    }
}