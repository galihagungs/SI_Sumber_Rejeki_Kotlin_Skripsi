package com.gas.sisteminformasisj.kasir

import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gas.sisteminformasisj.R
import com.gas.sisteminformasisj.admin.EditMenuAdmin
import com.gas.sisteminformasisj.api.GetBarangResponse
import com.gas.sisteminformasisj.api.UserModelApi
import com.gas.sisteminformasisj.databinding.ActivityKasirTambahBinding
import com.gas.sisteminformasisj.login.LoginActivity
import com.gas.sisteminformasisj.viewmodel.BarangViewModel
import com.gas.sisteminformasisj.viewmodel.CartViewModel
import java.util.*


class KasirTambahActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKasirTambahBinding
    private lateinit var adapter: ListAdapterBarang
    private lateinit var barangModelArrayList: ArrayList<SearchBarang>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKasirTambahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        val layoutManager = LinearLayoutManager(this)
        binding.rvHeroes.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvHeroes.addItemDecoration(itemDecoration)

        setupData()
        Search()

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
        val KasirModelGetALLAPI = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[BarangViewModel::class.java]
        KasirModelGetALLAPI.getAllBarang()
        KasirModelGetALLAPI.postValuesItemBarang.observe(this){GetBarangResponse ->
//            setList(GetBarangResponse)
        }
    }
//    private fun setList(Barang: GetBarangResponse?) {
//        barangModelArrayList = ArrayList<SearchBarang>()
//        for (data in Barang?.values!!){
//            barangModelArrayList.add(SearchBarang(data.idBarang,data.code,data.namaBarang,data.stok,data.harga))
//        }
//        Log.d(ContentValues.TAG, "Data Array List Barang: $barangModelArrayList")
//
//        adapter = ListAdapterBarang(barangModelArrayList)
//        binding.rvHeroes?.scrollToPosition(adapter.itemCount)
//        binding.rvHeroes?.adapter = adapter
//
////        adapter.setOnItemClickCallback(object : ListAdapterBarang.OnItemClickCallback {
////            override fun onItemClicked(data: SearchBarang) {
////                showSelectedStory(data)
////            }
////        })
//    }
    private fun showSelectedStory(data: SearchBarang){
        showDialog(data)
    }


    private fun showDialog(data: SearchBarang){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.input_barang_kasir_tambah)

        dialog.window!!.setBackgroundDrawableResource(R.drawable.bg_window_input_barang)

        val btnClose: Button = dialog.findViewById(R.id.btn_no)
        val btntambah: Button = dialog.findViewById(R.id.btn_yes)
        val namaBarang: TextView = dialog.findViewById(R.id.namaBarang)
        val qty: TextView = dialog.findViewById(com.gas.sisteminformasisj.R.id.qty)
        val plusqty: ImageButton = dialog.findViewById(R.id.tambahQTY)
        val minqty: ImageButton = dialog.findViewById(R.id.kurangQTY)
        var qtyvalue = 0
        namaBarang.text = data.getnamaBarang()
        qty.text = qtyvalue.toString()
        plusqty.setOnClickListener {
            if ( qtyvalue < 20){
                qtyvalue++
                qty.text = qtyvalue.toString()
            }
        }
        minqty.setOnClickListener {
            if (qtyvalue != 0){
                qtyvalue--
                qty.text = qtyvalue.toString()
            }
        }

//        btntambah.setOnClickListener{
//            val data2 = ArrayList<BarangModel>()
//            data2.add(BarangModel(data.getidbarang(),data.getCode(),data.getnamaBarang(),qtyvalue,data.getHarga()))
//            val intent = Intent(this, KasirActivity::class.java)
//            intent.putParcelableArrayListExtra("Data", data2)
//            startActivity(intent)
//            finish()
//            dialog.dismiss()
//        }
        btnClose.setOnClickListener { dialog.dismiss() }
        dialog.show()

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
        val filteredList = ArrayList<SearchBarang>()
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
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredList)
        }
    }
}