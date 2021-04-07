package com.fsadev.pizzabuilder.models.chat;

import com.google.firebase.database.DataSnapshot;

import java.util.Date;
import java.util.Objects;

public class Message {
    private final String Content;
    private final boolean isMe;
    private final Long Time;
    private boolean isReaded;
    //Constructor para mensajes propios
    public Message(String content) {
        Content = content;
        isMe = true;
        Time = new Date().getTime();
        isReaded = true;
    }
    //Construccion para mensajes del servidor
    public Message(DataSnapshot snapshot) {
        Content = Objects.requireNonNull(snapshot.child("content").getValue().toString());
        this.isMe = (boolean) snapshot.child("me").getValue();
        Time = (Long) snapshot.child("time").getValue();
        isReaded = (boolean) snapshot.child("readed").getValue();
    }

    public String getContent() {
        return Content;
    }

    public boolean isMe() {
        return isMe;
    }

    public Long getTime() {
        return Time;
    }

    public boolean isReaded() {
        return isReaded;
    }

    public void setReaded(boolean readed) {
        isReaded = readed;
    }
}
