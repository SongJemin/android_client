package com.a.android.wheretogo.tabfragment;

/**
 * Created by JAMCOM on 2017-08-27.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a.android.wheretogo.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment2 extends Fragment {


    public Fragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragment2, container, false);
        // Inflate the layout for this fragment

        //Bundle bundle2 = getArguments();
        //String phone = bundle2.getString("phone");

        String phone = getArguments().getString("phone"); // 전달한 key 값
        String newAddress = getArguments().getString("newAddress"); // 전달한 key 값

        TextView phoneView = (TextView) v.findViewById (R.id.phone_view);
        TextView addressView = (TextView) v.findViewById (R.id.address_view);
        //텍스트뷰에 데이터를 붙임
        phoneView.setText(phone);
        addressView.setText(newAddress);

        return v;
    }

}