package com.gas.sisteminformasisj.admin

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.gas.sisteminformasisj.R
import com.gas.sisteminformasisj.api.*
import com.gas.sisteminformasisj.databinding.ActivityAdminTambahBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AdminTambahActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminTambahBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminTambahBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setListrole()
        binding.BackMenuAdmin.setOnClickListener {
            back()
        }
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

    private fun setListrole(){
        val rolesItemSpinner = resources.getStringArray(R.array.Roles)
        val spinner = findViewById<Spinner>(R.id.spinner_roles)
        if (spinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, rolesItemSpinner)
            spinner.adapter = adapter
//            spinner.setSelection(data.roles)
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    when (spinner.selectedItem) {
                        "Pilih Role" ->{
                            Toast.makeText(this@AdminTambahActivity, "Masukan Role", Toast.LENGTH_SHORT).show()
                        }
                        "Admin" -> {
                            tambahUserQuerry(1)
                        }
                        "Pengelola" -> {
                            tambahUserQuerry(2)
                        }
                        "Karyawan" -> {
                            tambahUserQuerry(3)
                        }
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    private fun tambahUserQuerry(role: Int) {
        var nama = binding.editTextTextNama.text.toString()
        var username = binding.editTextTextUsername.text.toString()
        var password = binding.inputPassword.text.toString()
        var tambahuser = tambahUser(username,password,nama,role)

        binding.buttonUbahData.setOnClickListener {
            Toast.makeText(this@AdminTambahActivity, tambahuser.toString(), Toast.LENGTH_SHORT).show()
//            Toast.makeText(this@AdminTambahActivity, username, Toast.LENGTH_SHORT).show()
            when {
                nama.isEmpty()->{
                    Toast.makeText(this@AdminTambahActivity, "Masukan Nama", Toast.LENGTH_SHORT).show()
                    tambahUserQuerry(role)
                }
                username.isEmpty() -> {
                    Toast.makeText(this@AdminTambahActivity, "Masukan Username", Toast.LENGTH_SHORT).show()
                    tambahUserQuerry(role)
                }
                password.isEmpty() -> {
                    Toast.makeText(this@AdminTambahActivity, "Masukan Password", Toast.LENGTH_SHORT).show()
                    tambahUserQuerry(role)
                }
                role == 0 ->{
                    Toast.makeText(this@AdminTambahActivity, "Masukan Role", Toast.LENGTH_SHORT).show()
                    tambahUserQuerry(role)
                }
                else -> {
                    Toast.makeText(this@AdminTambahActivity, tambahuser.toString(), Toast.LENGTH_SHORT).show()
                    val client = ApiConfig().getApiService().tambahUser(tambahuser)
                    client.enqueue(object : Callback<UpdateResponse> {
                        override fun onResponse(
                            call: Call<UpdateResponse>,
                            response: Response<UpdateResponse>
                        ) {
                            val resBody = response.body()
                            if (resBody?.status == 200){
//                                Toast.makeText(this@AdminTambahActivity, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                                Log.d(ContentValues.TAG, resBody.toString())
                                val builder = AlertDialog.Builder(this@AdminTambahActivity)
                                builder.setMessage("User sudah dibuat")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok") { dialog, id ->
                                        back()
                                        finish()
                                    }
                                val alert = builder.create()
                                alert.show()
                            }else{
                                Toast.makeText(this@AdminTambahActivity, "Usernam Sudah Digunakan", Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                            Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
                        }

                    })
                }
            }


        }
    }

    private fun back(){
        val intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
        finish()
    }
}