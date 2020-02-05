package com.example.ml_vision;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitClientInstance
{
    String BASE_URL = "http://192.168.135.149:5000" ;

    @Multipart
    @POST("/ocr")
    Call<LabReport> upload(@Part MultipartBody.Part imageFile,
                              @Part("description") RequestBody description
                );

}
