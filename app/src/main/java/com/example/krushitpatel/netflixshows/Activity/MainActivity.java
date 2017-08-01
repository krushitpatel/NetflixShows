package com.example.krushitpatel.netflixshows.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;
    private ProgressDialog progressDialog;
    ArrayList<Movie> movieList;
    private String TitleURL = "https://netflixroulette.net/api/api.php?title=";
    private String ActorURL = "http://netflixroulette.net/api/api.php?actor=";
    String UrlTitlePath,UrlActorPath;
    Movie movie;
    String data;
    @BindView(R.id.search) EditText editText;
    @BindView(R.id.searchButton) Button searchButton;
    private static Response response;
    @OnClick(R.id.searchButton) void buttonClicked(){
        data = editText.getText().toString();
                UrlTitlePath = TitleURL+data;
                Intent intent = new Intent(MainActivity.this,MovieListActivity.class);
                intent.putExtra("urlTitle",UrlTitlePath);
                startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        Fresco.initialize(this);
        movieList = new ArrayList<Movie>();
    }


}

