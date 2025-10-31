package com.example.fatorapido.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fatorapido.R;
import com.example.fatorapido.models.Fato;
import com.example.fatorapido.utils.ServicoTraducao;

import java.util.List;

public class FavoritoAdapter extends RecyclerView.Adapter<FavoritoAdapter.FavoritoViewHolder>
{
    private final List<Fato> favoritos;
    private final OnItemRemoveListener removeListener;
    private final ServicoTraducao servicoTraducao;

    public interface OnItemRemoveListener
    {
        void onItemRemove(Fato fato);
    }

    public FavoritoAdapter(List<Fato> favoritos, OnItemRemoveListener removeListener)
    {
        this.favoritos = favoritos;
        this.removeListener = removeListener;
        this.servicoTraducao = new ServicoTraducao();
    }

    @NonNull
    @Override
    public FavoritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorito, parent, false);
        return new FavoritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritoViewHolder holder, int position)
    {
        Fato fato = favoritos.get(position);
        holder.bind(fato, removeListener, servicoTraducao);
    }

    @Override
    public int getItemCount()
    {
        return favoritos != null ? favoritos.size() : 0;
    }

    public void fecharTradutor()
    {
        servicoTraducao.fechar();
    }

    static class FavoritoViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView textViewFato;
        private final Button buttonRemover;

        public FavoritoViewHolder(@NonNull View itemView)
        {
            super(itemView);
            textViewFato = itemView.findViewById(R.id.textViewFatoFavorito);
            buttonRemover = itemView.findViewById(R.id.buttonRemoverFavorito);
        }

        public void bind(final Fato fato, final OnItemRemoveListener listener, final ServicoTraducao servicoTraducao)
        {
            servicoTraducao.traduzir(fato.getTexto(), new ServicoTraducao.TraducaoListener()
            {
                @Override
                public void onSucesso(String textoTraduzido)
                {
                    textViewFato.setText(textoTraduzido);
                }

                @Override
                public void onFalha(String erro)
                {
                    textViewFato.setText(fato.getTexto());
                }
            });
            buttonRemover.setOnClickListener(v -> listener.onItemRemove(fato));
        }
    }
}