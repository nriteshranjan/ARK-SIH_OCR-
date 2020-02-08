package com.example.ml_vision;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitClientInstance
{
    String BASE_URL = "http://flaskocr-env-1.rbcnwddmre.ap-south-1.elasticbeanstalk.com" ;

    @Multipart
    @POST("/ocr")
    Call<LabReport> upload(@Part MultipartBody.Part imageFile,
                              @Part("description") RequestBody description
                );

}
