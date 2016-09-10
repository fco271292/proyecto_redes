package com.uaemex.rr.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.ActionBarActivity
import com.uaemex.rr.R
import groovy.transform.CompileStatic

@CompileStatic
abstract class SimpleFragment extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        FragmentManager fm = getSupportFragmentManager()
        Fragment fragment = fm.findFragmentById(R.id.fragment_container)
        if (fragment==null)
            fm.beginTransaction().add(R.id.fragment_container, createFragment()).commit()

    }

    abstract Fragment createFragment()
}