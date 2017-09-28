package com.a.android.wheretogo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a.android.wheretogo.retrofit.ApiClient;


public class StartActivity extends AppCompatActivity {
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ApiClient.getInstance().create();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ImageView image=new ImageView(this);
        image.setImageResource(R.drawable.mark2);

        TextView markView = (TextView) findViewById(R.id.markView);
        Button nextButton = (Button) findViewById(R.id.nextButton);
        TextView registerButton = (TextView) findViewById(R.id.registerButton);
        TextView nonmembersButton = (TextView) findViewById(R.id.nonmembersButton);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/NotoSansCJKkr-Bold.otf");  //asset > fonts 폴더 내 폰트파일 적용
        markView.setTypeface(typeFace);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //GPS 설정화면으로 이동
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
            finish();
        }

        //마시멜로 이상이면 권한 요청하기
        if (Build.VERSION.SDK_INT >= 23) {
            //권한이 없는 경우
            if (ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(StartActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            //권한이 있는 경우
            else {
                requestMyLocation();
            }
        }
        //마시멜로 아래
        else {
            requestMyLocation();
        }

        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent login2Intent = new Intent(StartActivity.this,LoginActivity.class);
                StartActivity.this.startActivity(login2Intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent registerIntent = new Intent(StartActivity.this,RegisterActivity.class);
                StartActivity.this.startActivity(registerIntent);
            }
        });

        nonmembersButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent registerIntent = new Intent(StartActivity.this,MainActivity.class);
                StartActivity.this.startActivity(registerIntent);
            }
        });
    }

    //권한 요청후 응답 콜백
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //ACCESS_COARSE_LOCATION 권한
        if (requestCode == 1) {
            //권한받음
            Log.v("asdf", "권한받음");
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestMyLocation();
            }
            //권한못받음
            else {
                Toast.makeText(this, "권한없음", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    //위치정보 구하기 리스너
    LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            if (ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //나의 위치를 한번만 가져오기 위해
            locationManager.removeUpdates(locationListener);
            //위도 경도
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("gps", "onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    //나의 위치 요청
    public void requestMyLocation() {
        if (ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //요청
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

}
