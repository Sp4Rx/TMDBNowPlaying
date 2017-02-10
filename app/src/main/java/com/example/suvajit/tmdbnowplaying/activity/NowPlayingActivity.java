package com.example.suvajit.tmdbnowplaying.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.suvajit.tmdbnowplaying.R;
import com.example.suvajit.tmdbnowplaying.adapter.MovieListRecyclerViewAdapter;
import com.example.suvajit.tmdbnowplaying.handler.NowPlayingListJsonHandler;
import com.example.suvajit.tmdbnowplaying.pojo.Config;
import com.example.suvajit.tmdbnowplaying.pojo.Movie;
import com.example.suvajit.tmdbnowplaying.util.DividerItemDecoration;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class NowPlayingActivity extends AppCompatActivity {

    //Global Declarations
    ArrayList<Movie> movieArrayList;
    RecyclerView recyclerView;
    private String currentPage = "1";
    private NowPlayingActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        //Declarations and initializations
        context = this;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerMovies);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        Button buttonPrevious = (Button) findViewById(R.id.buttonPrevious);
        Button buttonNext = (Button) findViewById(R.id.buttonNext);

        if (!Config.API_KEY.equals("")) {

            loadNowPlayingList(currentPage);

            View.OnClickListener btnOnclickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.buttonPrevious:
                            if (currentPage.equals("1")) {
                                Toast.makeText(context, "This is the first page", Toast.LENGTH_SHORT).show();
                            } else {
                                int tempCurrentPage = Integer.parseInt(currentPage);
                                tempCurrentPage--;
                                currentPage = String.valueOf(tempCurrentPage);
                                loadNowPlayingList(currentPage);
                            }
                            break;
                        case R.id.buttonNext:
                            if (currentPage.equals(Movie.totalPages)) {
                                Toast.makeText(context, "This is the last page", Toast.LENGTH_SHORT).show();
                            } else {
                                int tempCurrentPage = Integer.parseInt(currentPage);
                                tempCurrentPage++;
                                currentPage = String.valueOf(tempCurrentPage);
                                loadNowPlayingList(currentPage);
                            }
                            break;
                    }
                }
            };
            buttonPrevious.setOnClickListener(btnOnclickListener);
            buttonNext.setOnClickListener(btnOnclickListener);
        } else
            Toast.makeText(context, "Set api key in Congig class", Toast.LENGTH_LONG).show();

    }


    private void loadNowPlayingList(String pageNo) {
        if (isNetworkAvailable()) {
            //Declarations and initializations
            Uri.Builder builder = new Uri.Builder();
            MovieListRecyclerViewAdapter movieListRecyclerViewAdapter;

            //Url Builder for getting list of now playing movies
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath("now_playing")
                    .appendQueryParameter("api_key", Config.API_KEY)
                    .appendQueryParameter("language", "en-US")
                    .appendQueryParameter("page", pageNo)
                    .build();
            String url = builder.toString();

            //Download json with async task
            try {
                movieArrayList = new NowPlayingListJsonHandler().execute(url).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            movieListRecyclerViewAdapter = new MovieListRecyclerViewAdapter(movieArrayList);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(movieListRecyclerViewAdapter);
        } else
            Toast.makeText(context, "Connect to a network to see now playing list", Toast.LENGTH_SHORT).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
