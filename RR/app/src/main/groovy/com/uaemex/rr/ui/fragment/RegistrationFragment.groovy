package com.uaemex.rr.ui.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.uaemex.rr.BuildConfig
import com.uaemex.rr.R
import com.uaemex.rr.api.client.FabricRRWebClient
import com.uaemex.rr.api.client.LoginRRWebService
import com.uaemex.rr.api.client.UserRRWebService
import com.uaemex.rr.api.model.JsonWebToken
import com.uaemex.rr.api.model.User
import com.uaemex.rr.api.model.command.JsonWebTokenCommand
import groovy.transform.CompileStatic
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@CompileStatic
class RegistrationFragment extends Fragment {

    Button mButtonRegister
    EditText mEditTextName
    EditText mEditTextLastName
    EditText mEditTextEmail
    EditText mEditTextUsername
    EditText mEditTextPassword

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        customToolBar()
    }

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
            User user = populateRegister()
            if (validateForm(user)) {
                generateTokenLogin(user)
            } else {
                Toast.makeText(getActivity(),R.string.check_information, Toast.LENGTH_SHORT).show()
            }
        }
        mEditTextName = view.findViewById(R.id.nameRegister) as EditText
        mEditTextLastName = view.findViewById(R.id.lastNameRegister) as EditText
        mEditTextEmail = view.findViewById(R.id.emailRegister) as EditText
        mEditTextUsername = view.findViewById(R.id.usernameRegister) as EditText
        mEditTextPassword = view.findViewById(R.id.passwordRegister) as EditText
    }

    User populateRegister() {
        String name = mEditTextName.text as String
        String lastName = mEditTextLastName.text as String
        String email = mEditTextEmail.text as String
        String userName = mEditTextUsername.text as String
        String password = mEditTextPassword.text as String
        new User(name: name, lastName: lastName, email: email, username: userName, password: password)
    }

    Boolean validateForm(User user) {
        Boolean validate = false
        if (user.name && user.lastName && validateEmail(user.email) && user.username && user.password) {
            validate = true
        }
        validate
    }

    Boolean validateEmail(String email) {
        Boolean validate = false
        def pattern = /[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[A-Za-z]{2,4}/
        if (email ==~ pattern) {
            validate = true
        } else {
            Toast.makeText(getActivity(), R.string.email_invalid, Toast.LENGTH_SHORT).show()
        }
        validate
    }

    void cleanform() {
        mEditTextName.text = ""
        mEditTextLastName.text = ""
        mEditTextEmail.text = ""
        mEditTextUsername.text = ""
        mEditTextPassword.text = ""
    }

    void generateTokenLogin(User user) {
        LoginRRWebService client = FabricRRWebClient.createService(LoginRRWebService)
        client.tokenLogin(new JsonWebTokenCommand(username: BuildConfig.USER, password: BuildConfig.PASSWORD)).enqueue(new Callback<JsonWebToken>() {
            @Override
            void onResponse(Call<JsonWebToken> call, Response<JsonWebToken> response) {
                if (response.code() == 200) {
                    saveUser(response.body().accessToken, user)
                }
            }

            @Override
            void onFailure(Call<JsonWebToken> call, Throwable t) {
                println("-" * 100)
                println("Error al generar token usuario ${t.message}")
            }

        })
    }

    void saveUser(String tokenLogin, User user) {
        if (tokenLogin) {
            String authenticationHeader = "Bearer ${tokenLogin}"
            UserRRWebService client = FabricRRWebClient.createService(UserRRWebService)
            client.addUSer(authenticationHeader, user.name, user.lastName, user.email, user.username, user.password).enqueue(new Callback<ResponseBody>() {
                @Override
                void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String responseServer = response?.body()?.string()
                    if (!responseServer.findAll("invalid")) {
                        Toast.makeText(getActivity(), R.string.user_save_success, Toast.LENGTH_SHORT).show()
                        cleanform()
                        getActivity().onBackPressed()
                    } else {
                        Toast.makeText(getActivity(), R.string.username_invalid, Toast.LENGTH_SHORT).show()
                    }
                }

                @Override
                void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getActivity(), R.string.error_server_503, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


    void customToolBar() {
        TextView titleToolbar = getActivity().findViewById(R.id.toolbar_title) as TextView
        titleToolbar.text = "Registrar usuario"
        Toolbar toolbar = getActivity().findViewById(R.id.toolbarRR) as Toolbar
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar)
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false)
        ((AppCompatActivity) getActivity()).getSupportActionBar().show()
    }
}