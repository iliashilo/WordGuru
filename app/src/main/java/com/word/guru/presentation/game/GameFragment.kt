package com.word.guru.presentation.game

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.word.guru.databinding.FragmentGameBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameFragment : Fragment(), OnGameFinishedListener, WordChecker {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by viewModels()
    private val args: GameFragmentArgs by navArgs()

    private var backPressedOnce = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.game = Game.Base(binding, this, this)

            viewModel.game?.startGame(viewModel.getRandomWord(args.lettersCount))
        } else {
            viewModel.game?.changeBinding(binding)
        }

        viewModel.game?.displayLetterCells()

        val onKeyClicked = OnClickListener {
            val key = (it as TextView)
            if (key.text.length == 1) {
                viewModel.game?.handleLetterKey(key)
            } else {
                viewModel.game?.handleDeleteKey()
            }
        }

        binding.firstKeyLineLayout.children.forEach {
            it.setOnClickListener(onKeyClicked)
        }
        binding.secondKeyLineLayout.children.forEach {
            it.setOnClickListener(onKeyClicked)
        }
        binding.thirdKeyLineLayout.children.forEach {
            it.setOnClickListener(onKeyClicked)
        }
    }

    override fun onFinish(win: Boolean, guessedWord: String) {
        val alertDialogBuilder = AlertDialog.Builder(binding.root.context)
        if (win) {
            alertDialogBuilder.setTitle("Congratulations!")
            alertDialogBuilder.setMessage("You Won!\n\nGuessed word was: $guessedWord")
            viewModel.incrementScores()
        } else {
            alertDialogBuilder.setTitle("Loose!")
            alertDialogBuilder.setMessage("You Loose!\n\nGuessed word was: $guessedWord")
        }
        alertDialogBuilder.setPositiveButton("Proceed") { _, _ ->
            binding.root.findNavController().popBackStack()
        }
        alertDialogBuilder.show()
    }

    override fun isWordExist(word: String): Boolean {
        return viewModel.isWordExist(word)
    }

}

interface OnGameFinishedListener {
    fun onFinish(win: Boolean, guessedWord: String)
}

interface WordChecker {
    fun isWordExist(word: String): Boolean
}