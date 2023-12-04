package Algonquin.cst2335;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import Algonquin.cst2335.databinding.ActivityChatRoomBinding;
import Algonquin.cst2335.databinding.SentMessageBinding;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import Algonquin.cst2335.ui.MessageDatabase;
import Algonquin.cst2335.ui.data.ChatMessage;
import Algonquin.cst2335.ui.data.ChatMessageDAO;
import Algonquin.cst2335.ui.data.ChatRoomViewModel;
import Algonquin.cst2335.ui.data.MessageDetailsFragment;

public class ChatRoom extends AppCompatActivity {
    private static final String API_KEY = "b9b01cac333447c5a870c05469af8311";
    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    ChatRoomViewModel chatModel;
    private RecyclerView.Adapter myAdapter;

    MessageDatabase myDB;

    ChatMessageDAO myDAO;
    Executor thread = Executors.newSingleThreadExecutor();
    private boolean isFrameLayoutVisible = false;

    protected RequestQueue queue = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_1) {
            TextView messageText = findViewById(R.id.message);
            ChatMessage m = chatModel.selectedMessage.getValue();
            int position = messages.indexOf(m);
            AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
            builder.setMessage("Do you want to delete the message: " + messageText.getText())
                    .setTitle("Question: ")
                    .setPositiveButton("Yes", (d, c) -> {
                        thread.execute(() -> {
                            myDAO.deleteMessage(m);
                        });
                        messages.remove(position);
                        myAdapter.notifyItemRemoved(position);
                        Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                .setAction("Undo", cl -> {
                                    thread.execute(() -> {
                                        myDAO.insertMessage(m);
                                    });
                                    messages.add(position, m);
                                    myAdapter.notifyItemInserted(position);
                                }).show();
                    }).setNegativeButton("No", (d, c) -> {
                    }).create()
                    .show();

            // Toggle framelayout visibility.
            isFrameLayoutVisible = !isFrameLayoutVisible;
            FrameLayout frameLayout = binding.fragmentLocation;
            updateFrameLayoutVisibility();

        } else if (item.getItemId() == R.id.item_2) {
            Toast.makeText(this, "Version 1.0, code by @Abdulaziz SHeikh Omar", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    private void updateFrameLayoutVisibility() {
        FrameLayout fragmentLocation = findViewById(R.id.fragmentLocation);
        fragmentLocation.setVisibility(isFrameLayoutVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fragmentLocation.setOnClickListener(click -> {
            updateFrameLayoutVisibility();
        });

        myDB = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();

        myDAO = myDB.cmDAO(); // only function in MessageDatabase
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();

        setSupportActionBar(binding.toolbar);

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {

            // Add all previous database messages
            List<ChatMessage> allMessages = myDAO.getAllMessage();
            messages.addAll(allMessages);
        });

        if (messages == null) {
            messages = new ArrayList<>();
            chatModel.messages.postValue(messages);
        }

        binding.sendButton.setOnClickListener(click -> {
            String msg = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());

            // Try-catch for each search
            String url = null;
            try {
                Log.d("URL_DEBUG", "Original message: " + msg);
                url = "https://api.spoonacular.com/recipes/complexSearch?query=" +
                        URLEncoder.encode(msg, "UTF-8")
                        + "&apiKey=" + API_KEY;
                Log.d("URL_DEBUG", "Generated URL: " + url);
            } catch (UnsupportedEncodingException e) {
                Log.e("URL_DEBUG", "Error encoding URL", e);
                throw new RuntimeException(e);
            }

            // Network request using Volley
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    response -> {
                        try {
                            JSONArray recipesArray = response.getJSONArray("query");

                            // Loop through each recipe
                            for (int i = 0; i < recipesArray.length(); i++) {
                                JSONObject recipeObject = recipesArray.getJSONObject(i);

                                // Get recipe details
                                String recipeTitle = recipeObject.getString("title");
                                String recipeImage = recipeObject.getString("image");
                                Long recipeID = recipeObject.getLong("id");

                                // Construct the URL for the second query to get recipe details
                                String imageUrl = "https://api.spoonacular.com/recipes/" + recipeID + "/information?apiKey=" + API_KEY;



                                // Update UI or RecyclerView with the new message
                                runOnUiThread(() -> {
                                    messages.add(new ChatMessage(msg, currentDateandTime, true, recipeTitle, recipeImage));

                                    myAdapter.notifyItemInserted(messages.size() - 1);
                                });

                                 //Insert into the database
                                thread.execute(() -> {
                                    long messageId = myDAO.insertMessage(new ChatMessage(msg, currentDateandTime, true, recipeTitle, recipeImage));
                                   // Do something with the messageId if needed
                               });

                                // Do something with recipe information (e.g., display in RecyclerView)
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        // Handle error response
                        Log.e("VolleyError", "Error during JSON request", error);
                    }
            );

            // Add the request to the queue
            queue.add(request);

            // Clear the input field
            binding.textInput.setText("");
        });

        binding.recycle.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.message);
                holder.timeText.setText(obj.timeSent);
                holder.recipeTitle.setText(obj.recipeTitle);

                // Load recipe image using Volley
                loadImageWithVolley(obj.recipeImage, holder.recipeImage, obj.id);
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                return 0; // Always return 0 as you only have one type of view
            }
        });

        binding.recycle.setLayoutManager(new LinearLayoutManager(this));

        chatModel.selectedMessage.observe(this, (newValue) -> {
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();

            binding.fragmentLocation.setVisibility(View.VISIBLE);
            MessageDetailsFragment chatFragment = new MessageDetailsFragment(newValue);
            tx.add(R.id.fragmentLocation, chatFragment);
            tx.replace(R.id.fragmentLocation, chatFragment);
            tx.commit();
            tx.addToBackStack("");
        });
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        TextView recipeTitle;

        ImageView recipeImage; // Add this line




        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
            recipeTitle = itemView.findViewById(R.id.recipeTitle);
            recipeImage = itemView.findViewById(R.id.recipeImage);

            itemView.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();
                ChatMessage selected = messages.get(position);
                chatModel.selectedMessage.postValue(selected);
            });
        }

    }
    private void loadImageWithVolley(String imageUrl, ImageView imageView, long recipeId) {
        ImageRequest imgReq = new ImageRequest(imageUrl, (bitmap) -> {
            try {
                saveBitmapToFile(bitmap, recipeId);
                runOnUiThread(() -> {
                    imageView.setImageBitmap(bitmap);
                    imageView.setVisibility(View.VISIBLE);
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {});
        queue.add(imgReq);
    }

    private void saveBitmapToFile(Bitmap bitmap, long recipeId) throws IOException {
        FileOutputStream fOut = openFileOutput(recipeId + ".png", Context.MODE_PRIVATE);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        fOut.flush();
        fOut.close();
    }
}
