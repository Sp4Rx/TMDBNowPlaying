package com.example.suvajit.tmdbnowplaying.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.suvajit.tmdbnowplaying.R;
import com.example.suvajit.tmdbnowplaying.activity.MovieDetailActivity;
import com.example.suvajit.tmdbnowplaying.pojo.Movie;

import java.util.ArrayList;

/**
 * Created by Suvajit on 10-Feb-17.
 */

public class MovieListRecyclerViewAdapter extends RecyclerView.Adapter<MovieListRecyclerViewAdapter.ViewHolder> {
    private final ArrayList<Movie> movieArrayList;
    Context context;

    public MovieListRecyclerViewAdapter(ArrayList<Movie> movieArrayList) {
        this.movieArrayList = movieArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.recycler_view_movies_row,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Movie movie = movieArrayList.get(position);
        holder.textViewMovieName.setText(movie.getTitle());
        holder.textViewMovieName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra("Movie",movie);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMovieName;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewMovieName = (TextView) itemView.findViewById(R.id.textViewMovieName);
        }
    }
}
