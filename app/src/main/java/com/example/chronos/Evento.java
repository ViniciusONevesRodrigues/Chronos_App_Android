package com.example.chronos;

import android.graphics.Bitmap;

public class Evento {
    private String nome;
    private String dataHora;
    private Bitmap imagem;

    public Evento(String nome, String dataHora, Bitmap imagem) {
        this.nome = nome;
        this.dataHora = dataHora;
        this.imagem = imagem;
    }

    // Getters e setters para acessar nome, dataHora e imagem
    public String getNome() {
        return nome;
    }

    public String getDataHora() {
        return dataHora;
    }

    public Bitmap getImagem() {
        return imagem;
    }
}
