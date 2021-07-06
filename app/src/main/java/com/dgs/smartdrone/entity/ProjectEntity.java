package com.dgs.smartdrone.entity;

import java.util.List;

public class ProjectEntity {
    private String _id;
    private String namaProject;
    private String namaSurveyor;
    private String alamatProject;
    private String tglPlanning;
    private String updatedAt;
    private GPSEntity lokasi;
    private List<DeployDateEntity> deploy;
    private String tglTarget;
    private List<PinEntity> pin;
    private UserEntity user;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNamaProject() {
        return namaProject;
    }

    public void setNamaProject(String namaProject) {
        this.namaProject = namaProject;
    }

    public String getNamaSurveyor() {
        return namaSurveyor;
    }

    public void setNamaSurveyor(String namaSurveyor) {
        this.namaSurveyor = namaSurveyor;
    }

    public String getAlamatProject() {
        return alamatProject;
    }

    public void setAlamatProject(String alamatProject) {
        this.alamatProject = alamatProject;
    }

    public String getTglPlanning() {
        return tglPlanning;
    }

    public void setTglPlanning(String tglPlanning) {
        this.tglPlanning = tglPlanning;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public GPSEntity getLokasi() {
        return lokasi;
    }

    public void setLokasi(GPSEntity lokasi) {
        this.lokasi = lokasi;
    }

    public List<DeployDateEntity> getDeploy() {
        return deploy;
    }

    public void setDeploy(List<DeployDateEntity> deploy) {
        this.deploy = deploy;
    }

    public String getTglTarget() {
        return tglTarget;
    }

    public void setTglTarget(String tglTarget) {
        this.tglTarget = tglTarget;
    }

    public List<PinEntity> getPin() {
        return pin;
    }

    public void setPin(List<PinEntity> pin) {
        this.pin = pin;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
