package com.a.android.wheretogo.retrofit;

/**
 * Created by JAMCOM on 2017-09-07.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
public class ApiMessage {
    @JsonProperty("code")
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}