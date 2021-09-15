package com.example.studioweb.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.studioweb.databinding.FragmentOrcamentoBinding
import com.example.studioweb.viewmodel.GalleryViewModel

import kotlinx.android.synthetic.main.fragment_orcamento.*


class OrcamentoFragment : Fragment() {
    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentOrcamentoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
        _binding = FragmentOrcamentoBinding.inflate(inflater, container, false)


        return binding.root
    }
}