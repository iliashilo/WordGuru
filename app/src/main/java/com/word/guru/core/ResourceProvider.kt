package com.word.guru.core

import android.content.Context
import org.json.JSONArray

interface ResourceProvider {

    fun fetchJson(jsonRes: Int): JSONArray

    class Base(private val context: Context): ResourceProvider {

        override fun fetchJson(jsonRes: Int): JSONArray {
            val inputStream = context.resources.openRawResource(jsonRes)
            val jsonText = inputStream.bufferedReader().use { it.readText() }
            return JSONArray(jsonText)
        }

    }

}