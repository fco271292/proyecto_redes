package com.uaemex.rr.api.client


import groovy.transform.CompileStatic
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

@CompileStatic
interface RRWebService{

    @FormUrlEncoded
    @POST("/login/authenticate")
    Call<ResponseBody> login(
            @Field("username")  String username,
            @Field("password") String password
    )
}