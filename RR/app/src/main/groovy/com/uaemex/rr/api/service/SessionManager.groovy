package com.uaemex.rr.api.service

import android.content.Context
import com.uaemex.rr.api.model.JsonWebToken
import groovy.transform.CompileStatic

@CompileStatic
interface SessionManager{
    void setUserSession(String username,String refreshToken)
    boolean isUserSession()
    JsonWebToken getUserDetail()
    void setLogoutUser()

}