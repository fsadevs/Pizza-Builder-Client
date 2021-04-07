package com.fsadev.pizzabuilder.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.models.chat.Message;
import com.fsadev.pizzabuilder.models.chat.MessageAdapter;

import java.util.ArrayList;


public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Message> listMessages;
    private MessageAdapter adapter;
    private EditText tbxMessage;

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
        //setea el adaptador
        recyclerView.setAdapter(adapter);
    }

    //Envia el mensaje
    private void SendMessage(View view) {
        String content = tbxMessage.getText().toString();
        if (!content.isEmpty()){
            listMessages.add(new Message(content));
            adapter.notifyItemInserted(listMessages.size() - 1);
        }
        tbxMessage.setText("");
    }
}