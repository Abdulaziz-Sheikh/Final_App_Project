package Algonquin.CST2355.final_app_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import Algonquin.CST2355.final_app_project.databinding.ActivityDeezerSongBinding;
import Algonquin.CST2355.final_app_project.databinding.ArtistRowBinding;


/**
 * Deezer Song API
 */
public class DeezerSongActivity extends AppCompatActivity {


    /**
     * Activity Deezer Binding
     */
    ActivityDeezerSongBinding binding;

    /**
     * Recycler Adapter
     */
    RecyclerView.Adapter myAdapter = null;

    /**
     * Artist ArrayList
     */
    ArrayList<ArtistsDTO> artistNames = new ArrayList<>();

    /**
     * ArtistModel
     */
    FavoriteArtistViewModel artistModel;


    /**
     * Favorite Artists DAO
     */
    ArtistDAO fDAO;

    RequestQueue queue = null;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int option = item.getItemId();



        //If User selects home button
        if(option == R.id.homeBtn){


            //Alert Dialog is shown if the user wants to go home.
            AlertDialog.Builder builder = new AlertDialog.Builder(DeezerSongActivity.this);
            builder.setMessage("Would You like to exit this page?")
                            .setTitle("Leave Page")
                                    .setNegativeButton("No", (Dialog, Click) -> {} )
                                    .setPositiveButton("Yes", (Dialog, Click) -> {

                                        Toast.makeText(this, "Loading Home Button", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(this, MainActivity.class);
                                        startActivity(i);


                                    })
                                  .create().show();

//            Snackbar.make(binding.artistText, "", Snackbar.LENGTH_LONG).show();

            //If user selects Home options.
        } else if(option == R.id.favorites){
            Toast.makeText(this, "Favorites", Toast.LENGTH_LONG).show();

            if(artistNames == null){

                artistModel.artists.postValue(artistNames = new ArrayList<>());

                //Create Database
                FavoritesDatabase db = Room.databaseBuilder(getApplicationContext(), FavoritesDatabase.class, "ArtistsDAO").build();
                fDAO = db.DAO();


             //Get All Entries from database

            //Get All Artists
            Executor thread = Executors.newSingleThreadExecutor();

            thread.execute(() ->{

                List<ArtistsDTO> fromDatabase = fDAO.getAllArtist();
                artistNames.addAll(fromDatabase);

            });

            }
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(this);

        binding = ActivityDeezerSongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Create Toolbar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        //Retrieve the array list
        artistModel = new ViewModelProvider(this).get(FavoriteArtistViewModel.class);

        artistNames = artistModel.artists.getValue();


        //If Textview is Null
        if(artistNames == null){

            artistModel.artists.postValue(artistNames = new ArrayList<>());

            //Create Database
            FavoritesDatabase db = Room.databaseBuilder(getApplicationContext(), FavoritesDatabase.class, "ArtistsDAO").build();
            fDAO = db.DAO();


            //Get All Entries from database

            //Get All Artists
            Executor thread = Executors.newSingleThreadExecutor();

            thread.execute(() ->{

                List<ArtistsDTO> fromDatabase = fDAO.getAllArtist();
                artistNames.addAll(fromDatabase);

            });

        }


        //Load Fragment
        artistModel.selectedArtist.observe(this, (artistValue) -> {

            ArtistDetails artistFragment  = new ArtistDetails(artistValue);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentLocation, artistFragment)
                    .addToBackStack("")
                    .commit();

        });


        //Setting up a new adapter for the recycler view
        binding.artistRecyclerView.setAdapter(
                myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                ArtistRowBinding artistRowBinding = ArtistRowBinding.inflate(getLayoutInflater());
                return new MyRowHolder(artistRowBinding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {

                //Artist Object
                ArtistsDTO a = artistNames.get(position);

                //Set Image
                String image = a.getPictureUrl();
                Picasso.get().load(image)
                        .into(holder.artistPfp);

                //Set ID, Name, Tracks Link

                holder.artistID.setText("" + a.getId());
                holder.artistName.setText(a.getArtistName());
                holder.tracks.setText(a.getTracklist());

            }

            @Override
            public int getItemCount() {
                return artistNames.size();
            }


            @Override
            public int getItemViewType(int position) {
                //given the row, return an layout id for that row

                if(position < 3)
                    return 0;
                else
                    return 1;
            }
        });

        //Initialize Recycler View
        binding.artistRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        //Create Request
        queue = Volley.newRequestQueue(this);



        //Add Recent Search Using Shared Preferences
        SharedPreferences searchedArtist = getSharedPreferences("SearchHistoryData", Context.MODE_PRIVATE );
        String artistTyped = searchedArtist.getString("ArtistName", "");
        binding.searchField.setHint(artistTyped);



        //Search Button Action Listener
        binding.searchButton.setOnClickListener(click -> {


        //SharedPreferences
        SharedPreferences.Editor editor = searchedArtist.edit();
        editor.putString("ArtistName", binding.searchField.getText().toString());
        editor.apply();


            // Link to API
            String apiUrl = "https://api.deezer.com/search/artist/?q=" + binding.searchField.getText().toString();

            // Create a JsonObjectRequest
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,

                    //Response
                    response -> {

                        try {
                            // Check if there's Results
                            if (response.has("data")) {
                                JSONArray artistsArray = response.getJSONArray("data");

                                artistNames.clear();

                                for (int i = 0; i < artistsArray.length(); i++){
                                    JSONObject artist = artistsArray.getJSONObject(i);


                                    long ID = artist.getLong("id");
                                    String artistName = artist.getString("name");
                                    String tracklistUrl = artist.getString("tracklist");
                                    String pictureUrl = artist.getString("picture");


                                    ArtistsDTO artists = new ArtistsDTO(ID, artistName, tracklistUrl, pictureUrl);
                                    artistNames.add(artists);


                                }

                                //Notify Recycler View Adapter
                                myAdapter.notifyDataSetChanged();
                            } else {
                                // No results found
                                Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },

                    //Any Errors That will Occur
                    error -> {
                        Toast.makeText(DeezerSongActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    });

            // Add the request to the RequestQueue
            queue.add(request);

        });
    }




    class MyRowHolder extends RecyclerView.ViewHolder {

        public TextView artistID;
        public TextView artistName;

        public TextView tracks;

        public ImageView artistPfp;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            //Click on an Item
            itemView.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();
                ArtistsDTO selected = artistNames.get(position);


                Toast.makeText(DeezerSongActivity.this, "Clicked Test ", Toast.LENGTH_SHORT).show();


                //Setup Details Fragment
                artistModel.selectedArtist.postValue(selected);


                //Favorites will save by default when the app is loaded.
                AlertDialog.Builder builder = new AlertDialog.Builder(DeezerSongActivity.this);
                builder.setMessage("Would You like to save artists to favorite?")
                        .setTitle("Save Artist")
                        .setNegativeButton("No", (Dialog, Click) -> {} )
                        .setPositiveButton("Yes", (Dialog, Click) -> {


                            Toast.makeText(DeezerSongActivity.this, "Saving Artist", Toast.LENGTH_SHORT).show();




                            Executor addThread = Executors.newSingleThreadExecutor();
                                addThread.execute(() -> {
                                    fDAO.insertArtist(selected);
                                });

                             //Delete Artist From Fravorites.
                            Snackbar.make(artistName, "Undo", Snackbar.LENGTH_SHORT)
                                            .setAction("Undo", clk -> {

                                                Executor deleteThread = Executors.newSingleThreadExecutor();
                                                    deleteThread.execute(() -> {
                                                        fDAO.deleteArtist(selected);
                                                    });


                                            }).show();


                        })
                        .create().show();


            });

            //like onCreate above
            artistID = itemView.findViewById(R.id.artistID);
            artistName = itemView.findViewById(R.id.artistName);
            tracks = itemView.findViewById(R.id.tracklist);
            artistPfp = itemView.findViewById(R.id.artistPic);

        }
    }
}