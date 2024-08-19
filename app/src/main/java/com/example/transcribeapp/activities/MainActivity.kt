package com.example.transcribeapp.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.transcribeapp.R
import com.example.transcribeapp.databinding.ActivityMainBinding
import com.example.transcribeapp.extension.beGone
import com.example.transcribeapp.extension.beVisible



class MainActivity : AppCompatActivity(),NavController.OnDestinationChangedListener {


    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }




    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener(this@MainActivity)

        binding.bottomNavigation.setupWithNavController(navController)

        binding.bottomNavigation.setOnItemSelectedListener  { item:MenuItem ->
            when (item.itemId) {
                R.id.idHomeFragment -> {
                    navController.navigate(R.id.idHomeFragment)
                    true
                }
                R.id.idAiFragment -> {
                    navController.navigate(R.id.idAiFragment)
                    true
                }
                R.id.idCalenderFragment -> {
                    navController.navigate(R.id.idCalenderFragment)
                    true
                }
                R.id.idImportFragment -> {
                    navController.navigate(R.id.idImportFragment)
                    true
                }
                else -> false
            }
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }



    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {

        binding.apply {
            when(destination.id){

                R.id.idRecordingFragment->{
                    bottomNavigation.beGone()
                }

                R.id.idHomeFragment->{
                    bottomNavigation.beVisible()
                }
            }
        }
    }

}

