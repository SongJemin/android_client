package com.a.android.wheretogo;

/**
 * Created by JAMCOM on 2017-09-04.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PasswdCheckActivity extends AppCompatActivity {

    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_passwd);

        Button real_passwd_btn = (Button) findViewById(R.id.real_passwd_btn);
        Button ok_button = (Button) findViewById(R.id.btn_ok);
        final EditText check_passwd_text = (EditText) findViewById(R.id.check_pw_text);

        real_passwd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: set visible of EditText
                if (!flag) {

                    check_passwd_text.setTransformationMethod(SingleLineTransformationMethod.getInstance());
                    flag = !flag;
                } else {
                    check_passwd_text.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = !flag;
                }
            }
        });


        ok_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent okIntent = new Intent(PasswdCheckActivity.this, MyInformActivity.class);
                startActivity(okIntent);
            }
        });
    }
}