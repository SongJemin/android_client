package com.a.android.wheretogo;

/**
 * Created by JAMCOM on 2017-09-04.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MyInformActivity extends AppCompatActivity {
    private final String LOG_TAG = "Information2Activity";
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_change_info);
        Button ok_button = (Button) findViewById(R.id.btn_check02);
        Button button1 = (Button) findViewById(R.id.real_passwd_view);

        final EditText passWdEdit = (EditText) findViewById(R.id.edit_pw02);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: set visible of EditText
                if (!flag) {

                    passWdEdit.setTransformationMethod(SingleLineTransformationMethod.getInstance());
                    flag = !flag;
                    Log.e(LOG_TAG, "flag : " + flag);
                } else {
                    passWdEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = !flag;
                    Log.e(LOG_TAG, "flag : " + flag);
                }
            }
        });

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent(MyInformActivity.this, MainActivity.class);
                startActivity(returnIntent);
            }
        });

    }
}
