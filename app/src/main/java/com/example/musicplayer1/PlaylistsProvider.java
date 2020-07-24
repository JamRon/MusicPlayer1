package com.example.musicplayer1;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class PlaylistsProvider extends ContentProvider {

    public final static String DBNAME = "PlaylistsDatabase";

    protected static final class MainDatabaseHelper extends SQLiteOpenHelper {
        MainDatabaseHelper(Context context) {
            super(context, DBNAME, null, 0);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_MAIN);
        }
        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        }
    };

    public final static String TABLE_PLAYLISTSTABLE = "Playlist";
    public final static String COLUMN_TITLE = "title";
    public final static String COLUMN_TYPE = "type";
    public final static String COLUMN_SONGS = "songs";

    public static final String AUTHORITY = "com.musicplayer.provider";
    public static final Uri CONTENT_URI = Uri.parse(
            "content://" + AUTHORITY +"/" + TABLE_PLAYLISTSTABLE);

    private MainDatabaseHelper mOpenHelper;

    private static final String SQL_CREATE_MAIN = "CREATE TABLE " +
            TABLE_PLAYLISTSTABLE +  // Table's name
            "(" +               // The columns in the table
            " _ID INTEGER PRIMARY KEY, " +
            COLUMN_TITLE +
            " TEXT," +
            COLUMN_TYPE +
            " TEXT," +
            COLUMN_SONGS +
            " TEXT)";

    public PlaylistsProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        return mOpenHelper.getWritableDatabase().delete(TABLE_PLAYLISTSTABLE,selection,selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = mOpenHelper.getWritableDatabase().insert(TABLE_PLAYLISTSTABLE,null,values);
        return Uri.withAppendedPath(CONTENT_URI, "" + id);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MainDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        return db.query(TABLE_PLAYLISTSTABLE,projection,selection,selectionArgs,null,null,sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return mOpenHelper.getWritableDatabase().update(TABLE_PLAYLISTSTABLE,values,selection,selectionArgs);
    }
}
