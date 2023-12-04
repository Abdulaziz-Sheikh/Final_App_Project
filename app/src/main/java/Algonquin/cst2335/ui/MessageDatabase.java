package Algonquin.cst2335.ui;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import Algonquin.cst2335.ui.data.ChatMessage;
import Algonquin.cst2335.ui.data.ChatMessageDAO;

@Database(entities = {ChatMessage.class}, version =1)
public abstract class MessageDatabase extends RoomDatabase {

    public abstract ChatMessageDAO cmDAO();
}

