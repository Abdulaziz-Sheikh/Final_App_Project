package Algonquin.cst2335.ui.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="ID")
    public long id;

    @ColumnInfo(name="Message")
    private String message;
    @ColumnInfo(name="Time")
    private String timeSent;
    @ColumnInfo(name="Issent")
    private boolean isSentButton;

    public ChatMessage(String message, String timeSent, boolean isSentButton) {
        this.message = message;
        this.timeSent = timeSent;
        this.isSentButton = isSentButton;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public boolean isSentButton() {
        return isSentButton;
    }
}