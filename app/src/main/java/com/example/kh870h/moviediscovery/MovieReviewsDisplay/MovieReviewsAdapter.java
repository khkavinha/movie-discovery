package com.example.kh870h.moviediscovery.MovieReviewsDisplay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kh870h.moviediscovery.R;

import java.util.ArrayList;

/**
 * Created by kavin on 2/8/2018.
 */

public class MovieReviewsAdapter extends BaseAdapter {
    private ArrayList<MovieReviews> movieReviews;
    private Context mContext;
    private LayoutInflater inflater;

    public MovieReviewsAdapter(Context mContext, ArrayList<MovieReviews> movieReviews) {
        this.mContext = mContext;
        this.movieReviews = movieReviews;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return movieReviews.size();
    }

    @Override
    public Object getItem(int position) {
        return movieReviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateMovieReviewsList(ArrayList<MovieReviews> data) {
        movieReviews.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MovieReviews movieReviews = (MovieReviews) getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.activity_moviereviews, parent, false);

            TextView name = (TextView) convertView.findViewById(R.id.MovieReviewsAuthor);
            TextView review = (TextView) convertView.findViewById(R.id.MovieReviewsContent);

            name.setText(movieReviews.getAuthor().toString());
            review.setText(movieReviews.getContent().toString());
        }
        return convertView;
    }
}
