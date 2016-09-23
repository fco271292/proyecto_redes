package com.uaemex.rr.api.client

import com.uaemex.rr.api.model.User
import groovy.transform.CompileStatic
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

@CompileStatic
interface BitacoraRRWebService {

    @FormUrlEncoded
    @POST("/bitacoras")
    Call<ResponseBody> addBitacora(
            @Header("Authorization") String authorization,
            @Field("groupName") String groupName,
            @Field("user") String user,
            @Field("teacher") String teacher,
            @Field("laboratory") String laboratory,
            @Field("career") String career

    )

}