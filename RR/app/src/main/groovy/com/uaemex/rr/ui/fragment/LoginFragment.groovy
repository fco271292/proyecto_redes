package com.uaemex.rr.ui.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.uaemex.rr.R
import com.uaemex.rr.api.client.FabricRRWebClient
import com.uaemex.rr.api.client.GitHubClient
import com.uaemex.rr.api.model.UserGithubDetail
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
            //changeFragment(new BitacoraFragment())
            GitHubClient client = FabricRRWebClient.createService(GitHubClient)
            populateForm()
            client.getUserGithub(username).enqueue(new Callback<UserGithubDetail>() {
                @Override
                void onResponse(Call<UserGithubDetail> call, Response<UserGithubDetail> response) {
                    println("*"*100)
                    println(response.body().dump())
                }

                @Override
                void onFailure(Call<UserGithubDetail> call, Throwable t) {
                    println("-"*100)
                    println("${t?.message}")
                }
            })
        }
        mEditTextUsername = view.findViewById(R.id.username) as EditText
        mEditTextPassword = view.findViewById(R.id.password) as EditText

    }

    void populateForm(){
        username = mEditTextUsername.text
        password = mEditTextPassword.text
    }
}