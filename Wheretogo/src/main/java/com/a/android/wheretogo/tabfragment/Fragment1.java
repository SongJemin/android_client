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
public class Fragment1 extends Fragment {


    public Fragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragment1, container, false);


        // Inflate the layout for this fragment

        //Bundle extra = getArguments();
        //String title = extra.getString("title");
        String title = getArguments().getString("title"); // 전달한 key 값
        String category = getArguments().getString("category"); // 전달한 key 값
        //String place_url = getArguments().getString("place_url"); // 전달한 key 값
        double distance = getArguments().getDouble("distance"); // 전달한 key 값
        String distance_string= Double.toString(distance);

        String s=category.replace("> ", "#");
        String s2=s.replace(",", " #");
        String result=s2.replace("음식점", "");
        //String s4=s3.replace(title, "");

        TextView nameView = (TextView) v.findViewById (R.id.name_view);
        TextView titleView = (TextView) v.findViewById (R.id.keyword_view);
        TextView distanceView = (TextView) v.findViewById (R.id.distance_view);
        TextView phoneView = (TextView) v.findViewById (R.id.phone_view);
        TextView addressView = (TextView) v.findViewById (R.id.address_view);
        String phone = getArguments().getString("phone"); // 전달한 key 값
        String newAddress = getArguments().getString("newAddress"); // 전달한 key 값



        //텍스트뷰에 데이터를 붙임
        nameView.setText(title);
        titleView.setText(result);
        distanceView.setText(distance_string+"m");
        phoneView.setText(phone);
        addressView.setText(newAddress);
        //placeUrlView.setText(place_url);



        return v;
    }

}