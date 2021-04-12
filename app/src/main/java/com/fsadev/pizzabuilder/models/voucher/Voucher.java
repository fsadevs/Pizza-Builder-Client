package com.fsadev.pizzabuilder.models.voucher;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class Voucher {
    private final String VoucherID;
    private final String Name;
    private final int Discount;
    private final int Price;
    private final String Type;

    public Voucher(String voucherID, String type, String name, int discount, int price) {
        VoucherID = voucherID;
        Name = name;
        Discount = discount;
        Price = price;
        Type = type;
    }
    public Voucher(DocumentSnapshot doc){
        VoucherID = doc.getId();
        Name = doc.getString("nombre");
        Discount = Objects.requireNonNull(doc.getDouble("descuento")).intValue();
        Price = Objects.requireNonNull(doc.getDouble("precio")).intValue();
        Type = doc.getString("tipo");
    }

    public String getVoucherID() {
        return VoucherID;
    }

    public String getName() {
        return Name;
    }

    public int getDiscount() {
        return Discount;
    }

    public int getPrice() {
        return Price;
    }

    public String getType() {
        return Type;
    }
}
