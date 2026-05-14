package com.example.androidpractice.data.api

import com.google.gson.annotations.SerializedName

data class GithubRepoDto(
    val id: Long,
    val name: String,
    val description: String?,
    val language: String?,

    @SerializedName("stargazers_count")
    val stargazersCount: Int,

    @SerializedName("forks_count")
    val forksCount: Int,

    @SerializedName("open_issues_count")
    val openIssuesCount: Int,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("html_url")
    val htmlUrl: String,

    val topics: List<String>?,

    val owner: OwnerDto
)

data class OwnerDto(
    val login: String
)