package com.uaemex.rr.api.model

import com.google.gson.annotations.SerializedName
import groovy.transform.CompileStatic

@CompileStatic
class User {

    @SerializedName("name")
    String name
    @SerializedName("lastName")
    String lastName
    @SerializedName("email")
    String email
    @SerializedName("username")
    String username
    @SerializedName("password")
    String password
    @SerializedName("id")
    String id

}