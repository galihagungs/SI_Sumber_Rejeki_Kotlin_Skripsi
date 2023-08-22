package com.gas.sisteminformasisj

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gas.sisteminformasisj.local.UserPreference
import com.gas.sisteminformasisj.login.LoginViewModel
import com.gas.sisteminformasisj.mainmenu.MainViewModelLocal

class ViewModelFactory(private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModelLocal::class.java) ->{
                MainViewModelLocal(pref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) ->{
                LoginViewModel(pref) as T
            }
            else -> throw  IllegalArgumentException("Unknown ViewModel class: "+ modelClass.name)
        }
    }
}