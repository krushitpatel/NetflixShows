package com.example.krushitpatel.netflixshows.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.krushitpatel.netflixshows.Adapter.Movie;
import com.example.krushitpatel.netflixshows.Adapter.RecyclerviewAdapter;
import com.example.krushitpatel.netflixshows.R;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;
    private ProgressDialog progressDialog;
    ArrayList<Movie> movieList;
    String urlTitle,urlActor;
    Movie movie;
    private static Response response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        Fresco.initialize(this);
        movieList = new ArrayList<Movie>();

        urlTitle = getIntent().getStringExtra("urlTitle");
        urlActor = getIntent().getStringExtra("urlActor");
//        UrlActorPath = ActorURL+data;
        if(!urlTitle.isEmpty() || !urlActor.isEmpty()){
            new GetMovie().execute();
        }else {
            //editText.setText("");
            Toast.makeText(MovieListActivity.this,"Please insert correct data..",Toast.LENGTH_LONG).show();
        }
        movie = new Movie();
    }
    public class GetMovie extends AsyncTask<Object, Object, ArrayList<Movie>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MovieListActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected ArrayList<Movie> doInBackground(Object... params) {
            try{
                    if(urlTitle != null && urlActor == null) {
                        movie.show_title = getData().getString("show_title");
                        movie.poster = getData().getString("poster");
                        movie.runtime = getData().getString("runtime");
                        movie.category = getData().getString("category");
                        movie.unit = Integer.parseInt(getData().getString("unit"));
                        movie.show_id = Integer.parseInt(getData().getString("show_id"));
                        movie.release_year = getData().getString("release_year");
                        movie.rating = getData().getString("rating");
                        movie.show_cast = getData().getString("show_cast");
                        movie.director = getData().getString("director");
                        movie.summary = getData().getString("summary");
                        movie.mediatype = Integer.parseInt(getData().getString("mediatype"));
                        if(movieList.isEmpty()){
                            movieList.add(movie);
                        }else{
                            movieList.clear();
                            movieList.add(movie);
                        }

                    }else if(urlActor != null) {
                        for(int i = 0 ; i<getActorData().length() ; i++){
                            JSONObject jsonObject = getActorData().getJSONObject(i);
                            movie.show_title = jsonObject.getString("show_title");
                            movie.poster = jsonObject.getString("poster");
                            movie.runtime = jsonObject.getString("runtime");
                            movie.category = jsonObject.getString("category");
                            movie.unit = Integer.parseInt(jsonObject.getString("unit"));
                            movie.show_id = Integer.parseInt(jsonObject.getString("show_id"));
                            movie.release_year = jsonObject.getString("release_year");
                            movie.rating = jsonObject.getString("rating");
                            movie.show_cast = jsonObject.getString("show_cast");
                            movie.director = jsonObject.getString("director");
                            movie.summary = jsonObject.getString("summary");
                            movie.mediatype = jsonObject.getInt("mediatype");
                            movieList.add(movie);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            Log.d("Movie data", movieList.toString());
            return movieList;
        }
        @Override
        protected void onPostExecute(ArrayList<Movie> aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerviewAdapter = new RecyclerviewAdapter(getApplicationContext(),movieList);
            recyclerView.setAdapter(recyclerviewAdapter);
        }
    }
    public JSONObject getData(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlTitle)
                .build();
        try {
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public JSONArray getActorData(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlActor)
                .build();
        try {
            response = client.newCall(request).execute();
            return new JSONArray(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
