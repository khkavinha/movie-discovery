package com.example.kh870h.moviediscovery;


import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kh870h.moviediscovery.MovieReviewsDisplay.MovieReviews;
import com.example.kh870h.moviediscovery.MovieReviewsDisplay.MovieReviewsAdapter;
import com.example.kh870h.moviediscovery.MovieReviewsDisplay.MovieReviewsAsyncTask;
import com.example.kh870h.moviediscovery.MovieTrailersDisplay.MovieTrailers;
import com.example.kh870h.moviediscovery.MovieTrailersDisplay.MovieTrailersAsyncLoader;
import com.example.kh870h.moviediscovery.MovieTrailersDisplay.MovieTrailersListAdapter;
import com.example.kh870h.moviediscovery.MovieTrailersDisplay.NonScrollListView;
import com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract;
import com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesDBHelper;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI;


/**
 * Created by kh870h on 11/25/2017.
 */

public class DetailActivity extends FragmentActivity implements
        LoaderManager.LoaderCallbacks {
    TextView mTextViewTitle;
    TextView mTextViewOverView;
    TextView mTextViewReleaseDate;
    TextView mTextViewRating;
    ImageView mTextViewPosterPath;
    Button mCheckmarkButton;
    Button mRemoveCheckmarkButton;
    private MovieReviewsAdapter movieReviewsAdapter;
    private MovieTrailersListAdapter movieTrailersListAdapter;
    private static final int LOADER1 = 1;
    private static final int LOADER2 = 2;
    public static int scrollX = 0;
    public static int scrollY = -1;
    NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        movieTrailersListAdapter = new MovieTrailersListAdapter(this, new ArrayList<MovieTrailers>());
        movieReviewsAdapter = new MovieReviewsAdapter(this, new ArrayList<MovieReviews>());
        mTextViewTitle = (TextView) findViewById(R.id.title_text);
        mTextViewOverView = (TextView) findViewById(R.id.overview_text);
        mTextViewRating = (TextView) findViewById(R.id.vote_average);
        mTextViewReleaseDate = (TextView) findViewById(R.id.release_date);
        mTextViewPosterPath = (ImageView) findViewById(R.id.image_thumbnail);
        mCheckmarkButton = (Button) findViewById(R.id.checkmark_button);
        NonScrollListView nonScrollListView = (NonScrollListView) findViewById(R.id.lv_nonscroll_list);
        ListView nonScrollListView1 = (ListView) findViewById(R.id.reviews_nonscroll_list);
        mRemoveCheckmarkButton = (Button) findViewById(R.id.remove_favorites);
        nestedScrollView = (NestedScrollView) findViewById(R.id.scrollView);

        //set the adapter
        nonScrollListView.setAdapter(movieTrailersListAdapter);
        nonScrollListView1.setAdapter(movieReviewsAdapter);

        //initialize the loader
        getSupportLoaderManager().initLoader(LOADER1, null, this).forceLoad();
        getSupportLoaderManager().initLoader(LOADER2, null, this).forceLoad();


        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            mTextViewTitle.setText(getIntent().getStringExtra("title"));
            mTextViewOverView.setText(getIntent().getStringExtra("overview"));
            String movieRatings = getIntent().getStringExtra("vote_average");

            //set overall rating in a scale of #/10
            Resources res = getResources();
            String movieOverallRatings = res.getString(R.string.movies_overall_rating, movieRatings);
            mTextViewRating.setText(movieOverallRatings);

            String movieReleaseDates = getIntent().getStringExtra("release_date");
            String[] movieReleaseDateSplit = movieReleaseDates.split("-");
            mTextViewReleaseDate.setText(movieReleaseDateSplit[0]);
            String imagePath = getIntent().getStringExtra("poster_path");
            String posterImage = "http://image.tmdb.org/t/p/w500/" + imagePath;
            Picasso.with(getBaseContext()).load(posterImage)
                    .into(mTextViewPosterPath);
        }

        mCheckmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData(movieId(), title(), release(), vote_average(), overview(), poster_path());
            }
        });

        mRemoveCheckmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(movieId());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        scrollX = nestedScrollView.getScrollX();
        scrollY = nestedScrollView.getScrollY();
    }

    @Override
    protected void onResume() {
        super.onResume();
        nestedScrollView.post(new Runnable() {
            @Override
            public void run() {
                nestedScrollView.scrollTo(scrollX, scrollY);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("SCROLL_POSITION",
                new int[]{nestedScrollView.getScrollX(), nestedScrollView.getScrollY()});
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] position = savedInstanceState.getIntArray("SCROLL_POSITION");
        if (position != null)
            nestedScrollView.post(new Runnable() {
                @Override
                public void run() {
                    nestedScrollView.scrollTo(position[0], position[1]);
                }
            });
    }


    public String poster_path() {
        String moviePosterPath = getIntent().getStringExtra("poster_path");
        return moviePosterPath;
    }

    public String overview() {
        String movieOverview = getIntent().getStringExtra("overview");
        return movieOverview;
    }

    public String vote_average() {
        String movievoteAverage = getIntent().getStringExtra("vote_average");
        return movievoteAverage;
    }

    public String release() {
        String movieRelease = getIntent().getStringExtra("release_date");
        return movieRelease;
    }

    public String title() {
        String movieTitle = getIntent().getStringExtra("title");
        return movieTitle;
    }

    public String movieId() {
        String movieId = getIntent().getStringExtra("id");
        return movieId;
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER1:
                return new MovieTrailersAsyncLoader(this, movieId());
            case LOADER2:
                return new MovieReviewsAsyncTask(this, movieId());
        }
        return null;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader loader, Object data) {
        int id = loader.getId();
        switch (id) {
            case LOADER1:
                movieTrailersListAdapter.updateMoviesTrailersList((ArrayList<MovieTrailers>) data);
                break;
            case LOADER2:
                movieReviewsAdapter.updateMovieReviewsList((ArrayList<MovieReviews>) data);
                break;
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader loader) {
        int id = loader.getId();
        switch (id) {
            case LOADER1:
                movieTrailersListAdapter.updateMoviesTrailersList(new ArrayList<MovieTrailers>());
                break;
            case LOADER2:
                movieReviewsAdapter.updateMovieReviewsList(new ArrayList<MovieReviews>());
                break;
        }
    }

    public void addData(String id, String title, String release, String average, String overview, String poster) {
        ContentValues contentValues = new ContentValues();
        //create a new map, where it grabs the json objects
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.KEY_ID, id);
        Log.d("Value ID to DB", id);
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.KEY_TITLE, title);
        Log.d("Value title to DB", title);
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.KEY_RELEASE, release);
        Log.d("Value release to DB", release);
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.KEY_AVERAGE, average);
        Log.d("Value average to DB", average);
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.KEY_OVERVIEW, overview);
        Log.d("Value overview to DB", overview);
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.KEY_POSTER_PATH, poster);
        Log.d("Value poster_path to DB", poster);
        //inserting the data into the database
        Uri uri = getContentResolver().insert(CONTENT_URI, contentValues);

        if (uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }
        finish();
    }

    public void deleteData(String id) {
        //delete the data into the database
        getContentResolver().delete(CONTENT_URI, "id=?", new String[]{id});
        finish();
    }
}

