package com.uaemex.rr.ui.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson
import com.uaemex.rr.R
import com.uaemex.rr.api.client.*
import com.uaemex.rr.api.model.*
import com.uaemex.rr.api.model.command.BitacoraCommand
import com.uaemex.rr.api.service.SessionManagerImpl
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BitacoraFragment extends Fragment {

    EditText mEditTextGroup
    ImageView mImageViewCamera
    Spinner mSpinnerTeacher
    Spinner mSpinnerLaboratory
    Spinner mSpinnerCareer
    Button mButtonBitacora
    SessionManagerImpl mSessionManager
    List<Career> mCareerList
    List<Laboratory> mLaboratoryList
    List<Teacher> mTeacherList
    JsonWebToken mJsonWebTokenSessionManager
    JsonWebToken mJsonWebToken
    User mUser

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        mSessionManager = new SessionManagerImpl(getActivity().getApplicationContext())
        customToolBar()
        mJsonWebTokenSessionManager = mSessionManager.getUserDetail()
        recoveryToken(mJsonWebTokenSessionManager.refreshToken)
    }

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState)
        View view = inflater.inflate(R.layout.fragment_bitacora, container, false)
        bindingWidgets(view)
        view
    }

    void bindingWidgets(View view) {
        mEditTextGroup = view.findViewById(R.id.groupNameBitacora) as EditText
        mImageViewCamera = view.findViewById(R.id.cameraBitacora) as ImageView
        mSpinnerTeacher = view.findViewById(R.id.spinnerTeacherBitacora) as Spinner
        mSpinnerLaboratory = view.findViewById(R.id.spinnerLaboratoryBitacora) as Spinner
        mSpinnerCareer = view.findViewById(R.id.spinnerCareerBitacora) as Spinner
        mButtonBitacora = view.findViewById(R.id.buttonRegisterBitacora) as Button
        mButtonBitacora.onClickListener = {
            if (validateForm()) {
                addBitacora(mJsonWebToken.accessToken, populateBitacora())
            } else {
                Toast.makeText(getActivity(), R.string.check_information, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Boolean validateForm() {
        Boolean validate = false
        if (mEditTextGroup.text as String && mSpinnerCareer.getSelectedItem() && mSpinnerLaboratory && mSpinnerTeacher) {
            validate = true
        }
        validate
    }

    BitacoraCommand populateBitacora() {
        Career career = mSpinnerCareer.getSelectedItem() as Career
        Laboratory laboratory = mSpinnerLaboratory.getSelectedItem() as Laboratory
        Teacher teacher = mSpinnerTeacher.getSelectedItem() as Teacher
        String semester = mEditTextGroup.text as String
        new BitacoraCommand(groupName: semester, user: mUser.id, teacher: teacher.id, laboratory: laboratory.id, career: career.id)
    }

    void cleanBitacora() {
        mEditTextGroup.text = ""
        mSpinnerCareer.setSelection(0)
        mSpinnerLaboratory.setSelection(0)
        mSpinnerTeacher.setSelection(0)
    }

    void addBitacora(String tokenLogin, BitacoraCommand bitacora) {
        String authenticationHeader = "Bearer ${tokenLogin}"
        BitacoraRRWebService client = FabricRRWebClient.createService(BitacoraRRWebService)
        client.addBitacora(authenticationHeader, bitacora.groupName, bitacora.user, bitacora.teacher, bitacora.laboratory, bitacora.career).enqueue(new Callback<ResponseBody>() {
            @Override
            void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Toast.makeText(getActivity(), "Guardado con exito", Toast.LENGTH_SHORT).show()
                    cleanBitacora()
                }
            }

            @Override
            void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "No se puedo guardar", Toast.LENGTH_SHORT).show()
            }
        })
    }

    void recoveryToken(String refresh_token) {
        String grant_type = "refresh_token"
        LoginRRWebService client = FabricRRWebClient.createService(LoginRRWebService)
        if (refresh_token) {
            client.refreshToken(grant_type, refresh_token).enqueue(new Callback<ResponseBody>() {
                @Override
                void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    println(response.code())
                    if (response.code() == 200) {
                        mJsonWebToken = deserializationJsonWebToken(response?.body()?.string())
                        populateSpinners(mJsonWebToken?.accessToken)
                    }
                }

                @Override
                void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getActivity(), R.string.error_server_503, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    void populateSpinners(String jsonWebToken) {
        if (mJsonWebToken) {
            getUserByUsername(mJsonWebToken.accessToken, mJsonWebToken.username)
            getCareers(mJsonWebToken.accessToken)
            getLaboratories(mJsonWebToken.accessToken)
            getTeachers(mJsonWebToken.accessToken)
        }
    }

    void getCareers(String tokenLogin) {
        String authenticationHeader = "Bearer ${tokenLogin}"
        CareerRRWebService client = FabricRRWebClient.createService(CareerRRWebService)
        client.getCareers(authenticationHeader).enqueue(new Callback<List<Career>>() {
            @Override
            void onResponse(Call<List<Career>> call, Response<List<Career>> response) {
                if (response.code() == 200) {
                    mCareerList = response.body()
                    mSpinnerCareer.setAdapter(getSpinnerAdapterCareer(mCareerList))
                }
            }

            @Override
            void onFailure(Call<List<Career>> call, Throwable t) {

            }
        })
    }

    void getLaboratories(String tokenLogin) {
        String authenticationHeader = "Bearer ${tokenLogin}"
        LaboratoryRRWebService client = FabricRRWebClient.createService(LaboratoryRRWebService)
        client.getLaboratories(authenticationHeader).enqueue(new Callback<List<Laboratory>>() {
            @Override
            void onResponse(Call<List<Laboratory>> call, Response<List<Laboratory>> response) {
                if (response.code() == 200) {
                    mLaboratoryList = response.body()
                    mSpinnerLaboratory.setAdapter(getSpinnerAdapterLaboratory(mLaboratoryList))
                }
            }

            @Override
            void onFailure(Call<List<Laboratory>> call, Throwable t) {

            }
        })
    }

    void getTeachers(String tokenLogin) {
        String authenticationHeader = "Bearer ${tokenLogin}"
        TeacherRRWebService client = FabricRRWebClient.createService(TeacherRRWebService)
        client.getTeachers(authenticationHeader).enqueue(new Callback<List<Teacher>>() {
            @Override
            void onResponse(Call<List<Teacher>> call, Response<List<Teacher>> response) {
                if (response.code() == 200) {
                    mTeacherList = response.body()
                    mTeacherList.each { teacher ->
                        teacher.fullName = teacher.name + " " + teacher.lastName
                    }
                    mSpinnerTeacher.setAdapter(getSpinnerAdapterTeacher(mTeacherList))
                }
            }

            @Override
            void onFailure(Call<List<Teacher>> call, Throwable t) {

            }
        })
    }

    void getUserByUsername(String tokenLogin, String username) {
        String authenticationHeader = "Bearer ${tokenLogin}"
        UserRRWebService client = FabricRRWebClient.createService(UserRRWebService)
        client.getUserByUsername(authenticationHeader, username).enqueue(new Callback<User>() {
            @Override
            void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    mUser = response.body()
                }
            }

            @Override
            void onFailure(Call<User> call, Throwable t) {

            }
        })
    }

    JsonWebToken deserializationJsonWebToken(String response) {
        Gson gson = new Gson()
        JsonWebToken jsonWebToken = gson.fromJson(response, JsonWebToken)
    }

    ArrayAdapter<Career> getSpinnerAdapterCareer(List<Career> careerList) {
        ArrayAdapter<Career> arrayAdapter = new ArrayAdapter<Career>(getActivity().getApplicationContext(), R.layout.spinner_custom, careerList)
    }

    ArrayAdapter<Laboratory> getSpinnerAdapterLaboratory(List<Laboratory> laboratoryList) {
        ArrayAdapter<Laboratory> arrayAdapter = new ArrayAdapter<Laboratory>(getActivity().getApplicationContext(), R.layout.spinner_custom, laboratoryList)
    }

    ArrayAdapter<Teacher> getSpinnerAdapterTeacher(List<Teacher> teacherList) {
        ArrayAdapter<Teacher> arrayAdapter = new ArrayAdapter<Teacher>(getActivity().getApplicationContext(), R.layout.spinner_custom, teacherList)
    }

    void changeFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(((ViewGroup) getView().getParent()).getId(), fragment)
                .addToBackStack(null)
                .commit()
    }

    void customToolBar() {
        TextView titleToolbar = (TextView) getActivity().findViewById(R.id.toolbar_title)
        titleToolbar.text = "Bitácora"
        Toolbar toolbar = getActivity().findViewById(R.id.toolbarRR) as Toolbar
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar)
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false)
        ((AppCompatActivity) getActivity()).getSupportActionBar().show()
    }

}