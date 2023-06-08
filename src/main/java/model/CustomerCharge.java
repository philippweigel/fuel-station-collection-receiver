package model;

public class CustomerCharge {

    private int customerId;
    private int stationId;
    private float sumKwh;
    private String uid;

    public CustomerCharge(int customerId, int stationId, float sumKwh, String uid) {
        this.customerId = customerId;
        this.stationId = stationId;
        this.sumKwh = sumKwh;
        this.uid = uid;
    }

    public CustomerCharge() {
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public float getSumKwh() {
        return sumKwh;
    }

    public void setSumKwh(float sumKwh) {
        this.sumKwh = sumKwh;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
