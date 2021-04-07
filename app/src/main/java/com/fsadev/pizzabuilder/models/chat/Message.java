package com.fsadev.pizzabuilder.models.chat;

import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;

import java.util.Date;

public class Message {
    private final String Content;
    private final boolean isMe;
    private final Timestamp Time;

    //Constructor para mensajes propios
    public Message(String content) {
        Content = content;
        this.isMe = true;
        Time = Timestamp.now();
    }
    //Construccion para mensajes del servidor
    public Message(DataSnapshot snapshot) {
        Content = snapshot.child("content").getValue().toString();
        this.isMe = (boolean) snapshot.child("isMe").getValue();
        Time = (Timestamp) snapshot.child("time").getValue();
    }

    public String getContent() {
        return Content;
    }

    public boolean isMe() {
        return isMe;
    }

    public Timestamp getTime() {
        return Time;
    }
}
