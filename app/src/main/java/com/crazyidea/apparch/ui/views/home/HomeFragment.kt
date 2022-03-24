package com.crazyidea.apparch.ui.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crazyidea.apparch.R
import com.crazyidea.apparch.model.Status
import com.crazyidea.apparch.databinding.FragmentHomeBinding
import com.crazyidea.apparch.withSimpleAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel by viewModels<UniversitiesViewModel>()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchUniversities("Egypt")
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uniList.collect{
                    viewModel.uniList.collect{
                        when (it.status) {
                            Status.SUCCESS -> {
                                binding.loading.visibility = GONE
                                it.data?.let { data ->
                                    binding.list.withSimpleAdapter(data, R.layout.item_university) {
                                        val countryTV = itemView.findViewById<TextView>(R.id.country)
                                        val uniTV = itemView.findViewById<TextView>(R.id.universityName)
                                        uniTV.text = it.name
                                        countryTV.text = it.country
                                    }
                                }
                            }
                            Status.LOADING -> {
                                binding.loading.visibility = VISIBLE
                            }
                            Status.ERROR -> {
                                binding.loading.visibility = GONE
                                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()

                            }
                        }
                    }
                }
            }
        }
    }
}