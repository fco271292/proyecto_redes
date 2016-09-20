package com.uaemex.rr.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.uaemex.rr.R
import com.uaemex.rr.ui.fragment.BitacoraFragment
import com.uaemex.rr.view.SimpleFragment
import groovy.transform.CompileStatic

@CompileStatic
public class BitacoraActivity extends SimpleFragment {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
    }

    @Override
    Fragment createFragment() {
        new BitacoraFragment()
    }

    static Intent invokeActivity(Context context) {
        Intent intent = new Intent(context, BitacoraActivity)
        intent
    }
}
