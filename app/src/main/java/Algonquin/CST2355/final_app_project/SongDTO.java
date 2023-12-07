package Algonquin.CST2355.final_app_project;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SongDTO {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    long id;

    @ColumnInfo(name = "songTitle")
    String title;
    @ColumnInfo(name = "songDuration")
    String duration;
    @ColumnInfo(name = "albumName")
    String albumName;

    @ColumnInfo(name = "albumCover")
    String albumCoverUrl;


    public SongDTO(String title, String duration, String albumName, String albumCoverUrl){
        this.title = title;
        this.duration = duration;
        this.albumName = albumName;
        this.albumCoverUrl = albumCoverUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getAlbumCoverUrl() {
        return albumCoverUrl;
    }





}
