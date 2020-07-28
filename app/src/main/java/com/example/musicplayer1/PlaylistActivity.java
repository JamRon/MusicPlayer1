package com.example.musicplayer1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class PlaylistActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {

    private static Context context;
    ArrayList<Playlist> playlists;
    ListView playlistLV;
    ImageButton create;
    ArrayAdapter<String>  playlistAdapter;

    ArrayList<Song> songList;
    ArrayList<String> playlistNames;
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    private MusicController controller;
    private boolean paused=false, playbackPaused=false;
    ArrayAdapter<String> menuAdapter;

    View.OnClickListener createListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PlaylistDialog f = new PlaylistDialog();
            f.show(getSupportFragmentManager(),"dialog");
        }
    };

    AdapterView.OnItemClickListener playlistListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant
                    return;
                }}
            String pName = playlistNames.get(position);
            songList = findPlaylist(pName).getSongList();

            Collections.sort(songList, new Comparator<Song>(){
                public int compare(Song a, Song b){
                    return a.getTitle().compareTo(b.getTitle());
                }
            });
            SongAdapter songAdt = new SongAdapter(PlaylistActivity.this, songList);
            playlistLV.setAdapter(songAdt);
            playlistLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    songPicked(view);
                }
            });
            setController();
        }
    }
    ;@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //menu item selected
        switch (item.getItemId()) {
            case R.id.action_shuffle:
                //shuffle
                musicSrv.setShuffle();
                break;
            case R.id.action_end:
                stopService(playIntent);
                musicSrv=null;
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        PlaylistActivity.context = getApplicationContext();

        playlistLV = findViewById(R.id.playlist_LV);
        create = findViewById(R.id.imageButton);
        create.setOnClickListener(createListner);
        loadData();
        playlistNames = new ArrayList<>();
        for (int i = 0; i < playlists.size(); i++){
            String curTitle = playlists.get(i).getTitle();
            playlistNames.add(curTitle);
        }
        playlistAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, playlistNames);
        playlistLV.setAdapter(playlistAdapter);
        playlistLV.setOnItemClickListener(playlistListener);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(paused){
            setController();
            paused=false;
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        paused=true;
    }

    @Override
    protected void onStop() {
        if (controller!= null)
            controller.hide();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv=null;
        if (musicConnection != null) {
            unbindService(musicConnection);
        }
        super.onDestroy();
    }

    public Playlist findPlaylist(String title){
        Playlist foundPlaylist = null;
        for (int i = 0; i < playlists.size(); i++){
            if(title.equals(playlists.get(i).getTitle())){
                foundPlaylist = playlists.get(i);
                break;
            }
        }
        return foundPlaylist;
    }
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            // musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };
    public void songPicked(View view){
        musicSrv.setList(songList);
        musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
        musicSrv.playSong();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }
    private void setController(){
        //set the controller up
        controller = new MusicController(this);
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.space1));
        controller.setEnabled(true);
    }
    private void playNext(){
        musicSrv.playNext();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }
    private void playPrev(){
        musicSrv.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    @Override
    public void start() {
        musicSrv.go();
    }

    @Override
    public void pause() {
        playbackPaused = true;
        musicSrv.pausePlayer();
    }
    @Override
    public int getDuration() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if(musicSrv!=null && musicBound)
            return musicSrv.isPng();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(playlists);
        editor.putString("playlists list", json);
        editor.apply();
    }
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("playlists list", null);
        Type type = new TypeToken<ArrayList<Playlist>>() {}.getType();
        playlists = gson.fromJson(json, type);
        if (playlists == null) {
            playlists = new ArrayList<>();
        }
    }
    public void addPlaylist(Playlist playlist){
        playlists.add(playlist);
    }

    public static Context getContextOfApplication(){
            return PlaylistActivity.context;
        }
    }