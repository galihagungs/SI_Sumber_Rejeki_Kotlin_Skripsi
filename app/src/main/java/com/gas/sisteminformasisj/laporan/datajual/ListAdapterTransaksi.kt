package com.gas.sisteminformasisj.laporan.datajual

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gas.sisteminformasisj.R
import com.gas.sisteminformasisj.admin.SearchUser


class ListAdapterTransaksi (TransaksiModelArrayList: ArrayList<DataFilter>): RecyclerView.Adapter<ListAdapterTransaksi.ListViewHolder>() {

    private var TransaksiModelArrayList: ArrayList<DataFilter>

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun filterList(filterList: ArrayList<DataFilter>) {
        // below line is to add our filtered
        // list in our course array list.
        TransaksiModelArrayList = filterList
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var id_transaksi: TextView = itemView.findViewById(R.id.id_transaksi)
        var tanggal: TextView = itemView.findViewById(R.id.TanggalTran)
        var detial: ImageButton = itemView.findViewById(R.id.detailTransaksi)
        var hapus: ImageButton = itemView.findViewById(R.id.hapusTransaksi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_transaksi,parent,false)
        return ListAdapterTransaksi.ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val Transaksi =  TransaksiModelArrayList[position]
        holder.id_transaksi.text = "SJ/001/${Transaksi.getidtransaksi()}"
        holder.tanggal.text = Transaksi.getTanggaldiplay()
        holder.detial.setOnClickListener {
            onItemClickCallback.onItemClicked(TransaksiModelArrayList[holder.adapterPosition])
        }
        holder.hapus.setOnClickListener {
            onItemClickCallback.onItemClicked2(Transaksi.getidtransaksi())
        }
    }

    override fun getItemCount(): Int {
        return TransaksiModelArrayList.size
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: DataFilter)
        fun onItemClicked2(data: Int)
    }

    init {
        this.TransaksiModelArrayList = TransaksiModelArrayList
    }
}