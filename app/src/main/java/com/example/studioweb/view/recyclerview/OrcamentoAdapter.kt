package com.example.studioweb.view.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studioweb.R
import com.example.studioweb.services.repository.models.OrcamentoModelAPI

class OrcamentoAdapter : RecyclerView.Adapter<OrcamentoViewHolder>() {
    private var mOrcamentoList : List<OrcamentoModelAPI.OrcamentoResponse> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrcamentoViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.row_orcamento, parent, false)
        return OrcamentoViewHolder(item)
    }

    override fun onBindViewHolder(holder: OrcamentoViewHolder, position: Int) {
        holder.bind(mOrcamentoList[position])

    }

    override fun getItemCount(): Int {
        return mOrcamentoList.count()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateOrcamentos(list: List<OrcamentoModelAPI.OrcamentoResponse>) {
        mOrcamentoList = list
        notifyDataSetChanged()
    }

}