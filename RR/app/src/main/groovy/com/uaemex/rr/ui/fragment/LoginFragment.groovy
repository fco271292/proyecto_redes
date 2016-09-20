package com.uaemex.rr.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.uaemex.rr.R
import com.uaemex.rr.api.client.FabricRRWebClient
import com.uaemex.rr.api.client.LoginRRWebService
import com.uaemex.rr.api.model.JsonWebToken
import com.uaemex.rr.api.model.command.JsonWebTokenCommand
import com.uaemex.rr.api.service.SessionManagerImpl
import com.uaemex.rr.ui.activity.BitacoraActivity
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@CompileStatic
class LoginFragment extends Fragment {

    FloatingActionButton mFloatingActionButtonRegistration
    Button mButtonLogin
    EditText mEditTextUsername
    EditText mEditTextPassword
    String username
    String password
    SessionManagerImpl mSessionManager

    @Override
    void onResume() {
        super.onResume()
        customToolBar()
    }

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        mSessionManager = new SessionManagerImpl(getActivity().getApplicationContext())
        customToolBar()
    }

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
        checkSession()
        mFloatingActionButtonRegistration = (FloatingActionButton) view.findViewById(R.id.fabAddUser)
        mFloatingActionButtonRegistration.onClickListener = {
            changeFragment(new RegistrationFragment())
        }
        mButtonLogin = view.findViewById(R.id.buttonLogin) as Button
        mButtonLogin.onClickListener = {
            LoginRRWebService client = FabricRRWebClient.createService(LoginRRWebService)
            populateForm()
            client.tokenLogin(new JsonWebTokenCommand(username: username, password: password)).enqueue(new Callback<JsonWebToken>() {

                @Override
                void onResponse(Call<JsonWebToken> call, Response<JsonWebToken> response) {
                    if (response.code() == 200) {
                        Toast.makeText(getActivity(), R.string.user_password_correct, Toast.LENGTH_SHORT).show()
                        mSessionManager.setUserSession(response.body().username, response.body().refreshToken)
                        changeFragment(new BitacoraFragment())
                    } else {
                        Toast.makeText(getActivity(), R.string.user_password_incorrect, Toast.LENGTH_SHORT).show()
                        cleanForm()
                    }
                }

                @Override
                void onFailure(Call<JsonWebToken> call, Throwable t) {
                    Toast.makeText(getActivity(), R.string.error_server_503, Toast.LENGTH_SHORT).show()
                }
            })
        }
        mEditTextUsername = view.findViewById(R.id.username) as EditText
        mEditTextPassword = view.findViewById(R.id.password) as EditText

    }

    void populateForm() {
        username = mEditTextUsername.text
        password = mEditTextPassword.text
    }

    void cleanForm() {
        mEditTextUsername.text = ""
        mEditTextPassword.text = ""
    }

    void checkSession() {
        if (mSessionManager.isUserSession()) {
            Intent intent = BitacoraActivity.invokeActivity(getActivity().getApplicationContext())
            startActivity(intent)
            getActivity().finish()
        }
    }

    void customToolBar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbarRR) as Toolbar
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar)
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false)
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide()
    }

}