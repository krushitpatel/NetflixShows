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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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


                    if(!urlTitle.isEmpty() && urlActor.isEmpty()) {
                        Gson gson = new Gson();
                        Movie movie = gson.fromJson(String.valueOf(getData()),Movie.class);
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
                                JSONObject jsonObject = getActorData().getJSONObject(i);
                                Gson gson = new Gson();
                                movie = new Movie();
                                movie = gson.fromJson(String.valueOf(jsonObject),Movie.class);
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
}catch (JSONException e)
{
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
