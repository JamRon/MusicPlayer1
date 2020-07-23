package com.example.musicplayer1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.LinkedList;

public class PlaylistActivity extends AppCompatActivity {

    Playlist[] playlists;
    ListView playlistLV;
    ImageButton create;

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
        playlists = new Playlist[10]; //TODO: NEEDS CORRECT INITIALIZATION
        LinkedList<String> playlistNames = new LinkedList<>();
        for(int i = 0; i < playlists.length; i++){
            playlistNames.add(playlists[i].getTitle());
        }
        ArrayAdapter<String> playlistAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, playlistNames);
        playlistLV.setAdapter(playlistAdapter);
    }
}