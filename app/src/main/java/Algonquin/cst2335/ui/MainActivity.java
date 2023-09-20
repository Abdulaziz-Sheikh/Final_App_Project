package Algonquin.cst2335.ui;

import Algonquin.cst2335.databinding.ActivityMainBinding;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
        variableBinding.textview.setText((CharSequence) viewModel.editString);
        variableBinding.mybutton.setOnClickListener(click -> {
            viewModel.editString.postValue(variableBinding.myedittext.getText().toString());

            EditText myEdit = findViewById(R.id.myedittext);
            String editString = myEdit.getText().toString();
            //variableBinding.textview.setText("Your edit text has: " + editString);


            viewModel.editString.observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    variableBinding.textview.setText("Your edit text has: " + s);
                }
            });

        });
    }
}
