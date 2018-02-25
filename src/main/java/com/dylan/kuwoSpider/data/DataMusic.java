package com.dylan.kuwoSpider.data;

public class DataMusic {

    /**
     * id : MUSIC_118990
     * name : 发如雪
     * artist : 周杰伦
     * album : 十一月的萧邦
     * pay : 16515324
     */

    private String id;
    private String name;
    private String artist;
    private String album;
    private String pay;

    public static DataMusic objectFromData(String str) {
        return new com.google.gson.Gson().fromJson(str, DataMusic.class);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }
}
