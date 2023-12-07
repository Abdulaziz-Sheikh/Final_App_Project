package Algonquin.CST2355.final_app_project;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArtistDAO {
    @Insert
    long insertArtist(ArtistsDTO a);

    @Query("select * from ArtistsDTO")
    List<ArtistsDTO> getAllArtist();

    @Delete
    void deleteArtist(ArtistsDTO a);
}