package com.uaemex.rr.api.model

import groovy.transform.CompileStatic

@CompileStatic
class Teacher {

    String dateCreated
    String lastUpdated
    String id
    String name
    String lastName
    String fullName

    @Override
    public String toString() {
        fullName
    }
}