package com.a.android.wheretogo.retrofit;

/**
 * Created by JAMCOM on 2017-09-07.
 */

        import com.google.gson.FieldNamingPolicy;
        import com.google.gson.Gson;
        import com.google.gson.GsonBuilder;
        import com.google.gson.internal.bind.DateTypeAdapter;

        import java.util.Date;

        import retrofit2.Retrofit;
        import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by YangHC on 2017-08-08.
 */

public class ApiClient {
    private ApiService apiService;
    private static ApiClient instance;
    private static final String PROD = "13.59.184.129";

    public void create(){
        /**
         * Gson 컨버터 이용
         */
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getApiServer(PROD))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static ApiClient getInstance() {
        if (instance == null) {
            synchronized (ApiClient.class) {
                if (instance == null) {
                    instance = new ApiClient();
                }
            }
        }
        return instance;
    }

    public static String getApiServer(String hostIp) {
        return "http://" + hostIp + ":8080";
    }

    public ApiService getApiService() {
        return apiService;
    }
}