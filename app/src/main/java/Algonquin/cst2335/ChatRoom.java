package Algonquin.cst2335;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import Algonquin.cst2335.databinding.ActivityChatRoomBinding;
import Algonquin.cst2335.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {
    ActivityChatRoomBinding binding;
    ArrayList<String> messages = new ArrayList<>();
    private RecyclerView.Adapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());
      binding.sendbutton.setOnClickListener(click -> {
          messages.add(binding.textInput.getText().toString());
          binding.textInput.setText("");
              });


     binding.recycleView.setAdapter(myAdapter=new RecyclerView.Adapter<MyRowHolder>() {
         class MyRowHolder extends RecyclerView.ViewHolder {
             TextView messageText;
             TextView timeText;
             public MyRowHolder(@NonNull View itemView) {
                 super(itemView);
                 messageText = itemView.findViewById(R.id.message);
                 timeText = itemView.findViewById(R.id.time);
             }
         }

         @NonNull
         @Override
         public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
             SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
             return new MyRowHolder(binding.getRoot()) ;

         }
        public int getItemViewType(int position){
             return 0;
         }


         @Override
         public int getItemCount() {
             return messages.size();

         }

         @Override
         public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
             holder.messageText.setText("");
             holder.timeText.setText("");
             String obj = messages.get(position);
             holder.messageText.setText(obj);


         }



     }));

    }
}