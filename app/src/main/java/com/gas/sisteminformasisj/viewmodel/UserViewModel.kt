package com.gas.sisteminformasisj.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gas.sisteminformasisj.api.ApiConfig
import com.gas.sisteminformasisj.api.GetData
import com.gas.sisteminformasisj.api.GetUserResponse
import com.gas.sisteminformasisj.local.UserModelDB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel: ViewModel() {
    private  val _postValuesItem = MutableLiveData<GetUserResponse?>()
    val  postValuesItem: MutableLiveData<GetUserResponse?> = _postValuesItem

    fun updateDataAPI(id_user: Int?) {
        val getData = GetData(id_user)
        val client = ApiConfig().getApiService().getUser(getData)
        client.enqueue(object : Callback<GetUserResponse> {
            override fun onResponse(
                call: Call<GetUserResponse>,
                response: Response<GetUserResponse>
            ) {
                val resBody = response.body()
//                val value =  resBody?.values
                Log.e(ContentValues.TAG, "onSuccess: $resBody")
                _postValuesItem.value = resBody
            }

            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
}