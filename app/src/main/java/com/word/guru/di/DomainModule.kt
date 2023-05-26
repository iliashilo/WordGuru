package com.word.guru.di

import com.word.guru.core.ResourceProvider
import com.word.guru.data.ScoresPreferences
import com.word.guru.domain.GetRandomWordUseCase
import com.word.guru.domain.GetScoresUseCase
import com.word.guru.domain.IncrementScoresUseCase
import com.word.guru.domain.IsWordExistUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetScoresUseCase(scoresPreferences: ScoresPreferences): GetScoresUseCase =
        GetScoresUseCase.Base(scoresPreferences = scoresPreferences)

    @Provides
    fun provideIncrementScoresUseCase(scoresPreferences: ScoresPreferences): IncrementScoresUseCase =
        IncrementScoresUseCase.Base(scoresPreferences = scoresPreferences)

    @Provides
    fun provideGetRandomWordUseCase(resourceProvider: ResourceProvider): GetRandomWordUseCase =
        GetRandomWordUseCase.Base(resourceProvider = resourceProvider)

    @Provides
    fun provideIsWordExistUseCase(resourceProvider: ResourceProvider): IsWordExistUseCase =
        IsWordExistUseCase.Base(resourceProvider = resourceProvider)
}