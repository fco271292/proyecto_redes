package com.uaemex.rr.ui.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.uaemex.rr.R
import groovy.transform.CompileStatic

@CompileStatic
class RegistrationFragment extends Fragment {

    Button mButtonRegister
    EditText mEditTextName
    EditText mEditTextLastName
    EditText mEditTextEmail
    EditText mEditTextUsername
    EditText mEditTextPassword

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState)
        View view = inflater.inflate(R.layout.fragment_register, container, false)
        bindingWidgets(view)
        view
    }

    void bindingWidgets(View view) {
        mButtonRegister = view.findViewById(R.id.buttonRegister) as Button
        mButtonRegister.onClickListener = {
            println("registro")
            populateRegister()
        }
        mEditTextName = view.findViewById(R.id.nameRegister) as EditText
        mEditTextLastName = view.findViewById(R.id.lastNameRegister) as EditText
        mEditTextEmail = view.findViewById(R.id.emailRegister) as EditText
        mEditTextUsername = view.findViewById(R.id.usernameRegister) as EditText
        mEditTextPassword = view.findViewById(R.id.passwordRegister) as EditText
    }

    void populateRegister() {
        String name = mEditTextName.text
        String lastName = mEditTextLastName.text
        String email = mEditTextEmail.text
        String userName = mEditTextUsername.text
        String password = mEditTextPassword.text
        println("Nombre: ${name} ${lastName} ${email} ${userName} ${password}")
    }
}