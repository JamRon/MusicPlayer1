package com.example.musicplayer1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedList;

public class ArtistsActivity extends AppCompatActivity {
    Artist[] artists;
    ListView artistLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists);

        artistLV = findViewById(R.id.albumLV);

        artists = new Artist[10]; //TODO: NEEDS CORRECT INITIALIZATION
        LinkedList<String> playlistNames = new LinkedList<>();
        for(int i = 0; i < artists.length; i++){
            playlistNames.add(artists[i].getName());
        }
        ArrayAdapter<String> playlistAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, playlistNames);
        artistLV.setAdapter(playlistAdapter);
    }
}