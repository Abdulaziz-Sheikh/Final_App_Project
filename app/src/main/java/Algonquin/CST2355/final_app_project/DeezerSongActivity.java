package Algonquin.CST2355.final_app_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import Algonquin.CST2355.final_app_project.databinding.ActivityDeezerSongBinding;
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
    ArrayList<FavoriteArtists> artistNames = new ArrayList<>();

    /**
     * ArtistModel
     */
    FavoriteArtistViewModel artistModel;


    /**
     * Favorite Artists DAO
     */
    FavoriteArtistDAO fDAO;


    //Variables for Volley

    protected String nameOfArtist;
    RequestQueue queue = null;






    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int option = item.getItemId();

        if(option == R.id.homeBtn){
//            Intent i = new Intent(this, MainActivity.class);
//            startActivity(i);
            Toast.makeText(this, "Home Button Clicked", Toast.LENGTH_SHORT).show();

//            Snackbar.make(binding.artistText, "", Snackbar.LENGTH_LONG).show();
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
//        setContentView(R.layout.activity_deezer_song);

        queue = Volley.newRequestQueue(this);
//        getSupportActionBar().setTitle("");







        TextView textview = findViewById(R.id.artistText);
        Button enterBtn = findViewById(R.id.enterBtn);

        binding = ActivityDeezerSongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        //Retrieve the array list
        artistModel = new ViewModelProvider(this).get(FavoriteArtistViewModel.class);


        if(artistNames == null){
            artistModel.artists.postValue(artistNames = new ArrayList<>());

            FavoritesDatabase db = Room.databaseBuilder(getApplicationContext(), FavoritesDatabase.class, "FavoriteArtists").build();
            fDAO = db.DAO();


            //Get All Entries from database

            Executor thread = Executors.newSingleThreadExecutor();

            thread.execute(() ->{

                List<FavoriteArtists> fromDatabase = fDAO.getAllArtist();
                artistNames.addAll(fromDatabase);

            });

        }


//        if (messages == null) {
//            chatModel.messages.postValue(messages = new ArrayList<>());
//
//            //Build Database
//            MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "ChatMessage").build();
//            mDAO = db.cmDAO();
//
//            //Get All Entries from database
//            Executor thread = Executors.newSingleThreadExecutor();
//            thread.execute( () -> {
//
//                List<ChatMessage> fromDatabase = mDAO.getAllMessages();//return a List
//                messages.addAll(fromDatabase);//this adds all messages from the database
//
//            });
//        }




        //Entering in a new Artist
        binding.enterBtn.setOnClickListener(click -> {

            //Get Input from Text Box
            String inputtedArtist = binding.artistText.getText().toString();

            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());

            //Typed Artist
            FavoriteArtists a = new FavoriteArtists(inputtedArtist, currentDateandTime, true);
            artistNames.add(a);//Add new Artist Object

            //Set Text Back to default
            binding.artistText.setText("");

            myAdapter.notifyDataSetChanged();

//            SharedPreferences.


            /*
                Whatever Artist you input it should be saved into the history

                Shared Preferences. Set emailText to the email address typed


                SharedPreferences searchedArtist = getSharedPreferences("MyData", Context.MODE_PRIVATE );
                String emailAddress = searchedArtist.getString("LoginName", "");
                emailText.setText(emailAddress);
             */

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
             * Second Language (Easy just do it through strings.xml)
             */
        });

        EditText searchedArtist  = findViewById(R.id.artistText);


        //This is a test Button
        binding.volleyBtn.setOnClickListener(click -> {
            nameOfArtist = binding.artistText.getText().toString();

            Toast.makeText(this, "Volley Clicked", Toast.LENGTH_SHORT).show();

            String stringUrl = "https://api.deezer.com/search/artist/?q=" + nameOfArtist + "";

            Toast.makeText(DeezerSongActivity.this, "Clicked", Toast.LENGTH_SHORT).show();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringUrl, null,
                    (response) -> {

                        try {
                            JSONObject id = response.getJSONObject("id");

                            String name = response.getString(nameOfArtist);


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    } ,
                    (error) -> { });
            queue.add(request);



        });

        //Setting up a new adapter for the recycler view
        binding.artistRecyclerView.setAdapter(
                myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                /* This is where you last left off. You're trying to figure out why the messages won't print on the
                * recycler view */

                SentRowBinding songBinding = SentRowBinding.inflate(getLayoutInflater());
                return new MyRowHolder(songBinding.getRoot());


            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {

                FavoriteArtists a = artistNames.get(position);

                holder.msgText.setText(a.getArtist());
                holder.timeText.setText(a.getTimeSent());

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

//        SharedPreferences preferences = getSharedPreferences("Artists", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("username", "")


//        Snackbar.make(textview, "You deleted message #" + textview., Snackbar.LENGTH_LONG).show();

    }

    class MyRowHolder extends RecyclerView.ViewHolder {

        public TextView msgText;
        public TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            //Click on an Item
            itemView.setOnClickListener(click -> {
                int rowNum = getAbsoluteAdapterPosition();

            });


            //like onCreate above
            msgText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time); //find the ids from XML to java
        }
    }
}