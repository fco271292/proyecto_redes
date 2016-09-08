package com.uaemex.rr

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

class RRFragment extends Fragment {
    Button mButtonLogin

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState)
        View view = inflater.inflate(R.layout.fragment_rr, container, false)
        mButtonLogin = (Button) view.findViewById(R.id.buttonLogin)
        mButtonLogin.onClickListener = {
            Toast.makeText(getActivity(),"${new Date().format("dd-MM-yyyy hh:mm:ss")}",Toast.LENGTH_SHORT).show()
        }
        view
    }
}