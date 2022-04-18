package com.teamenergy.ui.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teamenergy.R
import com.teamenergy.databinding.FragmentAccountBinding
import com.teamenergy.databinding.FragmentLoginBinding
import com.teamenergy.databinding.FragmentNotificationBinding
import com.teamenergy.teamenergy.BaseEnergyViewModel
import com.teamenergy.ui.home.HomeActivity
import org.koin.android.ext.android.inject


class NotificationFragment : Fragment() {

    private var binding: FragmentNotificationBinding? = null
    private val viewModel by inject<BaseEnergyViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@NotificationFragment
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as HomeActivity).setBottomVisibility(true)
    }

}