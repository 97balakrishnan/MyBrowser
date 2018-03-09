package com.example.balakrishnan.mybrowser;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by balakrishnan on 4/3/18.
 */

public class SearchSuggestion {
    public ArrayList<String> sList;
    SearchSuggestion(){}
    SearchSuggestion(String q)
    {
        sList=new ArrayList<>();

        Retrofit retrofit = null;
        retrofit = new Retrofit.Builder()
                .baseUrl("http://suggestqueries.google.com/complete/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService mAPIService;
        mAPIService= retrofit.create(APIService.class);
        Call<ResponseBody> call = mAPIService.SuggestionCall("firefox","en",q);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("response code "+response.code()+" "+response.message());
                if(response.isSuccessful())
                {
                    try{
                        sList.clear();
                        String temp = response.body().string();
                        System.out.println(temp);
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }


}
