package com.uaemex.rr.api.client

import com.uaemex.rr.api.model.Career
import com.uaemex.rr.api.model.Laboratory
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

@CompileStatic
interface LaboratoryRRWebService {

    @GET("/laboratories")
    Call<List<Laboratory>> getLaboratories(
            @Header("Authorization") String authorization
    )

}