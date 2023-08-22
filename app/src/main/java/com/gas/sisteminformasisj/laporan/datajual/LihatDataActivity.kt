package com.gas.sisteminformasisj.laporan.datajual

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gas.sisteminformasisj.api.GetTransaksiResponse
import com.gas.sisteminformasisj.api.TransaksiModelAPI
import com.gas.sisteminformasisj.databinding.ActivityLihatDataBinding
import com.gas.sisteminformasisj.laporan.datajual.detail.DetailTransaksiActivity
import com.gas.sisteminformasisj.laporan.datajual.detail.PDF
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.text.SimpleDateFormat
import java.util.*


class LihatDataActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLihatDataBinding
    private lateinit var adapter: ListAdapterTransaksi
    private lateinit var TransaksiModelArrayList: ArrayList<DataFilter>
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLihatDataBinding.inflate(layoutInflater)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupData(){
        val lihatDataJualViewModelApi = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[LihatDataJualViewModelApi::class.java]
        lihatDataJualViewModelApi.setData()
        lihatDataJualViewModelApi.ListJualData.observe(this){it ->
            setList(it)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setList(User: GetTransaksiResponse?) {
        TransaksiModelArrayList = ArrayList<DataFilter>()
        for (data in User?.values!!){

            val string = data.tanggal
            val defaultTimezone = TimeZone.getDefault().id
            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(string.replace("Z$".toRegex(), "+0000"))
            Log.d(TAG, "date: " + SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date))
            val tanggalfix = SimpleDateFormat("yyyy-MM-dd").format(date).toString()
            TransaksiModelArrayList.add(DataFilter(data.idTransaksi,tanggalfix,data.tanggal))
        }

        adapter = ListAdapterTransaksi(TransaksiModelArrayList)
        binding.rvHeroes.scrollToPosition(adapter.itemCount)
        binding.rvHeroes.adapter = adapter
        adapter.setOnItemClickCallback(object : ListAdapterTransaksi.OnItemClickCallback{
            override fun onItemClicked(data: DataFilter) {
                requsetStorage(data)
                selectedButtonEdit(data)
            }

            override fun onItemClicked2(data: Int) {
                selectedButtonHapus(data)
            }

        })

    }

    private fun Search() {
        binding.search.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filter(newText.toInt())
                }
                return false
            }
        })
    }

    private fun selectedButtonHapus(data: Int) {
        val builder = AlertDialog.Builder(this@LihatDataActivity)
        builder.setMessage("Apa Kamu Yakin Untuk Menghapus Data?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun filter(id: Int) {
        val filteredList = ArrayList<DataFilter>()
//        Log.d(TAG,"Data Array List User Filter: $userModelArrayList")
        for (item in TransaksiModelArrayList){
            // checking if the entered string matched with any item of our recycler view.
//            Toast.makeText(this, item.getidUser(), Toast.LENGTH_SHORT).show()
            if(item.getidtransaksi().equals(id)){
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

    private fun selectedButtonEdit(data: DataFilter) {
        val data2 = ArrayList<TransaksiModelAPI>()
        data2.add(TransaksiModelAPI(data.getidtransaksi(),data.getTanggal()))
        val intent = Intent(this, DetailTransaksiActivity::class.java)
        intent.putParcelableArrayListExtra("Data_Transaksi", data2)
        startActivity(intent)
        finish()
    }
    private fun PrintPDF(data: DataFilter) {
        val pdf = PDF(this,data)
        pdf.getData(data)

    }

    private fun requsetStorage(data: DataFilter) {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()){
                        PrintPDF(data)
                    }
                    if (report.isAnyPermissionPermanentlyDenied){

                    }

                }
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).withErrorListener{
                Toast.makeText(applicationContext,"ErrorOccired!",Toast.LENGTH_SHORT).show()
            }.onSameThread().check()
    }


}