package com.senac.controlecombustivel;

/**
 * Created by Dantieris on 09/05/2015.
 */
public class TiposCombustivel {

    private int id;
    private Tipo tipo;
    private Combustivel combustivel;
    private double preco;
    private Posto posto;

    public TiposCombustivel(int id, Posto posto, double preco, Tipo tipo, Combustivel combustivel) {
        this.combustivel = combustivel;
        this.id = id;
        this.posto = posto;
        this.preco = preco;
        this.tipo = tipo;
    }

    public TiposCombustivel(int id) {
        this.id = id;
    }

    public TiposCombustivel(){}

    public Combustivel getCombustivel() {
        return combustivel;
    }

    public void setCombustivel(Combustivel combustivel) {
        this.combustivel = combustivel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Posto getPosto() {
        return posto;
    }

    public void setPosto(Posto posto) {
        this.posto = posto;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }
}
