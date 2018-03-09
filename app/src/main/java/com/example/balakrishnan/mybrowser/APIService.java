package com.example.balakrishnan.mybrowser;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by balakrishnan on 9/3/18.
 */

public interface APIService{
    @GET("search")
    Call<ResponseBody>SuggestionCall
            (
        @Query("output") String output,
        @Query("h1") String h1,
        @Query("q") String q
            );
}
