package com.example.studioweb.view.recyclerview

import android.app.AlertDialog
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.studioweb.R
import com.example.studioweb.listener.APIListenerOrcamento
import com.example.studioweb.listener.APIListenerOrcamentoGetId
import com.example.studioweb.services.repository.models.OrcamentoModelAPI
import com.example.studioweb.services.repository.remote.orcamento.OrcamentoRepositoryAPI
import kotlinx.android.synthetic.main.dialog_edit_orcamento.view.*
import kotlinx.android.synthetic.main.fragment_orcamento.view.*
import kotlinx.android.synthetic.main.row_orcamento.view.*

class OrcamentoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val mOrcamentoRepositoryAPI = OrcamentoRepositoryAPI()
    private val mUpdateOrcamento = MutableLiveData<Boolean>()
    var updateOrcamento: LiveData<Boolean> = mUpdateOrcamento

    fun bind(orcamento: OrcamentoModelAPI.OrcamentoResponse) {

        observe()
        setData(orcamento)
        setListener(orcamento)
    }

    private fun observe() {
        updateOrcamento.observe(itemView.context as LifecycleOwner, Observer {
            if (it) {
             Toast.makeText(itemView.context, "Orçamento salvo com sucesso!", Toast.LENGTH_SHORT).show()
            }
        })
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
            val id = orcamento.id

            mOrcamentoRepositoryAPI.getOrcamentoById(id, object : APIListenerOrcamentoGetId {
                override fun onSuccess(modelAPI: OrcamentoModelAPI.OrcamentoResponse) {
                    var s = ""
                }

                override fun onFailure(str: String) {
                    var s = ""
                }

            })

            val view = View.inflate(itemView.context, R.layout.dialog_edit_orcamento, null)

            val builder = AlertDialog.Builder(itemView.context)
            builder.setView(view)

            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            view.et_editNome.setText(orcamento.nome)
            view.et_editCpf.setText(orcamento.cpf)
            view.et_editTelefone.setText(orcamento.telefone)
            view.et_editEmail.setText(orcamento.email)
            view.et_editRamo.setText(orcamento.ramo)
            view.et_editNomeEmpresa.setText(orcamento.empresa)

            view.btn_editOrcamento.setOnClickListener {
                val editNome = view.et_editNome.text.toString()
                val editCpf = view.et_editCpf.text.toString()
                val editTelefone = view.et_editTelefone.text.toString()
                val editEmail = view.et_editEmail.text.toString()
                val editRamo = view.et_editRamo.text.toString()
                val editEmpresa = view.et_editNomeEmpresa.text.toString()
                val editStatus = orcamento.status

                val checkLosAngeles = view.check_editLosAngeles.isChecked
                val checkLasVegas = view.check_editVegas.isChecked
                val checkParis = view.check_editParis.isChecked
                val checkMadrid = view.check_editMadrid.isChecked
                val checkVeneza = view.check_editVeneza.isChecked
                val checkLondon = view.check_editLondon.isChecked

                val templatesArray = ArrayList<String>()

                if (checkLosAngeles) {
                    templatesArray.add("Los Angeles")
                }
                if (checkLasVegas) {
                    templatesArray.add("Las Vegas")
                }
                if (checkParis) {
                    templatesArray.add("Paris")
                }
                if (checkMadrid) {
                    templatesArray.add("Madrid")
                }
                if (checkVeneza) {
                    templatesArray.add("Veneza")
                }
                if (checkLondon) {
                    templatesArray.add("London")
                }

                val templatesIncorreto = templatesArray.toString()
                val templates = templatesIncorreto.replace("[", "").replace("]", "")


                if (editRamo.isEmpty() || editEmpresa.isEmpty()) {
                    Toast.makeText(view.context, "Informe todos os campos!", Toast.LENGTH_SHORT).show()
                } else {
                    mOrcamentoRepositoryAPI.updateOrcamentoById(id, editNome, editCpf, editTelefone, editEmail, editRamo, editEmpresa, templates, editStatus, object : APIListenerOrcamento{
                        override fun onSuccess(modelAPI: OrcamentoModelAPI.OrcamentoResponse) {
                            mUpdateOrcamento.value = true
                        }

                        override fun onFailure(str: String) {
                            mUpdateOrcamento.value = false
                        }

                    })
                }
            dialog.hide()
            }
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

