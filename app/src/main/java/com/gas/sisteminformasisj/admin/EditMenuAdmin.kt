package com.gas.sisteminformasisj.admin

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.gas.sisteminformasisj.R
import com.gas.sisteminformasisj.api.*
import com.gas.sisteminformasisj.databinding.ActivityEditMenuAdminBinding
import com.gas.sisteminformasisj.mainmenu.MainActivity
import com.gas.sisteminformasisj.mainmenu.MainViewModelLocal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class EditMenuAdmin : AppCompatActivity() {
    private lateinit var mainViewModelLocal: MainViewModelLocal
    private lateinit var binding: ActivityEditMenuAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMenuAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        dataSet()
        backToMenu()
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

    private fun dataSet(){
        val dataSet = intent.extras?.getParcelableArrayList<UserModelApi>("Data")
        if (dataSet != null) {
            for (data in dataSet){
                binding.editTextTextUsername.setText(data.username)
                binding.editTextTextNama.setText(data.nama)
                val rolesItemSpinner = resources.getStringArray(R.array.Roles)
                val spinner = findViewById<Spinner>(R.id.spinner_roles)
                if (spinner != null) {
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, rolesItemSpinner)
                    spinner.adapter = adapter
                    spinner.setSelection(data.roles)
                    spinner.onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>,
                                                    view: View, position: Int, id: Long) {
                            when (spinner.selectedItem) {
                                "Admin" -> {
                                    updateDataUser(data.id_user,0)
                                }
                                "Pengelola" -> {
                                    updateDataUser(data.id_user,1)
                                }
                                "Karyawan" -> {
                                    updateDataUser(data.id_user,2)
                                }
                            }
                        }
                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // write code to perform some action
                        }
                    }
                }
            }

        }

    }

    private fun updateDataUser(user_id: Int, rolesPos: Int) {
        val username = binding.editTextTextUsername.text.toString()
        val nama = binding.editTextTextUsername.text.toString()
        val update = UpdateUser(user_id,username,nama,rolesPos)

        binding.buttonUbahData.setOnClickListener {
            val client = ApiConfig().getApiService().updateUser(update)
            client.enqueue(object : Callback<UpdateResponse> {
                override fun onResponse(
                    call: Call<UpdateResponse>,
                    response: Response<UpdateResponse>
                ) {
                    val resBody = response.body()
                    Log.d(ContentValues.TAG, "Respone Update barang: ${resBody.toString()}")
                }
                override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
                }

            })
        }

    }

    private fun backToMenu(){
        binding.BackMenuAdmin.setOnClickListener{
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}

