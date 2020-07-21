package com.example.musicplayer1;

public class Song {
    private long id;
    private String type;
    private String title;
    private String artist;


    public Song(long songID, String songTitle, String songArtist, String songType) {
        this.id=songID;
        this.title=songTitle;
        this.artist=songArtist;
        this.type = songType;


    }

    public long getID(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public String getArtist(){
        return this.artist;
    }

    public String getType(){
        return this.type;
    }
}
