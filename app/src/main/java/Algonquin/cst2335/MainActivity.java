package Algonquin.cst2335;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView myText = findViewById(R.id.textview);
        Button myButton = findViewById(R.id.mybutton);
        EditText myEdit = findViewById(R.id.myedittext);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //myText.setText("you clicked the button");
                //myEdit.setText("you clicked the button");
                //myButton.setText("you clicked the button");
                String editString = myEdit.getText().toString();
                myText.setText( "Your edit text has: " + editString);
            }
        });
    }
}



