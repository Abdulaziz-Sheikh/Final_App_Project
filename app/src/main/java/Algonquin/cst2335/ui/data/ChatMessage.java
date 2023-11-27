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

    public ChatMessage(){}
    public ChatMessage(String m, String t, boolean sent){
        message = m;
        timeSent = t;
        isSentButton = sent;
    }

    public String getMessage() {
        return message;
    }
    public String getTimeSent(){
        return timeSent;
    }

}
