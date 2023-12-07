package Algonquin.CST2355.final_app_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Algonquin.CST2355.final_app_project.databinding.ActivityChatRoomBinding;
import Algonquin.CST2355.final_app_project.databinding.SentRowBinding;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ArrayList<String> messages = new ArrayList<>(); // no message empty storing for now

    RecyclerView.Adapter<MyRowHolder> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.search.setOnClickListener( click ->{
            messages.add("New Recipe" + messages.size());
            myAdapter.notifyDataSetChanged();
        });

        binding.recyclerView.setAdapter(myAdapter= new RecyclerView.Adapter<MyRowHolder>() {

            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SentRowBinding rowBinding= SentRowBinding.inflate(getLayoutInflater(), parent, false);

                return new MyRowHolder( rowBinding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
            //holds variables
                holder.message.setText("text for now" + position);
                holder.time.setText("time for row" + position);
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }
        });
        } //populate the list

    class MyRowHolder extends RecyclerView.ViewHolder {
        public TextView message;
        public TextView time;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            message =itemView.findViewById(R.id.message);
            time=itemView.findViewById(R.id.time);

        }
    }

}
