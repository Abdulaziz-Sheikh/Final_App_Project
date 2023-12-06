package Algonquin.cst2335.ui.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class ChatMessage {
    @ColumnInfo(name="message")
    public String message;
    @ColumnInfo(name="TimeSent")
    public String timeSent;
    @ColumnInfo(name="SendOrReceive")
    public boolean isSentButton;

    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo(name="recipeTitle")
    public String recipeTitle;  // New field for recipe title

    @ColumnInfo(name="recipeImage")
    public String recipeImage;  // New field for recipe image URL


    public ChatMessage(){}
    public ChatMessage(String m, String t, boolean sent, String title, String imageUrl){
        message = m;
        timeSent = t;
        isSentButton = sent;
        recipeTitle = title;  // Assign the 'title' parameter to the 'recipeTitle' member variable.
        recipeImage = imageUrl;  // Assign the 'imageUrl' parameter to the 'recipeImage' member variable.
    }


    public String getMessage() {
        return message;
    }
    public String getTimeSent(){
        return timeSent;
    }
    public String getRecipeTitle() {
        return recipeTitle;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

}
