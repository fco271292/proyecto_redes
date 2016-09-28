package com.uaemex.rr.api.client

import groovy.transform.CompileStatic
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

@CompileStatic
interface UploadFileRRWebService {

    @Multipart
    @POST("/documents")
    Call<ResponseBody> uploadDocument(
            @Header("Authorization") String authorization,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    )

}