package com.example.musicplayer1;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyArrayList extends ArrayList<Song> {
    private ArrayList<Song> arrayList = new ArrayList<>();
    private float size = arrayList.size();

    public boolean add(ArrayList<Song> songs, int pos){
        Song addSong = songs.get(pos);
        arrayList.add(addSong);
        return arrayList.add(addSong);
    }

    public ArrayList<Song> getArrayList() {
        return arrayList;
    }

    public float getSize() {
        return size;
    }
}
