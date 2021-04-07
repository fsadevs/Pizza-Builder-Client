package com.fsadev.pizzabuilder.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.models.chat.Message;
import com.fsadev.pizzabuilder.models.chat.MessageAdapter;
import com.fsadev.pizzabuilder.models.user.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Message> listMessages;
    private MessageAdapter adapter;
    private EditText tbxMessage;
    private DatabaseReference chatRef;
    private ChildEventListener chatListener;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        //vistas
        listMessages = new ArrayList<>();
        recyclerView = root.findViewById(R.id.chat_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new MessageAdapter(listMessages);
        tbxMessage = root.findViewById(R.id.chat_tbxMessage);
        //Boton enviar msj
        root.findViewById(R.id.chat_sendMessage).setOnClickListener(this::SendMessage);
        //Metodos
        getMessagesFromDatabase();
        //------------------------------------------------------------------------------------------
        return root;
    }

    //Recibe los mensajes
    private void getMessagesFromDatabase() {
        chatRef = FirebaseDatabase.getInstance().getReference().child("Usuarios")
                .child(UserInfo.getUserID()).child("Chat");
        //seteo del listener para los mensajes
       chatListener = new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               //Verifica si el snapshot existe y tiene hijos
               if (snapshot.exists()){
                   //Crea el mensaje
                   Message message = new Message(snapshot);
                   // verifica si es nuevo y lo marca como leido
                   if (!message.isReaded()) {
                       chatRef.child(snapshot.getKey()).child("readed").setValue(true);
                   }
                   //añade el mensaje a la lista
                   listMessages.add(message);
                   //notifica la insercion al adaptador
                   int index = listMessages.size() - 1;
                   adapter.notifyItemInserted(index);
                   //mueve el recycler al ultimo mensaje
                   recyclerView.smoothScrollToPosition(index);
               }
           }
           @Override
           public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
           @Override
           public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
           @Override
           public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
           @Override
           public void onCancelled(@NonNull DatabaseError error) { }
       };
       //Inicializa el listener
       setMessageListener();
        //setea el adaptador
        recyclerView.setAdapter(adapter);
    }

    //Activa el listener
    private void setMessageListener() {
        chatRef.addChildEventListener(chatListener);
    }

    //Envia el mensaje
    private void SendMessage(View view) {
        String content = tbxMessage.getText().toString();
        //Verifica si el mensaje tiene contenido y si la referencia se inicializo correctamente
        if (!content.isEmpty() && chatRef != null){
            Message message = new Message(content);
            //Inserta el mensaje en la base de datos
            chatRef.push().setValue(message);
        }
        //
        tbxMessage.setText("");
    }

}