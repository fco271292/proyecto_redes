package com.uaemex.rr.api.client

import com.uaemex.rr.api.model.User
import groovy.transform.CompileStatic
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

@CompileStatic
interface UserRRWebService {

    @FormUrlEncoded
    @POST("/users")
    Call<ResponseBody> addUSer(
            @Header("Authorization") String authorization,
            @Field("name") String name,
            @Field("lastName") String lastName,
            @Field("email") String email,
            @Field("username") String username,
            @Field("password") String password

    )

}