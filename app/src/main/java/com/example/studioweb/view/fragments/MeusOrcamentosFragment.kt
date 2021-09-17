package com.example.studioweb.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studioweb.constants.SharedPreferencesConstants
import com.example.studioweb.databinding.FragmentMeusOrcamentosBinding
import com.example.studioweb.services.repository.local.SecurityPreferences
import com.example.studioweb.view.recyclerview.OrcamentoAdapter
import com.example.studioweb.viewmodel.MeusOrcamentosViewModel

class MeusOrcamentosFragment : Fragment() {
    private lateinit var mSecurityPreferences: SecurityPreferences
    private lateinit var mMeusOrcamentosViewModel: MeusOrcamentosViewModel
    private val mAdapter : OrcamentoAdapter = OrcamentoAdapter()

    private var _binding: FragmentMeusOrcamentosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mMeusOrcamentosViewModel = ViewModelProvider(this).get(MeusOrcamentosViewModel::class.java)
        mSecurityPreferences = SecurityPreferences(context)
        _binding = FragmentMeusOrcamentosBinding.inflate(inflater, container, false)
        binding.tvSemOrcamento.visibility = GONE

        //RecyclerView
        // Obter a Recycler
        val recycler = binding.recyclerOrcamentos

        // Definir um Layout
        recycler.layoutManager = LinearLayoutManager(context)

        // Definir um Adapter
        recycler.adapter = mAdapter

        val cpf =(mSecurityPreferences.get(SharedPreferencesConstants.SHARED.CPF))
        observer()
        mMeusOrcamentosViewModel.buscar(cpf)



        return binding.root
    }

    private fun observer() {
        mMeusOrcamentosViewModel.orcamentoList.observe(viewLifecycleOwner, Observer{
            mAdapter.updateOrcamentos(it)
            if(it.isEmpty()){
                binding.tvSemOrcamento.visibility = VISIBLE
            } else{
                binding.tvSemOrcamento.visibility = GONE
            }
        })
    }
}