package com.fsadev.pizzabuilder.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.models.pizza.CartPizza;
import com.fsadev.pizzabuilder.models.pizza.FavoriteAdapter;
import com.fsadev.pizzabuilder.models.pizza.ListPizza;
import com.fsadev.pizzabuilder.models.pizza.Pizza;
import com.fsadev.pizzabuilder.models.user.UserInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FavoritesFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Pizza> favoritesList;
    private FavoriteAdapter adapter;
    private Map<String, Double> priceList;
    private Double basePrice;
    private SearchView tbxSearch;


    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);
        //------------------------------------------------------------------------------------------
        //busqueda
        tbxSearch = root.findViewById(R.id.favorites_search);
        //inicializa el adaptador
        favoritesList = new ArrayList<>();
        //recycler
        recyclerView = root.findViewById(R.id.favorites_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        //Metodos
        Initialize();
        getIngredients();
        getBasePrice();

        //------------------------------------------------------------------------------------------
        return root;
    }

    //Inicializa el buscador
    private void onSearchPerformed() {
        tbxSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    //Recibe el precio base--
    private void getBasePrice() {
        FirebaseFirestore.getInstance().collection("Variables")
                .document("Precios").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                basePrice = task.getResult().getDouble("basico");
            }
        });
    }


    //Recibe los ingredientes
    private void getIngredients() {
        FirebaseFirestore.getInstance().collection("Ingredientes").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        priceList = new HashMap<>();
                        for (DocumentSnapshot doc : task.getResult()) {
                            priceList.put(doc.getString("nombre"), doc.getDouble("precio"));
                        }
                    }
                });
    }

    //Recibe la coleccion con los favoritos
    private void Initialize() {
        FirebaseFirestore.getInstance().collection("Usuarios")
                .document(UserInfo.getUserID()).collection("Favoritos")
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                populateRecycler(task.getResult());
            } else {
                errorFetchingFavorites();
            }
        });
    }

    //Maneja el error al obtener los favoritos
    private void errorFetchingFavorites() {
    }

    //Carga los documentos
    private void populateRecycler(QuerySnapshot result) {
        for (DocumentSnapshot doc : result) {
            favoritesList.add(new Pizza(doc));
        }
        //Carga la lista al recycler
        adapter = new FavoriteAdapter(favoritesList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClick(this::addToCart);
        onSearchPerformed();
    }

    //Añade la pizza al carrito
    private void addToCart(int position) {
        if (priceList != null && basePrice != null) {
            //datos de la pizza
            String name = favoritesList.get(position).getName();
            String sauce = favoritesList.get(position).getSauce();
            String cheese = favoritesList.get(position).getCheese();
            String toppings = favoritesList.get(position).getToppings();
            //Separa el string en los diferentes componentes
            String[] toppingList = toppings.split(", ");
            Double price = 0 + basePrice;
            //Busca los precios de los ingredientes del favorito------------------------------------

            //precio de los toppings
            for (String topping : toppingList) {
                try {
                    price += priceList.get(topping);
                } catch (Exception ignored) {
                }
            }
            //precio del queso
            try {
                price += priceList.get(sauce);
            } catch (Exception ignored) {
            }
            //precio de la salsa
            try {
                price += priceList.get(cheese);
            } catch (Exception ignored) {
            }
            //TODO: verificar si todos los ingredientes estan disponibles antes de mandar al checkout
            //Arma el objeto pizza y lo añade a la lista--------------------------------------------
            ListPizza.addPizza(new CartPizza(name, sauce, cheese, toppings, price));
            Toast.makeText(getContext(), "Añadido al carrito", Toast.LENGTH_SHORT).show();
            //Animacion para mostrar que se añadio el item
            Navigation.findNavController(recyclerView).navigate(R.id.action_nav_favorites_to_nav_cart);
        }


    }


}
