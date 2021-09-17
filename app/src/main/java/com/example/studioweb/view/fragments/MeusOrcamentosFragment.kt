package com.example.studioweb.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.example.studioweb.constants.SharedPreferencesConstants
import com.example.studioweb.databinding.FragmentMeusOrcamentosBinding
import com.example.studioweb.services.repository.local.SecurityPreferences
import com.example.studioweb.services.repository.models.OrcamentoModelAPI
import com.example.studioweb.viewmodel.MeusOrcamentosViewModel

class MeusOrcamentosFragment : Fragment() {
    private lateinit var mSecurityPreferences: SecurityPreferences
    private lateinit var mMeusOrcamentosViewModel: MeusOrcamentosViewModel

    private var _binding: FragmentMeusOrcamentosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mMeusOrcamentosViewModel = ViewModelProvider(this).get(MeusOrcamentosViewModel::class.java)
        mSecurityPreferences = SecurityPreferences(context)
        _binding = FragmentMeusOrcamentosBinding.inflate(inflater, container, false)








        return binding.root
    }
}