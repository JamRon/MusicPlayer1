package com.example.musicplayer1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;

public class PlaylistActivity extends AppCompatActivity {

    private static Context context;
    ArrayList<Playlist> playlists;
    ListView playlistLV;
    ImageButton create;
    Cursor mCursor;
    CursorAdapter mCursorAdapter;

    View.OnClickListener createListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PlaylistDialog f = new PlaylistDialog();
            f.show(getSupportFragmentManager(),"dialog");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        PlaylistActivity.context = getApplicationContext();

        playlistLV = findViewById(R.id.playlist_LV);
        create = findViewById(R.id.imageButton);
        create.setOnClickListener(createListner);
        loadData();
        if(!playlists.isEmpty()) {
            TextView test = new TextView(this);
            test.setText(playlists.get(0).getTitle());
            test.setPadding(50, 50, 50, 50);
        }

        mCursor = getContentResolver().query(PlaylistsProvider.CONTENT_URI,new String[]{PlaylistsProvider.COLUMN_TITLE},null,null,null);
        mCursorAdapter = new SimpleCursorAdapter(getApplicationContext(),R.layout.playlist_cursor_listview,
                mCursor,new String[]{PlaylistsProvider.COLUMN_TITLE}, new int[]{R.id.title_TV}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        playlistLV.setAdapter(mCursorAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCursorAdapter.notifyDataSetChanged();
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(playlists);
        editor.putString("playlists list", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("playlists list", null);
        Type type = new TypeToken<ArrayList<Playlist>>() {}.getType();
        playlists = gson.fromJson(json, type);
        if (playlists == null) {
            playlists = new ArrayList<>();
        }
    }
    public void addPlaylist(Playlist playlist){
        playlists.add(playlist);
    }

    public static Context getContextOfApplication(){
            return PlaylistActivity.context;
        }
    }