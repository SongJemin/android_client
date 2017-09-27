package com.a.android.wheretogo;

/**
 * Created by JAMCOM on 2017-08-27.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.a.android.wheretogo.tabfragment.Fragment1;

public class PopActivity extends AppCompatActivity implements View.OnClickListener{


    private final int FRAGMENT1 = 1;
    private final int FRAGMENT2 = 2;
    public String category;
    public String title;
    public Double distance;
    public String place_url;
    public String phone;
    public String newAddress;
    Bundle bundle = new Bundle();


    private Button bt_tab1, bt_tab2, bt_tab3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_main);


        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (display.getWidth() * 0.8); //Display 사이즈의 70%
        int height = (int) (display.getHeight() * 0.8);  //Display 사이즈의 90%
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;



        Intent popintent = getIntent();
        category = popintent.getStringExtra("category");
        title = popintent.getStringExtra("title");
        phone = popintent.getStringExtra("phone");
        place_url = popintent.getStringExtra("place_url");
        newAddress = popintent.getStringExtra("newAddress");
        distance = popintent.getDoubleExtra("distance",0.0);
        // 위젯에 대한 참조
        bt_tab1 = (Button)findViewById(R.id.bt_tab1);

        // 탭 버튼에 대한 리스너 연결
        bt_tab1.setOnClickListener(this);

        // 임의로 액티비티 호출 시점에 어느 프레그먼트를 프레임레이아웃에 띄울 것인지를 정함
        callFragment(FRAGMENT1);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_tab1 :

                // '버튼1' 클릭 시 '프래그먼트1' 호출
                callFragment(FRAGMENT1);
                break;

        }
    }

    private void callFragment(int frament_no){

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no){
            case 1:

                // '프래그먼트1' 호출
                Fragment1 fragment1 = new Fragment1();
                 // 파라미터는 전달할 데이터 개수
                bundle.putString("category", category); // key , value
                bundle.putString("title", title); // key , value
                bundle.putString("place_url", place_url); // key , value
                bundle.putDouble("distance", distance); // key , value
                bundle.putString("phone", phone); // key , value
                bundle.putString("newAddress", newAddress); // key , value
                fragment1.setArguments(bundle);

                transaction.replace(R.id.fragment_container, fragment1);
                transaction.commit();
                break;

        }

    }


}
