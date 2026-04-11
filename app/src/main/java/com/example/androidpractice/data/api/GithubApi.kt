package com.example.androidpractice.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    @GET("users/{owner}/repos")
    suspend fun getRepos(
        @Path("owner") owner: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): List<GithubRepoDto>
}