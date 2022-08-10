package com.example.gungunaoo_android;

public class model {
    String url, song_Name, song_Desc;

    model() {

    }

    public model(String url, String song_Name, String song_Desc) {
        this.url = url;
        this.song_Name = song_Name;
        this.song_Desc = song_Desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSong_Name() {
        return song_Name;
    }

    public void setSong_Name(String song_Name) {
        this.song_Name = song_Name;
    }

    public String getSong_Desc() {
        return song_Desc;
    }

    public void setSong_Desc(String song_Desc) {
        this.song_Desc = song_Desc;
    }
}
