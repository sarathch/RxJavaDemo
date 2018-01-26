package com.example.syennamani.meowfest;

/**
 * Created by syennamani on 1/24/2018.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class CatsListAdapter extends RecyclerView.Adapter<CatsListAdapter.MyViewHolder> {

    private List<Cats> catsList;
    private Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, timestamp, description ;
        public ImageView image_cats;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
            description = (TextView) view.findViewById(R.id.description);
            image_cats = view.findViewById(R.id.iv_cat);
        }
    }


    public CatsListAdapter(List<Cats> catsList) {
        this.catsList = catsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cats_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Cats cats = catsList.get(position);
        holder.title.setText(cats.getTitle());
        holder.timestamp.setText(cats.getTimestamp());
        holder.description.setText(cats.getDescription());
        Picasso.with(context).load(cats.getImage_url()).into(holder.image_cats);
    }

    @Override
    public int getItemCount() {
        return catsList.size();
    }

}
