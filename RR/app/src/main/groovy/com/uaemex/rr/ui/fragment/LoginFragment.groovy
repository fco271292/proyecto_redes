package com.uaemex.rr.ui.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.uaemex.rr.R
import groovy.transform.CompileStatic

@CompileStatic
class LoginFragment extends Fragment {

    FloatingActionButton mFloatingActionButtonRegistration
    Button mButtonLogin

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState)
        View view = inflater.inflate(R.layout.fragment_login, container, false)
        bindingWidgets(view)
        view
    }

    void changeFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(((ViewGroup) getView().getParent()).getId(), fragment)
                .addToBackStack(null)
                .commit()
    }

    void bindingWidgets(View view) {
        mFloatingActionButtonRegistration = (FloatingActionButton) view.findViewById(R.id.fabAddUser)
        mFloatingActionButtonRegistration.onClickListener = {
            changeFragment(new RegistrationFragment())
        }
        mButtonLogin = view.findViewById(R.id.buttonLogin) as Button
        mButtonLogin.onClickListener = {
            changeFragment(new BitacoraFragment())
        }
    }
}