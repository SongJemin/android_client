package com.a.android.wheretogo.retrofit;

/**
 * Created by JAMCOM on 2017-09-12.
 */
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by YangHC on 2017-07-01.
 */

public class User implements Serializable{
    @JsonProperty("idx")
    private String idx;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("passwd")
    private String passwd;

    @JsonProperty("phoneNum")
    private String phoneNum;

    public User() {
    }

    @JsonCreator
    public User(String userName, String email, String passwd) {
        this.userName = userName;
        this.email = email;
        this.passwd = passwd;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
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