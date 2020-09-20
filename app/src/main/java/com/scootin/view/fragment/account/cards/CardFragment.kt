package com.scootin.view.fragment.account.cards

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.scootin.R
import com.scootin.databinding.FragmentAccountBinding
import com.scootin.databinding.FragmentSaveCardsBinding
import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.account.CardFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CardFragment : Fragment(R.layout.fragment_save_cards) {

    private var binding by autoCleared<FragmentSaveCardsBinding>()
    private val viewModel: CardFragmentViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSaveCardsBinding.bind(view)

    }


}