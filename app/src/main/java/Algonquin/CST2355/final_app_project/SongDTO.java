package Algonquin.CST2355.final_app_project;

public class SongDTO {


    long id;
    String title;
    String duration;
    String albumName;
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
