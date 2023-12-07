package Algonquin.CST2355.final_app_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import Algonquin.CST2355.final_app_project.databinding.ActivityDeezerSongBinding;
import Algonquin.CST2355.final_app_project.databinding.ArtistRowBinding;
import Algonquin.CST2355.final_app_project.databinding.SentRowBinding;


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


    //Variables for Volley
    protected String nameOfArtist;
    RequestQueue queue = null;






    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int option = item.getItemId();

        if(option == R.id.homeBtn){
//            Intent i = new Intent(this, MainActivity.class);
//            startActivity(i);
            Toast.makeText(this, "Loading Home Button", Toast.LENGTH_SHORT).show();

//            Snackbar.make(binding.artistText, "", Snackbar.LENGTH_LONG).show();
        } else if(option == R.id.cookingRecipe){
            Toast.makeText(this, "Loading Cooking Recipe", Toast.LENGTH_SHORT).show();

        } else if(option == R.id.sunriselookup){
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();


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

//        TextView textview = findViewById(R.id.searchField);
//        Button enterBtn = findViewById(R.id.enterBtn);

        binding = ActivityDeezerSongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Create Toolbar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);








        //Retrieve the array list
        artistModel = new ViewModelProvider(this).get(FavoriteArtistViewModel.class);

        artistNames = artistModel.artists.getValue();


        if(artistNames == null){
            artistModel.artists.postValue(artistNames = new ArrayList<>());

            FavoritesDatabase db = Room.databaseBuilder(getApplicationContext(), FavoritesDatabase.class, "ArtistsDAO").build();
            fDAO = db.DAO();


            //Get All Entries from database

            Executor thread = Executors.newSingleThreadExecutor();

            thread.execute(() ->{

                List<ArtistsDTO> fromDatabase = fDAO.getAllArtist();
                artistNames.addAll(fromDatabase);

            });

        }








        /**
         * Display the artist that's searched for
         *
         * Use sharedpreferences for search history
         *
         * Retrive Data using JSON (Volley)
         *
         * Use A Help menu
         *
         * Activity Must be accessable by selecting a graphical Icon, like selecting tabs.
         * - Cooking Recipe: Food png
         * - Deezer Song: Musical note
         * - Sunrise sunset look up: Sun/moon logo
         *
         *
         * Second Language (Easy just do it through strings-fr.xml)
         */

        //Working Method For Shared Preferences [Copy This Exact Code]
//        SharedPreferences searchedArtist = getSharedPreferences("SearchHistoryData", Context.MODE_PRIVATE );
//        String artistTyped = searchedArtist.getString("ArtistName", "");
//        binding.searchField.setHint(artistTyped);

        //Add Artist To database Test
//        binding.addArtist.setOnClickListener(click -> {
//            Toast.makeText(this, "Add Test", Toast.LENGTH_SHORT).show();
            //Working Method For Shared Preferences [Copy This Exact Code]
//            SharedPreferences.Editor editor = searchedArtist.edit();
//            editor.putString("ArtistName", binding.searchField.getText().toString());
//            editor.apply();


//            Toast.makeText(this, "Adding Artist to Database!", Toast.LENGTH_SHORT).show();
//
//            String newlyAdded = binding.searchField.getText().toString();
//            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
//            String currentDateandTime = sdf.format(new Date());
//            FavoriteArtists artistsName = new FavoriteArtists(newlyAdded, currentDateandTime, true);
//            artistNames.add(artistsName);
//
//            //Clear Previous text
//            binding.searchField.setText("");
//
//            myAdapter.notifyDataSetChanged();
//
//            Executor thread1 = Executors.newSingleThreadExecutor();
//            thread1.execute(( ) -> {
//                //this is on a background thread
//                artistsName.id = fDAO.insertArtist(artistsName); //get the ID from the database
//
//            });

//        });



        //Setting up a new adapter for the recycler view
        binding.artistRecyclerView.setAdapter(
                myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//                SentRowBinding songBinding = SentRowBinding.inflate(getLayoutInflater());
//                return new MyRowHolder(songBinding.getRoot());
                ArtistRowBinding artistRowBinding = ArtistRowBinding.inflate(getLayoutInflater());
                return new MyRowHolder(artistRowBinding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {

                ArtistsDTO a = artistNames.get(position);

                String image = a.getPictureUrl();
                Picasso.get().load(image)
                        .error(R.drawable.androidbackground)
                        .into(holder.artistPfp);


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


        //Alert dialog, Snack Dialog, shared preferences

//        AlertDialog.Builder builder = new AlertDialog.Builder( DeezerSongActivity.this );


        queue = Volley.newRequestQueue(this);


        SharedPreferences searchedArtist = getSharedPreferences("SearchHistoryData", Context.MODE_PRIVATE );
        String artistTyped = searchedArtist.getString("ArtistName", "");
        binding.searchField.setHint(artistTyped);


        binding.searchButton.setOnClickListener(click -> {

        //SharedPreferences
        SharedPreferences.Editor editor = searchedArtist.edit();
        editor.putString("ArtistName", binding.searchField.getText().toString());
        editor.apply();


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
                ArtistsDTO selectedArtists = artistNames.get(position);

                Toast.makeText(DeezerSongActivity.this, "Row: " + position, Toast.LENGTH_SHORT).show();


//                FavoriteArtists removedMessage = artistNames.get(position);
//                Executor thread2 = Executors.newSingleThreadExecutor();
//                                        thread2.execute(() -> {
//                                        //delete from database
//                                        fDAO.deleteArtist(removedMessage);
//                                        });
//
//                                        artistNames.remove(position);
//                                        myAdapter.notifyItemRemoved(position);




            });


            //like onCreate above
            artistID = itemView.findViewById(R.id.artistID);
            artistName = itemView.findViewById(R.id.artistName);
            tracks = itemView.findViewById(R.id.tracklist);
            artistPfp = itemView.findViewById(R.id.artistPic);

        }
    }
}