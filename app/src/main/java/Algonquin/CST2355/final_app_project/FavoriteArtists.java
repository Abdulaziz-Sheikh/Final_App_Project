package Algonquin.CST2355.final_app_project;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FavoriteArtists {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "Artist")
    String artist;

    @ColumnInfo(name="timeSent")
    String timeSent;


    @ColumnInfo(name = "isFavorite")
    boolean setToFavorite;

    FavoriteArtists(){}

    FavoriteArtists(String artist, String time, boolean favorite){
        this.artist = artist;
        timeSent = time;
        setToFavorite = favorite;
    }

    public String getArtist() {
        return artist;
    }

    public String getTimeSent(){
        return timeSent;
    }
    public boolean favoriteSet(){
        return setToFavorite;
    }
}
