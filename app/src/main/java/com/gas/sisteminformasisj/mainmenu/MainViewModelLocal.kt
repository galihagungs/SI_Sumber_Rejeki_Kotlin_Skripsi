package com.gas.sisteminformasisj.mainmenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.gas.sisteminformasisj.local.UserModelDB
import com.gas.sisteminformasisj.local.UserPreference
import kotlinx.coroutines.launch

class MainViewModelLocal(private val pref: UserPreference): ViewModel() {
    fun getUser(): LiveData<UserModelDB> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun simpanUserMain(id_user: Int,username: String, password: String, nama: String, roles: String) {
        viewModelScope.launch {
//            Log.d(TAG, "loginTokenViewModel: $token")
            pref.simpanUser(id_user, username, password, nama, roles)

        }
    }
}