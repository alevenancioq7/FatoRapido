package com.example.fatorapido.models;

import com.google.gson.annotations.SerializedName;

public class FatoResponse
{
    @SerializedName("data")
    private Fato fato;

    public Fato getFato()
    {
        return fato;
    }

    public void setFato(Fato fato)
    {
        this.fato = fato;
    }
}
