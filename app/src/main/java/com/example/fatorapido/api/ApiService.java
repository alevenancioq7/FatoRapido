package com.example.fatorapido.api;

import com.example.fatorapido.models.Fato;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService
{
    // Serve para buscar um fato aleatório
    @GET("api/facts/random")
    Call<Fato> buscarFatoAleatorio();

    // Serve para buscar fatos de uma categoria específica
    @GET("api/facts/category/{category}")
    Call<List<Fato>> buscarFatosPorCategoria(@Path("category") String category);
}