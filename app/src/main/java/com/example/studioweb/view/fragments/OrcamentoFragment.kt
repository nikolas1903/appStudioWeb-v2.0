package com.example.studioweb.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.studioweb.constants.SharedPreferencesConstants
import com.example.studioweb.databinding.FragmentOrcamentoBinding
import com.example.studioweb.services.repository.local.SecurityPreferences
import com.example.studioweb.view.LoginActivity
import com.example.studioweb.view.utils.CpfMask
import com.example.studioweb.view.utils.TelefoneMask
import com.example.studioweb.viewmodel.MeusOrcamentosViewModel
import com.example.studioweb.viewmodel.OrcamentoViewModel

class OrcamentoFragment : Fragment() {
    private lateinit var mOrcamentoViewModel: OrcamentoViewModel
    private lateinit var mSecurityPreferences: SecurityPreferences
    private var _binding: FragmentOrcamentoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        mOrcamentoViewModel = ViewModelProvider(this).get(OrcamentoViewModel::class.java)
        _binding = FragmentOrcamentoBinding.inflate(inflater, container, false)

        mSecurityPreferences = SecurityPreferences(context)

        binding.etCpf.addTextChangedListener(CpfMask.mask("###.###.###-##", binding.etCpf))
        binding.etTelefone.addTextChangedListener(TelefoneMask.insert("(##)#####-####", binding.etTelefone))

        setData()
        observe()
        setListeners()
        return binding.root
    }

    private fun observe() {
        mOrcamentoViewModel.createOrcamento.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(context, "Orçamento enviado!", Toast.LENGTH_SHORT).show()
                binding.etRamo.setText("")
                binding.etNomeEmpresa.setText("")
                binding.checkLosAngeles.isChecked = false
                binding.checkVegas.isChecked = false
                binding.checkParis.isChecked = false
                binding.checkMadrid.isChecked = false
                binding.checkVeneza.isChecked = false
                binding.checkLondon.isChecked = false
            }
        })
    }

    private fun setData() {
        binding.etNome.setText(mSecurityPreferences.get(SharedPreferencesConstants.SHARED.NOME))
        binding.etCpf.setText(mSecurityPreferences.get(SharedPreferencesConstants.SHARED.CPF))
        binding.etTelefone.setText(mSecurityPreferences.get(SharedPreferencesConstants.SHARED.TELEFONE))
        binding.etEmail.setText(mSecurityPreferences.get(SharedPreferencesConstants.SHARED.EMAIL))
    }

    private fun setListeners() {
        // Listener botão de Enviar Form
        binding.btnEnviarOrcamento.setOnClickListener {
            val nome = binding.etNome.text.toString()
            val cpfInvalido = binding.etCpf.text.toString()
            val cpf = cpfInvalido.replace(".", "").replace("-", "")
            val telefone = binding.etTelefone.text.toString()
            val email = binding.etEmail.text.toString()
            val ramo = binding.etRamo.text.toString()
            val nomeEmpresa = binding.etNomeEmpresa.text.toString()
            val checkLosAngeles = binding.checkLosAngeles.isChecked
            val checkLasVegas = binding.checkVegas.isChecked
            val checkParis = binding.checkParis.isChecked
            val checkMadrid = binding.checkMadrid.isChecked
            val checkVeneza = binding.checkVeneza.isChecked
            val checkLondon = binding.checkLondon.isChecked

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


            if (ramo.isEmpty() || nomeEmpresa.isEmpty()) {
                Toast.makeText(context, "Informe todos os campos!", Toast.LENGTH_SHORT).show()
            } else {
                mOrcamentoViewModel.enviarOrcamento(nome, cpf, telefone, email, ramo, nomeEmpresa, templates)
            }
        }
    }
}
