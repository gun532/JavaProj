package DTO;

import Entities.Message;

public class SendMessageLaterDto extends DtoBase {
    private int receiver;
    private Message msg;

    public SendMessageLaterDto(String func, int receiver, Message msg) {
        super(func);
        this.receiver = receiver;
        this.msg = msg;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public Message getMsg() {
        return msg;
    }

    public void setMsg(Message msg) {
        this.msg = msg;
    }
}
