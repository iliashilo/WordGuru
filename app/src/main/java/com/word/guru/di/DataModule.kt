package com.word.guru.di

import android.content.Context
import com.word.guru.data.ScoresPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideScoresPreferences(@ApplicationContext context: Context): ScoresPreferences =
        ScoresPreferences.Base(context = context)

}