package com.manta.whatsapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.manta.whatsapp.SenderListActivity
import com.manta.whatsapp.WhatsAppConnectActivity
import com.manta.whatsapp.databinding.FragmentWhatsappBinding

class WhatsAppFragment : Fragment() {
    private var _binding: FragmentWhatsappBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWhatsappBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnConnect.setOnClickListener {
            startActivity(Intent(requireContext(), WhatsAppConnectActivity::class.java))
        }
        binding.btnManageSenders.setOnClickListener {
            startActivity(Intent(requireContext(), SenderListActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}