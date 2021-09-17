package com.example.studioweb.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.studioweb.databinding.FragmentHomeBinding
import com.example.studioweb.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var mHomeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        mHomeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setListeners()

        return binding.root
    }

    private fun setListeners() {
        binding.imgSite1.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://technext.github.io/mirko/"))
            startActivity(browserIntent)
        }

        binding.imgSite2.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://technext.github.io/landmark/"))
            startActivity(browserIntent)
        }

        binding.imgSite3.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://technext.github.io/spify/"))
            startActivity(browserIntent)
        }

        binding.imgSite4.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://technext.github.io/restaurantly/"))
            startActivity(browserIntent)
        }

        binding.imgSite5.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://technext.github.io/favison/index.html"))
            startActivity(browserIntent)
        }

        binding.imgSite6.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://demos.creative-tim.com/vue-now-ui-kit/?_ga=2.264841935.1525604233.1626715090-1662022310.1626042553#/"))
            startActivity(browserIntent)
        }
    }
}