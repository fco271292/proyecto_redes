package com.uaemex.rr.ui.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import com.uaemex.rr.R
import groovy.transform.CompileStatic

@CompileStatic
class BitacoraFragment extends Fragment{

    EditText mEditTextGroup
    ImageView mImageViewCamera
    Spinner mSpinnerTeacher
    Spinner mSpinnerLaboratory
    Spinner mSpinnerCareer

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
    }

    void changeFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(((ViewGroup) getView().getParent()).getId(), fragment)
                .addToBackStack(null)
                .commit()
    }
}