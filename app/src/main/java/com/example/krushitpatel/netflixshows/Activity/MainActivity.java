package com.example.krushitpatel.netflixshows.Activity;


import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import com.example.krushitpatel.netflixshows.R;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    String dataMovieTitle, dataActor;
    @BindViews({R.id.searchMovie, R.id.searchActor})
    List<EditText> editText;
    @BindView(R.id.searchButton)
    Button searchButton;
    private static Response response;

    @OnClick(R.id.searchButton)
    void buttonClicked() {
        dataMovieTitle = editText.get(0).getText().toString();
        dataActor = editText.get(1).getText().toString();

        Intent intent = new Intent(MainActivity.this, MovieListActivity.class);
        if (dataMovieTitle != null || dataActor != null) {
            intent.putExtra("urlTitle", dataMovieTitle);
            intent.putExtra("urlActor", dataActor);
        }

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


}

