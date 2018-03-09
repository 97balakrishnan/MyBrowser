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
import android.support.v4.app.SupportActivity;
/**
 * Created by balakrishnan on 4/3/18.
 */

public class SearchSuggestion {

    SearchSuggestion(){}
    public void updateSuggestion(String q)
    {
        WebActivity.sList.clear();

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
                        String temp = response.body().string();
                        System.out.println(temp);
                        String[] data = temp.replace("[","").replace("]","").split(",");
            //          System.out.println("suggestion data "+data[1]+" "+temp.length());
                        for(int i=0;i<data.length;i++){
                            WebActivity.sList.add(new Suggestion(data[i]));
                        }
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
