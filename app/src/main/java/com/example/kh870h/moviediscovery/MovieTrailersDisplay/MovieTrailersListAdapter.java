package com.example.kh870h.moviediscovery.MovieTrailersDisplay;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kh870h.moviediscovery.R;

import java.util.ArrayList;


public class MovieTrailersListAdapter extends BaseAdapter {
    private ArrayList<MovieTrailers> movieTrailers;
    private Context mContext;
    private LayoutInflater inflater;

    public MovieTrailersListAdapter(Context context, ArrayList<MovieTrailers> movieTrailers) {
        this.mContext = context;
        this.movieTrailers = movieTrailers;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return movieTrailers.size(); //returns the total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return movieTrailers.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateMoviesTrailersList(ArrayList<MovieTrailers> data) {
        movieTrailers.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MovieTrailers movieTrailers = (MovieTrailers) getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.activity_listview, parent, false);

            //get the textview for item name and item description
            TextView movieTrailesrName = (TextView) convertView.findViewById(R.id.TrailersArrayList);

            //sets the text for movie trailers name
            movieTrailesrName.setText(mContext.getResources().getString(R.string.trailers_titles, position + 1));

            ImageView playButton = (ImageView) convertView.findViewById(R.id.play_button);
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String url = "https://www.youtube.com/watch?v=" + movieTrailers.getId();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    mContext.startActivity(i);
                }
            });
        }
        return convertView;
    }
}
