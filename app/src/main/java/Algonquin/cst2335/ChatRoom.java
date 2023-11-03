package Algonquin.cst2335;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Algonquin.cst2335.databinding.ActivityChatRoomBinding;
import Algonquin.cst2335.databinding.SentMessageBinding;
import Algonquin.cst2335.ui.data.ChatMessage;
import Algonquin.cst2335.ui.data.ChatRoomViewModel;

public class ChatRoom extends AppCompatActivity {
    ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel;

    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        ArrayList<ChatMessage> messages = chatModel.messages.getValue();

        if (messages == null) {
            messages = new ArrayList<>();
            chatModel.messages.setValue(messages); // Use setValue to set the initial value
        }

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ArrayList<ChatMessage> finalMessages = messages;
        binding.sendbutton.setOnClickListener(click -> {
            String messageText = binding.textInput.getText().toString();
            if (!messageText.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
                String currentDateandTime = sdf.format(new Date());
                ChatMessage newMessage = new ChatMessage(messageText, getCurrentTime(), true);
                finalMessages.add(newMessage);
                binding.textInput.setText("");
            }

        });
        Button receiveButton = findViewById(R.id.recieve);
        receiveButton.setOnClickListener(click -> {
            String messageText = "Received message"; // Replace with the actual received message
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage newMessage = new ChatMessage(messageText, currentDateandTime, false);
            finalMessages.add(newMessage);
        });

        class MyRowHolder extends RecyclerView.ViewHolder {
            TextView messageText;
            TextView timeText;

            public MyRowHolder(@NonNull View itemView) {
                super(itemView);
                messageText = itemView.findViewById(R.id.message);
                timeText = itemView.findViewById(R.id.time);
            }
        }
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<ChatMessage> finalMessages1 = messages;
        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public int getItemCount() {
                return finalMessages.size();
            }
            public int getItemViewType(int position) {
                if (finalMessages.get(position).isSentButton()) {
                    return 0; // Sent message
                } else {
                    return 1; // Received message
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage chatMessage = finalMessages.get(position);
                holder.messageText.setText(finalMessages.get(position).getMessage());
                holder.timeText.setText(finalMessages.get(position).getTimeSent());
            }
        });
    }

    // Define a method to get the current time
    private String getCurrentTime() {
        // Implement this method to return the current time in the desired format
        // For example, you can use SimpleDateFormat to format the time.
        // Here's an example format: "yyyy-MM-dd HH:mm:ss"
        // Replace this example with your actual implementation.
        return "Your current time implementation";
    }
}
