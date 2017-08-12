package com.example.krushitpatel.netflixshows.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.krushitpatel.netflixshows.Adapter.Movie;
import com.example.krushitpatel.netflixshows.Adapter.RecyclerviewAdapter;
import com.example.krushitpatel.netflixshows.R;
import com.example.krushitpatel.netflixshows.Utils.ConnectionDetector;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;
    private ProgressDialog progressDialog;
    ArrayList movieList;
    String urlTitle,urlActor;
    Movie movie;
    private static Response response;
    String UrlTitlePath,UrlActorPath;
    private String TitleURL = "https://netflixroulette.net/api/api.php?title=";
    private String ActorURL = "http://netflixroulette.net/api/api.php?actor=";
    Boolean isInternetPresent = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        Fresco.initialize(this);
        movieList = new ArrayList<>();
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        urlTitle = getIntent().getStringExtra("urlTitle");
        urlActor = getIntent().getStringExtra("urlActor");
        UrlTitlePath = TitleURL+urlTitle;
        UrlActorPath = ActorURL+urlActor;
        isInternetPresent = connectionDetector.isConnectingToInternet();
//        UrlActorPath = ActorURL+data;
        if(!isInternetPresent){
           Toast.makeText(this,"Please check your internet connection !",Toast.LENGTH_LONG).show();
        }else{
            if(!urlTitle.isEmpty() || !urlActor.isEmpty()){
                new GetMovie().execute();
            }else {
                Toast.makeText(MovieListActivity.this,"Please insert correct data..",Toast.LENGTH_LONG).show();
            }
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

                   // if(!urlTitle.isEmpty() && urlActor.isEmpty()) {

                    if(!urlTitle.isEmpty() && urlActor.isEmpty()) {

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
                        }



                    if (urlTitle.isEmpty() && !urlActor.isEmpty()){
                        if(getActorData()!=null){
                            for(int i = 0 ; i<getActorData().length() ; i++){
                                movie = new Movie();
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
                        }else {
                            Handler h = new Handler(Looper.getMainLooper());
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MovieListActivity.this,"Please insert correct name",Toast.LENGTH_LONG).show();
                                }
                            });

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
    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view,int position);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.cancel();
        }
    }

    public JSONObject getData(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(UrlTitlePath)
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
                .url(UrlActorPath)
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
