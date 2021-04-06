package com.fsadev.pizzabuilder.models.ingredients;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fsadev.pizzabuilder.R;

import java.util.ArrayList;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder>{

    private final ArrayList<Ingredient> listIngredients;
    private OnItemClickListener mListener;

    //Constructor que recibe la lista de objetos a crear
    public IngredientsAdapter(ArrayList<Ingredient> list){
        this.listIngredients = list;
    }

    //Maneja el click en el radio button
    public interface OnItemClickListener{
        void onSelectClick(int position);
    }
    //inicializa el listener
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    //----------------------------------------------------------------------------------------------
    @NonNull
    @Override
    public IngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Crea el layout para la vista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient,parent,false);
        return new ViewHolder(view);
    }
    //Carga los datos a la vista y maneja eventos
    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapter.ViewHolder holder, int position) {
        //cambia el nombre por el del ingrediente
        holder.radioButton.setText(listIngredients.get(position).getNombre());
        //maneja la disponibilidad, si es falso desactiva el radiobutton
        holder.radioButton.setEnabled(listIngredients.get(position).getDisponibilidad());
        //maneja el check
        holder.radioButton.setChecked(listIngredients.get(position).isChecked());
    }

    //Devuelve la cantidad de items que haya en la lista
    @Override
    public int getItemCount() {
        return listIngredients.size();
    }

    //Genera la vista individual con sus componentes
    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.item_ingredient_RadioButton);
            //onClick para el radio button
            radioButton.setOnClickListener(v -> {
                if (mListener!=null){
                    int position = getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION){
                        mListener.onSelectClick(position);
                    }
                }
            });
        }
    }


}
