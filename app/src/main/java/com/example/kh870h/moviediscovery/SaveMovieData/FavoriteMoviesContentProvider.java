package com.example.kh870h.moviediscovery.SaveMovieData;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.FAVORITE_MOVIE_DB_TABLE_NAME;
import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.KEY_AVERAGE;
import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.KEY_ID;
import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.KEY_OVERVIEW;
import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.KEY_POSTER_PATH;
import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.KEY_RELEASE;
import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.KEY_TITLE;
import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.PATH_FAVORITE_MOVIE_DB;

/**
 * Created by kavin on 1/30/2018.
 */

public class FavoriteMoviesContentProvider extends ContentProvider {

    //member variable to initialized onCreate
    private FavoriteMoviesDBHelper mFavoriteMoviesDBHelper;

    public static final int TASKS = 100;
    public static final int TASK_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, PATH_FAVORITE_MOVIE_DB, TASKS);
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, PATH_FAVORITE_MOVIE_DB + "/#", TASK_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavoriteMoviesDBHelper = new FavoriteMoviesDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mFavoriteMoviesDBHelper.getReadableDatabase();

        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {


            case TASK_WITH_ID: {
                String normalizedFavoriteMoviesString = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{normalizedFavoriteMoviesString};

                retCursor = mFavoriteMoviesDBHelper.getReadableDatabase().query(
                        FAVORITE_MOVIE_DB_TABLE_NAME,
                        null,
                        KEY_ID + " = ? " +
                                FavoriteMoviesContract.FavoriteMoviesEntry.KEY_TITLE + " = ? " +
                                FavoriteMoviesContract.FavoriteMoviesEntry.KEY_RELEASE + " = ? " +
                                FavoriteMoviesContract.FavoriteMoviesEntry.KEY_AVERAGE + " = ? " +
                                FavoriteMoviesContract.FavoriteMoviesEntry.KEY_OVERVIEW + " = ? " +
                                FavoriteMoviesContract.FavoriteMoviesEntry.KEY_POSTER_PATH + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case TASKS: {
                retCursor = db.query(
                        PATH_FAVORITE_MOVIE_DB,                 //table query
                        projection,                             //the columns to return
                        selection,                              //the columns for the WHERE clause
                        selectionArgs,                          //the values for the WHERE clause
                        null,                           //don't group rows
                        null,                            //don't filter by row groups
                        sortOrder);                             //the sort order

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        //get access to the Favorite Movie database
        final SQLiteDatabase db = mFavoriteMoviesDBHelper.getWritableDatabase();

        //uri matching code to identify the match for tasks directory
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TASKS:
                long id = db.insertWithOnConflict(FAVORITE_MOVIE_DB_TABLE_NAME, null, values,
                        SQLiteDatabase.CONFLICT_REPLACE);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mFavoriteMoviesDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int taskDeleted;

        //delete the single row
        switch (match) {
            case TASKS:
                //String id = uri.getPathSegments().get(0);
                taskDeleted = db.delete(FAVORITE_MOVIE_DB_TABLE_NAME, "id=?", selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (taskDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return taskDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
