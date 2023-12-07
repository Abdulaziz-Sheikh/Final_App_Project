package Algonquin.CST2355.final_app_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button deezerSongPicker = findViewById(R.id.deezer);
        Button recipieSearch = findViewById(R.id.recipieSearch);
        Button sunsetSunriseLookUp = findViewById(R.id.sunriselookup);


        //Open Deezer Song Picker
        deezerSongPicker.setOnClickListener(click -> {
            Intent deezerPage = new Intent(this, DeezerSongActivity.class);
            startActivity(deezerPage);
        });


    }
}