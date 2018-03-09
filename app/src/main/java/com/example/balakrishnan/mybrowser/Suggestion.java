package com.example.balakrishnan.mybrowser;

import com.google.gson.annotations.SerializedName;

/**
 * Created by balakrishnan on 9/3/18.
 */

public class Suggestion
{
    @SerializedName("data")
    private String data;

    public String getData ()
    {
        return data;
    }

    public Suggestion(String data) {
        this.data = data;
    }

    public void setData (String data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [data = "+data+"]";
    }
}

