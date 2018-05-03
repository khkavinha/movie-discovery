package com.example.kh870h.moviediscovery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI;
import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.KEY_AVERAGE;
import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.KEY_ID;
import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.KEY_OVERVIEW;
import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.KEY_POSTER_PATH;
import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.KEY_RELEASE;
import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.KEY_TITLE;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    GridView moviesList;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static MovieAdapter movieAdapter;
    public String POPULAR_MOVIE = "http://api.themoviedb.org/3/movie/popular?api_key=a123d2e1c301088dc5d18b1d0297bf74";
    public String TOP_RATED_MOVIE = "http://api.themoviedb.org/3/movie/top_rated?api_key=a123d2e1c301088dc5d18b1d0297bf74";
    public String FAVORITE_MOVIES;
    FavoriteMoviesAdapter favoriteMoviesAdapter;
    private static final int CONTACT_LOADER_ID = 78;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moviesList = (GridView) findViewById(R.id.grid_view);

        final Cursor c = managedQuery(CONTENT_URI, null, null, null, null);

        //construct a custom adapter to attach it to our gridview
        movieAdapter = new MovieAdapter(MainActivity.this, R.layout.activity_gridview, new ArrayList<MovieItem>());
        if (savedInstanceState != null) {
            final int gridViewPosition = savedInstanceState.getInt("SAVED_LAYOUT_MANAGER");
            moviesList.post(new Runnable() {
                @Override
                public void run() {
                    moviesList.setSelection(gridViewPosition);
                }
            });
        }

        //initialize the adapter
        moviesList.setAdapter(movieAdapter);
        moviesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                c.moveToPosition(position);
                String ids = c.getString(c.getColumnIndexOrThrow("id"));
                String title = c.getString(c.getColumnIndexOrThrow("title"));
                String release_date = c.getString(c.getColumnIndexOrThrow("release_date"));
                String vote_average = c.getString(c.getColumnIndexOrThrow("vote_average"));
                String overview = c.getString(c.getColumnIndexOrThrow("overview"));
                String poster_path = c.getString(c.getColumnIndexOrThrow("poster_path"));
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                intent.putExtra("id", ids);
                intent.putExtra("title", title);
                intent.putExtra("release_date", release_date);
                intent.putExtra("vote_average", vote_average);
                intent.putExtra("overview", overview);
                intent.putExtra("poster_path", poster_path);
                startActivity(intent);
            }
        });

        //initialize the loader with a special ID and defined callbacks
        getSupportLoaderManager().initLoader(CONTACT_LOADER_ID, new Bundle(), favoriteMoviesLoader);

        favoriteMoviesAdapter = new FavoriteMoviesAdapter(this, null, new ArrayList<MovieItem>());

        SharedPreferences sharedPreferences = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        String sortOrder = sharedPreferences.getString("sort_order", null);
        FAVORITE_MOVIES = "favorite";

        if (sortOrder != null && sortOrder.equalsIgnoreCase(FAVORITE_MOVIES)) {
            moviesList.setAdapter(favoriteMoviesAdapter);
        } else if (sortOrder != null && sortOrder.equalsIgnoreCase(TOP_RATED_MOVIE)) {
            new movieData().execute(TOP_RATED_MOVIE);
        } else {
            new movieData().execute(POPULAR_MOVIE);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        int index = moviesList.getFirstVisiblePosition();
        savedInstanceState.putInt("SAVED_LAYOUT_MANAGER", index);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        Context context = MainActivity.this;

        SharedPreferences sharedPreferences = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch (itemThatWasClickedId) {
            case R.id.action_search:
                if (isOnline()) {
                    Toast.makeText(context, "Top Rated Movies is Clicked", Toast.LENGTH_LONG).show();
                    new movieData().execute(TOP_RATED_MOVIE);
                    editor.putString("sort_order", TOP_RATED_MOVIE);
                    editor.apply();
                    moviesList.setAdapter(movieAdapter);
                }
                break;
            case R.id.popular_movies:
                if (isOnline()) {
                    Toast.makeText(context, "Popular Movies is Clicked", Toast.LENGTH_LONG).show();
                    new movieData().execute(POPULAR_MOVIE);
                    editor.putString("sort_order", POPULAR_MOVIE);
                    editor.apply();
                    moviesList.setAdapter(movieAdapter);
                }
                break;
            case R.id.favorite_movies_selections:
                //set the favorite movies that is marked
                Toast.makeText(context, "Favorite Movies is Clicked", Toast.LENGTH_LONG).show();
                FAVORITE_MOVIES = "favorite";
                editor.putString("sort_order", FAVORITE_MOVIES);
                editor.apply();
                moviesList.setAdapter(favoriteMoviesAdapter);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private LoaderManager.LoaderCallbacks<Cursor> favoriteMoviesLoader =
            new LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    String[] favoriteMoviesFields = new String[]{
                            "id as _id",
                            KEY_TITLE,
                            KEY_RELEASE,
                            KEY_AVERAGE,
                            KEY_OVERVIEW,
                            KEY_POSTER_PATH
                    };
                    return new CursorLoader(MainActivity.this,
                            CONTENT_URI,
                            favoriteMoviesFields,
                            null,
                            null,
                            null
                    );
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                    favoriteMoviesAdapter.changeCursor(data);
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    favoriteMoviesAdapter.changeCursor(null);
                }
            };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mTaskData = null;

            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    deliverResult(mTaskData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    String[] favoriteMoviesFields = new String[]{
                            KEY_ID,
                            KEY_TITLE,
                            KEY_RELEASE,
                            KEY_AVERAGE,
                            KEY_OVERVIEW,
                            KEY_POSTER_PATH
                    };

                    return getContentResolver().query(CONTENT_URI,
                            favoriteMoviesFields,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // The swapCursor() method assigns the new Cursor to the adapter
        favoriteMoviesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Clear the Cursor we were using with another call to the swapCursor()
        favoriteMoviesAdapter.swapCursor(null);
    }


    private class movieData extends AsyncTask<String, Void, ArrayList<MovieItem>> {
        private final String LOG_TAG = movieData.class.getSimpleName();


        @Override
        protected ArrayList doInBackground(String... urls) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieInformation = null;
            String urls1 = urls[0];

            try {
                //Create a URL object holding our url
                URL url = new URL(urls1);

                //Create a connection to open connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    //do nothing
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    //Stream was empty. No point in parsing.
                    return null;
                }
                movieInformation = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                movieInformation = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getMovieJsonObject(movieInformation);
            } catch (JSONException j) {
                Log.e(LOG_TAG, "JSON Error", j);
            }
            return null;
        }


        private ArrayList<MovieItem> getMovieJsonObject(String movieJSONObject) throws JSONException {

            final String m_Result = "results";
            final String m_ID = "id";
            final String m_Title = "title";
            final String m_Release = "release_date";
            final String m_Vote = "vote_average";
            final String m_Overview = "overview";
            final String m_Poster = "poster_path";

            JSONObject movieJSON = new JSONObject(movieJSONObject);
            JSONArray movieInformation = movieJSON.getJSONArray(m_Result);
            ArrayList<MovieItem> movieItems = new ArrayList<>();

            for (int i = 0; i < movieInformation.length(); i++) {
                JSONObject result_movie = movieInformation.getJSONObject(i);
                MovieItem item = new MovieItem(result_movie.getString(m_ID),
                        result_movie.getString(m_Title),
                        result_movie.getString(m_Poster),
                        result_movie.getString(m_Overview),
                        result_movie.getString(m_Vote),
                        result_movie.getString(m_Release),
                        "url"
                );

                Log.d("result movies in main a", "result movie data: " + result_movie);

                for (MovieItem result : movieItems) {
                    Log.v(LOG_TAG, " preparing data " + result.getPosterPath() + " " + result.getOverView());
                }
                movieItems.add(item);
            }
            return movieItems;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieItem> movieItems) {
            //this method will be running on UI thread
            movieAdapter.updateMovies(movieItems);
            movieAdapter.notifyDataSetChanged();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}