package Algonquin.cst2335.ui;

import Algonquin.cst2335.databinding.ActivityMainBinding;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Algonquin.cst2335.R;
import Algonquin.cst2335.ui.data.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding variableBinding;
    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        //ask prof about char sequence and why its necessary heere
        //variableBinding.textview.setText((CharSequence) viewModel.editString);

/*
        variableBinding.mybutton.setOnClickListener(click -> {
            viewModel.editString.postValue(variableBinding.myedittext.getText().toString());

            EditText myEdit = findViewById(R.id.myedittext);
            String editString = myEdit.getText().toString();
            //variableBinding.textview.setText("Your edit text has: " + editString);
        //ALL COUMPOUND BUTTON LOGIC



        });

        viewModel.editString.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                variableBinding.textview.setText("Your edit text has: " + s);
            }});


        viewModel.doyoudrinkcoffee.observe(this, selected -> {
            variableBinding.thecheckbox.setChecked(selected);
            variableBinding.theradiobutton.setChecked(selected);
            variableBinding.theswitch.setChecked(selected);

            String message = "The value is now: " + selected;
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
        variableBinding.thecheckbox.setOnCheckedChangeListener((buttonView, doyoudrinkcoffee) -> {
            viewModel.doyoudrinkcoffee.postValue(doyoudrinkcoffee);
        });

        variableBinding.theradiobutton.setOnCheckedChangeListener((buttonView, doyoudrinkcoffee) -> {
            viewModel.doyoudrinkcoffee.postValue(doyoudrinkcoffee);
        });

        variableBinding.theswitch.setOnCheckedChangeListener((buttonView, doyoudrinkcoffee) -> {
            viewModel.doyoudrinkcoffee.postValue(doyoudrinkcoffee);
        });

//image widget logic
        variableBinding.theimagebtn.setOnClickListener( clk -> {
           // myEdit.setText("Image Clicked");
            int width = variableBinding.theimagebtn.getWidth();
            int height = variableBinding.theimagebtn.getHeight();
            String message = "The width = " + width + " and height = " + height;
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        } );
   */
    }
}
