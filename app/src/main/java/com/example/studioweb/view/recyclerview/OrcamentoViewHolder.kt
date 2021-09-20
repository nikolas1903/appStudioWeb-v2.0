package com.example.studioweb.view.recyclerview

import android.app.AlertDialog
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.studioweb.R
import com.example.studioweb.services.repository.models.OrcamentoModelAPI
import kotlinx.android.synthetic.main.row_orcamento.view.*

class OrcamentoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(orcamento: OrcamentoModelAPI.OrcamentoResponse) {

        setData(orcamento)
        setListener(orcamento)
    }

    private fun setListener(orcamento: OrcamentoModelAPI.OrcamentoResponse) {
        //Listener Ver Status Orçamento
        val btnStatus = itemView.findViewById<Button>(R.id.btn_status)
        btnStatus.setOnClickListener {
            val status = orcamento.status
            val id = orcamento.id
            AlertDialog.Builder(itemView.context)
                .setTitle("Informação sobre orçamento: $id")
                .setMessage("Status: $status")
                .setNeutralButton("OK", null)
                .show()
        }

        //Listener Editar Orçamento
        val btnExcluir = itemView.findViewById<Button>(R.id.btn_editarOrcamento)
        btnExcluir.setOnClickListener {
            Toast.makeText(itemView.context, "recebi 2", Toast.LENGTH_LONG).show()
        }
    }

    private fun setData(orcamento: OrcamentoModelAPI.OrcamentoResponse) {
        val nome = itemView.findViewById<TextView>(R.id.tv_nomeOrcamento)
        nome.text = orcamento.nome
        val empresa = itemView.findViewById<TextView>(R.id.tv_empresaOrcamento)
        empresa.text = orcamento.empresa
        val templates = itemView.findViewById<TextView>(R.id.tv_templatesOrcamento)
        templates.text = orcamento.templates

        val quaisTemplates = itemView.findViewById<TextView>(R.id.tv_templatesFavs)

        if (orcamento.templates.isEmpty()) {
            quaisTemplates.visibility = GONE
            templates.visibility = GONE
        } else {
            quaisTemplates.visibility = VISIBLE
            templates.visibility = VISIBLE
        }

        val status = orcamento.status
        if (status == "Finalizado") {
            itemView.img_finalizado.visibility = VISIBLE
            itemView.tv_finalizado.visibility = VISIBLE
        } else {
            itemView.img_finalizado.visibility = GONE
            itemView.tv_finalizado.visibility = GONE
        }

        if (status == "Recebido") {
            itemView.img_pendente.visibility = VISIBLE
            itemView.tv_pendente.visibility = VISIBLE
        } else {
            itemView.img_pendente.visibility = GONE
            itemView.tv_pendente.visibility = GONE
        }
    }
}

