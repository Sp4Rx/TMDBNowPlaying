package com.example.suvajit.tmdbnowplaying.handler;

import android.os.AsyncTask;

import com.example.suvajit.tmdbnowplaying.pojo.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Suvajit on 10-Feb-17.
 */

public class NowPlayingListJsonHandler extends AsyncTask<String, Object, ArrayList<Movie>> {
    @Override
    protected ArrayList<Movie> doInBackground(String... strings) {
        try {
            //Connect to the url
            URL url = new URL(strings[0]);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(7000);
            connection.setConnectTimeout(7000);
            connection.connect();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                //Download json data
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine())!= null){
                    sb.append(line).append("\n");
                }

                //Parse Json
                JSONObject jsonRoot = new JSONObject(sb.toString());
                Movie.totalPages = jsonRoot.getString("total_pages");
                JSONArray jsonArrayMovieList = jsonRoot.getJSONArray("results");
                ArrayList<Movie> movieArrayList = new ArrayList<>();
                for (int i =0;i<jsonArrayMovieList.length();i++){
                    Movie movie = new Movie();
                    JSONObject jsonMovie = jsonArrayMovieList.getJSONObject(i);
                    movie.setId(jsonMovie.getString("id"));
                    movie.setTitle(jsonMovie.getString("title"));
                    movie.setPosterPath(jsonMovie.getString("poster_path"));
                    movie.setOverView(jsonMovie.getString("overview"));
                    movie.setPopularity(jsonMovie.getString("popularity"));
                    movie.setVoteCount(jsonMovie.getString("vote_count"));
                    movie.setVoteAvg(jsonMovie.getString("vote_average"));

                    movieArrayList.add(movie);
                }

                return movieArrayList;


            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
