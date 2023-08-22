package com.gas.sisteminformasisj.api

import com.google.gson.annotations.SerializedName

data class CekUserResponse(

	@field:SerializedName("auth")
	val auth: Boolean,

	@field:SerializedName("message")
	val message: String
)
