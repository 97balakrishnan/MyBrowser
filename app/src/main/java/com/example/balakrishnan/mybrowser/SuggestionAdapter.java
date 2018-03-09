package com.example.balakrishnan.mybrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static com.example.balakrishnan.mybrowser.HomeActivity.sAdapter1;
import static com.example.balakrishnan.mybrowser.HomeActivity.sList1;
import static com.example.balakrishnan.mybrowser.WebActivity.sAdapter;
import static com.example.balakrishnan.mybrowser.WebActivity.sList;

/**
 * Created by balakrishnan on 9/3/18.
 */

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.MyViewHolder> {

    private List<Suggestion> SuggestionList;
    Context context;
    Activity act;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView SuggestionName;

        public MyViewHolder(View view) {
            super(view);
            SuggestionName = (TextView) view.findViewById(R.id.SuggestionName);
        }
    }

    public SuggestionAdapter(List<Suggestion> verticalList, Context context,Activity act) {
        this.SuggestionList = verticalList;
        this.context = context;
        this.act=act;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/product_san_regular.ttf");
        //System.out.println("class name "+act.getClass().getSimpleName());
        final String name = SuggestionList.get(position).getData().toString().replace("\"","").trim();
        holder.SuggestionName.setText(name);
        holder.SuggestionName.setTypeface(font);

        holder.SuggestionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String actName=act.getClass().getSimpleName().trim();
                System.out.println(actName);
                if(actName.equals("HomeActivity"))
                {
                    sList1.clear();
                    sAdapter1.notifyDataSetChanged();

                    Intent intent = new Intent(context,WebActivity.class);
                    intent.putExtra("url","http://google.com/search?q="+name);
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(act, HomeActivity.urlET,HomeActivity.urlET.getTransitionName());
                    act.startActivity(intent,optionsCompat.toBundle());
                    if(sList!=null && sAdapter!=null) {
                        sList.clear();
                        sAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    WebActivity.webView.loadUrl("http://google.com/search?q=" + name);
                    WebActivity.urlET.clearFocus();
                    sList.clear();
                    sAdapter.notifyDataSetChanged();

                }


                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return SuggestionList.size();
    }
}