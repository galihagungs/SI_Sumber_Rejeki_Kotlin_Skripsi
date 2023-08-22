package com.gas.sisteminformasisj.api

import com.google.gson.annotations.SerializedName

data class LogoutResponse(

	@field:SerializedName("auth")
	val auth: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
