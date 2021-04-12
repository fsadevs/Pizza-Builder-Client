package com.fsadev.pizzabuilder.models.pizza;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.fsadev.pizzabuilder.R;

import java.util.ArrayList;
import java.util.Collection;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> implements Filterable {
    private final ArrayList<Pizza> listFavorites;
    private final ArrayList<Pizza> fullList;
    private FavoriteAdapter.onFavoriteClickListener listener;
    protected Context context;

    //Constructor
    public FavoriteAdapter(ArrayList<Pizza> list) {
        fullList = list;
        listFavorites = new ArrayList<>(fullList);
    }

    //Interface que recibe la posicion del item
    public interface onFavoriteClickListener {
        void onItemClick(int position);
    }

    //Metodo publico para vincular el click
    public void setOnItemClick(onFavoriteClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Crea el layout para la vista
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtSauce.setText(String.format("Salsa: %s", listFavorites.get(position).getSauce()));
        holder.txtTopping.setText(String.format("Toppings: %s", listFavorites.get(position).getToppings()));
        holder.txtCheese.setText(String.format("Queso: %s", listFavorites.get(position).getCheese()));
        holder.txtName.setText(listFavorites.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return listFavorites.size();
    }


    //Filtros para la busqueda----------------------------------------------------------------------
    @Override
    public Filter getFilter() {
        return favoriteFilter;
    }
    //Filtro
    private final Filter favoriteFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Pizza> filteredList = new ArrayList<>();
            //en caso de que el constrait este nulo o vacio retorna la lista entera
            if (constraint== null || constraint.length()==0){
                filteredList.addAll(fullList);
            }else{
                //convierte el constrait en una string en minusculas y sin espacios
                String filterPattern = constraint.toString().toLowerCase().trim();
                //compara el string con la lista de pizzas
                for (Pizza pizza : fullList){
                    String name = pizza.getName().toLowerCase();
                    //si el item coincide con la busqueda lo añade
                    if (name.contains(filterPattern)){
                        filteredList.add(pizza);
                    }
                }
            }
            //Crea el resultado de la busqueda con la lista filtrada
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //limpia la lista
            listFavorites.clear();
            //llena la lista con los resultados
            listFavorites.addAll((Collection<? extends Pizza>) results.values);
            notifyDataSetChanged();
        }
    };

    //Clase que crea la vista-----------------------------------------------------------------------
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtSauce;
        private final TextView txtCheese;
        private final TextView txtTopping;
        private final TextView txtName;
        private final ImageView icon;
        private final ConstraintLayout ingredientsLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //textviews
            txtSauce = itemView.findViewById(R.id.favoriteItem_sauce);
            txtCheese = itemView.findViewById(R.id.favoriteItem_cheese);
            txtTopping = itemView.findViewById(R.id.favoriteItem_toppings);
            txtName = itemView.findViewById(R.id.favoriteItem_name);
            //icono
            icon = itemView.findViewById(R.id.favoriteItem_img);
            //layouts
            ingredientsLayout = itemView.findViewById(R.id.favorite_ingredients_layout);
            ConstraintLayout headerLayout = itemView.findViewById(R.id.favorite_headerLayout);
            headerLayout.setOnClickListener(this::onCardClick);
            //boton añadir al carrito
            itemView.findViewById(R.id.favoriteItem_btnAddToCart).setOnClickListener(this::AddToCart);
        }

        //Manda el callback para añadir al carrito
        private void AddToCart(View view) {
            //Setea el listener
            if (listener!=null){
                int position = getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                    listener.onItemClick(position);
                }
            }
        }

        //Muestra u oculta los ingredientes
        private void onCardClick(View view) {
            //Controla las animaciones de la lista de ingredientes y el icono
            if (ingredientsLayout.getVisibility() == View.GONE) {
                //Animacion del layout
                ingredientsLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.list_expand));
                ingredientsLayout.setVisibility(View.VISIBLE);
                //Animacion del boton
                icon.animate().rotationBy(90).setDuration(200).setInterpolator(new LinearInterpolator()).start();

            } else {
                //Listener para que cuando se ejecute la animacion tambien oculte el layout
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.list_contract);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ingredientsLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                //Animacion del layout
                ingredientsLayout.startAnimation(animation);
                //Animacion del boton
                icon.animate().rotationBy(-90).setDuration(200).start();

            }
        }
    }
}
