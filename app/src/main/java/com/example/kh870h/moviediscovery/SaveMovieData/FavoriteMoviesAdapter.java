package com.example.kh870h.moviediscovery.SaveMovieData;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.kh870h.moviediscovery.DetailActivity;
import com.example.kh870h.moviediscovery.MovieAdapter;
import com.example.kh870h.moviediscovery.MovieItem;
import com.example.kh870h.moviediscovery.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI;
import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.FAVORITE_MOVIE_DB_TABLE_NAME;
import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.KEY_AVERAGE;
import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.KEY_ID;
import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.KEY_OVERVIEW;
import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.KEY_POSTER_PATH;
import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.KEY_RELEASE;
import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.KEY_TITLE;

/**
 * Created by kavin on 1/30/2018.
 */

public class FavoriteMoviesAdapter extends CursorAdapter {
    private ArrayList<MovieItem> movieItems;

    public FavoriteMoviesAdapter(Context context, Cursor c, ArrayList<MovieItem> movieItems) {
        super(context, c);
        this.movieItems = movieItems;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.activity_gridview, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        //setting the images and textviews so it can be stored
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);


        Picasso.with(context).load("http://image.tmdb.org/t/p/w500/" + cursor.getString(cursor.getColumnIndexOrThrow("poster_path")))
                .error(R.drawable.ic_error_placeholder)
                .into(imageView);
    }


}
