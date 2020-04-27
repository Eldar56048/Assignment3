package com.company;

import java.util.Date;

public class Message {
    private int senderid,receiverid,messageid;
    String Message,date,time;
    public Message(int senderid,int receiverid,int messageid,String Message,String date,String time){
        setSenderid(senderid);
        setReceiverid(receiverid);
        setMessageid(messageid);
        setMessage(Message);
        setDate(date);
        setTime(time);
    }

    public Message(int senderid,int receiverid,int messageid,String Message,String date,String time,String acc){
        messageid=messageid;
        setSenderid(senderid);
        setReceiverid(receiverid);
        setMessageid(messageid);
        setMessage(Message);
        setDate(date);
        setTime(time);
    }

    public void setSenderid(int senderid) {
        this.senderid = senderid;
    }

    public int getSenderid() {
        return senderid;
    }

    public void setReceiverid(int receiverid) {
        this.receiverid = receiverid;
    }

    public int getReceiverid() {
        return receiverid;
    }

    public void setMessageid(int messageid) {
        this.messageid = messageid;
    }

    public int getMessageid() {
        return messageid;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getMessage() {
        return Message;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return senderid+" "+receiverid+" "+messageid+" "+Message+" "+date+" "+time;
    }
}
