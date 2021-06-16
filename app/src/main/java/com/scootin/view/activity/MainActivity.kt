package com.scootin.view.activity


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.razorpay.PaymentResultListener
import com.scootin.R
import com.scootin.databinding.ActivityMainBinding
import com.scootin.util.constants.AppConstants
import com.scootin.view.fragment.account.payment.ChangePaymentMethodFragment
import com.scootin.view.fragment.account.payment.PaymentFragment
import com.scootin.view.fragment.cart.CardPaymentPageFragment
import com.scootin.view.fragment.wallet.MyWalletFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PaymentResultListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        setSupportActionBar(binding.toolbar)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(listener, IntentFilter(AppConstants.INTENT_ACTION_USER_DISABLED))
    }
    
    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(listener)
        super.onDestroy()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val navGraphIds = setOf(
            R.id.titleScreen,
            R.id.account_fragment,
            R.id.cart_fragment,
            R.id.wallet_fragment
        )
        val bottomView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        val navController = findNavController(R.id.nav_host_container)

        navController.addOnDestinationChangedListener { navController, navDestination, bundle ->
            if (navGraphIds.contains(navDestination.id)) {
                Timber.i("Show")
                bottomView.visibility = View.VISIBLE
            } else {
                Timber.i("Its time to hide navigation controller")
                bottomView.visibility = View.GONE
            }
        }
        bottomView.setupWithNavController(navController)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        val navHostFragment = supportFragmentManager.fragments.first() as? NavHostFragment
        if (navHostFragment != null) {
            val childFragments = navHostFragment.childFragmentManager.fragments
            childFragments.forEach { fragment ->
                fragment.onActivityResult(requestCode, resultCode, data)
            }
        }

    }


    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        Timber.i("Payment $razorpayPaymentId")
        supportFragmentManager.fragments.forEach {
            if(it is NavHostFragment?){
                if (it != null) {
                    val childFragments = it.childFragmentManager.fragments
                    childFragments.forEach { fragment ->
                        if (fragment is MyWalletFragment) {
                            (fragment).onPaymentSuccess(razorpayPaymentId)
                        } else if (fragment is CardPaymentPageFragment) {
                            fragment.onPaymentSuccess(razorpayPaymentId)
                        } else if (fragment is PaymentFragment) {
                            fragment.onPaymentSuccess(razorpayPaymentId)
                        } else if (fragment is ChangePaymentMethodFragment) {
                            fragment.onPaymentSuccess(razorpayPaymentId)
                        }
                    }
                }
            }
        }
    }

    override fun onPaymentError(errorCode: Int, response: String?) {
        Timber.i("onPaymentError $errorCode response = $response")
    }

    fun moveToAnotherTab(id: Int) {
        val navigationView = binding.bottomNav
        navigationView.selectedItemId = id // R.id.reservation
    }

    fun setupBadging(count: Int) {
        val item = binding.bottomNav.menu.getItem(1)
        val badgeDrawable = binding.bottomNav.getOrCreateBadge(item.itemId)
        if (count == 0) {
            badgeDrawable.number = 0
            badgeDrawable.setVisible(false, true)
        } else {
            badgeDrawable.number = count
            badgeDrawable.backgroundColor = Color.BLUE
            badgeDrawable.setVisible(true, true)
        }
    }

    private val listener = MyBroadcastReceiver()

    inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent){
            when (intent.action) {
                AppConstants.INTENT_ACTION_USER_DISABLED -> {
                    Toast.makeText(context, "Account has been disabled by admin", Toast.LENGTH_LONG).show()
                    finish()
                }
                else -> Toast.makeText(context, "Action Not Found", Toast.LENGTH_LONG).show()
            }

        }
    }
}
