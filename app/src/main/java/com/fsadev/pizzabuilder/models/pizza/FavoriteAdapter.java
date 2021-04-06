package com.fsadev.pizzabuilder.models.pizza;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.fsadev.pizzabuilder.R;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private final ArrayList<Pizza> listFavorites;
    private FavoriteAdapter.onFavoriteClickListener listener;
    protected Context context;

    //Constructor
    public FavoriteAdapter(ArrayList<Pizza> listFavorites) {
        this.listFavorites = listFavorites;
    }

    //Interface que recibe la posicion del item
    public interface onFavoriteClickListener {
        void onItemClick(int position,int[]loc);
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

    //Clase que crea la vista
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtSauce, txtCheese, txtTopping,txtName;
        private ImageView icon;
        private ConstraintLayout ingredientsLayout, headerLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //textviews
            txtSauce = itemView.findViewById(R.id.favorite_txtSalsa);
            txtCheese = itemView.findViewById(R.id.favorite_txtQueso);
            txtTopping = itemView.findViewById(R.id.favorite_txtToppings);
            txtName = itemView.findViewById(R.id.favorite_txtName);
            //icono
            icon = itemView.findViewById(R.id.favorite_img);
            //layouts
            ingredientsLayout = itemView.findViewById(R.id.favorite_ingredients_layout);
            headerLayout = itemView.findViewById(R.id.favorite_headerLayout);
            headerLayout.setOnClickListener(this::onCardClick);
            //boton añadir al carrito
            itemView.findViewById(R.id.favorite_btnAddToCart).setOnClickListener(this::AddToCart);
        }

        //Manda el callback para añadir al carrito
        private void AddToCart(View view) {
            //Setea el listener
            if (listener!=null){
                int position = getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                    int[] loc = new int[2];
                    view.getLocationOnScreen(loc);
                    listener.onItemClick(position,loc );
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
