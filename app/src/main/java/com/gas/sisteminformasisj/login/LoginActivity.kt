package com.gas.sisteminformasisj.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.gas.sisteminformasisj.ViewModelFactory
import com.gas.sisteminformasisj.api.ApiConfig
import com.gas.sisteminformasisj.api.GetUserResponse
import com.gas.sisteminformasisj.api.LoginResponse
import com.gas.sisteminformasisj.api.UserLog
import com.gas.sisteminformasisj.databinding.ActivityLoginBinding
import com.gas.sisteminformasisj.local.UserModelDB
import com.gas.sisteminformasisj.local.UserPreference
import com.gas.sisteminformasisj.mainmenu.MainActivity
import com.gas.sisteminformasisj.viewmodel.UserViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var User: UserModelDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupViewModel()
        loginAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]
        loginViewModel.getUser().observe(this){
                user->
            this.User = user
        }
    }

    private fun loginAction(){
        binding.buttonLogin.setOnClickListener {
            val username = binding.inputUsername.text.toString()
            val password = binding.inputPassword.text.toString()
            val userLogin = UserLog(username,password)
//            Toast.makeText(this, userLogin.toString(), Toast.LENGTH_SHORT).show()

            when{
                username.isEmpty()->{
                    binding.inputUsername.error = "Masukan Username"
                }
                password.isEmpty() -> {
                    binding.inputPassword.error = "Masukan Password"
                }
                else ->{
                    val logService = ApiConfig().getApiService().login(UserLog(username,password))

                    logService.enqueue(object : Callback<LoginResponse>{
                        @SuppressLint("SuspiciousIndentation")
                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            if (response.isSuccessful){
                                val responseBody = response.body()
                                val status = responseBody?.success
                                val idUser = responseBody?.currUser
                                val token = responseBody?.token
//                                Toast.makeText(this@LoginActivity, responseBody.toString(), Toast.LENGTH_SHORT).show()
                                    if (!status!!){
                                        Toast.makeText(this@LoginActivity, status.toString(), Toast.LENGTH_SHORT).show()
                                    }else{
//                                        Toast.makeText(this@LoginActivity, "Login : Success, Token: $token", Toast.LENGTH_SHORT).show()
                                        updateData(idUser)
                                        if (idUser != null) {
                                            loginViewModel.login(token, idUser)
                                        }
                                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        finish()
                                    }


                            }else {
                                Toast.makeText(this@LoginActivity, "Error Status: Masukan Data Yang Sesuai", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Toast.makeText(this@LoginActivity, "Error Status: Masukan Data Yang Sesuai", Toast.LENGTH_SHORT).show()
                        }

                    })

                }
            }

        }
    }

    private fun updateData(idUser: Int?) {
        val mainViewModelApi = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[UserViewModel::class.java]
        mainViewModelApi.updateDataAPI(idUser)
        mainViewModelApi.postValuesItem.observe(this){ValuesItem->
            setData(ValuesItem)
        }
    }

    private fun setData(ValuesItem: GetUserResponse?) {
        for (data in ValuesItem?.values!!){
            loginViewModel.simpanUser(data.idUser,data.username,data.password,data.nama,data.roles.toString())
        }
    }
}