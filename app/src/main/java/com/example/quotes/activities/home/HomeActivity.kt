package com.example.quotes.activities.home


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.quotes.Model.Notification
import com.example.quotes.R
import com.example.quotes.base.BaseActivity
import com.example.quotes.databinding.ActivityDashBoardBinding
import com.example.quotes.fragments.notif.INotifView
import com.google.android.material.badge.BadgeDrawable


class HomeActivity : BaseActivity(), IHomeView {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var binding: ActivityDashBoardBinding
    private lateinit var presenter: HomePresenter
    lateinit var iNotifView: INotifView
    private lateinit var badge:BadgeDrawable

    private var itemselect: Int = R.id.tab_home
    override fun onResume() {
        super.onResume()
        binding.bottomNavigation.menu.findItem(itemselect).isChecked = true

    }
    override fun displayNotification(lnotif: MutableList<Notification>) {
        val currentFragment = navHostFragment.navController.currentDestination
        if (currentFragment!!.id == R.id.notifFragment) {
            iNotifView.addNotif(lnotif)
            badge.isVisible = false
        } else {
            badge.isVisible = true
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setUp()
        setContentView(binding.root)
        val builder = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.from_right)
            .setExitAnim(R.anim.to_left)
            .setPopEnterAnim(R.anim.from_left)
            .setPopExitAnim(R.anim.to_right)
            .build()
        presenter.addListenerNotif()
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab_home -> {
                    navController.navigate(R.id.homeFragment, null, builder)
                    itemselect = item.itemId
                    true
                }

                R.id.tab_notif -> {
                    navController.navigate(R.id.notifFragment, null, builder)
                    itemselect = item.itemId
                    badge.isVisible = false
                    true
                }

                R.id.tab_plus -> {
                    navController.navigate(R.id.upPostActivity, null, builder)
                    true
                }

                R.id.tab_user -> {
                    navController.navigate(R.id.showUserActivity, null, builder)
                    true
                }
                else -> false
            }
        }
    }


    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        val currentFragment = navHostFragment.navController.currentDestination
        when (currentFragment?.id) {
            R.id.homeFragment -> finish()
            R.id.notifFragment -> {
                if (binding.bottomNavigation.selectedItemId
                    == R.id.tab_home
                ) {
                    super.onBackPressed()
                } else {
                    binding.bottomNavigation.setSelectedItemId(R.id.tab_home)
                    itemselect = R.id.tab_home
                }
            }
            else -> super.onBackPressed()
        }
    }
    override fun setUp() {
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
        presenter = HomePresenter(this)
        badge = binding.bottomNavigation.getOrCreateBadge(R.id.tab_notif)
        badge.isVisible=false
    }
}