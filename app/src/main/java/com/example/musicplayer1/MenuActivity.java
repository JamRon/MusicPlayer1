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
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    ListView menu;
    AdapterView.OnItemClickListener menuListener = new AdapterView.OnItemClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position == 3){
                Context context = getApplicationContext();
                Intent activityIntent = new Intent(context, MainActivity.class);
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                activityIntent.setIdentifier("Songs");
                context.startActivity(activityIntent);
            }else if(position == 2){
                Context context = getApplicationContext();
                Intent activityIntent = new Intent(context, MainActivity.class);
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                activityIntent.setIdentifier("Artists");
                context.startActivity(activityIntent);
            }else if(position == 1){
                Context context = getApplicationContext();
                Intent activityIntent = new Intent(context, MainActivity.class);
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                activityIntent.setIdentifier("Albums");
                context.startActivity(activityIntent);
            }else{
                Context context = getApplicationContext();
                Intent activityIntent = new Intent(context, MainActivity.class);
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                activityIntent.setIdentifier("Playlists");
                context.startActivity(activityIntent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        menu = findViewById(R.id.menu_ListView);
        ArrayList<String> menuItems = new ArrayList<>();
        menuItems.add("Playlists");
        menuItems.add("Album");
        menuItems.add("Artists");
        menuItems.add("Songs");

        ListAdapter menuAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, menuItems);
        menu.setAdapter(menuAdapter);
    }
}