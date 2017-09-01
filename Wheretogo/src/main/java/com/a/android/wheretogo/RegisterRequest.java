package com.a.android.wheretogo;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JAMCOM on 2017-06-25.
 */

public class RegisterRequest extends StringRequest {
    final static private String URL = "http://ec2-13-59-184-129.us-east-2.compute.amazonaws.com/Register.php";
    private Map<String, String> parameters;

    public RegisterRequest(String userID, String userPassword, String userName, String phoneNum, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
        parameters.put("userName", userName);
        parameters.put("phoneNum", phoneNum);

    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
