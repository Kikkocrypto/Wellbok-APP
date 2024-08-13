package com.example.wellbook_drawer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.wellbook_drawer.R
import com.example.wellbook_drawer.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root



        binding.btnAggiungiUtente.setOnClickListener {
            findNavController().navigate(R.id.inserisci_Utente)
        }

        binding.btnAggiungiPrenotazione.setOnClickListener {
            findNavController().navigate(R.id.inserisci_Prenotazione)
        }

        binding.btnVisualizzaUtenti.setOnClickListener {
            findNavController().navigate(R.id.view_Users)
        }

        binding.btnVisualizzaPrenotazioni.setOnClickListener {
            findNavController().navigate(R.id.view_Reservations)
        }

                return root
            }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
        }
