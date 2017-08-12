package com.example.krushitpatel.netflixshows.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.krushitpatel.netflixshows.Activity.MainActivity;
import com.example.krushitpatel.netflixshows.Activity.MovieDetailsActivity;
import com.example.krushitpatel.netflixshows.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by krushitpatel on 2017-07-21.
 */

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.RecyclerViewHolder> {
    private List<Movie> movieList;
    private LayoutInflater layoutInflater;
    Context context;

    public RecyclerviewAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw, null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {

        holder.name.setText(movieList.get(position).getShow_title());
         holder.imageView.setImageURI(Uri.parse(movieList.get(position).getPoster()));
        holder.ratingBar.setRating(Float.parseFloat(movieList.get(position).getRating()));
        holder.category.setText(movieList.get(position).getCategory());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MovieDetailsActivity.class);
                i.putExtra("show_title",movieList.get(position).getShow_title().toString());
                i.putExtra("poster",movieList.get(position).getPoster().toString());
                context.startActivity(i);
            }
        });
}

    @Override
    public int getItemCount() {
        return this.movieList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
       // ImageView imageView;
        TextView name, category;
        RatingBar ratingBar;
        SimpleDraweeView simpleDraweeView;
        ImageView imageView;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.imagePoster);
            name = (TextView) itemView.findViewById(R.id.name);
            category = (TextView) itemView.findViewById(R.id.category);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
        }
    }

}