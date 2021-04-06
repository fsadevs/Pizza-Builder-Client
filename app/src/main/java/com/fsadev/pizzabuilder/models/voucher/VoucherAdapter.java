package com.fsadev.pizzabuilder.models.voucher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fsadev.pizzabuilder.R;

import java.util.ArrayList;

public class VoucherAdapter extends RecyclerView .Adapter<VoucherAdapter.ViewHolder>{
    ArrayList<Voucher> listVoucher;

    //Constructor
    public VoucherAdapter(ArrayList<Voucher> listVoucher) {
        this.listVoucher = listVoucher;
    }

    @NonNull
    @Override
    public VoucherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher,parent,false);
        return new VoucherAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherAdapter.ViewHolder holder, int position) {
        holder.voucherID.setText(listVoucher.get(position).getVoucherID());
        holder.description.setText(listVoucher.get(position).getDescription());
        String amount = String.valueOf(listVoucher.get(position).getAmount());
        holder.amount.setText(amount);
    }

    @Override
    public int getItemCount() {
        return listVoucher.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView description,amount,voucherID;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            voucherID = itemView.findViewById(R.id.voucher_id);
            description = itemView.findViewById(R.id.voucher_description);
            amount = itemView.findViewById(R.id.voucher_importe);
        }
    }
}
