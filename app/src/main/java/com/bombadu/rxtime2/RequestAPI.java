package com.bombadu.rxtime2;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

public interface RequestAPI {

    @GET("todos/1")
    Observable<ResponseBody> makeObservableQuery();
}
