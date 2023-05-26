package com.word.guru.domain

import com.word.guru.data.ScoresPreferences

interface GetScoresUseCase {

    fun execute(): Int

    class Base(private val scoresPreferences: ScoresPreferences) : GetScoresUseCase {

        override fun execute(): Int {
            return scoresPreferences.getScores()
        }

    }

}