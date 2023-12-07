package Algonquin.CST2355.final_app_project;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class FavoriteArtistViewModel extends ViewModel {

    public MutableLiveData<ArrayList<ArtistsDTO>> artists = new MutableLiveData<>();


}
