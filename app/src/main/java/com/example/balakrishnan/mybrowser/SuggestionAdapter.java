package com.example.balakrishnan.mybrowser;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static com.example.balakrishnan.mybrowser.WebActivity.sAdapter;
import static com.example.balakrishnan.mybrowser.WebActivity.sList;

/**
 * Created by balakrishnan on 9/3/18.
 */
public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.MyViewHolder> {

    private List<Suggestion> SuggestionList;
    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView SuggestionName;
        public ImageView SuggestionImage;
        public CardView SuggestionCardView;

        public MyViewHolder(View view) {
            super(view);
            SuggestionName = (TextView) view.findViewById(R.id.SuggestionName);


        }
    }

    public SuggestionAdapter(List<Suggestion> verticalList, Context context) {
        this.SuggestionList = verticalList;
        this.context = context;
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

        final String name = SuggestionList.get(position).getData().toString().replace("\"","").trim();
        holder.SuggestionName.setText(name);
        holder.SuggestionName.setTypeface(font);

        holder.SuggestionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebActivity.webView.loadUrl("http://google.com/search?q=" + name);
                sList.clear();
                sAdapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return SuggestionList.size();
    }
}