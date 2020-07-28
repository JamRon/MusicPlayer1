package com.example.musicplayer1;

import androidx.annotation.RequiresApi;
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
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
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
    ArrayAdapter<String>  playlistAdapter;

    View.OnClickListener createListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PlaylistDialog f = new PlaylistDialog();
            f.show(getSupportFragmentManager(),"dialog");
        }
    };

    AdapterView.OnItemClickListener playlistListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent activityIntent = new Intent(PlaylistActivity.this, SongActivity.class);
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(activityIntent);
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
        ArrayList<String> playlistNames = new ArrayList<>();
        for (int i = 0; i < playlists.size(); i++){
            String curTitle = playlists.get(i).getTitle();
            playlistNames.add(curTitle);
        }
        playlistAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, playlistNames);
        playlistLV.setAdapter(playlistAdapter);
        playlistLV.setOnItemClickListener(playlistListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        playlistAdapter.notifyDataSetChanged();
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