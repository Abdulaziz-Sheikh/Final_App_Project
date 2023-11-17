package Algonquin.cst2335.ui.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import Algonquin.cst2335.databinding.DetailsLayoutBinding;

public class MessageDetailsFragment extends Fragment {
    ChatMessage selected;
    public MessageDetailsFragment(ChatMessage m){
       selected= m;
    }
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);
        binding.messageId.setText(selected.getMessage());
        binding.timeId.setText(selected.getTimeSent());
        binding.sendId.setText(selected.isSentButton() ? "Sent" : "Received");
        binding.databaseId.setText("id" + selected.id);

        return binding.getRoot();


    }
}
