package com.dgs.smartdrone.entity;

public class PinEntity {
    public GPSEntity koordinat;
    public PoiEntity poi;
    public GimbalModelEntity gimbalmode;
    public IntervalmodeEntity intervalmode;
    public ActionsEntity actions;
    public String _id;
    public String name;
    public int speed;
    public int altitude;
    public int heading;
    public int curvesize;
    public int rotationdir;

    public GPSEntity getKoordinat() {
        return koordinat;
    }

    public void setKoordinat(GPSEntity koordinat) {
        this.koordinat = koordinat;
    }

    public PoiEntity getPoi() {
        return poi;
    }

    public void setPoi(PoiEntity poi) {
        this.poi = poi;
    }

    public GimbalModelEntity getGimbalmode() {
        return gimbalmode;
    }

    public void setGimbalmode(GimbalModelEntity gimbalmode) {
        this.gimbalmode = gimbalmode;
    }

    public IntervalmodeEntity getIntervalmode() {
        return intervalmode;
    }

    public void setIntervalmode(IntervalmodeEntity intervalmode) {
        this.intervalmode = intervalmode;
    }

    public ActionsEntity getActions() {
        return actions;
    }

    public void setActions(ActionsEntity actions) {
        this.actions = actions;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public int getHeading() {
        return heading;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public int getCurvesize() {
        return curvesize;
    }

    public void setCurvesize(int curvesize) {
        this.curvesize = curvesize;
    }

    public int getRotationdir() {
        return rotationdir;
    }

    public void setRotationdir(int rotationdir) {
        this.rotationdir = rotationdir;
    }
}
