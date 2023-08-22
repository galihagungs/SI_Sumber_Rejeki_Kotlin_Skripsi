package com.gas.sisteminformasisj.stok

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gas.sisteminformasisj.api.ApiConfig
import com.gas.sisteminformasisj.api.BarangModelAPI
import com.gas.sisteminformasisj.api.DeleteResponse
import com.gas.sisteminformasisj.api.GetBarangResponse
import com.gas.sisteminformasisj.databinding.ActivityStokBinding
import com.gas.sisteminformasisj.mainmenu.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class StokActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStokBinding
    private lateinit var adapter: ListAdapterStok
    private lateinit var barangModelArrayList: ArrayList<SearchBarangStok>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStokBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        val layoutManager = LinearLayoutManager(this)
        binding.rvHeroes.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvHeroes.addItemDecoration(itemDecoration)
        setupData()
        Search()
        binding.BackMenuMain.setOnClickListener {
            backtoMain()
        }
        binding.buttonTambahBarang.setOnClickListener {
            tambaBarang()
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
    private fun setupData(){
        val stokViewModelApi = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[StokViewModelApi::class.java]
        stokViewModelApi.setData()
        stokViewModelApi.stokData.observe(this){ GetBarangResponse ->
            setList(GetBarangResponse)
        }
    }

    private fun setList(dataBarang: GetBarangResponse?) {
        barangModelArrayList = ArrayList<SearchBarangStok>()
        for (data in dataBarang?.values!!){
            barangModelArrayList.add(SearchBarangStok(data.idBarang,data.code,data.namaBarang,data.stok,data.harga))
        }
        adapter = ListAdapterStok(barangModelArrayList)
        binding.rvHeroes.scrollToPosition(adapter.itemCount)
        binding.rvHeroes.adapter = adapter
        adapter.setOnItemClickCallback(object : ListAdapterStok.OnItemClickCallback{
            override fun onItemClicked(data: SearchBarangStok) {
                selectedButtonEdit(data)
            }

            override fun onItemClicked2(data: Int) {
                selectedButtonHapus(data)
            }

        })
    }

    private fun selectedButtonEdit(data: SearchBarangStok) {
        val data2 = ArrayList<BarangModelAPI>()
        data2.add(BarangModelAPI(data.getidBarang(),data.getCode(),data.getnamaBarang(),data.getStok(),data.getHarga()))
        val intent = Intent(this, EditBarangActivity::class.java)
        intent.putParcelableArrayListExtra("DataBarang", data2)
        startActivity(intent)
        finish()
    }

    private fun selectedButtonHapus(data: Int) {
        val builder = AlertDialog.Builder(this@StokActivity)
        builder.setMessage("Apa Kamu Yakin Untuk Menghapus Data?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                val client = ApiConfig().getApiService().deleteBarang(data)
                client.enqueue(object : Callback<DeleteResponse> {
                    override fun onResponse(
                        call: Call<DeleteResponse>,
                        response: Response<DeleteResponse>
                    ) {
                        val resBody = response.body()
                        val status = resBody?.status

                        if(status == 200){
                            Log.e(ContentValues.TAG, "onSuccess: $resBody")
                            Toast.makeText(this@StokActivity, "Delete Barang Berhasil", Toast.LENGTH_SHORT).show()
                            val builder = AlertDialog.Builder(this@StokActivity)
                            builder.setMessage("Data Sudah Terhapus")
                                .setCancelable(false)
                                .setPositiveButton("Ok") { dialog, id ->
                                    val intent = intent
                                    finish()
                                    startActivity(intent)
                                }
                            val alert = builder.create()
                            alert.show()
                        }else{
                            Log.e(ContentValues.TAG, "onSuccess: $resBody")
                            Toast.makeText(this@StokActivity, "Delete Barang Gagal", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                        Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
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
        val filteredList = ArrayList<SearchBarangStok>()
//        Log.d(TAG,"Data Array List User Filter: $userModelArrayList")
        for (item in barangModelArrayList){
            // checking if the entered string matched with any item of our recycler view.
//            Toast.makeText(this, item.getidUser(), Toast.LENGTH_SHORT).show()
            if(item.getnamaBarang().toLowerCase().contains(text.lowercase(Locale.getDefault()))) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "Data Tidak Ditemukan..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredList)
        }
    }

    private fun tambaBarang(){
        startActivity(Intent(this, TambahStokActivity::class.java))
        finish()
    }

    private fun backtoMain(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}