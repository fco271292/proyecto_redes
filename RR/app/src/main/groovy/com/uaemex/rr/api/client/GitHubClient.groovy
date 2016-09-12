package com.uaemex.rr.api.client

import com.uaemex.rr.api.model.UserGithubDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubClient {

    @GET("users/{user}")
    Call<UserGithubDetail> getUserGithub(@Path("user") String user)

}
