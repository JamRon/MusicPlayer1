package com.example.musicplayer1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

        artistLV = findViewById(R.id.artsitsLV);

        artists = new Artist[10]; //TODO: NEEDS CORRECT INITIALIZATION
        LinkedList<String> artistNames = new LinkedList<>();

        //retrieve song info
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor musicCursor = musicResolver.query(musicUri, new String[]{MediaStore.Audio.Media.ARTIST}, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                artistNames.add(musicCursor.getString(artistColumn));
            }
            while (musicCursor.moveToNext());
        }
        ArrayAdapter<String> artistAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, artistNames);
        artistLV.setAdapter(artistAdapter);
    }
}