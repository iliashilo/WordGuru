package com.word.guru.presentation.mode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.word.guru.domain.GetScoresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectModeViewModel @Inject constructor(
    private val getScoresUseCase: GetScoresUseCase
) : ViewModel() {

    private val scoresLiveData = MutableLiveData<Int>()

    init {
        updateScores()
    }

    fun updateScores() {
        val scores = getScoresUseCase.execute()
        scoresLiveData.value = scores
    }

    fun scoresLiveData(): LiveData<Int> = scoresLiveData

}