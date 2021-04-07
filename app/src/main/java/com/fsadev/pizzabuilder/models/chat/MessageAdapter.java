package com.fsadev.pizzabuilder.models.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.models.common.Formatear;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private final ArrayList<Message> listMessage;
    boolean isMe = false;

    public MessageAdapter(ArrayList<Message> listMessage) {
        this.listMessage = listMessage;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==1) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_self_message, parent, false));
        }else{
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_server_message, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (listMessage.get(position).isMe()){
            //Si es propio retorna 1
            return 1;
        }else{
            //si es del servidor retorna 0
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            isMe = listMessage.get(position).isMe();
        if (isMe) {
            holder.selfContent.setText(listMessage.get(position).getContent());
            String hora = Formatear.getTime(listMessage.get(position).getTime());
            holder.selfTime.setText(hora);
        }else{
            holder.serverContent.setText(listMessage.get(position).getContent());
            String hora = Formatear.getTime(listMessage.get(position).getTime());
            holder.serverTime.setText(hora);
        }
    }

    @Override
    public int getItemCount() {
        return listMessage.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView selfContent;
        private final TextView selfTime;
        private final TextView serverContent;
        private final TextView serverTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            selfContent = itemView.findViewById(R.id.message_self_content);
            selfTime = itemView.findViewById(R.id.message_self_time);
            serverContent = itemView.findViewById(R.id.message_server_content);
            serverTime = itemView.findViewById(R.id.message_server_time);
        }
    }
}
