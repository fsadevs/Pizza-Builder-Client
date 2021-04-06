package com.fsadev.pizzabuilder.models.pizza;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.models.common.Formatear;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private final ArrayList<CartPizza> listCartPizzas;
    private CartAdapter.OnItemClickListener mListener;
    private Context context;

    //Constructor que recibe la lista de objetos a crear
    public CartAdapter(ArrayList<CartPizza> list) {
        this.listCartPizzas = list;
    }

    //Maneja el click en el radio button
    public interface OnItemClickListener {
        void onSelectClick(int position);
    }

    //inicializa el listener
    public void setOnItemClickListener(CartAdapter.OnItemClickListener listener) {
        mListener = listener;
    }


    //----------------------------------------------------------------------------------------------
    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Crea el layout para la vista
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    //Carga los datos a la vista y maneja eventos
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        //Nombre
        String nombre = listCartPizzas.get(position).getNombre();
        String pizza;
        if (!nombre.equals("NN")) {
            pizza = listCartPizzas.get(position).getCantidad() + " x " + nombre;
        } else {
            pizza = listCartPizzas.get(position).getCantidad() + " x Pizza " + (position + 1);
        }
        holder.txtNombre.setText(pizza);
        //Ingredientes
        String salsa = "Salsa: " + listCartPizzas.get(position).getSalsa();
        holder.txtSalsa.setText(salsa);
        String queso = "Queso: " + listCartPizzas.get(position).getQueso();
        holder.txtQueso.setText(queso);
        String toppings = "Toppings: " + listCartPizzas.get(position).getToppings();
        holder.txtToppings.setText(toppings);
        //Precio total
        Double total = listCartPizzas.get(position).getPrecio() * listCartPizzas.get(position).getCantidad();
        holder.txtPrecio.setText(Formatear.ConvertirAPeso(total));
    }

    //Devuelve la cantidad de items que haya en la lista
    @Override
    public int getItemCount() {
        return listCartPizzas.size();
    }

    //Genera la vista individual con sus componentes
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSalsa, txtQueso, txtToppings, txtPrecio, txtNombre;
        ConstraintLayout ingredientsLayout, headerLayout;
        ImageView icon;
        Button btnDelete;

        //Crea la vista
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.cart_txtNombrePizza);
            txtPrecio = itemView.findViewById(R.id.cart_txtPrecioPizza);
            txtSalsa = itemView.findViewById(R.id.cart_txtSalsa);
            txtQueso = itemView.findViewById(R.id.cart_txtQueso);
            txtToppings = itemView.findViewById(R.id.cart_txtToppings);
            ingredientsLayout = itemView.findViewById(R.id.cart_ingredients_layout);
            icon = itemView.findViewById(R.id.cart_img);
            headerLayout = itemView.findViewById(R.id.cart_headerLayout);
            headerLayout.setOnClickListener(this::cardOnClick);
            btnDelete = itemView.findViewById(R.id.cart_btnDelete);
            btnDelete.setOnClickListener(this::DeleteItem);
        }

        private void DeleteItem(View view) {
            //Setea el listener
            if (mListener!=null){
                int position = getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                    mListener.onSelectClick(position);
                }
            }
        }

        //Controla los clicks en el cardView
        private void cardOnClick(View view) {
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
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ingredientsLayout.setVisibility(View.GONE);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
                //Animacion del layout
                ingredientsLayout.startAnimation(animation);
                //Animacion del boton
                icon.animate().rotationBy(-90).setDuration(200).start();

            }

        }
    }
}


