package com.gas.sisteminformasisj.api

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("currUser")
    val currUser: Int,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("token")
    val token: String
)
