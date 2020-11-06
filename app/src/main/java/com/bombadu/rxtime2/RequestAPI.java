package com.bombadu.rxtime2;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RequestAPI {

    @GET("posts")
    Observable<List<Post>> getPosts();

    @GET("posts/{id}")
    Observable<Post> getPost(
            @Path("id") int id
    );
}
