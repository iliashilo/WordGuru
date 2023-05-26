package com.word.guru.domain

import com.word.guru.R
import com.word.guru.core.ResourceProvider
import java.lang.IllegalStateException
import kotlin.random.Random

interface GetRandomWordUseCase {

    fun execute(lettersCount: Int): String

    class Base(private val resourceProvider: ResourceProvider) : GetRandomWordUseCase {

        override fun execute(lettersCount: Int): String {
            val jsonArray = when (lettersCount) {
                4 -> resourceProvider.fetchJson(R.raw.four_letter_words)
                5 -> resourceProvider.fetchJson(R.raw.five_letter_words)
                6 -> resourceProvider.fetchJson(R.raw.six_letter_words)
                else -> throw IllegalStateException("Inappropriate letters count!")
            }
            val random = Random(System.currentTimeMillis())
            val randomIndex = (0 until jsonArray.length()).random(random)
            return jsonArray.optString(randomIndex)
        }

    }

}