package com.bombadu.rxtime2;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

public interface RequestAPI {

    @GET("todos/1")
    Flowable<ResponseBody> makeQuery();
}
