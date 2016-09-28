package com.uaemex.rr.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
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
import com.uaemex.rr.util.CameraUtil
import groovy.transform.CompileStatic
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@CompileStatic
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
    CameraUtil mCameraUtil = new CameraUtil()
    File photoFile
    static final int CAPTURE_IMAGE = 1
    String documentId

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
        mImageViewCamera.onClickListener = {
            launchCamera()
        }
        mSpinnerTeacher = view.findViewById(R.id.spinnerTeacherBitacora) as Spinner
        mSpinnerLaboratory = view.findViewById(R.id.spinnerLaboratoryBitacora) as Spinner
        mSpinnerCareer = view.findViewById(R.id.spinnerCareerBitacora) as Spinner
        mButtonBitacora = view.findViewById(R.id.buttonRegisterBitacora) as Button
        mButtonBitacora.onClickListener = {
            //TODO: Revisar flujo para la creacion de la bitacora, con y sin fotografia
            if (validateForm()) {
                if (photoFile != null) {
                    uploadDocument(photoFile)
                }
                else{
                    addBitacora(mJsonWebToken.accessToken, populateBitacora())
                }
            } else {
                Toast.makeText(getActivity(), R.string.check_information, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Boolean validateForm() {
        Boolean validate = false
        if (mEditTextGroup.text as String && mEditTextGroup.text as String != "0"  && mSpinnerCareer.getSelectedItem() && mSpinnerLaboratory && mSpinnerTeacher) {
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
        photoFile = null
        documentId = null
    }

    void addBitacora(String tokenLogin, BitacoraCommand bitacora) {
        String authenticationHeader = "Bearer ${tokenLogin}"
        BitacoraRRWebService client = FabricRRWebClient.createService(BitacoraRRWebService)
        client.addBitacora(authenticationHeader, bitacora.groupName, bitacora.user, bitacora.teacher, bitacora.laboratory, bitacora.career,documentId).enqueue(new Callback<ResponseBody>() {
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
        } as Callback<ResponseBody>)
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
            } as Callback<ResponseBody>)
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
        } as Callback<List<Career>>)
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
        } as Callback<List<Laboratory>>)
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
        } as Callback<List<Teacher>>)
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
        } as Callback<User>)
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

    void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(getActivity().getApplicationContext().getPackageManager()) != null) {
            try {
                photoFile = mCameraUtil.createPhoto("IMG")
            } catch (IOException ex) {
                Toast.makeText(getActivity(), "Error al tomar foto", Toast.LENGTH_SHORT).show()
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                Log.d("BitacoraFragment", takePictureIntent.dump())
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE)
            }
        }
    }

    @Override
    void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE) {
            if (resultCode == getActivity().RESULT_OK) {
                Bitmap bitmapResize = mCameraUtil.getScaledBitmap(photoFile.absolutePath, getActivity())
                File photo = mCameraUtil.saveBitmapToFile(bitmapResize, photoFile.getName())
                mCameraUtil.addPictureToGallery(getActivity().getApplicationContext(), photo.getPath())
                Toast.makeText(getActivity().getApplicationContext(), R.string.take_photo_success, Toast.LENGTH_SHORT).show()
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                Toast.makeText(getActivity(), R.string.take_photo_cancel, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(getActivity(), R.string.take_photo_error, Toast.LENGTH_LONG).show()
            }
        }
    }

    void uploadDocument(File file) {
       if (mJsonWebToken && file) {
            String authenticationHeader = "Bearer ${mJsonWebToken.accessToken}"
            UploadFileRRWebService client = FabricRRWebClient.createService(UploadFileRRWebService)
            File photo = new File(file.absolutePath)

            RequestBody requestPhoto = RequestBody.create(MediaType.parse("multipart/form-data"), photo)
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", photo.name, requestPhoto)

            String descriptionString = "File ${photo.name}"
            RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString)

            client.uploadDocument(authenticationHeader, description, body).enqueue(new Callback<ResponseBody>() {
                @Override
                void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Toast.makeText(getActivity(), R.string.upload_photo_success, Toast.LENGTH_SHORT).show()
                        documentId = response?.body()?.string()
                    }
                    addBitacora(mJsonWebToken.accessToken, populateBitacora())
                }

                @Override
                void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getActivity(), R.string.upload_photo_error, Toast.LENGTH_SHORT).show()
                    cleanBitacora()
                }
            } as Callback<ResponseBody>)
        }
    }

}