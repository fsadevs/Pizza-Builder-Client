package com.fsadev.pizzabuilder.models.common;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Formatear {
    //toma un double y devulve una string con el formato de peso
    public static String ConvertirAPeso(Double importe){
        DecimalFormat formatter = new DecimalFormat("#0.00");
         String formateado = formatter.format(Double.parseDouble(String.valueOf(importe)));
        return "$ " + formateado.replace(".",",");
    }

    public static String getTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(date);
    }

}
