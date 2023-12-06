package Algonquin.CST2355.final_app_project;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import Algonquin.CST2355.final_app_project.databinding.DetailsLayoutBinding;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MessageDetailsFragment extends Fragment {

    private ChatMessage selected;
    private RequestQueue queue;
    private Executor thread = Executors.newSingleThreadExecutor();
    private ChatMessageDAO myDAO;
    public MessageDetailsFragment(ChatMessage m,ChatMessageDAO dao) {
        selected = m;
        myDAO=dao;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //View view = inflater.inflate(R.layout.details_layout, container, false);
        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);

        binding.messageText.setText(selected.message);
        binding.timeText.setText(selected.timeSent);
        binding.databaseText.setText("id = " + selected.id);
       // binding.recipeInfo.setText(selected.recipeInfo);
      // binding.recipeUrl.setText(selected.recipeUrl);


        // Initialize the RequestQueue
        queue = Volley.newRequestQueue(requireContext());

        // Load recipe image
        loadImageWithVolley(selected.recipeImage, binding.recipeImage, selected.id);

        Button saveButton = binding.getRoot().findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> showSaveConfirmationDialog());



        return binding.getRoot();

    }
    private void showSaveConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Save Recipe");
        builder.setMessage("Do you want to save this recipe?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> saveRecipe());
        builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    private void saveRecipe() {
        // Perform the save action here, e.g., insert the recipe into a database
        // You can access the recipe details from the 'selected' object
        ChatMessage savedRecipe = new ChatMessage(
                selected.message,
                selected.timeSent,
                selected.isSentButton,
                selected.recipeTitle,
                selected.recipeImage
        );

        thread.execute(() -> {
            long insertedId = myDAO.insertMessage(savedRecipe);
            savedRecipe.id = insertedId;

            // Show a toast or any other feedback to the user
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Recipe saved!", Toast.LENGTH_SHORT).show();
            });
        });

        // Any code here will execute immediately after starting the thread
        // For example, you can put additional actions or return statements here if needed
    }

    private void loadImageWithVolley(String imageUrl, ImageView imageView, long recipeId) {
        ImageRequest imgReq = new ImageRequest(imageUrl, (bitmap) -> {
            try {
                saveBitmapToFile(bitmap, recipeId);
                requireActivity().runOnUiThread(() -> {
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
        //Not sure if this is ncessary considering it would just save what i already saved prior in chatroom leave here for now just in case
    }

}
