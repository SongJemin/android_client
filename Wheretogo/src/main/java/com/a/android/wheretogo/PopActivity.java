package com.a.android.wheretogo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class PopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pop2);

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int width = (int) (display.getWidth() * 0.8); //Display 사이즈의 70%

        int height = (int) (display.getHeight() * 0.7);  //Display 사이즈의 90%

        getWindow().getAttributes().width = width;

        getWindow().getAttributes().height = height;

        Intent popintent = getIntent();
        String title = popintent.getStringExtra("title");
        String category = popintent.getStringExtra("category");
        String phone = popintent.getStringExtra("phone");
        String newAddress = popintent.getStringExtra("newAddress");
        TextView titleView = (TextView) findViewById (R.id.phone_title);
        TextView categoryView = (TextView) findViewById (R.id.keyword_view);
        TextView phoneView = (TextView) findViewById (R.id.time_title);
        TextView newAddressView = (TextView) findViewById (R.id.newAddress_view);
        //텍스트뷰에 데이터를 붙임
        titleView.setText(title);
        categoryView.setText(category);
        phoneView.setText(phone);
        newAddressView.setText(newAddress);
    }
}
