package com.gas.sisteminformasisj.admin

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gas.sisteminformasisj.api.*
import com.gas.sisteminformasisj.databinding.ActivityAdminBinding
import com.gas.sisteminformasisj.mainmenu.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private lateinit var adapter: ListAdapterUser
    private lateinit var userModelArrayList: ArrayList<SearchUser>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        val layoutManager = LinearLayoutManager(this)
        binding.rvHeroes.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvHeroes.addItemDecoration(itemDecoration)

        setupData()
        backToMenu()
        Search()
        tambahakunActivity()
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

    private fun setupData(){
        val adminViewModelApi = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[AdminViewModelApi::class.java]
        adminViewModelApi.setData()
        adminViewModelApi.userData.observe(this){GetUserResponse ->
            setList(GetUserResponse)
        }
    }

    private fun setList(User: GetUserResponse?) {
        userModelArrayList = ArrayList<SearchUser>()
        for (data in User?.values!!){
            userModelArrayList.add(SearchUser(data.idUser,data.username,data.nama,data.roles))
        }
//        Log.d(TAG,"Data Array List User: $userModelArrayList")
//        val adapter = ListAdapterUser(userModelArrayList)
        adapter = ListAdapterUser(userModelArrayList)
        binding.rvHeroes.scrollToPosition(adapter.itemCount)
        binding.rvHeroes.adapter = adapter
        adapter.setOnItemClickCallback(object : ListAdapterUser.OnItemClickCallback{
            override fun onItemClicked(data: SearchUser) {
                selectedButtonEdit(data)
            }
            override fun onItemClicked2(data: SearchUser) {
                selectedButtonHapus(data)
            }
        })

    }
    private fun selectedButtonEdit(data: SearchUser) {
        val data2 = ArrayList<UserModelApi>()
        data2.add(UserModelApi(data.getidUser(),data.getuserName(),"",data.getNama(),data.getRole(),0))
        val intent = Intent(this,EditMenuAdmin::class.java)
        intent.putParcelableArrayListExtra("Data", data2)
        startActivity(intent)
    }

    private fun selectedButtonHapus(data: SearchUser) {
        val builder = AlertDialog.Builder(this@AdminActivity)
        builder.setMessage("Apa Kamu Yakin Untuk Menghapus User?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                val client = ApiConfig().getApiService().deleteUser(data.getidUser())
                client.enqueue(object : Callback<DeleteResponse> {
                    override fun onResponse(
                        call: Call<DeleteResponse>,
                        response: Response<DeleteResponse>
                    ) {
                        val resBody = response.body()
                        val status = resBody?.status

                        if(status == 200){
                            Log.e(TAG, "onSuccess: $resBody")
                            Toast.makeText(this@AdminActivity, "Delete User Berhasil", Toast.LENGTH_SHORT).show()
                            val builder = AlertDialog.Builder(this@AdminActivity)
                            builder.setMessage("Data Sudah Terhapus")
                                .setCancelable(false)
                                .setPositiveButton("Ok") { dialog, id ->
                                    setupData()
                                }
                            val alert = builder.create()
                            alert.show()
                        }else{
                            Log.e(TAG, "onSuccess: $resBody")
                            Toast.makeText(this@AdminActivity, "Delete User Gagal", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                        Log.e(TAG, "onFailure: ${t.message.toString()}")
                    }

                })
            }
            .setNegativeButton("No") { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun backToMenu(){
        binding.BackMenuMainAdmin.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun Search() {
        binding.search.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filter(newText)
                }
                return false
            }
        })
    }

    private fun filter(text: String) {
        val filteredList = ArrayList<SearchUser>()
//        Log.d(TAG,"Data Array List User Filter: $userModelArrayList")
        for (item in userModelArrayList){
            // checking if the entered string matched with any item of our recycler view.
//            Toast.makeText(this, item.getidUser(), Toast.LENGTH_SHORT).show()
            if(item.getuserName().toLowerCase().contains(text.lowercase(Locale.getDefault()))) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredList)
        }
    }

    private fun tambahakunActivity(){
        binding.buttonTambahUser.setOnClickListener {
            startActivity(Intent(this, AdminTambahActivity::class.java))
        }
    }
}