package Algonquin.CST2355.final_app_project;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<ChatMessage> deletedRecipe = new MutableLiveData<>();

    public void setDeletedRecipe(ChatMessage recipe) {
        deletedRecipe.setValue(recipe);
    }

    public LiveData<ChatMessage> getDeletedRecipe() {
        return deletedRecipe;
    }
}
