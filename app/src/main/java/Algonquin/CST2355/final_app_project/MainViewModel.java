package Algonquin.CST2355.final_app_project;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    //list data offscreen
    public MutableLiveData <String> editString = new MutableLiveData<>();
    public MutableLiveData<Boolean> doyoudrinkcoffee = new MutableLiveData<>(false);


}
