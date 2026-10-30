package com.app.screentime.feature.models.model

data class ModelGirl(
    val id: String,
    val name: String,
    val age: Int,
    val city: String,
    val bio: String,
    val avatarUrl: String,
    val photos: List<String> = emptyList(),
    val audioRate: Double = 10.0,
    val videoRate: Double = 20.0,
    val isOnline: Boolean = false,
    val status: String = "model",
    val role: String = "model"
)
