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
import java.util.concurrent.TimeUnit;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp;
import com.spotify.android.appremote.api.error.NotLoggedInException;
import com.spotify.android.appremote.api.error.UserNotAuthorizedException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.client.PendingResult;
import com.spotify.protocol.client.Result;
import com.spotify.protocol.types.PlayerState;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;


public class SpotifyActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {

    private ArrayList<Song> songList;
    private ListView songView;

    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "a911f9e3de834eb6a0a1cf52848b360c";
    private static final String REDIRECT_URI = "http://MusicPLayer1.com://callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    private EditText searchText;
    private RequestQueue mRequest;
    private String  token = "";
    private Button get_request_Button;

    private int currentSong = 0;
    private MusicController controller;
    private boolean playing ;
    private long track_time;
    private long playback_time;




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
                        Log.d("SpotifyActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        if (error instanceof NotLoggedInException || error instanceof UserNotAuthorizedException) {
                            // Show login button and trigger the login flow from auth library when clicked
                            Log.d("SpotifyActivity", error.getMessage().toString());
                        } else if (error instanceof CouldNotFindSpotifyApp) {
                            // Show button to download Spotify
                            Log.d("SpotifyActivity", "You Need to downlaod Spotify!");
                        }
                        else {
                            Log.d("SpotifyActivity", error.getMessage());
                        }
                    }
                });

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify);

        songView = (ListView)findViewById(R.id.spotify_ListView);
        songList = new ArrayList<Song>();


        // Benji Testing out request from spotify
        get_request_Button = findViewById(R.id.spotify_Search_Button);
        searchText = findViewById(R.id.spotify_Search_TextView);
        setController();


        get_request_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGetRequest();
                controller.show(0);
            }
        });

        songView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song currrent_Song = songList.get(position);
                mSpotifyAppRemote.getPlayerApi().play(currrent_Song.getUri());
                currentSong = position;
                Toast.makeText(getApplicationContext(), currrent_Song.getUri(), Toast.LENGTH_SHORT).show();
                controller.show(0);
            }
        });

        songView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), "Long Press", Toast.LENGTH_SHORT).show();
                // inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_window, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });


                Button cancelButton = popupView.findViewById(R.id.add_2_playlist_cancel_button);
                Button addButton = popupView.findViewById(R.id.add_2_playlist_ok_button);
                Spinner playlistSpinner = popupView.findViewById(R.id.add_2_playlist_spinner);

                cancelButton.setOnClickListener(cancelListener);
                addButton.setOnClickListener(addListener);
                return true;
            }
        });
    }

    private void sendGetRequest() {
        mRequest = Volley.newRequestQueue(this);

        jsonParse();
    }

    private void jsonParse() {
        String input = searchText.getText().toString().replaceAll(" ", "%20");

        String url = "https://api.spotify.com/v1/search?q=" + input +"&type=track&market=US&limit=10&offset=0";
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
                                String image = track.getJSONObject("album").getJSONArray("images").getJSONObject(2).getString("url");


                                songList.add(new Song (0, title, artist, "spotify", album, uri, image));
                            }

                            SpotifyAdaptor songAdt = new SpotifyAdaptor(getApplicationContext(), android.R.layout.simple_list_item_1, songList);
                            songView.setAdapter(songAdt);
//                            SongAdapter songAdt = new SongAdapter(getApplicationContext(), songList);
//                            songView.setAdapter(songAdt);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("SpotifyActivity", Objects.requireNonNull(e.getMessage()));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SpotifyActivity", Objects.requireNonNull(error.getMessage()));
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

    View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "Pressed Cancel", Toast.LENGTH_SHORT).show();

        }
    };

    View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "Pressed Add", Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    public void start() {
        if (playback_time == 0)
            {mSpotifyAppRemote.getPlayerApi().play(songList.get(currentSong).getUri());}
        else
            {mSpotifyAppRemote.getPlayerApi().resume();}

        controller.show(0);
    }

    @Override
    public void pause() {
        mSpotifyAppRemote.getPlayerApi().pause();
        controller.show(0);
    }

    @Override
    public int getDuration() {

        mSpotifyAppRemote.getPlayerApi().getPlayerState()
                .setResultCallback(playerState -> {
                    // have fun with playerState
                    track_time = playerState.track.duration;
                })
                .setErrorCallback(throwable -> {
                    // =(
                    System.out.println(throwable.getMessage());
                });

        return (int) track_time;
    }

    @Override
    public int getCurrentPosition() {

        mSpotifyAppRemote.getPlayerApi().getPlayerState()
                .setResultCallback(playerState -> {
                    // have fun with playerState
                    playback_time = playerState.playbackPosition;
                })
                .setErrorCallback(throwable -> {
                    // =(
                    System.out.println(throwable.getMessage());
                });

        return (int) playback_time;
    }

    @Override
    public void seekTo(int pos) {
        mSpotifyAppRemote.getPlayerApi().seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        mSpotifyAppRemote.getPlayerApi().getPlayerState()
                .setResultCallback(playerState -> {
                    // have fun with playerState
                    playing = !playerState.isPaused;
                })
                .setErrorCallback(throwable -> {
                    // =(
                    System.out.println(throwable.getMessage());
                });

        return playing;
    }


    @Override
    public int getBufferPercentage() {
        return 100;
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

    private void setController(){
        //set the controller up
        controller = new MusicController(this);
        controller.setPrevNextListeners(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playNext();
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPrev();
                    }
                }
        );


        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.space));
        controller.setEnabled(true);

//        controller.set
    }

    //play next
    private void playNext(){

        if (currentSong == songList.size()-1){
            currentSong = 0;
            String nextSong = songList.get(currentSong).getUri();
            mSpotifyAppRemote.getPlayerApi().play(nextSong);
        } else {
            String nextSong = songList.get(++currentSong).getUri();
            mSpotifyAppRemote.getPlayerApi().play(nextSong);
        }

//        controller.show(0);
    }

    //play previous
    private void playPrev(){
        if (currentSong == 0){
            currentSong = songList.size()-1;
            String nextSong = songList.get(currentSong).getUri();
            mSpotifyAppRemote.getPlayerApi().play(nextSong);
        } else {
            String nextSong = songList.get(--currentSong).getUri();
            mSpotifyAppRemote.getPlayerApi().play(nextSong);
        }

//        controller.show(0);
    }

}

