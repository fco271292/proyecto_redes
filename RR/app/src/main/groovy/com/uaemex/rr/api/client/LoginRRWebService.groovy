package com.uaemex.rr.api.client

import com.uaemex.rr.api.model.JsonWebToken
import com.uaemex.rr.api.model.command.JsonWebTokenCommand
import groovy.transform.CompileStatic
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

@CompileStatic
interface LoginRRWebService {

    @FormUrlEncoded
    @POST("/login/authenticate")
    Call<ResponseBody> login(
            @Field("username") String username,
            @Field("password") String password
    )

    @POST("/api/login")
    Call<JsonWebToken> tokenLogin(
            @Body JsonWebTokenCommand jsonWebTokenCommand
    )

    @FormUrlEncoded
    @POST("/oauth/access_token")
    Call<ResponseBody> refreshToken(
            @Field("grant_type") String grant_type,
            @Field("refresh_token") String refresh_token
    )
}