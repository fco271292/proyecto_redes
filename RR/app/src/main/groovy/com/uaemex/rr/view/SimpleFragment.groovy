package com.uaemex.rr.view

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.ActionBarActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import com.uaemex.rr.R
import com.uaemex.rr.api.service.SessionManagerImpl
import com.uaemex.rr.ui.activity.PrincipalActivity
import com.uaemex.rr.ui.fragment.LoginFragment
import groovy.transform.CompileStatic

@CompileStatic
abstract class SimpleFragment extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        FragmentManager fm = getSupportFragmentManager()
        Fragment fragment = fm.findFragmentById(R.id.fragment_container)
        if (fragment == null)
            fm.beginTransaction().add(R.id.fragment_container, createFragment()).commit()

    }

    abstract Fragment createFragment()

    @Override
    void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState)
        setToolBar()
    }

    void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbarRR) as Toolbar
        setSupportActionBar(toolbar)
    }

    @Override
    boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @Override
    boolean onOptionsItemSelected(MenuItem item) {
        SessionManagerImpl mSessionManager = new SessionManagerImpl(getApplicationContext())
        switch (item.getItemId()) {
            case R.id.logout_user:
                if (mSessionManager){
                    mSessionManager.setLogoutUser()
                    finish()
                    Intent intent = PrincipalActivity.invokeActivity(getApplicationContext())
                    startActivity(intent)
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}