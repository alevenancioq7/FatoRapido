package com.example.fatorapido.utils;

import java.util.HashMap;
import java.util.Map;
public class Tradutor
{
    private static final Map<String, String> TRADUCOES = new HashMap<>();

    static
    {
        TRADUCOES.put("fun fact", "Fato Curioso");
        TRADUCOES.put("science", "Ciência");
        TRADUCOES.put("history", "História");
        TRADUCOES.put("technology", "Tecnologia");
        TRADUCOES.put("animals", "Animais");
        TRADUCOES.put("food", "Comida");
        TRADUCOES.put("travel", "Viagem");
        TRADUCOES.put("sports", "Esportes");
        TRADUCOES.put("music", "Música");
        TRADUCOES.put("movies", "Filmes");
        TRADUCOES.put("Facts App", "Fato Rápido");
        TRADUCOES.put("Loading...", "Carregando...");
        TRADUCOES.put("Error", "Erro");
        TRADUCOES.put("No data", "Sem dados");
        TRADUCOES.put("Try again", "Tentar novamente");
        TRADUCOES.put("Fact", "Fato");
        TRADUCOES.put("Category", "Categoria");
        TRADUCOES.put("Source", "Fonte");
    }

    public static String traduzir(String texto)
    {
        if(texto == null)
        {
            return "";
        }
        return TRADUCOES.getOrDefault(texto.toLowerCase(), texto);
    }

    public static String traduzirCategoria(String categoria)
    {
        if(categoria == null || categoria.isEmpty())
        {
            return "Sem categoria";
        }
        return traduzir(categoria);
    }
}