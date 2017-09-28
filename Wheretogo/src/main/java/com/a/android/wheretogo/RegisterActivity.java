package com.a.android.wheretogo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.a.android.wheretogo.retrofit.ApiClient;
import com.a.android.wheretogo.retrofit.ApiMessage;
import com.a.android.wheretogo.retrofit.SignUpForm;

import retrofit2.Call;
import retrofit2.Callback;

public class RegisterActivity extends AppCompatActivity {

    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView markView = (TextView) findViewById(R.id.markView);
        final EditText emailText = (EditText) findViewById(R.id.emailText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final EditText passwordConfirmText = (EditText) findViewById(R.id.passwordConfirmText);
        final EditText nameText = (EditText) findViewById(R.id.nameText);
        final EditText phoneText = (EditText) findViewById(R.id.phoneText);
        Button realPasswdViewBtn = (Button) findViewById(R.id.real_passwd_btn);
        Button realConfirmPasswdViewBtn = (Button) findViewById(R.id.real_passwd_confirm_btn);


        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/NotoSansCJKkr-Bold.otf");  //asset > fonts 폴더 내 폰트파일 적용
        markView.setTypeface(typeFace);
        Button registerButton = (Button) findViewById(R.id.registerBtn);

        realPasswdViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: set visible of EditText
                if (!flag) {
                    passwordText.setTransformationMethod(SingleLineTransformationMethod.getInstance());
                    flag = !flag;
                } else {
                    passwordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = !flag;
                }
            }
        });

        realConfirmPasswdViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: set visible of EditText
                if (!flag) {
                    passwordConfirmText.setTransformationMethod(SingleLineTransformationMethod.getInstance());
                    flag = !flag;
                } else {
                    passwordConfirmText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = !flag;
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Log.e("LOG","aaaa1");
                String email = emailText.getText().toString();
                String userPassword = passwordText.getText().toString();
                String userName = nameText.getText().toString();
                String phoneNum = phoneText.getText().toString();

                SignUpForm signUpForm = new SignUpForm(userName,email,userPassword, phoneNum);
                Log.e("LOG","aaaa2");

                ApiClient.getInstance()
                        .getApiService()
                        .userSignUp(1, signUpForm)
                        .enqueue(new Callback<ApiMessage>() {
                            @Override
                            public void onResponse(Call<ApiMessage> call, retrofit2.Response<ApiMessage> response) {
                                Log.e("LOG","Response");
                                if(response.body().getCode()==1){
                                    Log.e("LOG","Success");
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("회원가입 성공")
                                            .setPositiveButton("확인",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(RegisterActivity.this, StartActivity.class);
                                                    RegisterActivity.this.startActivity(intent);
                                                }
                                            })
                                            .create()
                                            .show();
                                }else{
                                    Log.e("LOG","ERROR :" + response.body().getCode());
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiMessage> call, Throwable t) {
                                Log.e("LOG","Fail");

                            }
                        });
            }
        });
    }
}
