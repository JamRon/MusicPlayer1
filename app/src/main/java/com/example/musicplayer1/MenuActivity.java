package com.example.musicplayer1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedList;



public class MenuActivity extends AppCompatActivity {
    ListView menu;


    AdapterView.OnItemClickListener menuListener = new AdapterView.OnItemClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0){
                Intent activityIntent = new Intent(MenuActivity.this, SongActivity.class);
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//                activityIntent.setIdentifier("Playlists");
                startActivity(activityIntent);
            }else if(position == 1){
                Intent activityIntent = new Intent(MenuActivity.this, SongActivity.class);
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//                activityIntent.setIdentifier("Albums");
                startActivity(activityIntent);
            }else if(position == 2){
                Intent activityIntent = new Intent(MenuActivity.this, SongActivity.class);
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//                activityIntent.setIdentifier("Artists");
                startActivity(activityIntent);
            }
            else if(position == 3){
                Intent activityIntent = new Intent(MenuActivity.this, SongActivity.class);
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//                activityIntent.setIdentifier("Songs");
                startActivity(activityIntent);
            }else if(position == 4){
                Intent activityIntent = new Intent(MenuActivity.this, SpotifyActivity.class);
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//                activityIntent.setIdentifier("Search");
                startActivity(activityIntent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        menu = findViewById(R.id.menu_ListView);
        LinkedList<String> menuItems = new LinkedList<>();
        menuItems.add("Playlists");
        menuItems.add("Album");
        menuItems.add("Artists");
        menuItems.add("Songs");
        menuItems.add("Search");

        ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, menuItems);

        menu.setOnItemClickListener(menuListener);
        menu.setAdapter(menuAdapter);
    }
}