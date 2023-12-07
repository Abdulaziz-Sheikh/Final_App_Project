package Algonquin.CST2355.final_app_project;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ArtistsDTO {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public long id;

    @ColumnInfo(name="ArtistName")
    protected String artistName;

    @ColumnInfo(name="tracklist")
    protected String tracklist;

    @ColumnInfo(name="pictureUrl")
    protected String pictureUrl;


    ArtistsDTO(){}

    ArtistsDTO(long id, String artist, String tracklist, String pictureUrl){
        this.id = id;
        this.artistName = artist;
        this.tracklist = tracklist;
        this.pictureUrl = pictureUrl;
    }

    public long getId(){
        return id;
    }
    public String getArtistName() {
        return artistName;
    }

    public String getTimeSent(){
        return tracklist;
    }
    public String getTracklist(){
        return tracklist;
    }

    public String getPictureUrl(){
        return pictureUrl;
    }
}
