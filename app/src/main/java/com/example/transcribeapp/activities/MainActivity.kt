package com.example.transcribeapp.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.transcribeapp.R
import com.example.transcribeapp.client.Keys
import com.example.transcribeapp.databinding.ActivityMainBinding
import com.example.transcribeapp.extension.beGone
import com.example.transcribeapp.extension.beVisible
import com.example.transcribeapp.extension.setLightStatusBar


class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {


    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var toggle: ActionBarDrawerToggle


    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener(this@MainActivity)

        setupNaveViewClick()
        toggle = ActionBarDrawerToggle(this, binding.container, R.string.open, R.string.close)




        binding.container.addDrawerListener(toggle)
        binding.menu.setOnClickListener {
            binding.container.openDrawer(GravityCompat.START)
        }

        binding.bottomNavigation.setupWithNavController(navController)

        binding.bottomNavigation.setOnItemSelectedListener { item: MenuItem ->
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

        window.setLightStatusBar()


        /* val window: Window = window
         window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
         window.statusBarColor = Color.WHITE

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
             window.insetsController?.setSystemBarsAppearance(
                 WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                 WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
             )
         } else {
             @Suppress("DEPRECATION")
             window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
         }*/


    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {

        binding.apply {
            when (destination.id) {

                R.id.idRecordingFragment -> {
                    bottomNavigation.beGone()
                }
                R.id.testFragment -> {
                    bottomNavigation.beGone()
                }

                R.id.idHomeFragment -> {
                    bottomNavigation.beVisible()
                    toolBar.beVisible()
                }

                R.id.signUpFragment, R.id.emailFragment,
                R.id.createAccount, R.id.password, R.id.verifyEmail,
                R.id.aiChatFragmentInner, R.id.idSummaryFragment,
                R.id.idConversationFragment, R.id.userProfileSetting,
                -> {

                    toolBar.visibility = View.GONE
                    bottomNavigation.visibility = View.INVISIBLE
                    /* toolBar.beGone()
                     bottomNavigation.beGone()*/
                }
            }
        }
    }


    private fun setupNaveViewClick() {

        binding.apply {

            Glide.with(this@MainActivity)
                .load(Keys.profile)
                .circleCrop()
                .into(customMenu.profileImg)

            customMenu.cross.setOnClickListener {
                container.closeDrawer(GravityCompat.START)
            }

            customMenu.rateApp.setOnClickListener {
                container.closeDrawer(GravityCompat.START)
            }

            customMenu.feedBack.setOnClickListener {
                container.closeDrawer(GravityCompat.START)
            }

            customMenu.shareApp.setOnClickListener {
                container.closeDrawer(GravityCompat.START)

            }
            customMenu.contactUs.setOnClickListener {
                container.closeDrawer(GravityCompat.START)
            }

            customMenu.aboutApp.setOnClickListener {
                container.closeDrawer(GravityCompat.START)
            }

            customMenu.setting.setOnClickListener {
                container.closeDrawer(GravityCompat.START)
                navController.navigate(R.id.userProfileSetting)
            }


        }

    }


}

