package model;

public class CompleteData {
    private String uid;
    private int customerId;
    private double totalKwh;

    public CompleteData(String uid, int customerId, double totalKwh) {
        this.uid = uid;
        this.customerId = customerId;
        this.totalKwh = totalKwh;
    }

    public CompleteData() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getTotalKwh() {
        return totalKwh;
    }

    public void setTotalKwh(double totalKwh) {
        this.totalKwh = totalKwh;
    }
}
