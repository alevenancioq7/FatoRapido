package com.example.fatorapido.utils;

import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class ServicoTraducao
{
    private final Translator translator;

    public interface TraducaoListener
    {
        void onSucesso(String textoTraduzido);
        void onFalha(String erro);
    }

    public ServicoTraducao()
    {
        TranslatorOptions options = new TranslatorOptions.Builder().setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.PORTUGUESE)
                .build();
        this.translator = Translation.getClient(options);
    }

    public void traduzir(String texto, TraducaoListener listener)
    {
        if(texto == null || texto.isEmpty())
        {
            listener.onSucesso("");
            return;
        }

        DownloadConditions conditions = new DownloadConditions.Builder().requireWifi().build();

        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(aVoid -> {translator.translate(texto)
                            .addOnSuccessListener(listener::onSucesso)
                            .addOnFailureListener(e -> listener.onFalha(e.getMessage()));
                })
                .addOnFailureListener(e -> listener.onFalha("Falha ao baixar modelo de tradução: " + e.getMessage()));
    }

    public void fechar()
    {
        translator.close();
    }
}