package com.gas.sisteminformasisj.mainmenu

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.gas.sisteminformasisj.ViewModelFactory
import com.gas.sisteminformasisj.admin.AdminActivity
import com.gas.sisteminformasisj.api.*
import com.gas.sisteminformasisj.databinding.ActivityMainBinding
import com.gas.sisteminformasisj.kasir.KasirActivity
import com.gas.sisteminformasisj.laporan.LaporanMainActivity
import com.gas.sisteminformasisj.local.UserModelDB
import com.gas.sisteminformasisj.local.UserPreference
import com.gas.sisteminformasisj.login.LoginActivity
import com.gas.sisteminformasisj.stok.StokActivity
import com.gas.sisteminformasisj.viewmodel.UserViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModelLocal: MainViewModelLocal
    private lateinit var binding: ActivityMainBinding
    private lateinit var User: UserModelDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupViewModel()
        laporanMenu()
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
        mainViewModelLocal = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[MainViewModelLocal::class.java]
        mainViewModelLocal.getUser().observe(this) { user ->
//            Toast.makeText(this, "Login : "+ user.islogin, Toast.LENGTH_SHORT).show()
            if (user.islogin) {
                updateData(user)
                checkStatus(user)
                this.User = user
//                Toast.makeText(this, "Login : "+ user.nama, Toast.LENGTH_SHORT).show()
                checkRole(user)
            } else {
                mainViewModelLocal.simpanUserMain(0,"zero","zero","zero","zero")
                backToLogin()
            }
            logoutAction(user)
            binding.UserHi.text = "HI, "+ user.nama
        }
    }
    private fun logoutAction(user: UserModelDB){
        binding.buttonLogout.setOnClickListener {
            logoutFun(user)
        }
    }

    private fun logoutFun(user: UserModelDB) {
        val id_User = user.id_user
        val token = user.token
        val logOut = LogOut(token,id_User)

        val client = ApiConfig().getApiService().logout(logOut)
        client.enqueue(object : Callback<CekUserResponse>{
            override fun onResponse(
                call: Call<CekUserResponse>,
                response: Response<CekUserResponse>
            ) {
                val resBody = response.body()
                if (resBody?.auth == true){
                    mainViewModelLocal.logout()
                    backToLogin()
                    finish()
                }else{
                    Log.e(TAG, "Logout Gagal")
                }
            }
            override fun onFailure(call: Call<CekUserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })

    }

    private fun checkStatus(user: UserModelDB) {
        val token = "Bearer ${user.token}"
//        Toast.makeText(this, user.id_user.toString(), Toast.LENGTH_SHORT).show()
//        val userCek = CekUser(user.id_user)
        val client = ApiConfig().getApiService().cekToken(token,user.id_user)
        client.enqueue(object : Callback<CekUserResponse>{
            override fun onResponse(
                call: Call<CekUserResponse>,
                response: Response<CekUserResponse>
            ) {
                val resBody = response.body()
                Log.d(TAG,resBody?.auth.toString())
                Log.e(TAG, "onSuccess: ${resBody?.auth}")
                if (resBody?.auth == true){
                    Log.d(TAG,"True: ${resBody.toString()}")
                    supportActionBar?.title = "Sumber Rejeki"
                }else{
                    logoutFun(user)
                    backToLogin()
                    Log.d(TAG,"False: ${resBody.toString()}")
                    finish()
                }
            }
            override fun onFailure(call: Call<CekUserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    private fun backToLogin(){
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun checkRole(user: UserModelDB) {
        var roles = user.roles;
        when (roles) {
            "1" -> {
                binding.AdminButton.isVisible = true
                adminMenu()
                kasirmenu()
                stokmenu()
            }
            "2" -> {
                binding.AdminButton.isVisible = false
                kasirmenu()
                stokmenu()
            }
            else -> {
                Toast.makeText(this, "Anda tidak mempunyai roles", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateData(user: UserModelDB) {
        val mainViewModelApi = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[UserViewModel::class.java]
        mainViewModelApi.updateDataAPI(user.id_user)
        mainViewModelApi.postValuesItem.observe(this){ValuesItem->
            setData(ValuesItem)
        }
    }

    private fun setData(ValuesItem: GetUserResponse?) {
        for (data in ValuesItem?.values!!){
            mainViewModelLocal.simpanUserMain(data.idUser,data.username,data.password,data.nama,data.roles.toString())
        }
    }

    private fun adminMenu(){
        binding.AdminButton.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
        }
    }
    private fun kasirmenu(){
        binding.KasirButton.setOnClickListener {
            startActivity(Intent(this, KasirActivity::class.java))
        }
    }
    private fun stokmenu(){
        binding.stokButton.setOnClickListener {
            startActivity(Intent(this, StokActivity::class.java))
        }
    }
    private fun laporanMenu(){
        binding.laporanButton.setOnClickListener {
            startActivity(Intent(this, LaporanMainActivity::class.java))
        }
    }
}