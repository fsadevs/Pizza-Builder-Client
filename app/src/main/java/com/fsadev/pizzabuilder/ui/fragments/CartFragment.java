package com.fsadev.pizzabuilder.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fsadev.pizzabuilder.R;
import com.fsadev.pizzabuilder.models.common.Formatear;
import com.fsadev.pizzabuilder.models.pizza.CartAdapter;
import com.fsadev.pizzabuilder.models.pizza.CartPizza;
import com.fsadev.pizzabuilder.models.pizza.ListPizza;
import com.fsadev.pizzabuilder.ui.activities.CheckoutActivity;


public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private Button btnConfirm,btnGoToBuilder;
    private Double totalPrice = 0.0;
    private ConstraintLayout emptyCartLayout;


    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_cart, container, false);
        //------------------------------------------------------------------------------------------
        //Recycler
        recyclerView = root.findViewById(R.id.cart_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new CartAdapter(ListPizza.getList());
        //Button
        btnConfirm = root.findViewById(R.id.cart_btnConfirm);
        btnConfirm.setOnClickListener(this::goToCheckout);
        btnGoToBuilder = root.findViewById(R.id.cart_btnGoToBuilder);
        btnGoToBuilder.setOnClickListener(this::goToBuilder);
        root.findViewById(R.id.emptyCart_goToBuilder).setOnClickListener(this::goToBuilder);
        //Layout de carrito vacio
        emptyCartLayout = root.findViewById(R.id.layout_empty_cart);
        //-----------------------------------------------------------------------------------------
        Initialize();
        //------------------------------------------------------------------------------------------
        return root;
    }
    //Manda al checkout
    private void goToCheckout(View view) {
        startActivity(new Intent(requireActivity(), CheckoutActivity.class));
        requireActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    //Oculta el layout de carrito vacío si es que hay algun item en la lista
    private void HideEmptyCartLayout() {
        emptyCartLayout.setVisibility(View.GONE);
        btnConfirm.setVisibility(View.VISIBLE);
        btnGoToBuilder.setVisibility(View.VISIBLE);
    }
    //Muestra el layout de carrito vacío si no hay ningun item en la lista
    private void ShowEmptyCartLayout() {
        emptyCartLayout.setVisibility(View.VISIBLE);
        btnConfirm.setVisibility(View.INVISIBLE);
        btnGoToBuilder.setVisibility(View.INVISIBLE);
    }

    //Lleva al builder
    private void goToBuilder(View view) {
        Navigation.findNavController(view).navigate(R.id.action_nav_cart_to_nav_builder);
    }

    //Inicia la carga de datos
    private void Initialize() {
        //Comprueba que la lista no este vacía
        if (ListPizza.getList().size() > 0){
            HideEmptyCartLayout();
            //Carga el adaptador al recyclerview
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(this::DeleteItemFromRecycler);
            //Calcula el total
            CalculateTotal();
        }else{
            ShowEmptyCartLayout();
        }
    }

    //Maneja los clicks en las cardView
    private void DeleteItemFromRecycler(int i) {
        //Remueve el item de la lista
        ListPizza.removePizza(i);
        adapter.notifyItemRemoved(i);
        //Vuelve a calcular el precio
        if (ListPizza.getList().size()==0){
            ShowEmptyCartLayout();
        }
        //Vuelve a calcular el total
        CalculateTotal();

    }

    //Calcula el precio total de los items y lo pone en el boton
    private void CalculateTotal() {
        //resetea el valor
        totalPrice = 0.0;
        for(CartPizza item : ListPizza.getList()){
            totalPrice += (item.getPrecio()*item.getCantidad());
        }
        ChangeButtonText();
    }

    //Cambia el texto del boton
    private void ChangeButtonText() {
        String btnText = "Continuar al pago (" + Formatear.ConvertirAPeso(totalPrice) + ")";
        btnConfirm.setText(btnText);
    }

    @Override
    public void onResume() {
        super.onResume();
        Initialize();
    }
}