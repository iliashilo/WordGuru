package com.word.guru.presentation.mode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.word.guru.databinding.FragmentSelectModeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectModeFragment : Fragment() {

    private var _binding: FragmentSelectModeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SelectModeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectModeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fourLettersModeButton.setOnClickListener {
            findNavController().navigate(
                SelectModeFragmentDirections.actionSelectModeFragmentToGameFragment(4)
            )
        }
        binding.fiveLettersModeButton.setOnClickListener {
            findNavController().navigate(
                SelectModeFragmentDirections.actionSelectModeFragmentToGameFragment(5)
            )
        }
        binding.sixLettersModeButton.setOnClickListener {
            findNavController().navigate(
                SelectModeFragmentDirections.actionSelectModeFragmentToGameFragment(6)
            )
        }

        viewModel.scoresLiveData().observe(viewLifecycleOwner) { scores ->
            binding.scoresTextView.text = scores.toString()
        }

        parentFragmentManager.addOnBackStackChangedListener {
            viewModel.updateScores()
        }

    }
}