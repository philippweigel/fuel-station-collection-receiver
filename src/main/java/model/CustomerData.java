package model;

public class CustomerData {
    private int customerId;
    private double sumKwh;

    public CustomerData(int customerId, double sumKwh) {
        this.customerId = customerId;
        this.sumKwh = sumKwh;
    }

    public void addSumKwh(double sumKwh) {
        this.sumKwh += sumKwh;
    }

    public int getCustomerId() {
        return customerId;
    }

    public double getSumKwh() {
        return sumKwh;
    }
}

