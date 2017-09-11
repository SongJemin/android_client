package com.a.android.wheretogo.retrofit;

/**
 * Created by JAMCOM on 2017-09-07.
 */

public class SignUpForm {
    private String userName;
    private String email;
    private String passwd;
    private String phoneNum;

    public SignUpForm() {
    }

    public SignUpForm(String userName, String email, String passwd, String phoneNum) {
        this.userName = userName;
        this.email = email;
        this.passwd = passwd;
        this.phoneNum = phoneNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}