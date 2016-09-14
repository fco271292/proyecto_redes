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
import android.widget.Toast
import com.uaemex.rr.R
import com.uaemex.rr.api.client.FabricRRWebClient
import com.uaemex.rr.api.client.RRWebService
import groovy.transform.CompileStatic
import okhttp3.ResponseBody
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
            RRWebService client = FabricRRWebClient.createService(RRWebService)
            populateForm()
            client.login(username, password).enqueue(new Callback<ResponseBody>() {

                @Override
                void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String responseLogin = response.raw()
                    if (!responseLogin.endsWith("login_error=1}")) {
                        Toast.makeText(getActivity(), R.string.user_password_correct, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(getActivity(), R.string.user_password_incorrect, Toast.LENGTH_SHORT).show()
                        cleanForm()
                    }
                }

                @Override
                void onFailure(Call<ResponseBody> call, Throwable t) {
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
}