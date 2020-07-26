package com.example.musicplayer1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.LinkedList;

public class PlaylistActivity extends AppCompatActivity {

    LinkedList<Playlist> playlists;
    ListView playlistLV;
    ImageButton create;

    Cursor mCursor;

    View.OnClickListener createListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           //TODO
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        playlistLV = findViewById(R.id.playlist_LV);
        create = findViewById(R.id.imageButton);
        create.setOnClickListener(createListner);
        playlists = new LinkedList<>();

        mCursor = getContentResolver().query(PlaylistsProvider.CONTENT_URI,new String[]{PlaylistsProvider.COLUMN_TITLE},null,null,null);
        CursorAdapter mCursorAdapter = new SimpleCursorAdapter(getApplicationContext(),R.layout.playlist_cursor_listview,
                mCursor,new String[]{PlaylistsProvider.COLUMN_TITLE}, new int[]{R.id.title_TV}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        playlistLV.setAdapter(mCursorAdapter);
    }
}