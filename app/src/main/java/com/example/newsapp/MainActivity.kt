package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var builder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.confirmation))
        builder.setMessage(
            getString(R.string.close_app_confirmation)
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.home_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.breakingNewsFragment -> {
                    navController.navigate(R.id.breakingNewsFragment)
                    true
                }

                R.id.savedNewsFragment -> {
                    navController.navigate(R.id.savedNewsFragment)
                    true
                }

                R.id.searchNewsFragment -> {
                    navController.navigate(R.id.searchNewsFragment)
                    true
                }

                else -> false
            }
        }


    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.breakingNewsFragment) {
            builder.setPositiveButton(getString(R.string.okay)) { _, _ ->
                moveTaskToBack(true)
                super.onBackPressed()
            }
            builder.setNegativeButton(getString(R.string.cancel)){ _, _ ->

            }
            val dialog = builder.create()
            dialog.show()
        } else {
            navController.navigate(R.id.breakingNewsFragment)
            binding.bottomNavigation.selectedItemId = R.id.breakingNewsFragment
        }
    }
}
