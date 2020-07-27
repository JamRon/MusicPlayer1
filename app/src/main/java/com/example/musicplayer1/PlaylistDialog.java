package com.example.musicplayer1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class PlaylistDialog extends DialogFragment {
    ArrayList<Song> songList;
    MyArrayList selectedItems;
    EditText playlistName;
    String playlistTitle;
    ListView songLV;

    DialogInterface.OnMultiChoiceClickListener songsListener = new DialogInterface.OnMultiChoiceClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            if (isChecked) {
                // If the user checked the item, add it to the selected items
                selectedItems.add(songList,which);
            } else if (selectedItems.contains(which)) {
                // Else, if the item is already in the array, remove it
                selectedItems.remove(Integer.valueOf(which));
            }

        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       // songList = getSongList();
        selectedItems = new MyArrayList();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        ArrayList<String> songsArray = new ArrayList<>();//getsongTitles();

        CharSequence[] cs = songsArray.toArray(new CharSequence[songsArray.size()]);
        CharSequence[] cs2 = {"www","meeman","monkey"};

        final View root = inflater.inflate(R.layout.fragment_playlist,null);
        playlistName = root.findViewById(R.id.playlistTitle);
        playlistTitle = playlistName.getText().toString();
        //songLV = root.findViewById(R.id.chooseSong);
        // ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,songsArray);
        //songLV.setAdapter(adapter);
            builder.setView(root)
                    .setMultiChoiceItems(cs2,null,songsListener)
                    .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            PlaylistActivity act = (PlaylistActivity) getActivity();
                            Playlist newPL = new Playlist(selectedItems,playlistTitle,"device");
                            act.addPlaylist(newPL);
                        }
                    });

        return builder.create();
    }

    public ArrayList<Song> getSongList() {
        //retrieve song info
        songList = new ArrayList<Song>();
        Context appContext = PlaylistActivity.getContextOfApplication();
        ContentResolver musicResolver = appContext.getContentResolver();
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

    public ArrayList<String> getsongTitles(){
        ArrayList<String> songTitles = new ArrayList<>();

        Context appContext = PlaylistActivity.getContextOfApplication();
        ContentResolver musicResolver = appContext.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            //add songs to list
            do {
                String thisTitle = musicCursor.getString(titleColumn);
                songTitles.add(thisTitle);
            }
            while (musicCursor.moveToNext());
        }

        return songTitles;
    }
}