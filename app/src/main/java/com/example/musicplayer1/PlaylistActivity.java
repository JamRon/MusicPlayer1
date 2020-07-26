package com.example.musicplayer1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

public class PlaylistActivity extends AppCompatActivity {

    LinkedList<Playlist> playlists;
    ListView playlistLV;
    ImageButton create;

    Cursor mCursor;
    ArrayList <Integer> selectedItems;

    View.OnClickListener createListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder
                    = new AlertDialog
                    .Builder(PlaylistActivity.this);

            builder.setTitle("Please Enter ");
            EditText pName = new EditText(PlaylistActivity.this);
            builder.setView(pName);

            builder.setCancelable(true);
            ContentResolver musicResolver = getContentResolver();
            Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

            Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
            builder.setPositiveButton("Continue",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlertDialog.Builder builder
                            = new AlertDialog
                            .Builder(PlaylistActivity.this);
                    builder.setTitle("Please Select the Songs")
                            .setMultiChoiceItems(musicCursor, null, android.provider.MediaStore.Audio.Media.TITLE, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    if (isChecked) {
                                        // If the user checked the item, add it to the selected items
                                        selectedItems.add(which);
                                    } else if (selectedItems.contains(which)) {
                                        // Else, if the item is already in the array, remove it
                                        selectedItems.remove(Integer.valueOf(which));
                                    }
                                }
                            });
                    builder.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alertDialog1 = builder.create();
                    alertDialog1.show();
                }
            });

            // Create the Alert dialog
            AlertDialog alertDialog = builder.create();

            // Show the Alert Dialog box
            alertDialog.show();
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

        public ArrayList getSongList() {
            //retrieve song info
            ArrayList<Song> songList = new ArrayList<Song>();
            ContentResolver musicResolver = getContentResolver();
            Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

            Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

            if(musicCursor!=null && musicCursor.moveToFirst()){
                //get columns
                int titleColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.TITLE);
                int idColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media._ID);
                int artistColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.ARTIST);
                int albumColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.ALBUM);
                //add songs to list
                do {
                    long thisId = musicCursor.getLong(idColumn);
                    String thisTitle = musicCursor.getString(titleColumn);
                    String thisArtist = musicCursor.getString(artistColumn);
                    String thisAlbum = musicCursor.getString(albumColumn);
                    songList.add(new Song(thisId, thisTitle, thisArtist, "onPrem", thisAlbum));
                }
                while (musicCursor.moveToNext());
            }
            return songList;
        }

    }