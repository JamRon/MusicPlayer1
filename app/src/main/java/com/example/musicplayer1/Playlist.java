package com.example.musicplayer1;

import java.util.ArrayList;
import java.util.LinkedList;

public class Playlist {

    private String title;
    private String type;
    private ArrayList<Song> songList;

    public Playlist(ArrayList<Song> songList, String title, String type){
        this.songList = songList;
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public ArrayList<Song> getSongList() {
        return songList;
    }
}
