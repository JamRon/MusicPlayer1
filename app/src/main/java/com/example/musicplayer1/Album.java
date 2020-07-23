package com.example.musicplayer1;

import java.util.LinkedList;

public class Album {

    private String title;
    private String artist;
    private String type;
    private LinkedList<Song> songList;

    public Album(LinkedList<Song> songList, String title, String artist,String type){
        this.songList = songList;
        this.title = title;
        this.artist = artist;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getType() {
        return type;
    }

    public LinkedList<Song> getSongList() {
        return songList;
    }
}
