package com.example.musicplayer1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.LinkedList;

public class AlbumActivity extends AppCompatActivity {
    LinkedList<Album> albums;
    ListView albumLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        albumLV = findViewById(R.id.albumLV);
        albums = new LinkedList<>(); //TODO: NEEDS CORRECT INITIALIZATION

        LinkedList<String> albumNames = new LinkedList<>();

        //retrieve song info
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor musicCursor = musicResolver.query(musicUri, new String[]{MediaStore.Audio.Media.ALBUM}, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int albumColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM);
            //add songs to list
            do {
                albumNames.add(musicCursor.getString(albumColumn));
            }
            while (musicCursor.moveToNext());
        }
        ArrayAdapter<String> albumAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, albumNames);
        albumLV.setAdapter(albumAdapter);
    }

}