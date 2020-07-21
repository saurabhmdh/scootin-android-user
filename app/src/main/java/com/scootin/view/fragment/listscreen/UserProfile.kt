/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scootin.view.fragment.listscreen

import android.os.Bundle

import android.view.View
import androidx.fragment.app.Fragment
import com.scootin.R
import com.scootin.databinding.FragmentUserProfileBinding
import com.scootin.view.fragment.listscreen.MyAdapter.Companion.USERNAME_KEY


/**
 * Shows a profile screen for a user, taking the name from the arguments.
 */
class UserProfile : Fragment(R.layout.fragment_user_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentUserProfileBinding.bind(view)

        binding.profileUserName.text = arguments?.getString(USERNAME_KEY) ?: "Saurabh"

    }
}
