package com.gas.sisteminformasisj.admin

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gas.sisteminformasisj.api.ApiConfig
import com.gas.sisteminformasisj.api.CariData
import com.gas.sisteminformasisj.api.GetUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AdminViewModelApi: ViewModel() {
    private val _userData = MutableLiveData<GetUserResponse?>()
    val userData: MutableLiveData<GetUserResponse?> = _userData

    private val _searchData = MutableLiveData<GetUserResponse?>()
    val SearchData: MutableLiveData<GetUserResponse?> = _searchData

    fun setData(){
        val client = ApiConfig().getApiService().getAllUser()
        client.enqueue(object : Callback<GetUserResponse>{
            override fun onResponse(
                call: Call<GetUserResponse>,
                response: Response<GetUserResponse>
            ) {
                if (response.isSuccessful) {
                    val resBody = response.body()
                    Log.d(TAG, "onSuccess: $resBody")

                    _userData.value = resBody
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
    fun searchData(username : String){
        val userCari = CariData(username)
        val client = ApiConfig().getApiService().searchUser(userCari)
        client.enqueue(object : Callback<GetUserResponse>{
            override fun onResponse(
                call: Call<GetUserResponse>,
                response: Response<GetUserResponse>
            ) {
                if (response.isSuccessful) {
                    val resBody = response.body()
                    Log.d(TAG, "onSuccess: $resBody")

                    _userData.value = resBody
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
}