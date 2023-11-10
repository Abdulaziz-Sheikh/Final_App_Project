package Algonquin.cst2335;

import static android.app.ProgressDialog.show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import Algonquin.cst2335.databinding.ActivityChatRoomBinding;
import Algonquin.cst2335.databinding.SentMessageBinding;
import Algonquin.cst2335.ui.MessageDatabase;
import Algonquin.cst2335.ui.data.ChatMessage;
import Algonquin.cst2335.ui.data.ChatMessageDAO;
import Algonquin.cst2335.ui.data.ChatRoomViewModel;

public class ChatRoom extends AppCompatActivity {
    ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel;
    private RecyclerView.Adapter myAdapter;
    ChatMessageDAO mDAO;
    ArrayList<ChatMessage> finalMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        finalMessages = chatModel.messages.getValue();

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "databasefileonphone").build();
        mDAO = db.cmDAO();

        if (finalMessages == null) {
            finalMessages = new ArrayList<>();
            chatModel.messages.setValue(finalMessages);
        }

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.sendbutton.setOnClickListener(click -> {
            String messageText = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage newMessage = new ChatMessage(messageText, getCurrentTime(), true);
            finalMessages.add(newMessage);
            binding.textInput.setText("");

            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(new Runnable() {
                @Override
                public void run() {
                    mDAO.insertMessage(newMessage);
                    runOnUiThread(() -> myAdapter.notifyDataSetChanged());
                }
            });
        });

        class MyRowHolder extends RecyclerView.ViewHolder {
            TextView messageText;
            TextView timeText;

            public MyRowHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnClickListener(clk -> {
                    int position = getAbsoluteAdapterPosition();
                    ChatMessage message = finalMessages.get(position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                    builder.setTitle("Warning!");
                    builder.setMessage("Are you sure you want to delete this message?");
                    builder.setPositiveButton("Yes", (a, b) -> {
                        Executor thread1 = Executors.newSingleThreadExecutor();
                        thread1.execute(new Runnable() {
                            @Override
                            public void run() {
                                mDAO.deleteMessage(message); // Delete from db
                                //finalMessages.remove(position);
                                //runOnUiThread(() -> myAdapter.notifyItemRemoved(position));
                                //myAdapter.notifyItemRemoved(position);
                               // Snackbar.make(itemView,"this message " + position,Snackbar.LENGTH_LONG);

                                //.show();

                            }

                        });
                        finalMessages.remove(position);
                        myAdapter.notifyItemRemoved(position);
                        Snackbar.make(itemView,"this message " + position,Snackbar.LENGTH_LONG)
                                .setAction("undo" , (click)-> {
                                    Executor thread2 = Executors.newSingleThreadExecutor();
                                    thread2.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            mDAO.insertMessage(message);
                                            runOnUiThread(() -> myAdapter.notifyDataSetChanged());
                                        }
                                    });
                                    finalMessages.add(position , message);
                                    myAdapter.notifyDataSetChanged();

                                })
                                .show();
                    });

                    builder.setNegativeButton("NO, I don't", (a, b) -> {
                        /* Empty */
                    });
                    builder.create().show();

                });
                messageText = itemView.findViewById(R.id.message);
                timeText = itemView.findViewById(R.id.time);
            }
        }

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
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

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }
}

