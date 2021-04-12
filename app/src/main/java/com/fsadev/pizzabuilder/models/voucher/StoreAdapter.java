package com.fsadev.pizzabuilder.models.voucher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.models.pizza.FavoriteAdapter;

import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {
    private final ArrayList<Voucher> itemList;
    private StoreAdapter.onItemClickListener mListener;

    public StoreAdapter(ArrayList<Voucher> itemList) {
        this.itemList = itemList;
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemName.setText(itemList.get(position).getName());
        holder.itemPrice.setText(String.valueOf(itemList.get(position).getPrice()));
        String type = itemList.get(position).getType();
        if (type.equals("pizza")){
            holder.itemIcon.setImageResource(R.drawable.ic_voucher_discount);
        }else if(type.equals("delivery")){
            holder.itemIcon.setImageResource(R.drawable.ic_voucher_delivery);
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemName;
        private final TextView itemPrice;
        private final ImageView itemIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.storeItem_name);
            itemPrice = itemView.findViewById(R.id.storeItem_price);
            itemIcon = itemView.findViewById(R.id.storeItem_icon);
            itemView.findViewById(R.id.storeItem_card).setOnClickListener(this::Purchase);
        }
        // Compra el item
        private void Purchase(View view) {
            if (mListener!=null){
                int position = getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                    mListener.onClick(position);
                }
            }
        }
    }
    // Interface
    public interface onItemClickListener{
        void onClick(int position);
    }
    public void addOnItemClickListener(onItemClickListener listener){
        mListener = listener;
    }
}
