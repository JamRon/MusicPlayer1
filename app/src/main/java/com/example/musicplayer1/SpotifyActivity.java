package com.example.musicplayer1;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp;
import com.spotify.android.appremote.api.error.NotLoggedInException;
import com.spotify.android.appremote.api.error.UserNotAuthorizedException;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;


public class SpotifyActivity extends AppCompatActivity {

    private ArrayList<Song> songList;
    private ListView songView;

    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "a911f9e3de834eb6a0a1cf52848b360c";
    private static final String REDIRECT_URI = "http://MusicPLayer1.com://callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    private EditText searchText;
    private RequestQueue mRequest;
    private TextView searchResult;
    private String  token = "";
    private Button get_request_Button;




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    token = response.getAccessToken();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();


        // Set the connection parameters
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"streaming"});
        AuthorizationRequest request = builder.build();

        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);

        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        connected();
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        if (error instanceof NotLoggedInException || error instanceof UserNotAuthorizedException) {
                            // Show login button and trigger the login flow from auth library when clicked
                            Log.d("MainActivity", error.getMessage().toString());
                        } else if (error instanceof CouldNotFindSpotifyApp) {
                            // Show button to download Spotify
                            Log.d("MainActivity", "You Need to downlaod Spotify!");
                        }
                        else {
                            Log.d("MainActivity", error.getMessage());
                        }
                    }
                });
    }



    private void connected() {
        mSpotifyAppRemote.getConnectApi().connectSwitchToLocalDevice();
//        mSpotifyAppRemote.getPlayerApi().play("spotify:track:6ztvsy3C6hPjVg9j4x1XKJ");
        Log.e("MainActivity", "WORKS");
    }
    //    spotify-auth-release-1.2.3
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify);

        songView = (ListView)findViewById(R.id.spotify_ListView);
        songList = new ArrayList<Song>();


        // Benji Testing out request from spotify
        get_request_Button = findViewById(R.id.spotify_Search_Button);
        searchText = findViewById(R.id.spotify_Search_TextView);

        get_request_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGetRequest();
            }
        });

        songView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song currrent_Song = songList.get(position);
                mSpotifyAppRemote.getPlayerApi().play(currrent_Song.getUri());
                Toast.makeText(getApplicationContext(), currrent_Song.getUri(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendGetRequest() {
        mRequest = Volley.newRequestQueue(this);

        jsonParse();
    }

    private void jsonParse() {
        String input = searchText.getText().toString().replaceAll(" ", "%20");

        String url = "https://api.spotify.com/v1/search?q=" + input +"&type=track&market=US&limit=5&offset=0";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonTracks = response.getJSONObject("tracks");
                            JSONArray jsonTracksArray = jsonTracks.getJSONArray("items");
                            songList.clear();
                            for (int i = 0; i < jsonTracksArray.length(); ++i) {
                                JSONObject track = jsonTracksArray.getJSONObject(i);
                                String title = track.getString("name");
                                String uri = track.getString("uri");
                                String album = track.getJSONObject("album").getString("name");
                                String artist = track.getJSONArray("artists").getJSONObject(0).getString("name");


                                songList.add(new Song (0, title, artist, "spotify", album, uri));
                            }
                            SongAdapter songAdt = new SongAdapter(getApplicationContext(), songList);
                            songView.setAdapter(songAdt);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("MainActivity", Objects.requireNonNull(e.getMessage()));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MainActivity", Objects.requireNonNull(error.getMessage()));
            }

        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        mRequest.add(request);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }


}

