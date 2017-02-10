package com.example.suvajit.tmdbnowplaying.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suvajit.tmdbnowplaying.R;
import com.example.suvajit.tmdbnowplaying.handler.DownloadImageTask;
import com.example.suvajit.tmdbnowplaying.pojo.Movie;

/**
 * Created by Suvajit on 10-Feb-17.
 */

public class MovieDetailActivity extends AppCompatActivity {
    private static final String IMG_REQ_URL = "http://image.tmdb.org/t/p/w500";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moview_detail);

        //Declarations and initializations
        TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        TextView textViewOverView = (TextView) findViewById(R.id.textViewOverView);
        RatingBar ratingBarPopularity = (RatingBar) findViewById(R.id.ratingBarPopilarity);
        ImageView imageViewPoster = (ImageView) findViewById(R.id.imageViewPoster);

        Intent intent = getIntent();

        //get moview from previous activity_now_playing
        Movie movie = (Movie) intent.getSerializableExtra("Movie");



        //Set Data to views
        textViewTitle.setText(movie.getTitle());
        textViewOverView.setText(movie.getOverView());
        ratingBarPopularity.setRating(Float.parseFloat(movie.getPopularity())*5/100);
        //Download and set Image task
        if(isNetworkAvailable()) {
            String imageDownloadUrl = IMG_REQ_URL + movie.getPosterPath();
            new DownloadImageTask(imageViewPoster).execute(imageDownloadUrl);
        }else
            Toast.makeText(this, "Connect to a network to see Images", Toast.LENGTH_SHORT).show();


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
