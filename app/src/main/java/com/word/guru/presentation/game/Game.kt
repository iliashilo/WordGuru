package com.word.guru.presentation.game

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.get
import com.word.guru.R
import com.word.guru.databinding.FragmentGameBinding

interface Game {

    fun startGame(wordToGuess: String)

    fun handleDeleteKey()

    fun handleLetterKey(key: TextView)

    fun displayLetterCells()

    fun changeBinding(binding: FragmentGameBinding)

    class Base(
        private var binding: FragmentGameBinding,
        private val onGameFinishedListener: OnGameFinishedListener,
        private val wordChecker: WordChecker
    ) : Game {

        private var gameState: GameState = GameState.NOT_STARTED

        private var wordToGuess = ""
        private var lettersCount = 0
        private var row = 0
        private var typedLetters = ""
        private val listOfAllLetters = mutableListOf<Char>()
        private var isAllowedNewLetter = false

        private val toast = Toast(binding.root.context.applicationContext).apply {
            setText("Not existing word!")
            duration = Toast.LENGTH_SHORT
        }

        override fun startGame(wordToGuess: String) {
            this.wordToGuess = wordToGuess
            lettersCount = wordToGuess.length
            row = 0
            gameState = GameState.STARTED
            isAllowedNewLetter = true
        }

        override fun changeBinding(binding: FragmentGameBinding) {
            this.binding = binding
        }

        override fun handleDeleteKey() {
            if (gameState == GameState.NOT_STARTED) return
            if (typedLetters.isNotEmpty()) {
                typedLetters = typedLetters.substring(0, typedLetters.length - 1)
                listOfAllLetters.removeLast()
                deleteLastLetter()
                isAllowedNewLetter = true
            }
        }

        override fun handleLetterKey(key: TextView) {
            if (gameState == GameState.NOT_STARTED) return
            if (!isAllowedNewLetter) return

            val pressedKey = key.text.toString().first()
            typedLetters += pressedKey
            listOfAllLetters.add(pressedKey)

            displayLetter(pressedKey)

            if (typedLetters.length != lettersCount) return

            if (wordChecker.isWordExist(typedLetters)) {
                checkWordAndMoveToNextRow(typedLetters)
            } else {
                isAllowedNewLetter = false
                displayWordNotExistMessage()
            }
        }

        private fun displayLetter(letter: Char) {
            (binding.lettersGridLayout.get(row * lettersCount + typedLetters.length - 1) as TextView).text =
                letter.toString()
        }

        override fun displayLetterCells() {
            val textViews = mutableListOf<TextView>()
            binding.lettersGridLayout.columnCount = lettersCount
            for (i in 0 until lettersCount * 6) {
                val textView = TextView(binding.lettersGridLayout.context).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 50.dpToPx(context)
                        height = 50.dpToPx(context)
                        setMargins(
                            1.dpToPx(context), 1.dpToPx(context),
                            1.dpToPx(context), 1.dpToPx(context)
                        )
                    }
                    background = AppCompatResources.getDrawable(context, R.drawable.cell_white)
                    gravity = Gravity.CENTER
                    maxLines = 1
                    setTextColor(ContextCompat.getColor(context, R.color.dark_blue))
                    textSize = 30f
                }
                textViews.add(textView)
                binding.lettersGridLayout.addView(textView)
            }
            listOfAllLetters.forEachIndexed { index, char ->
                textViews.get(index).text = char.toString()
            }
        }

        private fun Int.dpToPx(context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
            ).toInt()
        }

        private fun checkWordAndMoveToNextRow(word: String) {
            typedLetters = ""
            if (word == wordToGuess) {
                onGameFinishedListener.onFinish(true, wordToGuess)
                return
            }
            if (row == 5) {
                onGameFinishedListener.onFinish(false, wordToGuess)
                return
            }
            displayCorrectLetters(word)
            row++
        }

        private fun displayWordNotExistMessage() {
            toast.show()
        }

        private fun displayCorrectLetters(word: String) {

            var tempWordToGuess = wordToGuess

            word.forEachIndexed { index, char ->
                val actualIndex = wordToGuess.indexOf(char)
                if (actualIndex == -1) {
                    markLetterGrey(char, index)
                } else if (index == actualIndex) {
                    tempWordToGuess = tempWordToGuess.replaceFirst(char.toString(), "", true)
                    markLetterGreen(char, index)
                } else {
                    if (char in tempWordToGuess) {
                        tempWordToGuess = tempWordToGuess.replaceFirst(char.toString(), "", true)
                        markLetterYellow(char, index)
                    } else {
                        markLetterGrey(char, index)
                    }
                }
            }
        }

        private fun markLetterGreen(letter: Char, letterIndex: Int) {
            binding.lettersGridLayout.get(row * lettersCount + letterIndex)
                .setBackgroundResource(R.drawable.cell_green)
            changeKeyboardLetterColor(letter, R.drawable.cell_green, "green")
        }

        private fun markLetterYellow(letter: Char, letterIndex: Int) {
            binding.lettersGridLayout.get(row * lettersCount + letterIndex)
                .setBackgroundResource(R.drawable.cell_yellow)
            changeKeyboardLetterColor(letter, R.drawable.cell_yellow)
        }

        private fun markLetterGrey(letter: Char, letterIndex: Int) {
            binding.lettersGridLayout.get(row * lettersCount + letterIndex)
                .setBackgroundResource(R.drawable.cell_grey)
            changeKeyboardLetterColor(letter, R.drawable.cell_grey)
        }

        private fun changeKeyboardLetterColor(letter: Char, backgroundRes: Int, tag: String? = null) {
            val checkTextViewBlock: (View) -> Unit = checkTextViewBlock@{ it ->
                val textView = (it as TextView)
                if (textView.text.length > 1) return@checkTextViewBlock

                if (textView.tag == null && letter == textView.text.first()) {
                    textView.setBackgroundResource(backgroundRes)
                    textView.tag = tag
                }
            }
            binding.firstKeyLineLayout.children.forEach { checkTextViewBlock.invoke(it) }
            binding.secondKeyLineLayout.children.forEach { checkTextViewBlock.invoke(it) }
            binding.thirdKeyLineLayout.children.forEach { checkTextViewBlock.invoke(it) }
        }


        private fun deleteLastLetter() {
            val list = mutableListOf<View>()
            list.addAll(binding.lettersGridLayout.children)
            list.reverse()
            list.forEach {
                if ((it as TextView).text.isNotEmpty()) {
                    it.text = ""
                    return
                }
            }
        }
    }
}