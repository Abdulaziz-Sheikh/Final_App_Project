package Algonquin.CST2355.final_app_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import Algonquin.CST2355.final_app_project.databinding.ActivityDeezerSongBinding;
import Algonquin.CST2355.final_app_project.databinding.SentRowBinding;

public class DeezerSongActivity extends AppCompatActivity {


    ActivityDeezerSongBinding binding;
    RecyclerView.Adapter myAdapter = null;
    ArrayList<String> artistNames = new ArrayList<>();


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int option = item.getItemId();

        if(option == R.id.homeBtn){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);



//            Snackbar.make(binding.artistText, "", Snackbar.LENGTH_LONG).show();
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deezer_song);

        TextView textview = findViewById(R.id.artistText);
        Button enterBtn = findViewById(R.id.enterBtn);

        binding = ActivityDeezerSongBinding.inflate(getLayoutInflater());


        //
        enterBtn.setOnClickListener(click -> {
            Toast.makeText(getApplicationContext(), "Loading Artists",Toast.LENGTH_LONG).show();

            String artist = binding.artistText.getText().toString();
            artistNames.add(artist);

            //Set Text Back to default
            textview.setText("");

            myAdapter.notifyDataSetChanged();
//
//
//            SharedPreferences.
        });



        //Setting up a new adapter for the recycler view
        binding.artistRecylerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    SentRowBinding songBinding = SentRowBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(songBinding.getRoot());

            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {

                String artist = artistNames.get(position);

                holder.msgText.setText(artist);
                holder.timeText.setText("Time");

            }

            @Override
            public int getItemViewType(int position) {
                //given the row, return an layout id for that row

                if(position < 3)
                    return 0;
                else
                    return 1;
            }



            @Override
            public int getItemCount() {
                return artistNames.size();
            }

        });


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
            //like onCreate above
            msgText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time); //find the ids from XML to java
        }
    }
}