package Algonquin.CST2355.final_app_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import Algonquin.CST2355.final_app_project.databinding.ActivityArtistDetailsBinding;

public class ArtistDetails extends Fragment {


    /**
     * ArtistDTO Object
     */
    ArtistsDTO selectedArtist;

    public ArtistDetails(ArtistsDTO DTO){
        selectedArtist = DTO;

    }


    /**
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        //Details XML
        ActivityArtistDetailsBinding binding = ActivityArtistDetailsBinding.inflate(inflater);


        //Get Image -> Leave this for now just focus on displaying the artist name
        //And the tracklist link
        String image = selectedArtist.getPictureUrl();
//        Picasso.get().load(image)
//                .into(selectedArtist.getPictureUrl())


        //Bindings
        binding.ArtistsName.setText(selectedArtist.artistName);
        binding.tracklistLink.setText(selectedArtist.tracklist);


        return binding.getRoot();


    }


}