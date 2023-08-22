package com.gas.sisteminformasisj.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.gas.sisteminformasisj.local.UserModelDB
import com.gas.sisteminformasisj.local.UserPreference
import kotlinx.coroutines.launch

class LoginViewModel (private val pref: UserPreference): ViewModel() {
    fun getUser(): LiveData<UserModelDB> {
        return pref.getUser().asLiveData()
    }

    fun login(token: String?, id_user: Int) {
        viewModelScope.launch {
//            Log.d(TAG, "loginTokenViewModel: $token")
            pref.login(token.toString(), id_user)

        }
    }
    fun simpanUser(id_user: Int,username: String, password: String, nama: String, roles: String) {
        viewModelScope.launch {
//            Log.d(TAG, "loginTokenViewModel: $token")
            pref.simpanUser(id_user, username, password, nama, roles)

        }
    }
}