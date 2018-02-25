package com.dylan.kuwoSpider;


public class MusicBean {


    public MusicBean() {
    }
    private String mp3;
    private String duration;
    private String cover;
    private String title;
    private String artist;
    private String background;

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    @Override
    public String toString() {
        return "MusicBean{" +
                "mp3='" + mp3 + '\'' +
                ", duration='" + duration + '\'' +
                ", cover='" + cover + '\'' +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", background='" + background + '\'' +
                '}';
    }
}
