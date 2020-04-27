package com.company;

public class Friends {
    private int senderid, recieverid;
    private String status, request;

    public Friends(int senderid, int recieverid, String status, String request) {
        setSenderid(senderid);
        setRecieverid(recieverid);
        setStatus(status);
        setRequest(request);
    }

    public void setSenderid(int senderid) {
        this.senderid = senderid;
    }

    public int getSenderid() {
        return senderid;
    }

    public void setRecieverid(int recieverid) {
        this.recieverid = recieverid;
    }

    public int getRecieverid() {
        return recieverid;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }

    @Override
    public String toString() {
        return senderid+" "+recieverid+" "+status+" "+request;
    }
}
