package com.word.guru.domain

import com.google.gson.Gson
import com.word.guru.R
import com.word.guru.core.ResourceProvider
import java.lang.IllegalStateException

interface IsWordExistUseCase {

    fun execute(word: String): Boolean

    class Base(private val resourceProvider: ResourceProvider) : IsWordExistUseCase {

        override fun execute(word: String): Boolean {
            val jsonArray = when (word.length) {
                4 -> resourceProvider.fetchJson(R.raw.four_letter_words)
                5 -> resourceProvider.fetchJson(R.raw.five_letter_words)
                6 -> resourceProvider.fetchJson(R.raw.six_letter_words)
                else -> throw IllegalStateException("Inappropriate letters count!")
            }
            val allWords = Gson().fromJson(jsonArray.toString(), Array<String>::class.java)
            return binarySearch(allWords, word)
        }

        private fun binarySearch(words: Array<String>, target: String): Boolean {
            var left = 0
            var right = words.size - 1

            while (left <= right) {
                val mid = (left + right) / 2
                val midWord = words[mid]

                when {
                    midWord == target -> return true
                    midWord < target -> left = mid + 1
                    else -> right = mid - 1
                }
            }

            return false
        }

    }

}