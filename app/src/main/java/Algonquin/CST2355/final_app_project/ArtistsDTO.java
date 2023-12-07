package Algonquin.CST2355.final_app_project;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ArtistsDTO {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "Artist")
    String artistName;

    @ColumnInfo(name="tracklist")
    String tracklist;


    @ColumnInfo(name = "isFavorite")
    boolean setToFavorite;

    ArtistsDTO(){}

    ArtistsDTO(long id, String artist, String tracklist, boolean favorite){
        this.id = id;
        this.artistName = artist;
        this.tracklist = tracklist;
        setToFavorite = favorite;
    }

    public String
    public String getArtistName() {
        return artistName;
    }

    public String getTimeSent(){
        return tracklist;
    }
    public boolean favoriteSet(){
        return setToFavorite;
    }
}
