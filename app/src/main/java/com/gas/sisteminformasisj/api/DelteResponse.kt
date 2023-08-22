package com.gas.sisteminformasisj.api

import com.google.gson.annotations.SerializedName

data class DeleteResponse(

	@field:SerializedName("values")
	val values: String,

	@field:SerializedName("status")
	val status: Int
)
