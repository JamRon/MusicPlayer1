package com.example.musicplayer1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.LinkedList;

public class AlbumActivity extends AppCompatActivity {
    Album[] albums;
    ListView albumLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        albumLV = findViewById(R.id.albumLV);

        albums = new Album[10]; //TODO: NEEDS CORRECT INITIALIZATION
        LinkedList<String> playlistNames = new LinkedList<>();
        for(int i = 0; i < albums.length; i++){
            playlistNames.add(albums[i].getTitle());
        }
        ArrayAdapter<String> playlistAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, playlistNames);
        albumLV.setAdapter(playlistAdapter);
    }
}