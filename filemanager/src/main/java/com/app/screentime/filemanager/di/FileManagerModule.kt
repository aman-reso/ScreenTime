package com.app.screentime.filemanager.di

import android.content.Context
import com.app.screentime.filemanager.repository.FileManagerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FileManagerModule {

    @Provides
    @Singleton
    fun provideFileManagerRepository(
        @ApplicationContext context: Context
    ): FileManagerRepository {
        return FileManagerRepository(context)
    }
}

