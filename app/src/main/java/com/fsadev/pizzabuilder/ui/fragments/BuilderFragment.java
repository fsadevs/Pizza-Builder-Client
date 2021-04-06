package com.fsadev.pizzabuilder.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.models.common.Formatear;
import com.fsadev.pizzabuilder.models.ingredients.Ingredient;
import com.fsadev.pizzabuilder.models.ingredients.IngredientsAdapter;
import com.fsadev.pizzabuilder.models.ingredients.LayeredImageView;
import com.fsadev.pizzabuilder.models.ingredients.ToppingsAdapter;
import com.fsadev.pizzabuilder.models.ingredients.ToppingsList;
import com.fsadev.pizzabuilder.models.pizza.Pizza;
import com.fsadev.pizzabuilder.ui.dialogs.DialogConfirmPizza;
import com.fsadev.pizzabuilder.ui.dialogs.DialogSavePizza;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class BuilderFragment extends Fragment {


    private ImageView imgSalsa, imgQueso;
    private LayeredImageView imgTopping;
    private FrameLayout pizzaLayout;
    private ArrayList<Ingredient> listCheeses, listSauces,listToppings,listIngredients;
    private IngredientsAdapter saucesAdapter,cheesesAdapter;
    private ToppingsAdapter toppingsAdapter;
    private TextView txtPrecio;
    private Double numPrecio = 0.0, numBase = 0.0;
    private int selectedCheese = RecyclerView.NO_POSITION;
    private int selectedSauce = RecyclerView.NO_POSITION;
    private RecyclerView sauceRecyclerView,cheeseRecyclerView, toppingRecyclerView;
    private ProgressBar progressBar;
    private final Fragment fragment = this;
    private ConstraintLayout loaderLayout;
    private CheckBox checkBoxFavorites;

    public BuilderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //Crea las vistas-------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_builder, container, false);
        //Loader layout-----------------------------------------------------------------------------
        loaderLayout = root.findViewById(R.id.include_LaoderLayout);
        progressBar=root.findViewById(R.id.loader_progressBar);
        //Set pizza layout--------------------------------------------------------------------------
        pizzaLayout = root.findViewById(R.id.pizzaLayout);
        //Set imageview
        imgSalsa = root.findViewById(R.id.pb_salsa);
        imgQueso = root.findViewById(R.id.pb_cheese);
        imgTopping = root.findViewById(R.id.pb_toppings);
        //Precio
        txtPrecio = root.findViewById(R.id.pizza_price);
        //------------------------------------------------------------------------------------------
        //Crea las listas
        listCheeses = new ArrayList<>();
        listSauces = new ArrayList<>();
        listToppings = new ArrayList<>();
        listIngredients= new ArrayList<>();
        //Recyclerviews
        sauceRecyclerView = root.findViewById(R.id.salsasRecyclerView);
        sauceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        saucesAdapter = new IngredientsAdapter(listSauces);

        cheeseRecyclerView = root.findViewById(R.id.quesosRecyclerView);
        cheeseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        cheesesAdapter = new IngredientsAdapter(listCheeses);

        toppingRecyclerView = root.findViewById(R.id.toppingsRecyclerView);
        toppingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        toppingsAdapter = new ToppingsAdapter(listToppings);
        //Botones
        root.findViewById(R.id.btn_pedirPizza).setOnClickListener(this::ConfirmPizzaDialog);
        checkBoxFavorites = root.findViewById(R.id.btn_addToFavorites);
        checkBoxFavorites.setOnClickListener(this::AddToFav);
        //Obtinene los ingredientes
        GetIngredients();
        //------------------------------------------------------------------------------------------
        return root;
    }



    //Recibe los ingredientes del menu--------------------------------------------------------------
    private void GetIngredients() {

        //Instancia la colección
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Recibe los ingredientes
        db.collection("Ingredientes").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                //Total de items
                int totalItems = Objects.requireNonNull(task.getResult()).size();
                progressBar.setMax(totalItems);

                //Bucle para recorrer todos los objetos de la base de datos
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                    //Crea el objeto
                    Ingredient ingredient = new Ingredient(document);
                    //Clasifica por tipo
                    clasificaPorTipo(ingredient);
                }
                //Carga de items completada
                SetupIngredients();
            }else{
                //Maneja el error
                Toast.makeText(getContext(), "No se pudo recibir los datos", Toast.LENGTH_SHORT).show();
            }
        });
        //Recibe el precio base
        db.collection("Variables").document("Precios").get()
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful()){
                       numBase = Objects.requireNonNull(Objects.requireNonNull(task.getResult())
                               .getDouble("basico"));
                       numPrecio = numBase;
                       //Actualiza el texto del precio
                       txtPrecio.setText(Formatear.ConvertirAPeso(numBase));
                   }
                });
    }

    //Recibe el tipo y asigna el ingrediente a la lista correspondiente
    private void clasificaPorTipo(Ingredient ingredient) {
        listIngredients.add(ingredient);
        switch (ingredient.getTipo()) {
            case "queso":
                listCheeses.add(ingredient);
                break;
            case "salsa":
                listSauces.add(ingredient);
                break;
            case "topping":
                listToppings.add(ingredient);
                break;
        }
    }

    //Carga los valores de los ingredientes
    private void SetupIngredients() {
        //setea el click listener para el radiobutton
        saucesAdapter.setOnItemClickListener(this::SauceOnClick);
        cheesesAdapter.setOnItemClickListener(this::CheeseOnClick);
        toppingsAdapter.setOnItemClickListener(this::ToppingOnClick);
        //setea el recyclerview con el adaptador correspondiente
        sauceRecyclerView.setAdapter(saucesAdapter);
        cheeseRecyclerView.setAdapter(cheesesAdapter);
        toppingRecyclerView.setAdapter(toppingsAdapter);
        //genera el cache
        GenerateCache();
    }

    //Carga las imágenes de los items al cache de la aplicacion
    private void GenerateCache() {
        new Thread(()->{
            //Progreso actual
            final int i = progressBar.getProgress();
            //Si el progreso ya termino no pedirá indexará más cache
            if (i < progressBar.getMax()) {

                Glide.with(this)
                        .downloadOnly() //Descarga la imágen
                        .load(listIngredients.get(i).getImgURL())
                        .addListener(new RequestListener<File>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                                CloseFragment();
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                                //Si se descarga la imágen vuelve a llamar al método para que cargue otra imágen
                                progressBar.setProgress(i+1);
                                GenerateCache();
                                return false;
                            }
                        }).submit();

            }else{
                //Si ya cargo todas las imágenes cierra el menú
                CloseLoaderLayout();
            }
        }).start();

    }
    //Cierra el fragment
    private void CloseFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    //---------------------------------------------------------------------------------------------

    //Animaciones de la pizza y el precio
    private void AnimateViews() {
        pizzaLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate_pizza));
        txtPrecio.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.fastbounce));
    }

    //Cierra la pantalla de carga cuando termina el indexado
    private void CloseLoaderLayout() {
        requireActivity().runOnUiThread(() -> {
            //Anima el cerrar el layout
            Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.contractvertical);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) { }
                @Override
                public void onAnimationEnd(Animation animation) {
                    //cuando la animacion termina esconde el layout
                    loaderLayout.setVisibility(View.GONE);
                }
                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
            this.loaderLayout.startAnimation(animation);
        });
    }

    /*  El actributo isChecked del queso y la salsa guarda la selección
    Así incluso si la vista es reciclada el valor persiste  */
    //Maneja los clicks en el check de los toppings
    private void ToppingOnClick(int i) {
        boolean check = true;
        if (listToppings.get(i).isChecked()) {
            check = false;
        }
        listToppings.get(i).setChecked(check);
        UpdateToppingLayers();
    }

    //Se encarga de actualizar las capas del layeredImageView
    private void UpdateToppingLayers() {
        //Remueve todas las capas de la imagen
        imgTopping.removeAllLayers();
        //Verifica cuales ingredientes estan seleccionados
        for(Ingredient item : listToppings){
            if (item.isChecked()){
               Glide.with(this).asBitmap().listener(new RequestListener<Bitmap>() {
                   @Override
                   public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                       return false;
                   }
                   @Override
                   public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                       //Cuando carga el recurso añade la capa
                       imgTopping.addLayer(resource);
                       return false;
                   }
               }).load(item.getImgURL()).submit();
            }
        }
        CalculatePrice();
    }

    //Maneja los clicks en el radiobutton de los quesos
    private void CheeseOnClick(int position) {
        if ( selectedCheese!=RecyclerView.NO_POSITION){
            //Saca el check de la selección anterior
            listCheeses.get(selectedCheese).setChecked(false);
            cheesesAdapter.notifyItemChanged(selectedCheese);
        }
        //Actualiza el queso
        UpdateCheese(position);
        //Manda a calcular el precio
        CalculatePrice();
    }

    //Recibe la posicion del elemento y actualiza el queso
    private void UpdateCheese(int position) {
        listCheeses.get(position).setChecked(true);
        selectedCheese=position;
        //carga la imagen
        Glide.with(this).load(listCheeses.get(position).getImgURL())
                .into(imgQueso);
    }

    //Maneja los clicks en el radiobutton de las salsas
    private void SauceOnClick(int position) {
        if (selectedSauce != RecyclerView.NO_POSITION){
            //Saca el check de la selección anterior
            listSauces.get(selectedSauce).setChecked(false);
            saucesAdapter.notifyItemChanged(selectedSauce);
        }
        //Actualiza la salsa
        UpdateSauce(position);
        //Manda a calcular el precio
        CalculatePrice();
    }

    //recibe la posicion del elemento y actualiza la salsa
    private void UpdateSauce(int position) {
        listSauces.get(position).setChecked(true);
        selectedSauce = position;
        Glide.with(this).load(
                listSauces.get(position).getImgURL()).into(imgSalsa);
    }

    //Calcula el precio basado en la selección
    private void CalculatePrice() {
        Double queso = 0.0;
        Double salsa = 0.0;
        Double toppings =0.0;
        //Comprueba que se haya seleccionado algo
        if (selectedCheese!=RecyclerView.NO_POSITION) {
            queso = listCheeses.get(selectedCheese).getPrecio();
        }
        if (selectedSauce!=RecyclerView.NO_POSITION) {
            salsa = listSauces.get(selectedSauce).getPrecio();
        }
        for (Ingredient item : listToppings){
            if (item.isChecked()){
                toppings+=item.getPrecio();
            }
        }
        //Calcula el precio
        numPrecio = queso + salsa + toppings+ numBase;
        //Actualiza el texto
        txtPrecio.setText(Formatear.ConvertirAPeso(numPrecio));
        //Anima los elementos
        AnimateViews();
    }

    //Crea la pizza armada por el usuario
    private Pizza CreatePizza(){
        String salsa ="Ninguna";
        String queso = "Ninguno";
        String toppings = ToppingsList.getStringList(listToppings);
        if (selectedSauce!=RecyclerView.NO_POSITION){
            salsa = listSauces.get(selectedSauce).getNombre();
        }
        if (selectedCheese!=RecyclerView.NO_POSITION) {
            queso = listCheeses.get(selectedCheese).getNombre();
        }
        return new Pizza(salsa,queso,toppings);
    }


    //Maneja y valida los ingredientes de la pizza
    private void ConfirmPizzaDialog(View view){
        Pizza pizza = CreatePizza();
        //Dialogo para confirmar la pizza
        new DialogConfirmPizza(this, pizza, numPrecio).Show();
    }

    //Abre el dialogo para añadir la pizza a favoritos
    private void AddToFav(View view) {
        if (checkBoxFavorites.isChecked()){
            Pizza pizza = CreatePizza();
            new DialogSavePizza(getContext(),pizza);
        }

    }


}