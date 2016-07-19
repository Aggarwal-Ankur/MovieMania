package com.ankuraggarwal.moviemania.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Ankur on 19-Jul-16.
 */

public class FavoritesProvider extends ContentProvider {

    private static final UriMatcher URI_MATCHER;
    private FavoritesDbHelper mDbHelper;

    private static final int FAVORITE_MOVIE = 1;

    static  {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(FavoritesContract.AUTHORITY, "favorite_movie", FAVORITE_MOVIE);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new FavoritesDbHelper(  getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = URI_MATCHER.match(uri);

        switch (match) {
            case FAVORITE_MOVIE:
                return FavoritesContract.FavoriteMovies.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
