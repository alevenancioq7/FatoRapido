package com.example.fatorapido.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fatorapido.R;
import com.example.fatorapido.adapter.FatoAdapter;
import com.example.fatorapido.api.ApiClient;
import com.example.fatorapido.api.ApiService;
import com.example.fatorapido.models.Fato;
import com.example.fatorapido.utils.Preferencias;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{
    private RecyclerView recyclerViewFatos;
    private ProgressBar progressBar;
    private Button buttonFatoAleatorio;
    private Button buttonPorCategoria;
    private Button buttonVerFavoritos;
    private Spinner spinnerCategorias;
    private TextView textViewUltimaBusca;
    private Preferencias preferenciasManager;
    private FatoAdapter fatoAdapter;
    private List<Fato> listaFatosExibidos;
    private ApiService apiService;
    private Map<String, String> mapaCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferenciasManager = new Preferencias(this);
        apiService = ApiClient.getApiService();
        listaFatosExibidos = new ArrayList<>();
        inicializarViews();
        configurarRecyclerView();
        configurarSpinner();
        configurarListeners();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if(fatoAdapter != null)
        {
            fatoAdapter.fecharTradutor();
        }
    }

    private void inicializarViews()
    {
        recyclerViewFatos = findViewById(R.id.recyclerViewFatos);
        progressBar = findViewById(R.id.progressBar);
        buttonFatoAleatorio = findViewById(R.id.buttonFatoAleatorio);
        buttonPorCategoria = findViewById(R.id.buttonPorCategoria);
        spinnerCategorias = findViewById(R.id.spinnerCategorias);
        textViewUltimaBusca = findViewById(R.id.textViewUltimaBusca);
        buttonVerFavoritos = findViewById(R.id.buttonVerFavoritos);
        atualizarDataUltimaBusca();
    }

    private void configurarRecyclerView()
    {
        recyclerViewFatos.setLayoutManager(new LinearLayoutManager(this));
        fatoAdapter = new FatoAdapter(listaFatosExibidos, this::abrirDetalhesFato);
        recyclerViewFatos.setAdapter(fatoAdapter);
    }

    private void configurarSpinner()
    {
        mapaCategorias = new LinkedHashMap<>();
        mapaCategorias.put("Ciência", "science");
        mapaCategorias.put("História", "history");
        mapaCategorias.put("Tecnologia", "technology");
        mapaCategorias.put("Animais", "animals");
        mapaCategorias.put("Comida", "food");
        mapaCategorias.put("Viagem", "travel");
        mapaCategorias.put("Esportes", "sports");

        List<String> categoriasExibicao = new ArrayList<>(mapaCategorias.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriasExibicao);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorias.setAdapter(adapter);
    }

    private void configurarListeners()
    {
        buttonFatoAleatorio.setOnClickListener(v -> buscarFatoAleatorio());

        buttonPorCategoria.setOnClickListener(v -> {
            String categoriaEmPortugues = spinnerCategorias.getSelectedItem().toString();
            String categoriaParaApi = mapaCategorias.get(categoriaEmPortugues);
            buscarFatoPorCategoria(categoriaParaApi);
        });

        buttonVerFavoritos.setOnClickListener(v -> abrirTelaFavoritos());
    }

    private void abrirTelaFavoritos()
    {
        Intent intent = new Intent(this, FavoritosActivity.class);
        startActivity(intent);
    }

    private void adicionarFatoNaLista(Fato fato)
    {
        if(fato == null)
        {
            Toast.makeText(this, "Não foi possível encontrar um fato correspondente! :(", Toast.LENGTH_SHORT).show();
            return;
        }
        listaFatosExibidos.add(0, fato);
        fatoAdapter.notifyItemInserted(0);
        recyclerViewFatos.scrollToPosition(0);
        salvarDataUltimaBusca();
        Toast.makeText(MainActivity.this, "Novo fato adicionado!", Toast.LENGTH_SHORT).show();
    }

    // Serve para buscar um fato aleatório
    private void buscarFatoAleatorio()
    {
        mostrarProgresso(true);
        Call<Fato> call = apiService.buscarFatoAleatorio();

        call.enqueue(new Callback<Fato>()
        {
            @Override
            public void onResponse(Call<Fato> call, Response<Fato> response)
            {
                mostrarProgresso(false);
                if(response.isSuccessful() && response.body() != null)
                {
                    adicionarFatoNaLista(response.body());
                } else {
                    Toast.makeText(MainActivity.this, "Erro ao buscar fato aleatório! :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Fato> call, Throwable t)
            {
                mostrarProgresso(false);
                Toast.makeText(MainActivity.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Serve para buscar o fato por categoria
    private void buscarFatoPorCategoria(String categoria)
    {
        mostrarProgresso(true);
        Call<List<Fato>> call = apiService.buscarFatosPorCategoria(categoria);

        call.enqueue(new Callback<List<Fato>>()
        {
            @Override
            public void onResponse(Call<List<Fato>> call, Response<List<Fato>> response)
            {
                mostrarProgresso(false);
                if(response.isSuccessful() && response.body() != null && !response.body().isEmpty())
                {
                    List<Fato> fatosDaCategoria = response.body();
                    Collections.shuffle(fatosDaCategoria);
                    adicionarFatoNaLista(fatosDaCategoria.get(0));
                } else {
                    Toast.makeText(MainActivity.this, "Não encontrei fatos para a categoria: " + categoria, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Fato>> call, Throwable t)
            {
                mostrarProgresso(false);
                Toast.makeText(MainActivity.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarProgresso(boolean exibir)
    {
        progressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    // Serve para salvar a data e hora da busca
    private void salvarDataUltimaBusca()
    {
        String dataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        preferenciasManager.salvarUltimaBusca(dataHora);
        atualizarDataUltimaBusca();
    }

    private void atualizarDataUltimaBusca()
    {
        textViewUltimaBusca.setText("Última busca: " + preferenciasManager.getUltimaBusca());
    }

    // Serve para abrir a tela de detalhes do fato
    private void abrirDetalhesFato(Fato fato)
    {
        DetalhesActivity.iniciar(this, fato);
    }
}