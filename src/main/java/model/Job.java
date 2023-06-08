package model;

public class Job {
    private String uid;
    private int expectedMessageCount;

    public Job(String uid, int expectedMessageCount) {
        this.uid = uid;
        this.expectedMessageCount = expectedMessageCount;
    }

    public Job() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getExpectedMessageCount() {
        return expectedMessageCount;
    }

    public void setExpectedMessageCount(int expectedMessageCount) {
        this.expectedMessageCount = expectedMessageCount;
    }
}
