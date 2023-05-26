package com.word.guru.domain

import com.word.guru.data.ScoresPreferences

interface IncrementScoresUseCase {

    fun execute()

    class Base(private val scoresPreferences: ScoresPreferences) : IncrementScoresUseCase {

        override fun execute() {
            scoresPreferences.saveScores(scoresPreferences.getScores() + 1)
        }

    }

}