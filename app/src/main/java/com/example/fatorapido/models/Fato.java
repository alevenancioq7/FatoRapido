package com.example.fatorapido.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Fato implements Parcelable
{
    @SerializedName("id")
    private String id;

    @SerializedName("fact")
    private String texto;

    @SerializedName("source")
    private String fonte;

    @SerializedName("category")
    private String categoria;

    @SerializedName("image_url")
    private String urlImagem;

    public Fato() {}

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTexto()
    {
        return texto;
    }

    public void setTexto(String texto)
    {
        this.texto = texto;
    }

    public String getFonte()
    {
        return fonte;
    }

    public void setFonte(String fonte)
    {
        this.fonte = fonte;
    }

    public String getCategoria()
    {
        return categoria;
    }

    public void setCategoria(String categoria)
    {
        this.categoria = categoria;
    }

    public String getUrlImagem()
    {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem)
    {
        this.urlImagem = urlImagem;
    }

    protected Fato(Parcel in)
    {
        id = in.readString();
        texto = in.readString();
        fonte = in.readString();
        categoria = in.readString();
        urlImagem = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeString(texto);
        dest.writeString(fonte);
        dest.writeString(categoria);
        dest.writeString(urlImagem);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<Fato> CREATOR = new Creator<Fato>()
    {
        @Override
        public Fato createFromParcel(Parcel in)
        {
            return new Fato(in);
        }

        @Override
        public Fato[] newArray(int size)
        {
            return new Fato[size];
        }
    };
}