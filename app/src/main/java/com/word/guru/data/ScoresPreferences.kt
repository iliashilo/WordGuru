package com.word.guru.data

import android.content.Context

interface ScoresPreferences {

    fun saveScores(scores: Int)

    fun getScores(): Int

    class Base(context: Context) : ScoresPreferences {

        companion object {
            private const val SCORES_PREFERENCES = "SCORES_PREFERENCES"
            private const val SCORES_KEY = "SCORES_KEY"
        }
        private val sharedPrefs = context.getSharedPreferences(SCORES_PREFERENCES, Context.MODE_PRIVATE)
        override fun saveScores(scores: Int) {
            sharedPrefs.edit().putInt(SCORES_KEY, scores).apply()
        }
        override fun getScores(): Int {
            return sharedPrefs.getInt(SCORES_KEY, 0)
        }
    }
}
