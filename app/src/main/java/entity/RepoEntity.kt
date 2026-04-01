package com.example.androidpractice.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class RepoEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val owner: String,
    val description: String,
    val language: String?,
    val stars: Int,
    val forks: Int,
    val openIssues: Int,
    val updatedAt: String,
    val url: String
)
