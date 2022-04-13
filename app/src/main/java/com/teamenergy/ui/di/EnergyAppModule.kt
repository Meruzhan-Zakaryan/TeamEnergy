package com.teamenergy.ui.di

import com.teamenergy.teamenergy.BaseEnergyViewModel
import com.teamenergy.ui.welcome.WelcomeActivity
import com.teamenergy.ui.welcome.fragments.LoginFragment
import com.teamenergy.ui.welcome.fragments.SignUpFragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val energyModule = module {
    scope(named<LoginFragment>()) {
        viewModel { BaseEnergyViewModel(get()) }
    }
    scope(named<SignUpFragment>()) {
        viewModel { BaseEnergyViewModel(get()) }
    }
}