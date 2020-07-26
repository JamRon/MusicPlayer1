package com.example.musicplayer1;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

public class SpotifyAdaptor extends ArrayAdapter<Song> {
    private ArrayList<Song> songs;
    private LayoutInflater songInf;

//    public SpotifyAdaptor(Context c, ArrayList<Song> theSongs){
//        super();
//        songs=theSongs;
//        songInf=LayoutInflater.from(c);
//    }

    public SpotifyAdaptor(@NonNull Context context, int resource, @NonNull ArrayList<Song> theSongs) {
        super(context, resource, theSongs);
        songs=theSongs;
        songInf=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Song getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        @SuppressLint("ViewHolder") LinearLayout songLay = (LinearLayout)songInf.inflate
                (R.layout.spotify_song, parent, false);
        //get title and artist views
        TextView songView = (TextView)songLay.findViewById(R.id.song_title);
        TextView artistView = (TextView)songLay.findViewById(R.id.song_artist);
        ImageView albumeView =  (ImageView)songLay.findViewById(R.id.album_image);
        //get song using position
        Song currSong = songs.get(position);
        //get title and artist strings
        songView.setText(currSong.getTitle());
        artistView.setText(currSong.getArtist());
        Picasso.get().load(currSong.getSongImage()).into(albumeView);
        //set position as tag
        songLay.setTag(position);
        return songLay;
    }

}
