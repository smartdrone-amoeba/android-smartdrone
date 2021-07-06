package com.dgs.smartdrone.entity;

public class GimbalModelEntity {
    public boolean focuspoi;
    public int interpolate;

    public boolean isFocuspoi() {
        return focuspoi;
    }

    public void setFocuspoi(boolean focuspoi) {
        this.focuspoi = focuspoi;
    }

    public int getInterpolate() {
        return interpolate;
    }

    public void setInterpolate(int interpolate) {
        this.interpolate = interpolate;
    }
}
