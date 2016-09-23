package com.uaemex.rr.api.model

import com.google.gson.annotations.SerializedName
import groovy.transform.CompileStatic

@CompileStatic
class Career {

    String dateCreated
    String lastUpdated
    String id
    String name

    @Override
    public String toString() {
        name
    }
}