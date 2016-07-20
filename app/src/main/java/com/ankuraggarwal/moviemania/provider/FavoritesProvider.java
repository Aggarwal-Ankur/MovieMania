package com.ankuraggarwal.moviemania.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (URI_MATCHER.match(uri)) {
            case FAVORITE_MOVIE: {
                cursor = mDbHelper.getReadableDatabase().query(
                        FavoritesContract.FavoriteMovies.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
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
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri returnUri;

        switch (URI_MATCHER.match(uri)) {
            case FAVORITE_MOVIE: {
                long _id = db.insert(FavoritesContract.FavoriteMovies.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = getUriForId(_id, uri);
                }else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        return returnUri;
    }

    private Uri getUriForId(long id, Uri uri) {
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(itemUri, null);

            return itemUri;
        }else{
            throw new SQLException("Problem while inserting into uri: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int deletedRowCount;

        switch (URI_MATCHER.match(uri)) {
            case FAVORITE_MOVIE:
                deletedRowCount = db.delete(FavoritesContract.FavoriteMovies.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        if (deletedRowCount != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedRowCount;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int updatedRowCount;

        switch (URI_MATCHER.match(uri)) {
            case FAVORITE_MOVIE:
                updatedRowCount = db.update(FavoritesContract.FavoriteMovies.TABLE_NAME,
                        contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        if (updatedRowCount != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updatedRowCount;
    }
}
