package Algonquin.CST2355.final_app_project;
import androidx.room.Database;
import androidx.room.RoomDatabase;


    @Database(entities = {FavoriteArtists.class}, version = 1)

    public abstract class FavoritesDatabase  extends RoomDatabase {
        public abstract FavoriteArtistDAO DAO();
    }
