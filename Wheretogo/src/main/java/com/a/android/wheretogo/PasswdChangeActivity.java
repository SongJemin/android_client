package com.a.android.wheretogo;

/**
 * Created by JAMCOM on 2017-09-04.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class PasswdChangeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passwd);

        Button ok_button = (Button) findViewById(R.id.btn_ok);


        ok_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent okIntent = new Intent(PasswdChangeActivity.this, MyInformActivity.class);
                startActivity(okIntent);
            }
        });



    }
}