package com.dgs.smartdrone.entity;

public class PoiEntity {
     public boolean poiStatus;
     public int poiMode;
     public int poiLatitude;
     public int poiLongtude;
     public int poiAltiutde;

     public boolean isPoiStatus() {
          return poiStatus;
     }

     public void setPoiStatus(boolean poiStatus) {
          this.poiStatus = poiStatus;
     }

     public int getPoiMode() {
          return poiMode;
     }

     public void setPoiMode(int poiMode) {
          this.poiMode = poiMode;
     }

     public int getPoiLatitude() {
          return poiLatitude;
     }

     public void setPoiLatitude(int poiLatitude) {
          this.poiLatitude = poiLatitude;
     }

     public int getPoiLongtude() {
          return poiLongtude;
     }

     public void setPoiLongtude(int poiLongtude) {
          this.poiLongtude = poiLongtude;
     }

     public int getPoiAltiutde() {
          return poiAltiutde;
     }

     public void setPoiAltiutde(int poiAltiutde) {
          this.poiAltiutde = poiAltiutde;
     }
}
