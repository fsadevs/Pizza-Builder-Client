package com.fsadev.pizzabuilder.models.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.models.common.Formatear;
import com.fsadev.pizzabuilder.models.pizza.CartAdapter;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    ArrayList<Message> listMessage;

    public MessageAdapter(ArrayList<Message> listMessage) {
        this.listMessage = listMessage;
    }

    @NonNull @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_self_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        holder.txtContent.setText(listMessage.get(position).getContent());
        String hora = Formatear.getTime(listMessage.get(position).getTime().toDate());
        holder.txtTime.setText(hora);
    }

    @Override
    public int getItemCount() {
        return listMessage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtContent,txtTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.message_self_content);
            txtTime = itemView.findViewById(R.id.message_self_time);
        }
    }
}
