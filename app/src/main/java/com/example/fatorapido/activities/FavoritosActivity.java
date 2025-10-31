package com.example.fatorapido.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fatorapido.R;
import com.example.fatorapido.adapter.FavoritoAdapter;
import com.example.fatorapido.models.Fato;
import com.example.fatorapido.utils.Preferencias;
import java.util.List;

public class FavoritosActivity extends AppCompatActivity implements FavoritoAdapter.OnItemRemoveListener
{
    private RecyclerView recyclerViewFavoritos;
    private TextView textViewSemFavoritos;
    private Preferencias preferenciasManager;
    private List<Fato> listaFavoritos;
    private FavoritoAdapter favoritoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        setTitle("Meus Favoritos");
        preferenciasManager = new Preferencias(this);
        inicializarViews();
        carregarFavoritos();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(favoritoAdapter != null)
        {
            favoritoAdapter.fecharTradutor();
        }
    }

    private void inicializarViews()
    {
        recyclerViewFavoritos = findViewById(R.id.recyclerViewFavoritos);
        textViewSemFavoritos = findViewById(R.id.textViewSemFavoritos);
        recyclerViewFavoritos.setLayoutManager(new LinearLayoutManager(this));
    }

   // Serve para carregar a lista de favoritos
    private void carregarFavoritos()
    {
        listaFavoritos = preferenciasManager.getFavoritos();

        if(listaFavoritos.isEmpty())
        {
            textViewSemFavoritos.setVisibility(View.VISIBLE);
            recyclerViewFavoritos.setVisibility(View.GONE);
        } else {
            textViewSemFavoritos.setVisibility(View.GONE);
            recyclerViewFavoritos.setVisibility(View.VISIBLE);
            favoritoAdapter = new FavoritoAdapter(listaFavoritos, this);
            recyclerViewFavoritos.setAdapter(favoritoAdapter);
        }
    }

    @Override
    public void onItemRemove(Fato fato)
    {
        preferenciasManager.removerFavorito(fato);
        carregarFavoritos();
        Toast.makeText(this, "Favorito removido!", Toast.LENGTH_SHORT).show();
    }
}