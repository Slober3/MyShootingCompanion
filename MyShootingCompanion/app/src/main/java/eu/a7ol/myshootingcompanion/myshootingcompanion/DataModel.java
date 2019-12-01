package eu.a7ol.myshootingcompanion.myshootingcompanion;


import android.graphics.Color;

public class DataModel {

    String name;
    String Time;
    String Tot_time;
    String timedif;

    int id_;
    int image;
    int color;


    public DataModel(String name, String Time, int id_, int image, int colorBG, String timediff) {
        this.name = name;
        this.Time = Time;
        this.Tot_time = "0";
        this.id_ = id_;
        this.image=image;
        this.color= colorBG ;
        this.timedif= timediff;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return Time;
    }

    public String getTotTime() {
        return Tot_time;
    }
    public String getTimedif() {
        return timedif;
    }


    public int getImage() {
        return image;
    }

    public int getId() {
        return id_;
    }

    public int getColor() {
        return color;
    }

}