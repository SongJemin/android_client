package com.a.android.wheretogo.retrofit;

/**
 * Created by JAMCOM on 2017-09-07.
 */

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("/wheretogo/api/{version}/user/signup")
    Call<ApiMessage> userSignUp(@Path("version") int version
            , @Body SignUpForm request
    );

    //@POST("/beongae/api/{version}/user/signin")
  //  Call<User> userSignIn(@Path("version") int version
    //        , @Body UserSignInForm request
    //);


}