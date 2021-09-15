package com.example.studioweb.view.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studioweb.R
import com.example.studioweb.viewmodel.TodosUsuariosViewModel

class TodosUsuariosFragment : Fragment() {

    companion object {
        fun newInstance() = TodosUsuariosFragment()
    }

    private lateinit var viewModel: TodosUsuariosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todos_usuarios, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TodosUsuariosViewModel::class.java)
        // TODO: Use the ViewModel
    }

}