package com.a.android.wheretogo.retrofit;

/**
 * Created by JAMCOM on 2017-09-12.
 */


public class SignInForm {
    private String email;
    private String passwd;

    public SignInForm(String email, String passwd) {
        this.email = email;
        this.passwd = passwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}