package com.teamenergy.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teamenergy.R
import com.teamenergy.databinding.FragmentAccountBinding
import com.teamenergy.databinding.FragmentLoginBinding
import com.teamenergy.teamenergy.BaseEnergyViewModel
import org.koin.android.ext.android.inject


class AccountFragment : Fragment() {

    private var binding: FragmentAccountBinding? = null
    private val viewModel by inject<BaseEnergyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@AccountFragment
        }
        return binding!!.root
    }
}