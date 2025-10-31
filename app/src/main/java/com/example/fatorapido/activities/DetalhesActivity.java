package com.example.fatorapido.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.fatorapido.R;
import com.example.fatorapido.models.Fato;
import com.example.fatorapido.utils.Preferencias;
import com.example.fatorapido.utils.ServicoTraducao;
import com.example.fatorapido.utils.Tradutor;

public class DetalhesActivity extends AppCompatActivity
{
    public static final String EXTRA_FATO = "fato";
    private ImageView imageViewDetalhe;
    private TextView textViewCategoriaDetalhe;
    private TextView textViewTextoCompleto;
    private TextView textViewFonte;
    private Switch switchFavorito;
    private Button buttonCompartilhar;
    private Preferencias preferenciasManager;
    private Fato fato;
    private ServicoTraducao servicoTraducao;

    public static void iniciar(Context context, Fato fato)
    {
        Intent intent = new Intent(context, DetalhesActivity.class);
        intent.putExtra(EXTRA_FATO, fato);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        preferenciasManager = new Preferencias(this);
        servicoTraducao = new ServicoTraducao();
        inicializarViews();
        obterDadosIntent();
        preencherDados();
        configurarListeners();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        servicoTraducao.fechar();
    }

    private void inicializarViews()
    {
        imageViewDetalhe = findViewById(R.id.imageViewDetalhe);
        textViewCategoriaDetalhe = findViewById(R.id.textViewCategoriaDetalhe);
        textViewTextoCompleto = findViewById(R.id.textViewTextoCompleto);
        textViewFonte = findViewById(R.id.textViewFonte);
        switchFavorito = findViewById(R.id.switchFavorito);
        buttonCompartilhar = findViewById(R.id.buttonCompartilhar);
    }

    private void obterDadosIntent()
    {
        fato = getIntent().getParcelableExtra(EXTRA_FATO);
        if (fato == null)
        {
            Toast.makeText(this, "Erro ao carregar detalhes do fato.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void preencherDados()
    {
        if(fato == null)
        {
            return;
        }

        setTitle(Tradutor.traduzir("Fact"));

        // Serve para carregar a imagem do fato, se houver
        if(fato.getUrlImagem() != null && !fato.getUrlImagem().isEmpty())
        {
            imageViewDetalhe.setVisibility(View.VISIBLE);
            Glide.with(this).load(fato.getUrlImagem()).placeholder(R.drawable.img1).error(R.drawable.img2)
                    .into(imageViewDetalhe);
        } else {
            imageViewDetalhe.setVisibility(View.GONE);
        }

        textViewCategoriaDetalhe.setText(Tradutor.traduzirCategoria(fato.getCategoria()));
        textViewFonte.setText("Fonte: " + (fato.getFonte() != null ? fato.getFonte() : "Desconhecida"));

        // Serve para traduzir o texto completo do fato
        servicoTraducao.traduzir(fato.getTexto(), new ServicoTraducao.TraducaoListener()
        {
            @Override
            public void onSucesso(String textoTraduzido)
            {
                textViewTextoCompleto.setText(textoTraduzido);
            }

            @Override
            public void onFalha(String erro)
            {
                textViewTextoCompleto.setText(fato.getTexto());
                Toast.makeText(DetalhesActivity.this, "Falha ao traduzir: " + erro, Toast.LENGTH_SHORT).show();
            }
        });

        switchFavorito.setChecked(preferenciasManager.isFavorito(fato));
    }

    private void configurarListeners()
    {
        switchFavorito.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
            {
                preferenciasManager.adicionarFavorito(fato);
                Toast.makeText(DetalhesActivity.this,
                        "Fato adicionado aos favoritos! :)", Toast.LENGTH_SHORT).show();
            } else {
                preferenciasManager.removerFavorito(fato);
                Toast.makeText(DetalhesActivity.this,
                        "Fato removido dos favoritos!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonCompartilhar.setOnClickListener(v -> compartilharFato());
    }

    // Serve para compartilhar o fato
    private void compartilharFato()
    {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Fato Interessante");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Confira este fato interessante: " + textViewTextoCompleto.getText().toString());
        startActivity(Intent.createChooser(shareIntent, "Compartilhar via"));
    }
}