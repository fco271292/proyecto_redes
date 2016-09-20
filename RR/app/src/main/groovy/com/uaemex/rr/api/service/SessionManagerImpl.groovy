package com.uaemex.rr.api.service

import android.content.Context
import android.content.SharedPreferences
import com.uaemex.rr.api.model.JsonWebToken
import groovy.transform.CompileStatic

@CompileStatic
class SessionManagerImpl implements SessionManager {

    Context mContext
    SharedPreferences mSharedPreferences
    SharedPreferences.Editor mEditor
    final String USERNAME = "username"
    final String REFRESH_TOKEN = "refresh_token"
    final String IS_SESSION = "is_session"
    final Integer PRIVATE_MODE = 0

    SessionManagerImpl(Context context) {
        mContext = context
        mSharedPreferences = mContext.getSharedPreferences("UserRR", PRIVATE_MODE)
        mEditor = mSharedPreferences.edit()
    }

    @Override
    void setUserSession(String username, String refreshToken) {
        mEditor.putBoolean(IS_SESSION, true)
        mEditor.putString(USERNAME, username)
        mEditor.putString(REFRESH_TOKEN, refreshToken)
        mEditor.commit()
    }

    @Override
    boolean isUserSession() {
        mSharedPreferences.getBoolean(IS_SESSION, false)
    }

    @Override
    JsonWebToken getUserDetail() {
        String username = mSharedPreferences.getString(USERNAME, "")
        String refreshToken = mSharedPreferences.getString(REFRESH_TOKEN, "")
        new JsonWebToken(username: username, refreshToken: refreshToken)
    }

    @Override
    void setLogoutUser() {
        mEditor.clear()
        mEditor.commit()
    }
}