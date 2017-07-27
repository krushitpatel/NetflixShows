package com.example.krushitpatel.netflixshows.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.krushitpatel.netflixshows.Adapter.Movie;
import com.example.krushitpatel.netflixshows.Adapter.RecyclerviewAdapter;
import com.example.krushitpatel.netflixshows.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;
    private ProgressDialog progressDialog;
    ArrayList<Movie> movieList;
    EditText editText;
    Button searchButton;
    private String URL = "https://netflixroulette.net/api/api.php?title=";
    String UrlPath;
    Movie movie;
    String data;
    private static Response response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.search);
        searchButton = (Button) findViewById(R.id.searchButton);
        Fresco.initialize(this);
        movieList = new ArrayList<Movie>();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 data = editText.getText().toString();
                if(!data.isEmpty()){
                    UrlPath = URL+data;
                }else {
                    editText.setText("");
                    Toast.makeText(MainActivity.this,"Please enter the name..",Toast.LENGTH_LONG).show();
                }

                if(UrlPath != null){
                    new GetMovie().execute();
                }else{
                    Toast.makeText(MainActivity.this,"No data",Toast.LENGTH_LONG).show();

                }
            }
        });

        movie = new Movie();




    }
    public class GetMovie extends AsyncTask<Object, Object, ArrayList<Movie>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected ArrayList<Movie> doInBackground(Object... params) {
            try {
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

            } catch (JSONException e) {
                e.printStackTrace();
            }
            movieList.add(movie);
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
                .url(UrlPath)
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
    }

