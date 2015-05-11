package com.senac.controlecombustivel;

/**
 * Created by Dantieris on 09/05/2015.
 */
public class Tipo {
    private int id;
    private String nome;

    public Tipo(int id, String nome) {
        this.setId(id);
        this.setNome(nome);
    }

    public Tipo(int id) {
        this.id = id;
    }

    public Tipo(){}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
