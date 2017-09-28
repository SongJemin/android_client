package com.a.android.wheretogo;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.a.android.wheretogo.retrofit.ApiClient;
import com.a.android.wheretogo.retrofit.SignInForm;
import com.a.android.wheretogo.retrofit.User;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {
    private EditText emailText;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView markView = (TextView) findViewById(R.id.markView);
        emailText = (EditText) findViewById(R.id.emailText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final Button loginButton = (Button) findViewById(R.id.loginButton);
        Button realPasswdViewBtn = (Button) findViewById(R.id.real_passwd_view);
        TextView findPassword = (TextView) findViewById(R.id.findPasswordView);

        ImageView image=new ImageView(this);
        image.setImageResource(R.drawable.mark2);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/NotoSansCJKkr-Bold.otf");  //asset > fonts 폴더 내 폰트파일 적용
        markView.setTypeface(typeFace);
        SpannableString content = new SpannableString("비밀번호를 잊으셨나요?");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        findPassword.setText(content);

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

        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                final String emailID = emailText.getText().toString();
                final String userPassword = passwordText.getText().toString();


                SignInForm signInForm = new SignInForm(emailID,userPassword);

                ApiClient.getInstance()
                        .getApiService()
                        .userSignIn(1,signInForm)
                        .enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                                Log.e("LOG","Response");
                                if(response.body()!=null){
                                    Log.e("LOG","Login Success");
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    LoginActivity.this.startActivity(intent);

                                }else{
                                    Log.e("LOG","ERROR" );

                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Log.e("LOG","Fail");

                            }
                        });


            }
        });

    }
}
