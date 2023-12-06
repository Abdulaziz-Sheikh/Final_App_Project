package Algonquin.CST2355.final_app_project;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ChatMessage.class}, version =1, exportSchema = false)
public abstract class MessageDatabase extends RoomDatabase {

    public abstract ChatMessageDAO cmDAO();
}

