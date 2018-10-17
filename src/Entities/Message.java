package Entities;

import Entities.Employee.Employee;

import java.util.Vector;

public class Message {
    Vector<String> allMessages;
    int SenderId;

    public Message(int senderId) {
        this.allMessages = new Vector<>();
        SenderId = senderId;
    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }


    public Vector<String> getAllMessages() {
        return allMessages;
    }

    public int getSender() {
        return SenderId;
    }

    public void setSender(int sender) {
        SenderId = sender;
    }
}
