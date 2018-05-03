package com.example.kh870h.moviediscovery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import java.util.ArrayList;

/**
 * Created by kh870h on 11/3/2017.
 */

public class MovieAdapter extends BaseAdapter {

    private Context mContext;
    int resource;
    private ArrayList<MovieItem> movieItems;


    public MovieAdapter(Context context, int resource, ArrayList<MovieItem> movieItems) {
        this.movieItems = movieItems;
        this.mContext = context;
        this.resource = resource;
    }

    public void updateMovies(ArrayList<MovieItem> movieItems) {
        this.movieItems = movieItems;
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public MovieItem getItem(int position) {
        return movieItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View convertview = convertView;
        ViewHolder holder;

        if (convertview == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertview = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.mImageView = (ImageView) convertview.findViewById(R.id.imageView);
            convertview.setTag(holder);
        } else {
            holder = (ViewHolder) convertview.getTag();
        }

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Displaying Movie Overview", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("title", movieItems.get(position).getTitle());
                Log.d("title inside MA", "title: " + movieItems.get(position).getTitle());
                intent.putExtra("release_date", movieItems.get(position).getPosterPath());
                Log.d("release date inside MA", "release date: " + movieItems.get(position).getPosterPath());
                intent.putExtra("vote_average", movieItems.get(position).getRating());
                Log.d("vote average inside MA", "vote average: " + movieItems.get(position).getRating());
                intent.putExtra("overview", movieItems.get(position).getReleaseDate());
                Log.d("overview inside MA", "overview: " + movieItems.get(position).getReleaseDate());
                intent.putExtra("poster_path", movieItems.get(position).getOverView());
                Log.d("poster path inside MA", "poster path: " + movieItems.get(position).getOverView());
                intent.putExtra("id", movieItems.get(position).getId());
                Log.d("id inside MA", "id: " + movieItems.get(position).getId());
                v.getContext().startActivity(intent);
            }
        });

        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w500/" + movieItems.get(position).getOverView())
                .error(R.drawable.ic_error_placeholder)
                .into(holder.mImageView);
        return convertview;
    }

    public static class ViewHolder {
        public ImageView mImageView;
    }

}
