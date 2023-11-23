package Algonquin.CST2355.final_app_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Algonquin.CST2355.final_app_project.databinding.ActivityChatRoomBinding;
import Algonquin.CST2355.final_app_project.databinding.SentRowBinding;


public class ChatRoom extends AppCompatActivity {
    ArrayList<String> messages = new ArrayList<>(); // no message empty storing for now
    ActivityChatRoomBinding binding;
    RecyclerView.Adapter<MyRowHolder> myAdapter;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialize shared preference
        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String savedUserInput = prefs.getString("userInput", "");
        binding.userInput.setText(savedUserInput);

        binding.search.setOnClickListener(view -> {
            String userInput = binding.userInput.getText().toString();
            saveUserInput(userInput);
            messages.add(userInput);
            // messages.add("New Recipe" + messages.size()); this will hold the future recipe
            myAdapter.notifyDataSetChanged();
            //Toast message
            showToast("Message added!");

            // Show AlertDialog
            showAlertDialog();

            // Show Snackbar
            showSnackbar();
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            // binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SentRowBinding rowBinding = SentRowBinding.inflate(getLayoutInflater(), parent, false);

                return new MyRowHolder(rowBinding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                // holds variables
                holder.message.setText("Recipe:" + messages.get(position));
                holder.time.setText("time for row" + messages.get(position));
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }
        });
    } // populate the list

    private void saveUserInput(String userInput) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userInput", userInput);
        editor.apply();
    }
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Your message was added!");

        builder.setPositiveButton("OK", (dialog, which) -> {
            // Handle positive button click if needed
            // You can add code here to perform any action when the user clicks OK
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // Handle negative button click if needed
            // You can add code here to perform any action when the user clicks Cancel
        });

        builder.show();
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showSnackbar() {
        Snackbar.make(binding.getRoot(), "Message added!", Snackbar.LENGTH_SHORT).show();
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        public TextView message;
        public TextView time;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
        }
    }
}
