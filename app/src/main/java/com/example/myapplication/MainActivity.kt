package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_current_poll, R.id.navigation_history
            )
        )
        navView.setupWithNavController(navController)

        initViews()
    }

    private fun initViews() {

        val create_poll: CardView = binding.createPoll

        create_poll.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, CreatePollActivity::class.java)
            intent.putExtra("key", "value")
            startActivity(intent)
        })
    }

}