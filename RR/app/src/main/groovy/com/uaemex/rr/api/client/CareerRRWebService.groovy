package com.uaemex.rr.api.client

import com.uaemex.rr.api.model.Career
import groovy.transform.CompileStatic
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header

@CompileStatic
interface CareerRRWebService {

    @GET("/careers")
    Call<List<Career>> getCareers(
            @Header("Authorization") String authorization
    )

}