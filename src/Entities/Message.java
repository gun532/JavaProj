package Entities;

import Entities.Employee.Employee;

public class Message {
    String msg;
    int SenderId;

    public Message(String msg, int SenderId) {
        this.msg = msg;
        this.SenderId = SenderId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getSender() {
        return SenderId;
    }

    public void setSender(int sender) {
        SenderId = sender;
    }
}
