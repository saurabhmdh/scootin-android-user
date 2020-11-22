package com.scootin.view.activity


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.razorpay.PaymentResultListener
import com.scootin.R
import com.scootin.databinding.ActivityMainBinding
import com.scootin.network.request.VerifyAmountRequest
import com.scootin.util.navigation.setupWithNavController
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


        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
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
                        }
                    }
                }
            }
        }
        val navHostFragment = supportFragmentManager.fragments.first() as? NavHostFragment
        if (navHostFragment != null) {
            val childFragments = navHostFragment.childFragmentManager.fragments
            childFragments.forEach { fragment ->
                if (fragment is MyWalletFragment) {
                    (fragment).onPaymentSuccess(razorpayPaymentId)
                } else if (fragment is CardPaymentPageFragment) {
                    fragment.onPaymentSuccess(razorpayPaymentId)
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
}
