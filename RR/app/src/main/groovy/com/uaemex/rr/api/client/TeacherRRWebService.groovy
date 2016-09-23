package com.uaemex.rr.api.client

import com.uaemex.rr.api.model.Career
import com.uaemex.rr.api.model.Teacher
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

@CompileStatic
interface TeacherRRWebService {

    @GET("/teachers")
    Call<List<Teacher>> getTeachers(
            @Header("Authorization") String authorization
    )

}