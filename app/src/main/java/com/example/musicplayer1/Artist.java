package com.example.musicplayer1;

import java.util.LinkedList;

public class Artist {
    private String name;
    private String type;
    private LinkedList<Song> songList;

    public Artist(LinkedList<Song> songList, String name,String type){
        this.songList = songList;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public LinkedList<Song> getSongList() {
        return songList;
    }
}
