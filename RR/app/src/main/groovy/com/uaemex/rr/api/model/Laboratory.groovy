package com.uaemex.rr.api.model

import groovy.transform.CompileStatic

@CompileStatic
class Laboratory {

    String dateCreated
    String lastUpdated
    String id
    String name

    @Override
    public String toString() {
        name
    }
}