package com.example.phpandroid;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("save.php")
    Call<Note> saveNote(
            @Field("name") String name
    );

    @GET("read.php")
    Call<List<Note>> getNotes();
}
