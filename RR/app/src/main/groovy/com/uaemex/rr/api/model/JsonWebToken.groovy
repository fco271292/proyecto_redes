package com.uaemex.rr.api.model

import com.google.gson.annotations.SerializedName
import groovy.transform.CompileStatic

@CompileStatic
class JsonWebToken{

    @SerializedName("access_token")
    String accessToken
    @SerializedName("expires_in")
    String expiresIn
    @SerializedName("refresh_token")
    String refreshToken
    @SerializedName("roles")
    List<String> roles
    @SerializedName("token_type")
    String tokenType
    @SerializedName("username")
    String username

}