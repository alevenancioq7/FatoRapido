package com.example.fatorapido.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.fatorapido.R;
import com.example.fatorapido.models.Fato;
import com.example.fatorapido.utils.ServicoTraducao;
import com.example.fatorapido.utils.Tradutor;
import java.util.List;

public class FatoAdapter extends RecyclerView.Adapter<FatoAdapter.FatoViewHolder>
{
    private final List<Fato> listaFatos;
    private final OnItemClickListener listener;
    private final ServicoTraducao servicoTraducao;

    public interface OnItemClickListener
    {
        void onItemClick(Fato fato);
    }

    public FatoAdapter(List<Fato> listaFatos, OnItemClickListener listener)
    {
        this.listaFatos = listaFatos;
        this.listener = listener;
        this.servicoTraducao = new ServicoTraducao();
    }

    @NonNull
    @Override
    public FatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fato, parent, false);
        return new FatoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FatoViewHolder holder, int position)
    {
        Fato fato = listaFatos.get(position);
        holder.bind(fato, listener, servicoTraducao);
    }

    @Override
    public int getItemCount()
    {
        return listaFatos != null ? listaFatos.size() : 0;
    }

    public void fecharTradutor()
    {
        servicoTraducao.fechar();
    }

    static class FatoViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView textViewTitulo;
        private final TextView textViewCategoria;
        private final ImageView imageViewFato;

        public FatoViewHolder(@NonNull View itemView)
        {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.textViewTitulo);
            textViewCategoria = itemView.findViewById(R.id.textViewCategoria);
            imageViewFato = itemView.findViewById(R.id.imageViewFato);
        }

        public void bind(final Fato fato, final OnItemClickListener listener, final ServicoTraducao servicoTraducao)
        {
            String textoOriginal = fato.getTexto();

            if(textoOriginal != null && !textoOriginal.isEmpty())
            {
                String textoResumido = textoOriginal.length() > 100 ? textoOriginal.substring(0, 100) + "..." : textoOriginal;
                textViewTitulo.setText(textoResumido);

                servicoTraducao.traduzir(textoOriginal, new ServicoTraducao.TraducaoListener()
                {
                    @Override
                    public void onSucesso(String textoTraduzido)
                    {
                        String textoResumidoTraduzido = textoTraduzido.length() > 100 ? textoTraduzido.substring(0, 100) + "..." : textoTraduzido;
                        textViewTitulo.setText(textoResumidoTraduzido);
                    }

                    @Override
                    public void onFalha(String erro) {}
                });
            } else {
                textViewTitulo.setText("(Fato sem texto)");
            }

            textViewCategoria.setText(Tradutor.traduzirCategoria(fato.getCategoria()));

            // Serve para carregar a imagem do fato
            if(fato.getUrlImagem() != null && !fato.getUrlImagem().isEmpty())
            {
                imageViewFato.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext()).load(fato.getUrlImagem()).placeholder(R.drawable.img1).error(R.drawable.img2)
                        .into(imageViewFato);
            } else {
                imageViewFato.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> listener.onItemClick(fato));
        }
    }
}