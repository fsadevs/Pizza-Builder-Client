package com.fsadev.pizzabuilder.models.voucher;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class Voucher {
    private final String voucherID;
    private final String description;
    private final int amount;

    public Voucher(String voucherID, String descripcion, int importe) {
        this.voucherID = voucherID;
        this.description = descripcion;
        this.amount = importe;
    }
    public Voucher(DocumentSnapshot doc){
        voucherID = doc.getId();
        description = doc.getString("descripcion");
        Double mAmount = doc.getDouble("importe");
        amount = Objects.requireNonNull(mAmount).intValue();
    }

    public String getVoucherID() {
        return voucherID;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }
}
