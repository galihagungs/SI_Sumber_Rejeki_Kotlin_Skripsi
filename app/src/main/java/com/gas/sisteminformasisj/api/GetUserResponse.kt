package com.gas.sisteminformasisj.api

import com.google.gson.annotations.SerializedName

data class GetUserResponse(

	@field:SerializedName("values")
	val values: List<ValuesItem>,

	@field:SerializedName("status")
	val status: Int
)

data class ValuesItem(

	@field:SerializedName("isLogin")
	val isLogin: Int,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("roles")
	val roles: Int,

	@field:SerializedName("id_user")
	val idUser: Int,

	@field:SerializedName("username")
	val username: String,
)