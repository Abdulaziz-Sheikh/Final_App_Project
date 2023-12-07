package Algonquin.CST2355.final_app_project;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteArtistDAO {
    @Insert
    long insertArtist(FavoriteArtists a);

    @Query("select * from favoriteartists")
    List<FavoriteArtists> getAllArtist();

    @Delete
    void deleteArtist(FavoriteArtists a);
}