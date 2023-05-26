package com.word.guru.presentation.game

import androidx.lifecycle.ViewModel
import com.word.guru.domain.GetRandomWordUseCase
import com.word.guru.domain.IncrementScoresUseCase
import com.word.guru.domain.IsWordExistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val incrementScoresUseCase: IncrementScoresUseCase,
    private val getRandomWordUseCase: GetRandomWordUseCase,
    private val isWordExistUseCase: IsWordExistUseCase
) : ViewModel() {

    var game: Game? = null

    fun getRandomWord(lettersCount: Int): String {
        return getRandomWordUseCase.execute(lettersCount)
    }

    fun isWordExist(word: String): Boolean {
        return isWordExistUseCase.execute(word)
    }

    fun incrementScores() {
        incrementScoresUseCase.execute()
    }

}