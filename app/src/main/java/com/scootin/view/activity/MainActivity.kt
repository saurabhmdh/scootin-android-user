package com.scootin.view.activity


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
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
    private var currentNavController: LiveData<NavController>? = null

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

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val navGraphIds =
            listOf(R.navigation.home, R.navigation.cart, R.navigation.wallet, R.navigation.account)

        // Setup the bottom navigation view with a search of navigation graphs
        val controller = binding.bottomNav.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            try {
                setupActionBarWithNavController(navController)
            } catch (e: Exception) {
            }
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
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
            if((it is NavHostFragment) == true){
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
}
