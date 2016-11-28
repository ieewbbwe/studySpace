package com.webber.uploaddemo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by mxh on 2016/11/28.
 * Describe：
 */

public interface FileUploadService {

    @Multipart
    @POST("upload")
    Observable<Response<BaseResponse>> uploadFile(@Part("description") RequestBody description,
                                                  @Part MultipartBody.Part file);
}
