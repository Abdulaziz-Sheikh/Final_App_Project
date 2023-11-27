package Algonquin.cst2335;

import static android.app.ProgressDialog.show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import Algonquin.cst2335.databinding.ActivityChatRoomBinding;
import Algonquin.cst2335.databinding.SentMessageBinding;
import Algonquin.cst2335.databinding.ReceiveMessageBinding;


import Algonquin.cst2335.ui.MessageDatabase;
import Algonquin.cst2335.ui.data.ChatMessage;
import Algonquin.cst2335.ui.data.ChatMessageDAO;
import Algonquin.cst2335.ui.data.ChatRoomViewModel;
import Algonquin.cst2335.ui.data.MessageDetailsFragment;

public class ChatRoom extends AppCompatActivity {
    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    ChatRoomViewModel chatModel;
    private RecyclerView.Adapter myAdapter;

    MessageDatabase myDB;

    ChatMessageDAO myDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FrameLayout fragmentLocation = findViewById(R.id.fragmentLocation);

        boolean IAmTablet = fragmentLocation != null;

        myDB = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();

        myDAO = myDB.cmDAO(); // only funtion in MessageDatabase
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(()->{

            //add all privious database
            List<ChatMessage> allMessages = myDAO.getAllMessage();


            messages.addAll(allMessages);
        });


        if (messages == null){
            messages = new ArrayList<>();
            chatModel.messages.postValue(messages);
        }

        binding.sendButton.setOnClickListener(click -> {
            String msg = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            boolean isSent = true;
            ChatMessage newMessage = new ChatMessage(msg, currentDateandTime, isSent);

            Executor thread1 = Executors.newSingleThreadExecutor();

            thread1.execute(()-> {

                newMessage.id = myDAO.insertMessage(newMessage); // add to database


            });





            messages.add(newMessage);
            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.textInput.setText("");
        });
        binding.receiveButton.setOnClickListener(click -> {
            String msg = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            boolean isSent = false;
            ChatMessage newMessage = new ChatMessage(msg, currentDateandTime, isSent);

            Executor thread2 = Executors.newSingleThreadExecutor();

            thread2.execute(()->{

                newMessage.id = myDAO.insertMessage(newMessage); // add to database


            });

            messages.add(newMessage);
            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.textInput.setText("");
        });

        binding.recycleview.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {@NonNull
           @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0){
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }else{
                   ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater());
                   return new MyRowHolder(binding.getRoot());
                }

            }

            @Override
           public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.message);
                holder.timeText.setText(obj.timeSent);
            }

            @Override
            public int getItemCount() {
                return messages.size();         }
            @Override
            public int getItemViewType(int position) {
                ChatMessage msg = messages.get(position);
                if (msg.isSentButton) {return 0;} else {
                    return 1;
                }
            }
        });
        myAdapter = new RecyclerView.Adapter<MyRowHolder>(){
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0){
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }else{
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }

            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.message);
                holder.timeText.setText(obj.timeSent);
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }
            @Override
            public int getItemViewType(int position) {
                ChatMessage msg = messages.get(position);
                if (msg.isSentButton) {
                    return 0;
                } else {
                    return 1;
                }
            }
        };
        binding.recycle.setAdapter(myAdapter);

        binding.recycle.setLayoutManager(new LinearLayoutManager(this));

        chatModel.selectedMessage.observe(this, (newValue) -> {
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();

            MessageDetailsFragment chatFragment = new MessageDetailsFragment( newValue);
            tx.add(R.id.fragmentLocation, chatFragment);
            tx.replace(R.id.fragmentLocation, chatFragment);
            tx.commit();
            tx.addToBackStack("");



        });
    }
    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
            itemView.setOnClickListener( click ->{

                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                builder.setTitle("Question");
                builder.setMessage("Do you want to delete this?");
                builder.setPositiveButton("Go Ahead",(dlg,which)->{

                    //what is the index
                    int index = getAbsoluteAdapterPosition();

                    ChatMessage toDelete = messages.get(index);
                    Executor thread1 = Executors.newSingleThreadExecutor();

                    thread1.execute(()-> {
                        myDAO.deleteMessage(toDelete);
                        messages.remove(index); // remove from array list
                       // must be done on the main UI threa
                           myAdapter.notifyDataSetChanged();

                       });
                        Toast.makeText(this,"A message",Toast.LENGTH_LONG).show();
                       Snackbar.make(timeText,"Deleted your message #" + index,Snackbar.LENGTH_LONG).setAction("UNDO",clk ->{
                           Executor myThread = Executors.newSingleThreadExecutor();                           myThread.execute(()->{
                               messages.add(index, toDelete);
                                runOnUiThread(()->{
                                    myAdapter.notifyDataSetChanged();
                               });
                           });

                       }).show();
                });
            });

               builder.setNegativeButton("NO",(dl,wh)->{ /*hide the dialog,do nothing*/});

                //appear
                builder.create().show();

                int position = getAbsoluteAdapterPosition();
                ChatMessage selected = messages.get(position);

                chatModel.selectedMessage.postValue(selected);
            };
        }
    }
