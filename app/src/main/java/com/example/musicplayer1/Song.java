package com.example.musicplayer1;

public class Song {
    private long id;
    private String type;
    private String title;
    private String artist;
    private String album;
    private String uri;
    private String songImage;


    // Songs on phone
    public Song(long songID, String songTitle, String songArtist,
                String songType, String songAlbum) {
        this.id=songID;
        this.title=songTitle;
        this.artist=songArtist;
        this.type = songType;
        this.album = songAlbum;
        this.uri = null;
        this.songImage = null;
    }

    // Spotify
    public Song(long songID, String songTitle, String songArtist,
                String songType, String songAlbum, String songUri, String songImage) {
        this.id=songID;
        this.title=songTitle;
        this.artist=songArtist;
        this.type = songType;
        this.album = songAlbum;
        this.uri = songUri;
        this.songImage = songImage;
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

    public String getAlbum() {
        return this.album;
    }

    public String getUri() {
        return this.uri;
    }

    public String getSongImage() {
        return this.songImage;
    }
}
