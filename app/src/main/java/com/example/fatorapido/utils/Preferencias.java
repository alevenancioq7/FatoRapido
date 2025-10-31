package com.example.fatorapido.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.fatorapido.models.Fato;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Preferencias
{

    private static final String PREF_NAME = "FatoRapido";
    private static final String KEY_ULTIMA_BUSCA = "ultima_busca";
    private static final String KEY_FAVORITOS = "favoritos_lista";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public Preferencias(Context context)
    {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    // Serve para salvar a data e hora da última busca
    public void salvarUltimaBusca(String dataHora)
    {
        sharedPreferences.edit().putString(KEY_ULTIMA_BUSCA, dataHora).apply();
    }

    // Serve para recuperar a data e hora da última busca
    public String getUltimaBusca()
    {
        return sharedPreferences.getString(KEY_ULTIMA_BUSCA, "Nunca");
    }

    public List<Fato> getFavoritos()
    {
        String json = sharedPreferences.getString(KEY_FAVORITOS, null);
        if(json == null)
        {
            return new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<Fato>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private void salvarListaFavoritos(List<Fato> favoritos)
    {
        String json = gson.toJson(favoritos);
        sharedPreferences.edit().putString(KEY_FAVORITOS, json).apply();
    }

    public void adicionarFavorito(Fato fato)
    {
        List<Fato> favoritos = getFavoritos();
        if(!isFavorito(fato))
        {
            favoritos.add(fato);
            salvarListaFavoritos(favoritos);
        }
    }

    public void removerFavorito(Fato fato)
    {
        List<Fato> favoritos = getFavoritos();
        favoritos.removeIf(f -> f.getId().equals(fato.getId()));
        salvarListaFavoritos(favoritos);
    }

    public boolean isFavorito(Fato fato)
    {
        List<Fato> favoritos = getFavoritos();
        for(Fato favorito : favoritos)
        {
            if(favorito.getId().equals(fato.getId()))
            {
                return true;
            }
        }
        return false;
    }
}