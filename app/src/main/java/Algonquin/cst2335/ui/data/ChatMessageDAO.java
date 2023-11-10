package Algonquin.cst2335.ui.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;
@Dao
public interface ChatMessageDAO {
    @Insert
    long insertMessage(ChatMessage m);

    @Delete
    void deleteMessage(ChatMessage m);

    @Query("SELECT * FROM ChatMessage")
    List<ChatMessage> getAllMessages();
}
