package Algonquin.CST2355.final_app_project;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
    private SharedViewModel sharedViewModel;
    private OnRecipeDeletedListener recipeDeletedListener;
    public MessageDetailsFragment(ChatMessage m,ChatMessageDAO dao) {
        selected = m;
        myDAO=dao;
    }

    public MessageDetailsFragment(ChatMessage m, ChatMessageDAO dao, OnRecipeDeletedListener listener) {
        selected = m;
        myDAO = dao;
        recipeDeletedListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);

        binding.messageText.setText(selected.message);
        binding.timeText.setText(selected.timeSent);
        binding.databaseText.setText("id = " + selected.id);

        // Initialize the RequestQueue
        queue = Volley.newRequestQueue(requireContext());

        // Load recipe image
        loadImageWithVolley(selected.recipeImage, binding.recipeImage, selected.id);

        Button saveButton = binding.getRoot().findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> showSaveConfirmationDialog());

        Button deleteButton = binding.getRoot().findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(view -> showDeleteConfirmationDialog());

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
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
        ChatMessage savedRecipe = new ChatMessage(
                selected.message,
                selected.timeSent,
                selected.isSentButton,
                selected.recipeTitle,
                selected.recipeImage
        );

        long insertedId = myDAO.insertMessage(savedRecipe);
        savedRecipe.id = insertedId;

        Toast.makeText(requireContext(), "Recipe saved!", Toast.LENGTH_SHORT).show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete Recipe");
        builder.setMessage("Do you want to delete this recipe?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> deleteRecipe());
        builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    private void deleteRecipe() {
        myDAO.deleteMessage(selected);
        sharedViewModel.setDeletedRecipe(selected);

        recipeDeletedListener.onRecipeDeleted(selected);

        Toast.makeText(requireContext(), "Recipe deleted!", Toast.LENGTH_SHORT).show();
    }

    private void loadImageWithVolley(String imageUrl, ImageView imageView, long recipeId) {
        ImageRequest imgReq = new ImageRequest(imageUrl, (bitmap) -> {
            try {
                saveBitmapToFile(bitmap, recipeId);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {});
        queue.add(imgReq);
    }

    private void saveBitmapToFile(Bitmap bitmap, long recipeId) throws IOException {
        // Not sure if this is necessary considering it would just save what I already saved prior in the chatroom. Leave here for now just in case.
    }

    public interface OnRecipeDeletedListener {
        void onRecipeDeleted(ChatMessage deletedRecipe);
    }
}