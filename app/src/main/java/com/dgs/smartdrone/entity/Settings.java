package com.dgs.smartdrone.entity;

public class Settings {
    private int id;
    private String name;
    private int goway;
    private int handling;
    private float speed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGoway() {
        return goway;
    }
    public String getGowayN() {
        String output = "";
       switch (goway)
       {
           case 0:
            output = "None";
            break;
           case 1:
               output = "GoHome";
               break;
           case 2:
               output = "AutoLand";
               break;
           case 3:
               output = "BackTo 1st";
               break;
       }
       return  output;
    }

    public void setGoway(int goway) {
        this.goway = goway;
    }

    public int getHandling() {
        return handling;
    }

    public String getHandlingN() {
        String output = "";
        switch (handling)
        {
            case 0:
                output = "Auto";
                break;
            case 1:
                output = "Initial";
                break;
            case 2:
                output = "RC Control";
                break;
            case 3:
                output = "Use Waypoint";
                break;
        }
        return  output;
    }

    public void setHandling(int handling) {
        this.handling = handling;
    }

    public float getSpeed() {
        return speed;
    }

    public String getSpeedN() {
        String output = "";
        switch ((int) speed)
        {
            case 0:
                output = "Low";
                break;
            case 1:
                output = "Mid";
                break;
            case 2:
                output = "Hight";
                break;
        }
        return  String.valueOf(speed);
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}


