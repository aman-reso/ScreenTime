package com.app.screentime.core.faceverification.di

import android.content.Context
import com.app.screentime.core.faceverification.FaceVerificationManager
import com.app.screentime.core.faceverification.engine.FaceDetectorEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FaceVerificationModule {

    @Provides
    @Singleton
    fun provideFaceDetectorEngine(
        @ApplicationContext context: Context
    ): FaceDetectorEngine = FaceDetectorEngine(context)

    @Provides
    @Singleton
    fun provideFaceVerificationManager(
        @ApplicationContext context: Context,
        faceDetectorEngine: FaceDetectorEngine
    ): FaceVerificationManager = FaceVerificationManager(
        context = context,
        faceDetectorEngine = faceDetectorEngine
    )
}
