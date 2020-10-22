package com.scootin.view.fragment.wallet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.scootin.R
import com.scootin.databinding.FragmentWalletMyBinding
import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.WalletAdapter
import com.scootin.viewmodel.delivery.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyWalletFragment : Fragment(R.layout.fragment_wallet_my) {

    private val viewModel: CategoriesViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var walletAdapter: WalletAdapter
    private var binding by autoCleared<FragmentWalletMyBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWalletMyBinding.bind(view)
        setProductAdapter()
        viewModel.listTransaction(1)
        viewModel.listTransactionLiveData.observe(viewLifecycleOwner, Observer {
            walletAdapter.submitList(it.body())
        })

        binding.addMoney.setOnClickListener {
            viewModel.addMoney.observe(viewLifecycleOwner, Observer {
                Timber.i("addMoney = ${it.body()}")
            })
        }
    }

    private fun setProductAdapter() {
        walletAdapter = WalletAdapter(appExecutors)

        binding.productList.apply {
            adapter = walletAdapter
        }

    }
}