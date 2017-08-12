package com.example.krushitpatel.netflixshows.Activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.krushitpatel.netflixshows.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {
@BindView(R.id.imagePosterDetail)
SimpleDraweeView simpleDraweeView;

    //ImageView imagePoster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        Fresco.initialize(this);
        String poster = getIntent().getStringExtra("poster");
        simpleDraweeView.setImageURI(Uri.parse(poster));

    }
}
